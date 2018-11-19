package cn.ruc.edu.basecore;

import cn.ruc.edu.mbdatagen.Testpath;

import org.apache.http.impl.cookie.PublicSuffixDomainFilter;
import org.apache.tomcat.util.http.fileupload.ThresholdingOutputStream;
import org.elasticsearch.search.collapse.CollapseBuilder;

import com.mongodb.DB;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class UnStrDBBase<DBBase> {
    //Database Info
    public String ip;
    public int port;
    public String dbName;
    public String collectionName; //if the database has
    public DBBase dbBase; //you need to set this database type

    //These are for show progress and count the file count
    public String filepath;
    public int filesloadall = 0; //This is be read from the disk
    public int filesloadcount = 0;
    public int filesdowncount = 0;
    public int metasloadcount = 0;
    public int metasdowncount = 0;

    public int tempfilemetaloadcount = 0;
    public int tempfilemetadowncount = 0;
    public long tempfilesloadspread = 0;
    public long tempfilesdownspread = 0;

    public List<Integer> fileMetaloadList = new ArrayList<>();
    public List<Integer> fileMetadownList = new ArrayList<>();
    public List<Float> filesLoadList = new ArrayList<>();
    public List<Float> filesDownList = new ArrayList<>();

    //This session is for check if the database is linked or not
    public HttpSession session;

    //These varables are used for query function
    public LocalDateTime simulationStartDateTime;
    public LocalDateTime simulationEndDateTime;
    public Map<String, Long> mapAreaName;
    //public List<Map.Entry<String, Long>> sortedByValueAreaNameMap;
    public int towerheigh_min;
    public int towerheigh_max;
    public int sylenth_min;
    public int sylenth_max;
    public int sywidth_min;
    public int sywidth_max;

    public LocalDateTime fileDirName;

    //Finish is used to check if the query is finished
    public boolean finish = false;
    //normal is used to check if the query is doing
    public boolean normal = true;

    public int activecount = 0;

    public FileFunction fileFunction = new FileFunction();
    DecimalFormat fnum  =  new  DecimalFormat("##0.00");

    public void SetDbIp(String ip){ this.ip = ip; }
    public void SetDbPort(int port){ this.port = port; }
    public void SetDbName(String dbName){ this.dbName = dbName; }
    public void SetCollectionName(String collectionName){ this.collectionName = collectionName; }
    public void SetHttpSession(HttpSession session){ this.session = session;}
    //<--In this area, function is used to link and dislink database-->
    /*
    When it is succeed, remind to session.setAttribute("dbport", this.port);
     */
    public abstract boolean LinkDb() throws UnknownHostException;

    /*
    Remind to session.removeAttribute("dbport");
     */
    public abstract boolean DislinkDb();

    //<--In this area, functions are used to get database information-->
    /*
    This is for get file count from database.
    As an example, the MongoDB is for this:
    public long GetDbFilesCount()
	{
		if(mydb.getCollection("fs.files").getCount() == 0)
			return 0;
		return mydb.getCollection("fs.files").getCount();
	}
     */
    public abstract long GetDbFilesCount();

    public String GetDBName() { return dbName; }

    /*
    This is for getting database size, it is used to measure the
    database storage rate.
    As an example, the MongoDB is for this:
    public double GetDataSize()
    {
        if(mydb.getStats().get("dataSize") == null)
            return 0;
        return (double) mydb.getStats().get("dataSize");
    }
    */
    public abstract double GetDataSize();

    /*
   This is for getting storage size, it is used to measure the
   database storage rate.
   As an example, the MongoDB is for this:
   public double GetStorageSize()
    {
        return (double) mydb.getStats().get("storageSize");
    }
   */
   public abstract double GetStorageSize();

    //<--In this area, functions are used to get translation information in doing-->
    public int GetFileLoadCount() { return this.filesloadcount; }
    public int GetFileDownCount() { return this.filesdowncount; }
    public int GetMetaLoadCount() { return this.metasloadcount; }
    public int GetMetaDownCount() { return this.metasdowncount; }

    public long GetTempFileLoadSpread()
    {
        long back = this.tempfilesloadspread;
        this.tempfilesloadspread = 0;
        if(back == 0)
        {
            return 0;
        }
        fileFunction.WriteLogforTrans(fnum.format((float)back / (1024*1024)), "loadfile");
        this.filesLoadList.add((float)back / (1024 * 1024));
        return back;
    }

    public long GetTempFileDownSpread()
    {
        long back = this.tempfilesdownspread;
        this.tempfilesdownspread = 0;
        if(back == 0)
        {
            return 0;
        }
        fileFunction.WriteLogforTrans(fnum.format((float)back / (1024*1024)), "downfile");
        this.filesDownList.add((float)back / (1024 * 1024));
        return back;
    }

    public int GetTempMetaLoadSpread()
    {
        int back = this.tempfilemetaloadcount;
        this.tempfilemetaloadcount = 0;
        if(back == 0)
        {
            return 0;
        }
        fileFunction.WriteLogforTrans(back + "", "loadmeta");
        this.fileMetaloadList.add(back);
        return back;
    }

    public int GetTempMetaDownSpread()
    {
        int back = this.tempfilemetadowncount;
        this.tempfilemetadowncount = 0;
        if(back == 0)
        {
            return 0;
        }
        fileFunction.WriteLogforTrans(back + "", "downmeta");
        this.fileMetadownList.add(back);
        return back;
    }

    public String GetTimeFileLoadSpeed(){
        int startindex = 0;
        if(this.filesLoadList.size() > 50)
            startindex = this.filesLoadList.size() - 50; //
        List<Float> fileload100 = this.filesLoadList.subList(startindex, this.filesLoadList.size() - 1);
        if(fileload100.size() <= 0){
            return "[" + "{\"speed\":" + 0 + "}]" ;
        }
        String string = "[";
        for(float s : fileload100)
            string += "{\"speed\":" + fnum.format(s) + "},";
        string = string.substring(0, string.length() - 1) + "]";
        return string;
    }

    public String GetTimeFileDownSpeed(){
        int startindex = 0;
        if(this.filesDownList.size() > 50)
            startindex = this.filesDownList.size() - 50; //
        List<Float> filedown100 = this.filesDownList.subList(startindex, this.filesDownList.size() - 1);
        if(filedown100.size() <= 0){
            return "[" + "{\"speed\":" + 0 + "}]" ;
        }
        String string = "[";
        for(float s : filedown100)
            string += "{\"speed\":" + fnum.format(s) + "},";
        string = string.substring(0, string.length() - 1) + "]";
        return string;
    }

    public String GetTimeMetaLoadCount(){
        int startindex = 0;
        if(this.fileMetaloadList.size() > 50)
            startindex = this.fileMetaloadList.size() - 50; //
        List<Integer> metaload100 = this.fileMetaloadList.subList(startindex, this.fileMetaloadList.size() - 1);
        if(metaload100.size() <= 0){
            return "[" + "{\"speed\":" + 0 + "}]" ;
        }
        String string = "[";
        for(int s : metaload100)
            string += "{\"speed\":" + fnum.format(s) + "},";
        string = string.substring(0, string.length() - 1) + "]";
        return string;
    }

    public String GetTimeMetaDownCount(){
        int startindex = 0;
        if(this.fileMetadownList.size() > 50)
            startindex = this.fileMetadownList.size() - 50; //
        List<Integer> metadown100 = this.fileMetadownList.subList(startindex, this.fileMetadownList.size() - 1);
        if(metadown100.size() <= 0){
            return "[" + "{\"speed\":" + 0 + "}]" ;
        }
        String string = "[";
        for(int s : metadown100)
            string += "{\"speed\":" + fnum.format(s) + "},";
        string = string.substring(0, string.length() - 1) + "]";
        return string;
    }

    //<--In this area, functions are used to do translation like loadfile, loadmeta and so on-->
    public abstract boolean UpLoadFile(String filepath);
    public abstract boolean UpLoadMetafile(String metapath);
    public abstract boolean DownLoadFile(String downpath);
    public abstract boolean DownLoadMetaFile(String downpath);


    public boolean GetFinish(){
        return this.finish;
    }

    public boolean GetNormal(){
        return this.normal;
    }

    public void SetTransLogPath(){
        fileFunction.SetLogDataPathforTrans((String) session.getAttribute("translogpath"));
    }

    public void SetQueryLogPath(){
        fileFunction.SetLogDataPathforQuery((String) session.getAttribute("querylogpath"));
    }

    public void setFileDirName(LocalDateTime time){
        this.fileDirName = time;
    }

    public String getHistorySetting() {
        //遍历historysettings文档，选择最新版本(文档名最大)
        //这里需要设置正确的读文件地方
        String path = Testpath.class.getClassLoader().getResource("HistorySettings").getPath();
        File historyfile = new File(path);
        File[] arrayfile = historyfile.listFiles();
        long imax = Long.valueOf(arrayfile[0].getName().substring(0, arrayfile[0].getName().length()-4));
        for(int i=0;i<arrayfile.length;i++){
            if(Long.valueOf(arrayfile[i].getName().substring(0, arrayfile[i].getName().length()-4))>imax) {
                imax =Long.valueOf(arrayfile[i].getName().substring(0, arrayfile[i].getName().length()-4));
            }
        }
        String string = String.valueOf(imax)+".txt";
        return string;
    }

    /*public void getValue() throws IOException {
        String filename = this.getHistorySetting();
        BufferedReader reader = null;
        try {
            //这里需要设置正确的读文件地方
            String path = Testpath.class.getClassLoader().getResource("HistorySettings").getPath();
            reader = new BufferedReader(new FileReader(path + "/" + filename));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String sline="";
        String[] sarea = null;
        int count = 0;
        sline = reader.readLine();
        count = Integer.valueOf(sline.split("\t")[1]);

        String[] whole = new String[count];
        String[] stowerheigh = new String[count];
        String[] ssylenth = new String[count];
        String[] ssywidth = new String[count];
        ArrayList list = new ArrayList();
        try {
            while((sline = reader.readLine())!=null) {
                if(sline.split("\t").length>9)
                    list.add(sline);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(list.size());
        whole = (String[]) list.toArray(new String[list.size()]);
        for(int i=0;i<whole.length;i++) {
            stowerheigh[i] = whole[i].split("\t")[2];
            //System.out.println(stowerheigh[i]);
            ssylenth[i] = whole[i].split("\t")[3];
            ssywidth[i] = whole[i].split("\t")[4];
        }

        this.towerheigh_min=this.zuiZhi(stowerheigh)[0];
        this.towerheigh_max=this.zuiZhi(stowerheigh)[1];
        this.sylenth_min=this.zuiZhi(ssylenth)[0];
        this.sylenth_max=this.zuiZhi(ssylenth)[1];
        this.sywidth_min=this.zuiZhi(ssywidth)[0];
        this.sywidth_max=this.zuiZhi(ssywidth)[1];
    }
    */

    public void newGetValue(String fileName) throws IOException {
        this.fileDirName = LocalDateTime.now();
        BufferedReader reader = null;
        ArrayList<String> areaNameList = new ArrayList<>();
        ArrayList<Integer> towerHeighList = new ArrayList<>();
        ArrayList<Integer> fansLengthList = new ArrayList<>();
        ArrayList<Integer> fansWidthList = new ArrayList<>();
        try {
            //这里需要设置正确的读文件地方
            String path = Testpath.class.getClassLoader().getResource("HistorySettings").getPath();
            System.out.println(path + "/" + fileName);
            reader = new BufferedReader(new FileReader(path + "/" + fileName));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String lineString;

        while((lineString = reader.readLine()) != null){
            if(lineString.split("\t").length > 9){
                String[] lineContent = lineString.split("\t");
                towerHeighList.add(Integer.parseInt(lineContent[2]));
                fansLengthList.add(Integer.parseInt(lineContent[3]));
                fansWidthList.add(Integer.parseInt(lineContent[4]));
                areaNameList.add(lineContent[5]);
            }
            else if(lineString.contains("Simulation")){
                if(lineString.contains("Start")) {
                    String startTimeString = lineString.split(":\t")[1];
                    LocalDate startDate = LocalDate.parse(startTimeString.split("\t")[0]);
                    String date = startTimeString.split("\t")[1];
                    LocalTime startTime = LocalTime.of(Integer.parseInt(date.split(":")[0]),
                            Integer.parseInt(date.split(":")[1]), Integer.parseInt(date.split(":")[2]));
                    this.simulationStartDateTime = LocalDateTime.of(startDate, startTime);
                }
                else if (lineString.contains("End")){
                    String endTimeString = lineString.split(":\t")[1];
                    LocalDate endDate = LocalDate.parse(endTimeString.split("\t")[0]);
                    String date = endTimeString.split("\t")[1];
                    LocalTime endTime = LocalTime.of(Integer.parseInt(date.split(":")[0]),
                            Integer.parseInt(date.split(":")[1]), Integer.parseInt(date.split(":")[2]));
                    this.simulationEndDateTime = LocalDateTime.of(endDate, endTime);
                }
            }
        }

        this.mapAreaName = areaNameList.parallelStream().collect(Collectors.groupingBy(String::toString,Collectors.counting()));
        /*
        Comparator<Map.Entry<String, Long>> sortedByValueMap = new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                System.out.println((int)(o2.getValue() - o1.getValue()));
                return (int)(o2.getValue() - o1.getValue());
            }
        };
        List<Map.Entry<String, Long>> list = new ArrayList<>(this.mapAreaName.entrySet());
        Collections.sort(list,sortedByValueMap);
        this.sortedByValueAreaNameMap = list;
        */

        this.towerheigh_max = towerHeighList.parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        this.towerheigh_min = towerHeighList.parallelStream().min(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        this.sylenth_max = fansLengthList.parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        this.sylenth_min = fansLengthList.parallelStream().min(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        this.sywidth_max = fansWidthList.parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        this.sywidth_min = fansWidthList.parallelStream().min(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();


        System.out.println("simualtion_start_data_time : " + this.simulationStartDateTime);
        System.out.println("simualtion_end_data_time : " + this.simulationEndDateTime);
        this.mapAreaName.forEach((a,b) -> System.out.println("Key : " + a + ", Value : " + b));
        System.out.println("tower_height_max : " + this.towerheigh_max + " tower_height_min : " + this.towerheigh_min);
        System.out.println("fans_length_max : " + this.sylenth_max + " tower_height_min : " + this.sylenth_min);
        System.out.println("fans_width_max : " + this.sywidth_max + " tower_width_min : " + this.sywidth_min);

    }

    public String getDeviceTypeQueryContent(){
        int s = new Random().nextInt(3);

        switch(s){
            case 0 : return "BLADE";
            case 1 : return "DYNAMO";
            case 2 : return "TRANSMISSION";
            default: return "BLADE";
        }
    }

    public ArrayList<String> getAreaNameQueryContentList(){
        ArrayList<String> content = new ArrayList<>();
        int allAreaCount = this.mapAreaName.keySet().size();
        String[] areaNameArray = new String[allAreaCount];
        this.mapAreaName.keySet().toArray(areaNameArray);
        //Confirm that it will get at least two values
        //int count = new Random().nextInt(allAreaCount - 1) + 2;
        int choseCount = new Random().nextInt(allAreaCount);
        content.add(areaNameArray[choseCount]);
        for(int i = 1; i < 3; i++){
            boolean checkBoolean = true;
            choseCount = new Random().nextInt(allAreaCount);
            String name = areaNameArray[choseCount];
            for(int k = 0; k < content.size(); k++)
                if(name.equals(content.get(k))){
                    checkBoolean = false;
                    break;
                }
            if(checkBoolean)
                content.add(name);
            else
                i--;
        }
        return content;
    }

    public LocalDateTime[] getTimeQueryContentArray(){
        LocalDateTime[] content = new LocalDateTime[2];
        Long startTime = this.simulationStartDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond();
        Long endTime = this.simulationEndDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond();
        Long minusSecondTime = endTime - startTime;
        int hourAreaNum = (int)((endTime -startTime) / 3600);

        if(hourAreaNum == 0){
            content[0] = this.simulationStartDateTime;
            content[1] = this.simulationEndDateTime;
        } else{
            int startRandom = new Random().nextInt(minusSecondTime.intValue() / 2);
            hourAreaNum = hourAreaNum / 2;
            content[0] = this.simulationStartDateTime.plusSeconds(startRandom);
            content[1] = content[0].plusSeconds(hourAreaNum * 3600);
            if(content[1].isAfter(this.simulationEndDateTime))
                content[1] = simulationEndDateTime;
        }
        return content;
    }

    public String getAreaNameContent(){
        int allAreaCount = this.mapAreaName.keySet().size();
        String[] areaNameArray = new String[allAreaCount];
        this.mapAreaName.keySet().toArray(areaNameArray);
        int count = new Random().nextInt(allAreaCount);
        return areaNameArray[count];
    }

    public int[] getNumerRange(int type){
        int[] contentArray = new int[2];
        int minusNum = 0;
        switch (type){
            case 0 : {
                minusNum = this.sywidth_max - this.sywidth_min;
                contentArray[0] = new Random().nextInt(minusNum / 4) + this.sywidth_min;
                contentArray[1] = this.sywidth_max - new Random().nextInt(minusNum / 4);
                return contentArray;
            }
            case 1 : {
                minusNum = this.sylenth_max - this.sylenth_min;
                contentArray[0] = new Random().nextInt(minusNum / 4) + this.sylenth_min;
                contentArray[1] = this.sylenth_max - new Random().nextInt(minusNum / 4);
                return contentArray;
            }
            case 2 : {
                minusNum = this.towerheigh_max - this.towerheigh_min;
                contentArray[0] = new Random().nextInt(minusNum / 4) + this.towerheigh_min;
                contentArray[1] = this.towerheigh_max - new Random().nextInt(minusNum / 4);
                return contentArray;
            }
            default:{
                return null;
            }
        }
    }

    public SortedTuple<String, String, Integer> getSortAttributeName(){
        int i = new Random().nextInt(8);
        String sortAttributeName;
        switch (i){
            case 0: sortAttributeName = "DEVICETYPE";break;
            case 1: sortAttributeName =  "FILENAME";break;
            case 2: sortAttributeName =  "AREANAME";break;
            case 3: sortAttributeName =  "TOWERHEIGHT";break;
            case 4: sortAttributeName =  "BLADELENGTH";break;
            case 5: sortAttributeName =  "BLADEWIDTH";break;
            case 6: sortAttributeName =  "DEVICEID";break;
            case 7: sortAttributeName =  "SIMULATIONTIME";break;
            default: sortAttributeName =  "DEVICETYPE";break;
        }
        String sortedDataType;
        if(i <= 2)
            sortedDataType = "String";
        else if(i <= 6)
            sortedDataType = "Integer";
        else
            sortedDataType = "Date";
        int sortedType = new Random().nextInt(2);
        if(sortedType == 0)
            sortedType = -1;
        return new SortedTuple<>(sortAttributeName, sortedDataType, sortedType);
    }

    public Map<String, String> getHotQueryContent(){
        HashMap<String, String> hotQueryContent = new HashMap<>();

        int i = new Random().nextInt(5);
        String attributeName;
        switch (i) {
            case 0: attributeName = "DEVICETYPE";break;
            case 1: attributeName = "AREANAME";break;
            case 2: attributeName =  "TOWERHEIGHT";break;
            case 3: attributeName =  "BLADELENGTH";break;
            case 4: attributeName =  "BLADEWIDTH";break;
            default: attributeName = "DEVICETYPE";
        }

        String attributeContent;
        switch (i){
            case 0: attributeContent = getDeviceTypeQueryContent();break;
            case 1: attributeContent = getAreaNameContent();break;
            case 2: {
                int[] numberContentArray = getNumerRange(DevicePartNameEnum.TOWERHEIGHT.getValue());
                attributeContent = numberContentArray[0] + "-" + numberContentArray[1];break;
            }
            case 3: {
                int[] numberContentArray = getNumerRange(DevicePartNameEnum.BLADELENGTH.getValue());
                attributeContent = numberContentArray[0] + "-" + numberContentArray[1];break;
            }
            case 4: {
                int[] numberContentArray = getNumerRange(DevicePartNameEnum.BLADEWIDTH.getValue());
                attributeContent = numberContentArray[0] + "-" + numberContentArray[1];break;
            }
            default: attributeContent = getDeviceTypeQueryContent();break;
        }

        System.out.println(attributeContent);
        hotQueryContent.put(attributeName, attributeContent);
        return hotQueryContent;
    }

    //求最大值最小值
    public int[] zuiZhi(String[] str) {
        int[] c = new int[2];
        c[0] = Integer.valueOf(str[0]);//min
        c[1] = Integer.valueOf(str[0]);//max
        for(int i=0;i<str.length;i++) {
            if(c[0]>Integer.valueOf(str[i]))c[0]=Integer.valueOf(str[i]);
            if(c[1]<Integer.valueOf(str[i]))c[1]=Integer.valueOf(str[i]);
        }
        return c;
    }

    public long GetSeconds(String date,int hour,int minute,int second) throws ParseException {
        Calendar dsDate = Calendar.getInstance();
        int[] t = new int[3];
        int i = 0;
        String[] s = date.split("-");
        for(String ss : s)
        {
            t[i] = Integer.parseInt(ss);
            i++;
        }
        dsDate.set(t[0], t[1] - 1, t[2], hour, minute, second);
        long submillisec = dsDate.getTime().getTime();
        long allsecond =  submillisec / 1000;
        return allsecond;
    }

    //下列用于对文件名称按照递增排序
    @SuppressWarnings("unchecked")
    public static File[] OrderFileName(File[] s){
        List<File> fileList = Arrays.asList(s);
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2){
                if(f1.isDirectory() && f2.isFile())
                    return -1;
                if(f1.isFile() && f2.isDirectory())
                    return 1;
                return f1.getName().compareTo(f2.getName());
            }
        });
        return s;

    }


    //下列用于检测版本测试结果，需要传入文件生成总路径
    @SuppressWarnings("resource")
    public int[] CopyCheck(String datapath, HashMap<String, Integer> resulthm) throws NumberFormatException, IOException{
        int allcount = 0;
        int correctcount = 0;
        int errorcount = 0;
        System.out.println("成功进入检测");
        File checkfilepath = new File(datapath + "/.copynum");
        System.out.println("读取到文件路径：" + datapath + "/.copynum");
        File[] checkfileList = checkfilepath.listFiles();
        checkfileList = this.OrderFileName(checkfileList);

        int txtcount = checkfileList.length;
        Set<String> checkSet = resulthm.keySet();
        Iterator<String> check = checkSet.iterator();
        while(check.hasNext()){
            allcount++;
            System.out.println(allcount);
            String key = check.next();
            String ss = key.split("\\$")[0];
            int fileid = Integer.parseInt(ss.split("_")[0]);
            int checkfilename = fileid - (int)(fileid / txtcount) * txtcount;
            if(checkfilename == 0)
                checkfilename = txtcount;
            BufferedReader reader = new BufferedReader(new FileReader(datapath + "/.copynum/" + checkfilename + ".txt"));
            String filecontent = null;
            while((filecontent = reader.readLine()) != null){
                if(filecontent.contains(ss))
                {
                    int copynum = resulthm.get(key);
                    int checkcopynum = Integer.parseInt(filecontent.split(":")[1]);
                    if(copynum == checkcopynum){
                        correctcount++;
                        System.out.println("匹配成功");
                    }
                    else
                        errorcount++;
                    break;
                }
                else
                    continue;
            }
        }
        int[] result = new int[3];
        result[0] = allcount;
        result[1] = correctcount;
        result[2] = errorcount;
        return result;
    }

    public float getVariance(Float[] inputData) {
        float ave = 0;
        for (int i = 0; i < inputData.length; i++)
            ave += inputData[i];
        ave /= inputData.length;

        float sum = 0;
        for(int i = 0;i<inputData.length;i++)
            sum += (inputData[i] - ave)  * (inputData[i] - ave) ;
        sum /= inputData.length;
        return sum;
    }

    public int[] AbnormalCheck(String datapath, ArrayList<String> abnormalFileName){
        List<String> abnormalSorted = abnormalFileName.parallelStream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
        File[] sFile = new File(datapath + "/.abnormal").listFiles();
        ArrayList<String> trueList = new ArrayList<>();
        for(File s : sFile){
            System.out.println(s.getAbsolutePath());
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
                String lineString = null;
                while((lineString = br.readLine()) != null)
                    trueList.add(lineString);
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> trueSorted = trueList.parallelStream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
        int trueCheckNum = 0;
        for(int i = 0, j = 0; i < abnormalSorted.size() && j < trueSorted.size();){
            int check = abnormalSorted.get(i).compareTo(trueSorted.get(j));
            if(check < 0)
                i++;
            else if(check == 0){
                i++;
                j++;
                trueCheckNum++;
            }
            else
                j++;
        }
        int[] result = new int[3];
        result[0] = abnormalSorted.size();
        System.out.println("检测出来的文件数量：" + result[0]);
        result[1] = trueSorted.size();
        System.out.println("真正异常的文件数量：" + result[1]);
        result[2] = trueCheckNum;
        System.out.println("符合的异常文件数量：" + result[2]);
        return result;
    }

    //根据查询内容下载仿真文件
    public abstract void DownloadFileByList(List<String> downList, String downfilename, String downfilepath);

    //<--In this area, functions are used to work single varables queries-->
    public void SingleAttQuerry(String filedownpath) throws IOException {
        this.SetQueryLogPath();
        this.finish = false;
        List<String> object1 = QueryOne();
        this.DownloadFileByList(object1,QueryFileName.DOWN_QUERY_ONE, filedownpath);
        List<String> object2 = QueryTwo();
        this.DownloadFileByList(object2, QueryFileName.DOWN_QUERY_TWO, filedownpath);
        List<String> object3 = QueryThree();
        this.DownloadFileByList(object3, QueryFileName.DOWN_QUERY_THREE, filedownpath);
        this.finish = true;
    }

    public void MultiAttQuery(String downfilepath) throws IOException {
        this.SetQueryLogPath();
        this.finish = false;
        List<String> object4 = QueryFour();
        DownloadFileByList(object4, QueryFileName.DOWN_QUERY_FOUR, downfilepath);
        List<String> object5 = QueryFive();
        DownloadFileByList(object5, QueryFileName.DOWN_QUERY_FIVE, downfilepath);
        List<String> object6 = QuerySix();
        DownloadFileByList(object6, QueryFileName.DOWN_QUERY_SIX, downfilepath);
        List<String> object7 = QuerySeven();
        DownloadFileByList(object7, QueryFileName.DOWN_QUERY_SEVEN, downfilepath);
        this.finish = true;
    }

    public abstract void VersionQuery(String datapath) throws IOException;

    public void SortQuery(){
        this.SetQueryLogPath();
        this.finish = false;
        QueryNine();
        QueryTen();
        QueryEleven();
        this.finish = true;
    }

    public void SatisticsQuery(){
        this.SetQueryLogPath();
        this.finish = false;
        QueryTwelve();
        QueryThirteen();
        QueryFourteen();
        this.finish = true;
    }

    public abstract void AbnormalQuery(String datapath);

    public abstract void HotFileQuery(int queryCount, int interval);

    public abstract List<String>  QueryOne();
    public abstract List<String>  QueryTwo() throws IOException;
    public abstract List<String>  QueryThree();

    public abstract List<String> QueryFour();
    public abstract List<String> QueryFive();
    public abstract List<String> QuerySix() throws IOException;
    public abstract List<String> QuerySeven();

    public abstract void QueryNine();
    public abstract void QueryTen();
    public abstract void QueryEleven();

    public abstract void QueryTwelve();
    public abstract void QueryThirteen();
    public abstract void QueryFourteen();
    
}
