package cn.ruc.edu.dbfunction;

import cn.ruc.edu.basecore.*;
import cn.ruc.edu.mbdatagen.NewDateTime;
import cn.ruc.edu.mbdatagen.Testpath;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.elasticsearch.search.sort.SortOrder;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MongoDB extends UnStrDBBase<Mongo>{

    public DB mydb;

    public boolean LinkDb() {
        System.out.println("现在连接的数据库是：MongoDB");
        if(this.ip == null || this.port == -1){
            return false;
        }
        else {
            this.dbBase = new Mongo(this.ip, this.port);
            this.mydb = this.dbBase.getDB(this.dbName);
            this.session.setAttribute("dbport", this.port);
            return true;
        }
    }

    public boolean DislinkDb() {
        if(this.dbBase == null)
            return true;
        this.dbBase.close();
        session.removeAttribute("dbport");
        return true;
    }

    public long GetDbFilesCount() { return mydb.getCollection("fs.files").getCount(); }

    public double GetDataSize() { return Double.parseDouble(String.valueOf(mydb.getStats().get("dataSize"))); }

    public double GetStorageSize() { return Double.parseDouble(String.valueOf(mydb.getStats().get("storageSize"))); }

    public boolean UpLoadFile(String filepath) {
        this.SetTransLogPath();
        GridFS myfs = new GridFS(mydb);
        List<File> allLoadFileList = FileFunction.GetUpLoadFileList(filepath);
        long length = 0;
        long starttime = new Date().getTime();
        if(allLoadFileList.size() == 0)
            return false;
        else{
            for(File file : allLoadFileList)
            {
                long ofilestime = System.nanoTime();
                length += file.length();
                try {
                    GridFSInputFile inputfile = myfs.createFile(file);
                    inputfile.save();
                } catch (IOException e) {
                        e.printStackTrace();
                }

                long ofileetime = System.nanoTime();
                this.filesloadcount++;
                this.tempfilesloadspread += file.length();
                double speed = ((double)file.length()/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
                fileFunction.WriteLogforTrans(speed + "" , QueryFileName.LOAD_FILE_PER);
            }
        }
        long endtime = new Date().getTime();
        int seconds = (int)((endtime - starttime) / 1000);
        FileFunction.TimePrint(seconds);
        System.out.println("The average of translation is " + ((double)length/(1024*1024))/((float)(endtime - starttime)/1000) + "Mb/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                ((double)length/(1024*1024))/(float)seconds + "Mb/s", QueryFileName.DBINFO);
        return true;
    }

    public boolean UpLoadMetafile(String metapath){
        this.SetTransLogPath();
        MongoClient mc = new MongoClient();
        List<File> allLoadMetaList = FileFunction.GetUpLoadMetaFileList(metapath, "json");
        long starttime = new Date().getTime();
        for(File file : allLoadMetaList){
            BufferedReader fjson = null;
            try {
                fjson = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String all = "";
                String s = null;
                while((s = fjson.readLine()) != null)
                    all += s;
                Document doc = Document.parse(all);
                this.metasloadcount++;
                this.tempfilemetaloadcount ++;
                mc.getDatabase(dbName).getCollection(collectionName).insertOne(doc);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endtime = new Date().getTime();
        int seconds = (int)((endtime - starttime) / 1000);

        FileFunction.TimePrint(seconds);
        System.out.println("The average of document translation is " + (this.metasloadcount)/((float)(endtime - starttime)/1000) + "per/s\n");

        fileFunction.WriteLogforTrans(seconds + "," +
                (this.metasloadcount)/(float)seconds + "per/s", QueryFileName.DBINFO);
        return true;
    }

    public boolean DownLoadFile(String downpath) {
        this.SetTransLogPath();
        File logdir = new File(downpath + "/files" );
        if(!logdir.exists())
        {
            logdir.mkdirs();
        }

        BasicDBObject query = new BasicDBObject(),fields = new BasicDBObject();
        GridFS myFS = new GridFS(mydb);

        String filename = null;
        long filesize = 0;

        List<GridFSDBFile> gridFSDBFile = myFS.find(query, fields);
        Iterator<GridFSDBFile> it = gridFSDBFile.iterator();
        long allsize = 0;
        long filestime = new Date().getTime();
        //The is for download document
        while(it.hasNext())
        {
            GridFSDBFile gfile = it.next();
            filename = gfile.getFilename();
            filesize = gfile.getLength();
            allsize += filesize;
            long ofilestime = System.nanoTime();
            try {
                gfile.writeTo(downpath + "/files/" + filename.split("\\.")[0] + "_"
                        +gfile.getUploadDate().getTime() +
                        ".mbruc");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            long ofileetime = System.nanoTime();
            this.filesdowncount++;
            this.tempfilesdownspread += filesize;
            double speed = ((double)filesize/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
        fileFunction.WriteLogforTrans(speed + "" , QueryFileName.DOWN_FILE_PER);
            System.out.println("The translation of the " + filesdowncount + "th file is" + speed + "Mb/s\n");
        }
        long fileetime = new Date().getTime();
        int seconds = (int) ((fileetime - filestime) / 1000);

        FileFunction.TimePrint(seconds);
        System.out.println("The average of translation speed is " + ((double)allsize/(1024*1024))/((float)(fileetime - filestime)/1000) + "Mb/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                ((double)allsize/(1024*1024))/(float)seconds + "MB/s", QueryFileName.DBINFO);
        return true;
    }

    public boolean DownLoadMetaFile(String downpath) {
        this.SetTransLogPath();
        File logdir = new File(downpath + "/metadata");
        if( !logdir.exists())
        {
            logdir.mkdirs();
        }
        BasicDBObject query = new BasicDBObject(),fields = new BasicDBObject();
        GridFS myFS = new GridFS(mydb);
        String filename = null;
        long filesize = 0;
        DBCollection collection = mydb.getCollection(this.collectionName);
        DBCursor cursormeta = collection.find();
        List<GridFSDBFile> gridFSDBFile = myFS.find(query, fields);

        int i = 0;
        long filestime = new Date().getTime();
        while(cursormeta.hasNext())
        {

            try {
                filename = gridFSDBFile.get(i).getFilename();
                FileWriter fw = new FileWriter(downpath + "/metadata/" + filename.split("\\.")[0] + "_"
                        + gridFSDBFile.get(i).getUploadDate().getTime()
                        + ".json");
                fw.write(cursormeta.next().toString());
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            i++;
            this.metasdowncount++;
            this.tempfilemetadowncount ++;
        }
        long fileetime = new Date().getTime();
        int seconds = (int)((fileetime - filestime) / 1000);
        FileFunction.TimePrint(seconds);
        System.out.println("The average of document translation speed is " +
                (this.metasdowncount)/((float)(fileetime - filestime)/1000) + "per/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                (this.metasdowncount)/(float)seconds + "per/s", "dbinfo");
        DecimalFormat decimalFormat = new DecimalFormat(".00");

        long filesnum = this.GetDbFilesCount();
        double datasize = (double)(this.GetDataSize() / (1024 * 1024));
        double storagesize = (double)(this.GetStorageSize() / (1024 * 1024));
        double rates = ( datasize / storagesize ) * 100;
        storagesize = storagesize / 1024;

        fileFunction.WriteLogforTrans(decimalFormat.format(rates), QueryFileName.DBINFO);
        System.out.println(decimalFormat.format(rates));
        fileFunction.WriteLogforTrans(filesnum + "", QueryFileName.DBINFO);
        System.out.println(filesnum + "");
        fileFunction.WriteLogforTrans(this.GetDBName() + "", QueryFileName.DBINFO);
        System.out.println(this.GetDBName() + "");
        fileFunction.WriteLogforTrans(decimalFormat.format(storagesize), QueryFileName.DBINFO);
        System.out.println(decimalFormat.format(storagesize));
        return true;
    }

    public void VersionQuery(String datapath) throws IOException {
        this.SetQueryLogPath();
        this.finish = false;
        /*This is used when the database support this query.
        Actually, MongoDB don't support this

        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        //下面的代码用于统计文件同名的个数，并将每个文件的重名个数分组统计
        DBObject keys = new BasicDBObject("FILENAME",1);
        DBObject condition = null;
        DBObject initial = new BasicDBObject("count", 0);
        String reduce = "function(doc, out){out.count++;}";
        String finalize = "function(out){return out;}";
        long start = System.nanoTime();
        BasicDBList filelist = (BasicDBList ) dbCollection.group(keys, condition, initial, reduce, finalize);
        long end = System.nanoTime();
        System.out.println(filelist.size());
        int count = 0;
        HashMap<String, Integer> resulthm = new HashMap<String, Integer>(); // 用于结果记录
        for(int i = 0; i < filelist.size(); i++){
            DBObject obj = (DBObject ) filelist.get(i);
            String file = (String) obj.get("FILENAME");
            double counts = (Double ) obj.get("count");
            System.out.println(counts);
            resulthm.put(file, (int) counts);
            count += (int) counts;
        }
        int[] result = this.CopyCheck(datapath, resulthm);
        fileFunction.WriteLogforQuery("true", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery(String.valueOf(count), QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery(String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery(String.valueOf((count / (float)((end - start) / 1E6))), QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery(String.valueOf((float) result[1] / result[0] * 100), QueryFileName.QUERY_VERISON);
        */
        fileFunction.WriteLogforQuery("ability;false", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("rate;0", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("count;0", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("time;0", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("speed;0", QueryFileName.QUERY_VERISON);
        this.finish = true;
    }

    public void AbnormalQuery(String datapath) {
        this.SetQueryLogPath();
        this.finish = false;
        /*This is used when the database support this query.
        Actually, MongoDB don't support this

        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        DBObject query = new BasicDBObject();
        query.put("DEVICETYPE", "BLADE");
        DBCursor dbCursor = dbCollection.find(query);
        int count = dbCursor.count();

        System.out.println(count);

        ArrayList<String> abnormalFileName = new ArrayList<>();
        long starttime = System.nanoTime();
        System.out.println(starttime);
        while(dbCursor.hasNext()){
           DBObject dbObject = dbCursor.next();
           ArrayList<Double> windspeed = (ArrayList<Double> ) dbObject.get("WINDSPEED");
           double max = windspeed.parallelStream().collect(Collectors.maxBy(Comparator.comparingDouble(Double::doubleValue))).get();
           double min = windspeed.parallelStream().collect(Collectors.minBy(Comparator.comparingDouble(Double::doubleValue))).get();
           //针对异常文件，一般来说是min为负数且大于2(相对数，如果震荡因子大于1的时候就不行了)
           if(min < 0 || max - min > 3){
               System.out.println(dbObject.get("FILENAME").toString().split("\\$")[0] + ":" + dbObject.get("FILECOPYNUM"));
               abnormalFileName.add(dbObject.get("FILENAME").toString().split("\\$")[0] + ":" + dbObject.get("FILECOPYNUM"));
           }

       }
       long endtime = System.nanoTime();
       int[] result = AbnormalCheck(datapath, abnormalFileName);
       fileFunction.WriteLogforQuery("true", QueryFileName.QUERY_ABNORMAL);
       fileFunction.WriteLogforQuery(String.valueOf(count), QueryFileName.QUERY_ABNORMAL);
       fileFunction.WriteLogforQuery(String.valueOf((float)starttime / endtime), QueryFileName.QUERY_ABNORMAL);
       fileFunction.WriteLogforQuery(String.valueOf(count / ((float)starttime / endtime)), QueryFileName.QUERY_ABNORMAL);
       fileFunction.WriteLogforQuery(String.valueOf(result[0]),QueryFileName.QUERY_ABNORMAL);
       fileFunction.WriteLogforQuery(String.valueOf(result[1]),QueryFileName.QUERY_ABNORMAL);
       fileFunction.WriteLogforQuery(FileFunction.floatToString((float)result[2] / result[1] * 100), QueryFileName.QUERY_ABNORMAL);

       */
        fileFunction.WriteLogforQuery("ability;false", QueryFileName.QUERY_ABNORMAL);
        /*fileFunction.WriteLogforQuery("rate;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("count;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("time;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("speed;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("findcount;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("truecount;0", QueryFileName.QUERY_ABNORMAL);*/
        this.finish = true;
    }

    public void HotFileQuery(int queryCount, int interval) {
        this.SetQueryLogPath();
        this.finish = false;
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        DBObject query = new BasicDBObject();
        System.out.println("正在获取");
        Map<String, String> queryMap = getHotQueryContent();
        System.out.println("已经获取");
        String hotQueryAttributeName = (String) queryMap.keySet().toArray()[0];
        System.out.println(hotQueryAttributeName);
        if(hotQueryAttributeName.equals("TOWERHEIGHT")){
            String[] queryStringContentArray = queryMap.get(hotQueryAttributeName).split("-");
            int minValue = Integer.parseInt(queryStringContentArray[0]);
            int maxValue = Integer.parseInt(queryStringContentArray[1]);
            BasicDBObject min = new BasicDBObject()
                    .append("TOWERHEIGHT",new BasicDBObject("$gte", minValue));
            BasicDBObject max = new BasicDBObject()
                    .append("TOWERHEIGHT",new BasicDBObject("$lte", maxValue));
            //BasicDBObject towerFinalObj = new BasicDBObject("$and",Arrays.asList(min, max));
            query = new BasicDBObject("$and",Arrays.asList(min, max));
        } else if(hotQueryAttributeName.equals("BLADELENGTH")){
            String[] queryStringContentArray = queryMap.get(hotQueryAttributeName).split("-");
            int minValue = Integer.parseInt(queryStringContentArray[0]);
            int maxValue = Integer.parseInt(queryStringContentArray[1]);
            BasicDBObject min = new BasicDBObject()
                    .append("BLADELENGTH",new BasicDBObject("$gte", minValue));
            BasicDBObject max = new BasicDBObject()
                    .append("BLADELENGTH",new BasicDBObject("$lte", maxValue));
            //BasicDBObject bladeLengthFinalObj = new BasicDBObject("$and",Arrays.asList(min, max));
            query = new BasicDBObject("$and",Arrays.asList(min, max));
        } else if(hotQueryAttributeName.equals("BLADEWIDTH")){
            String[] queryStringContentArray = queryMap.get(hotQueryAttributeName).split("-");
            int minValue = Integer.parseInt(queryStringContentArray[0]);
            int maxValue = Integer.parseInt(queryStringContentArray[1]);
            BasicDBObject min = new BasicDBObject()
                    .append("BLADEWIDTH",new BasicDBObject("$gte", minValue));
            BasicDBObject max = new BasicDBObject()
                    .append("BLADEWIDTH",new BasicDBObject("$lte", maxValue));
            //BasicDBObject bladeWidthFinalObj = new BasicDBObject("$and",Arrays.asList(min, max));
            query = new BasicDBObject("$and",Arrays.asList(min, max));
        }
        else
            query.put(hotQueryAttributeName, queryMap.get(hotQueryAttributeName));        
        System.out.println("到这里没有问题");
        for(int i = 0; i < queryCount; i++) {        	
            long start = System.nanoTime();
            DBCursor dbCursor = dbCollection.find(query);
            Iterator<DBObject> countCurosr = dbCursor.iterator();
            while(countCurosr.hasNext()) {
            	countCurosr.next();
            }
            	
            long end = System.nanoTime();
            fileFunction.WriteLogforQuery(String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_HOTFILE);//用时
            System.out.println((float)((end - start) / 1E6));
            try {
                Thread.sleep (interval) ;
            } catch (InterruptedException ie){
            }
        }

        this.finish = true;
    }

    public List<String> QueryOne() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        DBObject query = new BasicDBObject();
        Map<String, Object> queryMap = new HashMap<>();
        String content = getDeviceTypeQueryContent();

        System.out.println(content);
        queryMap.put("DEVICETYPE", content);
        query.put("DEVICETYPE", content);      
        DBCursor dbCursor = dbCollection.find(query);
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        long start = System.nanoTime();
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();

        ArrayList<String> fileList = new ArrayList<>();
        int rightCount = this.CheckQuery(dbCursor.iterator(), queryMap);

        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }

        //FileFunction.queryMakeRecord(this.fileDirName, QueryFileName.QUERY_ONE, queryMap);
        fileFunction.WriteLogforQuery("querycontent;" + "DEVICETYPE:" + content, QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("count;" + String.valueOf(count), QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("time;" + String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("speed;" + String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_ONE);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-One is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("rate;" + FileFunction.floatToString((float) rightCount / count * 100));
        return fileList;
    }

    public List<String> QueryTwo() throws IOException {
    	DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        ArrayList<String> contentQueryList = getAreaNameQueryContentList();
        BasicDBObject AreaObj = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        for(String string : contentQueryList)
            values.add(string);      
        AreaObj.put("AREANAME",new BasicDBObject("$in",values));
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find(AreaObj);
        int count = 0;
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();
        ArrayList<String> fileList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("AREANAME", contentQueryList);
        int rightCount = this.CheckQuery(dbCursor.iterator(), queryMap);

        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }

        //FileFunction.queryMakeRecord(this.fileDirName, QueryFileName.QUERY_TWO, queryMap);
        fileFunction.WriteLogforQuery("querycontent;" + "AREANAME:" + contentQueryList, QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_TWO);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Two is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");     
        return fileList;
    }

    public List<String> QueryThree() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        ArrayList<String> fileList = new ArrayList<>();
        LocalDateTime[] queryContentArray = getTimeQueryContentArray();
        String stime = NewDateTime.GetTimeFormat(queryContentArray[0]);
        String etime = NewDateTime.GetTimeFormat(queryContentArray[1]);       
        System.out.println("query-three查询属性SIMULATIONTIME取的值是："+queryContentArray[0]+"--"+queryContentArray[1]);  
        DBObject groupFields = new BasicDBObject();       
        groupFields.put("SIMULATIONTIME", new BasicDBObject("$gte", stime).append("$lte", etime));      
        int count=0;
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find(groupFields);     
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }       
        long end = System.nanoTime();  
        ArrayList<LocalDateTime> timeQueryList = new ArrayList<>();
        timeQueryList.add(queryContentArray[0]);
        timeQueryList.add(queryContentArray[1]);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("SIMULATIONTIME", timeQueryList);             
        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }
        int rightCount = CheckQuery(dbCursor.iterator(), queryMap);      
        fileFunction.WriteLogforQuery("querytime;" + NewDateTime.GetTimeFormat(queryContentArray[0])
                        + "--" + NewDateTime.GetTimeFormat(queryContentArray[1])
                , QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToPresentString((float) rightCount / count), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_THREE);
        System.out.println(count);
        System.out.println("rate:"+FileFunction.floatToPresentString((float) rightCount / count));
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Three is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        return fileList;
        /*//String类型的日期转换为date类型
        LocalDateTime[] queryContentArray = getTimeQueryContentArray();      
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//string类型日期格式 "2008-4-24 09:51:00"
        Date mdate = null;      
        DBObject query = new BasicDBObject();
        DBCursor dbCursor = dbCollection.find(query);
        int count = 0;
        ArrayList<String> fileList = new ArrayList<>();   	 	
   	 	long lsdate = 0;long ledate = 0;
		try {
			lsdate = sdf.parse(NewDateTime.GetTimeFormat(queryContentArray[0])).getTime();
			ledate = sdf.parse(NewDateTime.GetTimeFormat(queryContentArray[1])).getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		System.out.println("lsdate: "+lsdate);
		System.out.println("ledate: "+ledate);		 	
        ArrayList<LocalDateTime> timeQueryList = new ArrayList<>();
        timeQueryList.add(queryContentArray[0]);
        timeQueryList.add(queryContentArray[1]);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("SIMULATIONTIME", timeQueryList);
        ArrayList<DBObject> trueQueryContentList = new ArrayList<>();
        long start = System.nanoTime();
        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            String msdate = dbObject.get("SIMULATIONTIME").toString();
            //String类型的日期转换为date类型
            try {
                mdate=sdf.parse(msdate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long lmdate = mdate.getTime();           
            //比较是否在规定时间范围内
            if((lmdate >= lsdate&&lmdate <= ledate)) {
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
                count++;
                trueQueryContentList.add(dbObject);           
            }
        }
        long end = System.nanoTime();
        int rightCount = CheckQuery(trueQueryContentList.iterator(), queryMap);      
        fileFunction.WriteLogforQuery("querytime;" + NewDateTime.GetTimeFormat(queryContentArray[0])
                        + "--" + NewDateTime.GetTimeFormat(queryContentArray[1])
                , QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToPresentString((float) rightCount / count), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_THREE);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Three is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        return fileList;*/
    }

    public List<String> QueryFour() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        DBObject query = new BasicDBObject();
        String deviceTypeContent = this.getDeviceTypeQueryContent();
        String areaNameContent = this.getAreaNameContent();
        query.put("DEVICETYPE", deviceTypeContent);
        query.put("AREANAME", areaNameContent);      
        int count = 0;
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find(query);
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();

        ArrayList<String> fileList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("DEVICETYPE", deviceTypeContent);
        queryMap.put("AREANAME", areaNameContent);

        int rightCount = this.CheckQuery(dbCursor.iterator(), queryMap);
        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }

        //FileFunction.queryMakeRecord(this.fileDirName, QueryFileName.QUERY_FOUR, queryMap);
        fileFunction.WriteLogforQuery("querycontent;" +"AREANAME:"+areaNameContent+" & DEVICETYPE:"+deviceTypeContent, QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_FOUR);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Four is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        return fileList;
    }

    public List<String> QueryFive() {
        List<Object> object = new ArrayList<Object>();
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        BasicDBObject AreaObj = new BasicDBObject();
        ArrayList<String> areaNameContentList = this.getAreaNameQueryContentList();
        String devicetypeContent = this.getDeviceTypeQueryContent();
        BasicDBList values = new BasicDBList();
        for(String string : areaNameContentList)
            values.add(string);     
        AreaObj.put("AREANAME",new BasicDBObject("$in",values));
        BasicDBObject TypeObj = new BasicDBObject("DEVICETYPE", devicetypeContent);
        BasicDBObject FinalObj = new BasicDBObject("$and", Arrays.asList(AreaObj, TypeObj));

        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find(FinalObj);
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();
        ArrayList<String> fileList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("AREANAME", areaNameContentList);
        queryMap.put("DEVICETYPE", devicetypeContent);
        int rightCount = this.CheckQuery(dbCursor.iterator(), queryMap);
        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }

        //FileFunction.queryMakeRecord(this.fileDirName, QueryFileName.QUERY_FIVE, queryMap);
        fileFunction.WriteLogforQuery("querycontent;" +"AREANAME:"+areaNameContentList+" & DEVICETYPE:"+devicetypeContent, QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_FIVE);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Five is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        return fileList;
    }

    public List<String> QuerySix() throws IOException {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        int[] bladeLengthQueryRange = this.getNumerRange(DevicePartNameEnum.BLADELENGTH.getValue());
        int[] towerHeightQueryRange = this.getNumerRange(DevicePartNameEnum.TOWERHEIGHT.getValue());
        BasicDBObject bladeLengthQueryRangeMin = new BasicDBObject();
        BasicDBObject bladeLengthQueryRangeMax = new BasicDBObject();
        BasicDBObject towerHeightQueryRangeMin = new BasicDBObject();
        BasicDBObject towerHeightQueryRangeMax = new BasicDBObject();

        bladeLengthQueryRangeMin.append("BLADELENGTH",new BasicDBObject("$gte", bladeLengthQueryRange[0]));
        bladeLengthQueryRangeMax.append("BLADELENGTH",new BasicDBObject("$lte",bladeLengthQueryRange[1]));

        towerHeightQueryRangeMin.append("TOWERHEIGHT",new BasicDBObject("$gte", towerHeightQueryRange[0]));
        towerHeightQueryRangeMax.append("TOWERHEIGHT",new BasicDBObject("$lte",towerHeightQueryRange[1]));
        BasicDBObject FinalObj = new BasicDBObject("$and", Arrays.asList(bladeLengthQueryRangeMin, bladeLengthQueryRangeMax,
                towerHeightQueryRangeMin, towerHeightQueryRangeMax));
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find(FinalObj);
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();
        ArrayList<String> fileList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("BLADELENGTH", bladeLengthQueryRange[0] + "-" + bladeLengthQueryRange[1]);
        queryMap.put("TOWERHEIGHT", towerHeightQueryRange[0] + "-" + towerHeightQueryRange[1]);

        /*queryContentListOne.add(this.sylenth_min + (int)(lengthsub / 4));
        queryContentListOne.add(this.sylenth_max - (int)(lengthsub / 4));
        queryContentListTwo.add(this.towerheigh_min + (int)(heightsub / 4));
        queryContentListTwo.add(this.towerheigh_max - (int)(heightsub / 4));
        queryMap.put("BLADELENGTH", (this.sylenth_min + (int)(lengthsub / 4)) + "-" + (this.sylenth_max - (int)(lengthsub / 4)));
        queryMap.put("TOWERHEIGHT", (this.towerheigh_min + (int)(heightsub / 4)) + "-" + (this.towerheigh_max - (int)(heightsub / 4)));
*/
        int rightCount = this.CheckQuery(dbCursor.iterator(), queryMap);

        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }

        //FileFunction.queryMakeRecord(this.fileDirName, QueryFileName.QUERY_SIX, queryMap);
        fileFunction.WriteLogforQuery("querycontent;" +"BLADELENGTH:"+bladeLengthQueryRange[0]+"--"+bladeLengthQueryRange[1]
				+" & TOWERHEIGHT:"+towerHeightQueryRange[0] + "-" + towerHeightQueryRange[1], QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_SIX);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Six is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        return fileList;
    }

    public List<String> QuerySeven() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);

        ArrayList<String> areaNameContentList = this.getAreaNameQueryContentList();
        BasicDBObject AreaObj = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        for(String string : areaNameContentList)
            values.add(string);

        AreaObj.put("AREANAME",new BasicDBObject("$in",values));
        String deviceTypeContent = this.getDeviceTypeQueryContent();
        BasicDBObject TypeObj = new BasicDBObject("DEVICETYPE", deviceTypeContent);
        int[] towerHeightArray = this.getNumerRange(DevicePartNameEnum.TOWERHEIGHT.getValue());

        BasicDBObject towerLengthQueryRangeMin = new BasicDBObject();
        BasicDBObject towerLengthQueryRangeMax = new BasicDBObject();
        towerLengthQueryRangeMin.append("TOWERHEIGHT",new BasicDBObject("$gte", towerHeightArray[0]));
        towerLengthQueryRangeMax.append("TOWERHEIGHT",new BasicDBObject("$lte", towerHeightArray[1]));
        BasicDBObject towerFinalObj = new BasicDBObject("$and",Arrays.asList(towerLengthQueryRangeMin,
                towerLengthQueryRangeMax));
        BasicDBObject FinalObj = new BasicDBObject("$and",Arrays.asList(AreaObj, TypeObj, towerFinalObj));
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find(FinalObj);

        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();
        ArrayList<String> fileList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();

        queryMap.put("DEVICETYPE", deviceTypeContent);
        queryMap.put("AREANAME", areaNameContentList);
        queryMap.put("TOWERHEIGHT", towerHeightArray[0] + "-" + towerHeightArray[1]);
        int rightCount = this.CheckQuery(dbCursor.iterator(), queryMap);

        try {
            while (dbCursor.hasNext()) {
                DBObject dbObject = dbCursor.next();
                fileList.add(dbObject.get("FILENAME").toString() + ":" + dbObject.get("FILECOPYNUM"));
            }
        } finally {
            dbCursor.close();
        }

        //FileFunction.queryMakeRecord(this.fileDirName, QueryFileName.QUERY_SEVEN, queryMap);
        fileFunction.WriteLogforQuery("querycontent;" +"AREANAME:"+areaNameContentList+" & DEVICETYPE:"+deviceTypeContent
				+" & TOWERHEIGHT:"+towerHeightArray[0]+"--"+towerHeightArray[1], QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_SEVEN);
        System.out.println(count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Seven is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        return fileList;
    }

    public void QueryNine() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        ///SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();       
        int c=1;
		SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
		while(c>0) {
			if(!queryContentTuple.sortedDataType.equals("Integer")) {
	    		queryContentTuple = this.getSortAttributeName();
	    		c++;
	    	}else {
	    		c=0;
	    	}				
		}
    	System.out.println("QueryNine选择排序的属性："+queryContentTuple.getSortAttributeContent()); 	 	
    	String order="";   	
    	if(queryContentTuple.getSortedType()==-1) {   		
    		order = "DESC";
    	}else {    		
    		order = "ASC";
    	}               
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find().sort(new BasicDBObject(queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedType()));
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();

        String checkSorted = CheckSorted(dbCursor.iterator(), queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedDataType(), queryContentTuple.getSortedType());
        //FileFunction.sortMakeRecord(this.fileDirName, QueryFileName.QUERY_NINE,
               // queryContentTuple.getSortAttributeContent(), queryContentTuple.getSortedType());
        fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_NINE);
        fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_NINE);
        fileFunction.WriteLogforQuery("ability;" + checkSorted, QueryFileName.QUERY_NINE);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_NINE);//排序文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_NINE);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_NINE);//排序速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Sort-Nine is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The ability is " + checkSorted);
    }

    public void QueryTen() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        //SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
        int c=1;
		SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
		while(c>0) {
			if(!queryContentTuple.sortedDataType.equals("String")) {
	    		queryContentTuple = this.getSortAttributeName();
	    		c++;
	    	}else {
	    		c=0;
	    	}				
		}
    	System.out.println("QueryTen选择排序的属性："+queryContentTuple.getSortAttributeContent()); 	 	
    	String order="";   	
    	if(queryContentTuple.getSortedType()==-1) {   		
    		order = "DESC";
    	}else {    		
    		order = "ASC";
    	}
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find().sort(new BasicDBObject(queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedType()));
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();
        String checkSorted = CheckSorted(dbCursor.iterator(),queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedDataType(), queryContentTuple.getSortedType());
        //FileFunction.sortMakeRecord(this.fileDirName, QueryFileName.QUERY_TEN,
               // queryContentTuple.getSortAttributeContent(), queryContentTuple.getSortedType());
        fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_TEN);
        fileFunction.WriteLogforQuery("ability;" + checkSorted, QueryFileName.QUERY_TEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_TEN);//排序文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_TEN);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf(count / (float)((end - start) / 1E6)), QueryFileName.QUERY_TEN);//排序速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Sort-Ten is " + count / (float)((end - start) / 1E6) + "per/ms");
    }

    public void QueryEleven() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        //SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
        int c=1;
		SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
		while(c>0) {
			if(!queryContentTuple.sortedDataType.equals("Date")) {
	    		queryContentTuple = this.getSortAttributeName();
	    		c++;
	    	}else {
	    		c=0;
	    	}				
		}
    	System.out.println("QueryEleven选择排序的属性："+queryContentTuple.getSortAttributeContent()); 	 	
    	String order="";   	
    	if(queryContentTuple.getSortedType()==-1) {   		
    		order = "DESC";
    	}else {    		
    		order = "ASC";
    	}    
        long start = System.nanoTime();
        DBCursor dbCursor = dbCollection.find().sort(new BasicDBObject(queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedType()));
        Iterator<DBObject> countCurosr = dbCursor.iterator();
        int count = 0;
        while(countCurosr.hasNext()){
            countCurosr.next();
            count++;
        }
        long end = System.nanoTime();
        String checkSorted = CheckSorted(dbCursor.iterator(), queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedDataType(), queryContentTuple.getSortedType());
        //FileFunction.sortMakeRecord(this.fileDirName, QueryFileName.QUERY_ELEVEN,
             //   queryContentTuple.getSortAttributeContent(), queryContentTuple.getSortedType());
        fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_ELEVEN);
        fileFunction.WriteLogforQuery("ability;" + checkSorted, QueryFileName.QUERY_ELEVEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_ELEVEN);//排序文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_ELEVEN);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_ELEVEN);//排序速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Sort-Eleven is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
    }

    public void QueryTwelve() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        int count = 0;
        long start = System.nanoTime();
        //unwind操作拆分WINDSPEED数组
        DBObject unwind = new BasicDBObject("$unwind", "$WINDSPEED");
        //Group操作
        DBObject groupFields = new BasicDBObject("_id","$_id");//按_id分组，每个文件分为一组
        groupFields.put("AvgSpeed", new BasicDBObject("$avg", "$WINDSPEED"));
        DBObject group = new BasicDBObject("$group", groupFields);
        //查看Group结果
        AggregationOutput output = dbCollection.aggregate(unwind,group);// 执行 aggregation命令
        count = output.results().toString().split(",").length/2;
        long end = System.nanoTime();

        System.out.println("时间：" + (end - start));
        int rightCount = CheckStatistics(output.results().iterator(), "AVG", "WINDSPEED", "AvgSpeed");

        fileFunction.WriteLogforQuery("ability;true", QueryFileName.QUERY_TWELVE);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_TWELVE);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_TWELVE);//文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_TWELVE);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_TWELVE);//速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Twelve is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
    }

    public void QueryThirteen() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        int count = 0;
        long start = System.nanoTime();
        //unwind操作拆分WINDSPEED数组
        DBObject unwind = new BasicDBObject("$unwind", "$WINDSPEED");
        //Group操作
        DBObject groupFields = new BasicDBObject("_id","$_id");//按_id分组，每个文件分为一组
        groupFields.put("MinSpeed", new BasicDBObject("$min", "$WINDSPEED"));
        DBObject group = new BasicDBObject("$group", groupFields);
        //查看Group结果
        AggregationOutput output = dbCollection.aggregate(unwind,group);// 执行 aggregation命令
        count = output.results().toString().split(",").length/2;
        long end = System.nanoTime();

        int rightCount = CheckStatistics(output.results().iterator(), "MIN", "WINDSPEED", "MinSpeed");

        fileFunction.WriteLogforQuery("ability;true", QueryFileName.QUERY_THIRTEEN);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_THIRTEEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_THIRTEEN);//文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_THIRTEEN);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_THIRTEEN);//速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Thriteen is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
    }

    public void QueryFourteen() {
        DB db = this.dbBase.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        int count = 0;
        long start = System.nanoTime();
        //unwind操作拆分WINDSPEED数组
        DBObject unwind = new BasicDBObject("$unwind", "$WINDSPEED");
        //Group操作
        DBObject groupFields = new BasicDBObject("_id","$_id");//按_id分组，每个文件分为一组
        groupFields.put("MaxSpeed", new BasicDBObject("$max", "$WINDSPEED"));
        DBObject group = new BasicDBObject("$group", groupFields);
        //查看Group结果
        AggregationOutput output = dbCollection.aggregate(unwind,group);// 执行 aggregation命令
        count = output.results().toString().split(",").length/2;
        long end = System.nanoTime();

        int rightCount = CheckStatistics(output.results().iterator(), "MAX", "WINDSPEED", "MaxSpeed");

        fileFunction.WriteLogforQuery("ability;true", QueryFileName.QUERY_FOURTEEN);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_FOURTEEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_FOURTEEN);//文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_FOURTEEN);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_FOURTEEN);//速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Forteen is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
    }

    public void DownloadFileByList(List<String> downList, String downfilename, String downfilepath) {
        File logdir = new File(downfilepath + "/" + downfilename + "/");
        if(!logdir.exists()){
            logdir.mkdirs();
        }
        DB db = this.dbBase.getDB(this.dbName);
        GridFS myFS = new GridFS(db);
        String filename = null;
        int filesdowncount = 0;
        long filesize;
        long allsize = 0;
        long filestime = new Date().getTime();
        int count = 0;
        double minspeed = 1E6;double maxspeed = 0;

        Iterator<String> downFileIterator = downList.iterator();

        while (downFileIterator.hasNext()){
            String content = downFileIterator.next();
            String fileName = content.split(":")[0];
            int copyNum = Integer.parseInt(content.split(":")[1]);
            BasicDBObject query = new BasicDBObject();
            query.put("filename", fileName);
            List<GridFSDBFile> gridFSDBFile = myFS.find(query);
            GridFSDBFile gfile = gridFSDBFile.get(copyNum - 1);
            filename = gfile.getFilename();
            filesize = gfile.getLength();
            allsize += filesize;
            long ofilestime = System.nanoTime();
            try {
                gfile.writeTo(downfilepath + "/" + downfilename + "/" + filename.split("\\.")[0] + "_"
                        + "_"  + copyNum +
                        ".mbruc"); //下载文件
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            long ofileetime = System.nanoTime();
            filesdowncount++;
            /*File ss = new File(downfilepath + "/" + downfilename + "/" + filename.split("\\.")[0] + "_"
                    + "_"  + copyNum +
                    ".mbruc");
            ss.delete();*/
            double speed = ((double)filesize/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
            if(speed<minspeed) {
                minspeed = speed;
            }
            if(speed>maxspeed) {
                maxspeed = speed;
            }
            fileFunction.WriteLogforQuery(String.valueOf(speed), downfilename);
           // System.out.println("File name is " + filename + "The current down file count ：" + filesdowncount + "; " +
           //         "The curent down file speed ：" + speed + "Mb/s\n");
            count++;
        }

        long fileetime = new Date().getTime();
        System.out.println("All down files count : " + count);
        System.out.println("The max speed is ："+maxspeed + "Mb/s");
        System.out.println("The min speed is ："+minspeed + "Mb/s");
        System.out.println("The average speed is ：" + ((double)allsize/(1024*1024))/((float)(fileetime - filestime)/1000) + "Mb/s");
    }

    public int CheckQuery(Iterator<DBObject> result, Map<String, Object> queryMap){
        int rightCount = 0;
        boolean checkRight = true;
        while(result.hasNext()){
            DBObject toCheckObject = result.next();
            Set<String> queryKeySet = queryMap.keySet();
            for(String query : queryKeySet){
                Object queryValue = queryMap.get(query);
                //判断是否为列表，如果是列表，则不是离散查询（数值、字符串）就是日期的范围查询
                if(queryValue.getClass().getName().equals("java.util.ArrayList")){
                    ArrayList checkQuery = (ArrayList) queryValue;
                    String checkItemType = checkQuery.get(0).getClass().getName();
                    //System.out.println("checkItemType: "+checkItemType);
                    //首先判断是否为数值类型
                    if(checkItemType.equals("java.lang.Integer")){
                        ArrayList<Integer> valueList = new ArrayList<>();
                        for(Object checkItem : checkQuery)
                            valueList.add((Integer) checkItem);
                        Integer checkAttrValue = (Integer ) toCheckObject.get(query);
                        boolean isRight = false;
                        for(Integer itemToCheck : valueList)
                            if(checkAttrValue == itemToCheck)
                                isRight = true;
                        if(!isRight){
                            checkRight = false;
                            break;
                        }
                    }

                    else if(checkItemType.equals("java.time.LocalDateTime")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Long min = null;Long max = null;	                      
                        String mintime = checkQuery.get(0).toString().replaceAll("T", " ");
                        String maxtime = checkQuery.get(1).toString().replaceAll("T", " ");                        
                        if(mintime.split(" ")[1]!=null&&mintime.split(" ")[1].split(":")[0]!=null&&mintime.split(" ")[1].split(":")[0].equals("12")) {
                        	try {
								min = sdf.parse(mintime).getTime()+Long.valueOf(43200000);										
							} catch (ParseException e1) {
								 //TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }
                        if(maxtime.split(" ")[1]!=null&&maxtime.split(" ")[1].split(":")[0]!=null&&maxtime.split(" ")[1].split(":")[0].equals("12")) {
                        	try {								
								max = sdf.parse(maxtime).getTime()+Long.valueOf(43200000);								
							} catch (ParseException e1) {
								 //TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }	                                              
                        try {
                        	min = sdf.parse(mintime).getTime();
							max = sdf.parse(maxtime).getTime();							
						} catch (ParseException e1) {
							 //TODO Auto-generated catch block
							e1.printStackTrace();
						}						
                        if(max < min){
                            Long temp = max;
                            max = min;
                            min = temp;
                        }	                        
                        String msdate = toCheckObject.get("SIMULATIONTIME").toString();
                        Long checkAttrValue = null;
                        if(msdate.split(" ")[1]!=null&&msdate.split(" ")[1].split(":")[0]!=null&&msdate.split(" ")[1].split(":")[0].equals("12")) {
                        	try {
								checkAttrValue = sdf.parse(msdate).getTime()+Long.valueOf(43200000);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }else {
                        	try {
								checkAttrValue = sdf.parse(msdate).getTime();
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }                       	                                                  
                        if(checkAttrValue < min || checkAttrValue > max) {
                        	checkRight = false;
                            break;
                        }                      
                    }
                    else if(checkItemType.equals("java.lang.Long")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Long min = (Long) checkQuery.get(0);
                        Long max = (Long) checkQuery.get(1);
                        if(max < min){
                            Long temp = max;
                            max = min;
                            min = temp;
                        }

                        String msdate = toCheckObject.get("SIMULATIONTIME").toString();
                        try {
                            Long checkAttrValue = sdf.parse(msdate).getTime();
                            if(checkAttrValue < min || checkAttrValue > max) {
                                checkRight = false;
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        ArrayList<String> valueList = new ArrayList<>();
                        for(Object checkItem : checkQuery)
                            valueList.add((String ) checkItem);
                        String checkAttrValue = (String ) toCheckObject.get(query);
                        boolean isRight = false;
                        for(String itemToCheck : valueList)
                            if(checkAttrValue.equals(itemToCheck))
                                isRight = true;
                        if(!isRight){
                            checkRight = false;
                            break;
                        }
                    }
                }

                else if(queryValue.getClass().getName().equals("java.lang.Integer")){
                    Integer checkAttrValue = (Integer ) toCheckObject.get(query);
                    Integer itemToCheck = (Integer ) queryValue;
                    if(checkAttrValue != itemToCheck){
                        checkRight = false;
                        break;
                    }
                }
                else if(queryValue.getClass().getName().equals("java.lang.Double")){
                    Double checkAttrValue = (Double ) toCheckObject.get(query);
                    Double itemToCheck = (Double ) queryValue;
                    if(checkAttrValue != itemToCheck){
                        checkRight = false;
                        break;
                    }
                }
                else{
                    String queryValueString = (String) queryValue;
                    if(queryValueString.contains("-")){
                        Integer min = Integer.parseInt(queryValueString.split("-")[0]);
                        Integer max = Integer.parseInt(queryValueString.split("-")[1]);
                        if(max < min){
                            Integer temp = max;
                            max = min;
                            min = temp;
                        }
                        Integer checkAttrValue = (Integer ) toCheckObject.get(query);
                        if(checkAttrValue < min || checkAttrValue > max){
                            checkRight = false;
                            break;
                        }
                    }
                    else{
                        String checkAttrValue = (String ) toCheckObject.get(query);
                        String itemToCheck = (String ) queryValue;
                        if(!checkAttrValue.equals(itemToCheck)){
                            checkRight = false;
                            break;
                        }
                    }
                }
            }
            if(checkRight)
                rightCount++;
            else
                checkRight = true;
        }
        return rightCount;
    }

    public int CheckStatistics(Iterator<DBObject> result, String statisticsType, String queryContent, String contentName){
        System.out.println("进入检测");
        int rightCount = 0;
       // System.out.println("1");
        String checkQuery = statisticsType + queryContent;
        //System.out.println("2");
        while(result.hasNext()){
            //System.out.println("3");
            DBObject resultObject = result.next();
           // System.out.println("4");
            ObjectId resultId = (ObjectId) resultObject.get("_id");
           // System.out.println("5");
            Double resultValue = (Double ) resultObject.get(contentName);
            //System.out.println("---------");
            DB db = this.dbBase.getDB(dbName);
            //System.out.println("*********");
            DBCollection dbCollection = db.getCollection(collectionName);
            DBObject query = new BasicDBObject();
            query.put("_id", resultId);
            DBCursor checkResultCursor = dbCollection.find(query);
            Double checkResult = (Double ) checkResultCursor.next().get(checkQuery);
            if(Math.abs(checkResult - resultValue) <= 1E-6)
                rightCount ++;
        }
        return rightCount;
    }

    public String CheckSorted(Iterator<DBObject> result, String query, String type, int sortWay){
        boolean check = true;
        ArrayList<Object> toCheckValueList = new ArrayList<>();
        while(result.hasNext()){
            DBObject toCheckObject = result.next();
            toCheckValueList.add(toCheckObject.get(query));
        }

        if(type.equals("Integer")){
            int i;
            for(i = 0; i < toCheckValueList.size() - 1; i++){
                Integer value1 = (Integer) toCheckValueList.get(i);
                Integer value2 = (Integer) toCheckValueList.get(i+1);
                if(sortWay == -1) {
                    if (value1 < value2) {
                        check = false;
                        break;
                    }
                }
                else
                if(value1 > value2){
                    check = false;
                    break;
                }
            }
        }
        else if(type.equals("Double")){
            int i;
            for(i = 0; i < toCheckValueList.size() - 1; i++){
                Double value1 = (Double) toCheckValueList.get(i);
                Double value2 = (Double) toCheckValueList.get(i+1);
                if(sortWay == -1) {
                    if (value1 < value2) {
                        check = false;
                        break;
                    }
                }
                else
                if(value1 > value2){
                    check = false;
                    break;
                }
            }
        }
        else{
            int i;
            for(i = 0; i < toCheckValueList.size() - 1; i++){
                String value1 = (String) toCheckValueList.get(i);
                String value2 = (String) toCheckValueList.get(i+1);
                if(sortWay == 1) {
                    if(value1.compareTo(value2) == 1){
                        check = false;
                        break;
                    }
                }
                else
                if(value1.compareTo(value2) == -1){
                    check = false;
                    break;
                }
            }
        }
        if(check)
            return "true";
        else
            return "false";
    }
}
