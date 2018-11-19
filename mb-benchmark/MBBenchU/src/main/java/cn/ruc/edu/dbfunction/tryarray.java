package cn.ruc.edu.dbfunction;

import cn.ruc.edu.basecore.DevicePartNameEnum;
import cn.ruc.edu.basecore.FileFunction;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class tryarray <E>{
    public static void main(String[] args) throws IOException {
//        Mongo s = new Mongo("localhost",27017);
//        DB db = s.getDB("FileDB");
//        DBCollection dbCollection = db.getCollection("FileMeta");
//        DBObject query = new BasicDBObject();
//        query.put("DEVICETYPE", "BLADE");
//        DBCursor dbCursor = dbCollection.find(query);
//        int count = dbCursor.count();
//        ArrayList<String> filename = new ArrayList<>();
//        while(dbCursor.hasNext()){
//            DBObject dbObject = dbCursor.next();
//            filename.add(dbObject.get("FILENAME") + "," + dbObject.get("FILECOPYNUM"));
//        }
//
//        List<String> fileSorted = filename.parallelStream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
//        for(String sss : fileSorted)
//            System.out.println(sss);
       /*
        File file = new File("D:\\newlogdata\\MongoDB\\2018-5-7-2\\2018-5-7-1\\" + QueryFileName.DOWN_QUERY_ONE + ".txt");
        ArrayList<Float> filelist = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String content = null;
            while((content = br.readLine()) != null)
                filelist.add(Float.parseFloat(content));
            System.out.println(filelist.size());
            if(filelist.size() > 300){
                int filter = filelist.size() / 300;
                System.out.println(filter);
                ArrayList<Float> leftQueryOne300List = new ArrayList<Float>();
                float sum = 0;
                int count = 0;
                for(int i = 0; i < filelist.size(); i++){
                    if((i+1) % filter == 0){
                        leftQueryOne300List.add(sum / filter);
                        sum = 0;
                        count = 0;
                    }
                    else
                        sum += filelist.get(i);
                    count ++;
                }
                if(sum != 0)
                    leftQueryOne300List.add(sum / count);
                System.out.println(leftQueryOne300List);
                filelist = leftQueryOne300List;
            }

            System.out.println(filelist.size());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
//        LocalDateTime date = LocalDateTime.of(2018, Month.MAY, 23, 0, 0, 0);
//        if(date.toLocalTime().getSecond() == 0){
//            System.out.println(date.toLocalDate().toString() + " " +  date.toLocalTime().toString() + ":00");
//        }
//        else
//            System.out.println(date.toLocalDate().toString() + " " +  date.toLocalTime().toString());
        /*
        Settings settings = Settings.builder().build();
        try {
            TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"),9300));
            final ClusterHealthResponse response = client
                    .admin()
                    .cluster()
                    .prepareHealth()
                    .setWaitForGreenStatus()
                    .setTimeout( TimeValue.timeValueSeconds( 2 ) )
                    .execute()
                    .actionGet();
            System.out.println(response.getStatus());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        */
        /*
        HashMap<String, Integer> ww = new HashMap<>();
        ww.put("1.1", 1);
        ww.put("1.2", 1);
        ww.put("2.1", 2);
        System.out.println(ww.size());
        Iterator<Map.Entry<String, Integer>> jaja = ww.entrySet().iterator();
        while(jaja.hasNext()){
            Map.Entry<String, Integer> s = jaja.next();
            System.out.println(s.getKey() + "," + s.getValue());
        }
        */

        /*
        Map<String, Object> haha = new HashMap<>();
        haha.put("wo", 1);

        ArrayList<String> list = new ArrayList<>();
        list.add("safd");
        list.add("sfasew");

        Object time = LocalDateTime.of(1994,4,4,4,4);
        System.out.println(time.getClass().getName());

        haha.put("vi", list);
        Set<String> sha = haha.keySet();
        for(String keyName : sha){

            Object value = haha.get(keyName);
            if(value.getClass().getName().equals("java.util.ArrayList"))
                for(Object o : (ArrayList)value)
                    System.out.println(o.getClass().getName());
            System.out.println(haha.get(keyName).getClass().getName());
            System.out.println(haha.get(keyName));
        }

        ArrayList<Integer> listNum = new ArrayList<>();
        listNum.add(1);
        listNum.add(100);
        Object s = listNum;
        for(Object ws : (ArrayList) s){

        }
        int j = 10;
        */

        /*
        Mongo dbBase = new Mongo("localhost", 27017);
        DB mydb = dbBase.getDB("FileDB");
        DBCollection collection = mydb.getCollection("FileMeta");

        BasicDBObject AreaObj = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add("XiBei");
        values.add("XiNan");
        values.add("QingZangGaoYuan");
        AreaObj.put("AREANAME",new BasicDBObject("$in",values));
        long start = System.nanoTime();
        DBCursor dbCursor = collection.find(AreaObj);
        while (dbCursor.hasNext()){
            System.out.println(dbCursor.next().get("_id"));
        }

        int count = 0;
        //unwind操作拆分WINDSPEED数组
        DBObject unwind = new BasicDBObject("$unwind", "$WINDSPEED");
        //Group操作
        DBObject groupFields = new BasicDBObject("_id","$_id");//按_id分组，每个文件分为一组
        groupFields.put("MaxSpeed", new BasicDBObject("$max", "$WINDSPEED"));
        DBObject group = new BasicDBObject("$group", groupFields);
        //查看Group结果
        AggregationOutput output = collection.aggregate(unwind,group);// 执行 aggregation命令
        count = output.results().toString().split(",").length/2;
        long end = System.nanoTime();

        Iterator<DBObject> ss = output.results().iterator();

        while(ss.hasNext()){
            System.out.println(ss.next().get("_id"));
        }

        int rightCount = CheckSatisatic(output.results().iterator(), "MAX", "WINDSPEED", "MaxSpeed");
        System.out.println(rightCount + " : " + count);

        DBObject query = new BasicDBObject();
        query.put("DEVICETYPE","BLADE");
        dbCursor = collection.find(query).sort(new BasicDBObject("SIMULATIONTIME",1));

        boolean hja = CheckSorted(dbCursor.iterator(), "SIMULATIONTIME", "String", "<");

        System.out.println(hja);
        */


        /*
        Map<String, String> map = new TreeMap<>();
        map.put("leftDevice1","haha");
        map.put("leftDevice1sameCount","2");
        map.put("leftDevice3","haha");
        map.put("leftDevice3sameCount","1");
        map.put("leftDevice2","haha");
        map.put("leftDevice2sameCount","4");
        map.put("rightDevice1","haha");
        map.put("rightDevice1sameCount","2");
        map.put("rightDevice3","haha");
        map.put("rightDevice3sameCount","1");
        map.put("rightDevice2","haha");
        map.put("rightDevice2sameCount","4");

        map.forEach((a, b)-> System.out.println("key : " + a + ", " + "value : " + b));

        System.out.println(map.get("rightDevice4"));

        if(map.get("rightDevice4") == null)
            System.out.println("可以获取null");
            */
        /*
        File[] dbFile = new File("D:/newlogdata/MongoDB").listFiles();
        String[] dbFileName = new String[dbFile.length];
        for(int i = 0; i < dbFile.length; i++)
            dbFileName[i] = dbFile[i].getName();
        String[] sortedFileName = new String[dbFileName.length];
        ArrayList<LocalDateTime> dbFileNameDateList = new ArrayList<>();
        LocalDate[] dbFileNameDate = new LocalDate[sortedFileName.length];
        for(int i = 0; i < sortedFileName.length; i++){
            String dbFileNameContent = dbFileName[i];
            int year = Integer.parseInt(dbFileNameContent.split("-")[0]);
            int month = Integer.parseInt(dbFileNameContent.split("-")[1]);
            int day = Integer.parseInt(dbFileNameContent.split("-")[2]);
            int mark = Integer.parseInt(dbFileNameContent.split("-")[3]);
            dbFileNameDateList.add(LocalDateTime.of(year, month, day, mark, 0, 0));
        }

        dbFileNameDateList.sort((a, b) -> a.compareTo(b));
        dbFileNameDateList.forEach(a -> System.out.println(a.getYear() + "-" + a.getMonthValue() + "-" + a.getDayOfMonth()
             + "-" + a.getHour()));
             */

        /*
        BufferedReader reader = null;

        LocalDateTime simulationStartDateTime = null;
        LocalDateTime simulationEndDataTime = null;
        ArrayList<String> areaNameList = new ArrayList<>();
        ArrayList<Integer> towerHeighList = new ArrayList<>();
        ArrayList<Integer> fansLengthList = new ArrayList<>();
        ArrayList<Integer> fansWidthList = new ArrayList<>();


        try {
            //这里需要设置正确的读文件地方
            reader = new BufferedReader(new FileReader("D:/GitHub/" +
                    "mbBench-U/MBBenchU/src/main/resources/HistorySettings/" +
                    "1521600592558.txt"));
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
                    simulationStartDateTime = LocalDateTime.of(startDate, startTime);
                }
                else if (lineString.contains("End")){
                    String endTimeString = lineString.split(":\t")[1];
                    LocalDate endDate = LocalDate.parse(endTimeString.split("\t")[0]);
                    String date = endTimeString.split("\t")[1];
                    LocalTime endTime = LocalTime.of(Integer.parseInt(date.split(":")[0]),
                            Integer.parseInt(date.split(":")[1]), Integer.parseInt(date.split(":")[2]));
                    simulationEndDataTime = LocalDateTime.of(endDate, endTime);
                }
            }
        }

        Map<String, Long> mapAreaName = areaNameList.parallelStream().collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        Integer towerheigh_max = towerHeighList.parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        Integer towerheigh_min = towerHeighList.parallelStream().min(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        Integer sylenth_max = fansLengthList.parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        Integer sylenth_min = fansLengthList.parallelStream().min(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        Integer sywidth_max = fansWidthList.parallelStream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();
        Integer sywidth_min = fansWidthList.parallelStream().min(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get();

        System.out.println("simualtion_start_data_time : " + simulationStartDateTime);
        System.out.println("simualtion_end_data_time : " + simulationEndDataTime);
        mapAreaName.forEach((a,b) -> System.out.println("Key : " + a + ", Value : " + b));
        System.out.println("tower_height_max : " + towerheigh_max + " tower_height_min : " + towerheigh_min);
        System.out.println("fans_length_max : " + sylenth_max + " tower_height_min : " + sylenth_min);
        System.out.println("fans_width_max : " + sywidth_max + " tower_width_min : " + sywidth_min);
        */

        /*
        long size = 0;
        File file = new File("J:/testData");
        System.out.println(file.length());
        File[] fileList = file.listFiles();
        System.out.println(fileList.length);
        for(File dirFile : fileList){
            if(!dirFile.getName().contains(".")){
                File[] files = dirFile.listFiles();
                for(File contentFile : files){
                    File[] trueContentFileArray = contentFile.listFiles();
                    for(File trueContentFile : trueContentFileArray)
                        size += trueContentFile.length();
                }
            }
        }
        System.out.println("所有文件的容量" + size);
        System.out.println("所有文件的东西：" + FileFunction.floatToString((float) size / (1024 * 1024 * 1024)));
       */

        /*
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        ArrayList<Integer> integerArrayList2 = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            integerArrayList.add(i);
            integerArrayList2.add(i);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("hahaha", integerArrayList);
        map.put("heihei", integerArrayList2);
        FileFunction.queryMakeRecord("haha", map);
        */

        /*
        Long newSecond1 = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long newSecond2 = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();

        System.out.println(newSecond1 + " -- " + newSecond2);
        */

        /*
        Long s = new Random().nextLong();
        System.out.println(s);
        */


        String hga = "HAPPppY,ss.sppP";
        //String hgg = hga.replaceAll("\\pP"," ");
        System.out.println(hga.replaceAll("\\pP",""));


        System.out.println(DevicePartNameEnum.BLADEWIDTH.toString());

    }

    public static boolean CheckSorted(Iterator<DBObject> result, String query, String type, String sortWay){
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
                if(sortWay.equals(">")) {
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
                if(sortWay.equals(">")) {
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
                if(sortWay.equals("<")) {
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
        return check;
    }

    public static int CheckSatisatic(Iterator<DBObject> result, String satisaticType, String queryContent, String contentName){
        int rightCount = 0;
        int count = 0;
        Mongo dbBase = new Mongo("localhost", 27017);
        String checkQuery = satisaticType + queryContent;
        while(result.hasNext()){
            count ++;
            DBObject resultObject = result.next();
            ObjectId resultId = (ObjectId) resultObject.get("_id");
            Double resultValue = (Double ) resultObject.get(contentName);
            DB mydb = dbBase.getDB("FileDB");
            DBCollection dbCollection = mydb.getCollection("FileMeta");
            DBObject query = new BasicDBObject();
            query.put("_id", resultId);
            DBCursor checkResultCursor = dbCollection.find(query);
            Double checkResult = (Double ) checkResultCursor.next().get(checkQuery);
            System.out.println("--------------------------------------------------");
            System.out.println("The " + count + " th RightResult : " + checkResult);
            System.out.println("The " + count + " th CheckResult : " + resultValue);
            if(Math.abs(checkResult - resultValue) <= 1E-6)
                rightCount ++;
        }
        return rightCount;
    }
}
