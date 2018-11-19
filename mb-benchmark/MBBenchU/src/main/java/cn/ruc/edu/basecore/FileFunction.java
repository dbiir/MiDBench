package cn.ruc.edu.basecore;

import cn.ruc.edu.mbdatagen.Testpath;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class FileFunction{
    private String transLogPath = null;
    private String queryLogPath = null;

    public void SetLogDataPathforTrans(String path){
        if(this.transLogPath == null)
            this.transLogPath = path;
    }

    public String GetLogPathforTrans(){
        if(this.transLogPath == null)
            return null;
        else
            return this.transLogPath;
    }

    public void SetLogDataPathforQuery(String path){
        if(this.queryLogPath == null)
            this.queryLogPath = path;
    }

    public String GetLogPathforQuery(){
        if(this.queryLogPath == null)
            return null;
        else
            return this.queryLogPath;
    }

    public static int GetFilesNumByPath(String path)
    {
        int filecount = 0;
        File[] allfiles = new File(path).listFiles();
        for(File s : allfiles){
            if(!s.getName().contains(".")){
                File[] inners = new File(s.getAbsolutePath() + "/file").listFiles();
                for(File sFile : inners){
                    filecount ++;
                }
            }
        }
        return filecount;
    }

    public static String floatToString(float num){
        DecimalFormat fnum  =  new  DecimalFormat("##0.00");
        return fnum.format(num);
    }

    public static String floatToPresentString(float num){
        DecimalFormat fnum  =  new  DecimalFormat("##0.00");
        return fnum.format(num * 100);
    }

    public static float GetFilesAllsizeByPath(String path)
    {
        File[] allfiles = new File(path).listFiles();
        long size = 0;
        float sizem = 0;

        for(File s : allfiles){
            if(s.isDirectory()){
                File[] inners = new File(s.getAbsolutePath() + "/file").listFiles();
                for(File sFile : inners){
                    size += sFile.length();
                }
                sizem += size / ( 1024 * 1024);
                size = 0;
            }
        }
        return sizem;
    }

    public static List<File> GetUpLoadFileList(String upLoadFilePath){
        List<File> allFileList = new ArrayList<File>();
        File rootFile = new File(upLoadFilePath);
        File[] rootFileList = rootFile.listFiles();
        if(rootFileList.length == 0)
            return null;
        for(File files : rootFileList) {
            if (files.getName().contains("."))
                continue;
            System.out.println(files.getName());
            File dataFile = new File(files.getAbsolutePath() + "/file");
            allFileList.addAll(Arrays.asList(dataFile.listFiles()));
        }
        return allFileList;
    }

    public static List<File> GetUpLoadMetaFileList(String upLoadMetaFilePath, String metaType){
        List<File> allFileList = new ArrayList<File>();
        File rootFile = new File(upLoadMetaFilePath);
        File[] rootFileList = rootFile.listFiles();
        if(rootFileList.length == 0)
            return null;
        long length = 0;
        for(File files : rootFileList) {
            if (files.getName().contains("."))
                continue;
            File dataFile = new File(files.getAbsolutePath() + "/metadata/" + metaType);
            allFileList.addAll(Arrays.asList(dataFile.listFiles()));
        }
        return allFileList;
    }

    public static long getDataSize(String datapath){
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
        return size;
    }

    public static void jiange()
    {
        for(int i = 0; i < 25; i++)
            System.out.print("*");
        System.out.println();
    }
    public static void TimePrint(int seconds)
    {
        int hour = 0, minus = 0;
        if(seconds > 3600)
        {
            hour = seconds / 3600;
            seconds = seconds % 3600;
        }
        if(seconds > 60)
        {
            minus = seconds / 60;
            seconds = seconds % 60;
        }

        if(hour != 0)
            System.out.println("Time is " + hour + "H : " + minus +"M : " + seconds + "S");
        else if(minus != 0)
            System.out.println("Time is " +  minus +"M : " + seconds + "S");
        else
            System.out.println("Time is " +  seconds + "S");
    }

    public static String getTime(int seconds)
    {
        int hour = 0, minus = 0;
        if(seconds > 3600)
        {
            hour = seconds / 3600;
            seconds = seconds % 3600;
        }
        if(seconds > 60)
        {
            minus = seconds / 60;
            seconds = seconds % 60;
        }
        if(hour != 0)
            return hour + " : " + minus +" : " + seconds + " ";
        else if(minus != 0)
            return minus +" : " + seconds + " ";
        else
            return seconds + " ";
    }

    public static String FormetFileSize(long fileS) {//
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public void WriteLogforTrans(String content, String name)
    {
        File filesnum = new File(this.transLogPath);
        if(!filesnum.exists()){
            filesnum.mkdirs();
        }

        File logdir = new File(this.transLogPath);
        if(!logdir.exists())
        {
            logdir.mkdirs();
        }
        else
        {
            File logload = new File(logdir.getAbsolutePath() + "/" + name + ".txt");
            if(!logload.exists())
            {
                try {
                    logload.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            BufferedReader reader = null;
            try
            {
                reader = new BufferedReader(new FileReader(logload));
                String check = reader.readLine();
                if(check == null)
                {
                    FileWriter fw = new FileWriter(logload.getAbsolutePath());
                    fw.write(content + "\n");
                    fw.flush();
                    fw.close();
                }
                else
                {
                    FileWriter fw = new FileWriter(logload.getAbsolutePath(), true);
                    fw.write(content + "\n");
                    fw.flush();
                    fw.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void WriteLogforQuery(String content, String name)
    {
        File filesnum = new File(this.queryLogPath);
        if(!filesnum.exists()){
            filesnum.mkdirs();
        }

        File logdir = new File(this.queryLogPath);
        if(!logdir.exists())
        {
            logdir.mkdirs();
        }
        else
        {
            File logload = new File(logdir.getAbsolutePath() + "/" + name + ".txt");
            if(!logload.exists())
            {
                try {
                    logload.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            BufferedReader reader = null;
            try
            {
                reader = new BufferedReader(new FileReader(logload));
                String check = reader.readLine();
                if(check == null)
                {
                    FileWriter fw = new FileWriter(logload.getAbsolutePath());
                    fw.write(content + "\n");
                    fw.flush();
                    fw.close();
                }
                else
                {
                    FileWriter fw = new FileWriter(logload.getAbsolutePath(), true);
                    fw.write(content + "\n");
                    fw.flush();
                    fw.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public byte[] readFile(String filename){
        try{

            if(filename==null || filename.equals(""))
            {
                System.out.println("filename is invalid.filename=" + filename);
                return new byte[0];
            }
            File file = new File(filename);
            long len = file.length();
            byte[] bytes = new byte[(int)len];
            try{
                BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
                int r = bufferedInputStream.read(bytes);
                if (r != len)
                    throw new IOException("读取文件不正确");
                bufferedInputStream.close();
            }catch(Exception ex){
                ex.printStackTrace();
                System.out.println("Read file Exception，filename=" + filename+"---"+ex);
            }

            return bytes;
        }catch(Exception ex){
            System.out.println("Read file Exception，filename=" + filename+"---"+ex);
        }
        return new byte[0];
    }

    public static String[] sortedFileName(String[] dbFileName){
        String[] sortedFileName = new String[dbFileName.length];
        ArrayList<LocalDateTime> dbFileNameDateList = new ArrayList<>();
        for(int i = 0; i < sortedFileName.length; i++){
            String dbFileNameContent = dbFileName[i];
            int year = Integer.parseInt(dbFileNameContent.split("-")[0]);
            int month = Integer.parseInt(dbFileNameContent.split("-")[1]);
            int day = Integer.parseInt(dbFileNameContent.split("-")[2]);
            int mark = Integer.parseInt(dbFileNameContent.split("-")[3]);
            dbFileNameDateList.add(LocalDateTime.of(year, month, day, mark, 0, 0));
        }
        dbFileNameDateList.sort((a, b) -> a.compareTo(b));
        for(int i = 0; i < sortedFileName.length; i++){
            LocalDateTime localDateTime = dbFileNameDateList.get(i);
            sortedFileName[i] = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                    + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour();
        }
        return sortedFileName;
    }

    public static String[] sortedNumberTypeFileNameArray(String[] fileName){
        String[] sortedFileName = new String[fileName.length];
        ArrayList<Long> fileNameList = new ArrayList<>();
        for(String name : fileName)
            fileNameList.add(Long.valueOf(name));
        fileNameList.sort((a,b) -> b.compareTo(a));
        for(int i = 0; i < sortedFileName.length; i++)
            sortedFileName[i] = Long.toString(fileNameList.get(i));
        return sortedFileName;
    }

    public static void queryMakeRecord(LocalDateTime dirName, String queryType, Map<String, Object> queryMap) {
        String path = Testpath.class.getClassLoader().getResource("QueryMake").getPath();
        path += "/" + dirName.toInstant(ZoneOffset.of("+8")).getEpochSecond();
        File dirFile = new File(path);
        if(!dirFile.exists())
            dirFile.mkdirs();
        String result = "";
        File file = new File(path + "/" + queryType + ".txt");
        StringBuilder stringBuilder = new StringBuilder("");
        Set<String> queryKeySet = queryMap.keySet();
        for (String query : queryKeySet) {
            Object queryValue = queryMap.get(query);
            //判断是否为列表，如果是列表，则不是离散查询就是数值的范围查询
            if (queryValue.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList checkQuery = ( ArrayList ) queryValue;
                String checkItemType = checkQuery.get(0).getClass().getName();
                //首先判断是否为数值类型
                if (checkItemType.equals("java.lang.Integer")) {
                    ArrayList<Integer> valueList = new ArrayList<>();
                    for (Object checkItem : checkQuery)
                        valueList.add(( Integer ) checkItem);
                    stringBuilder.append(query + "$");
                    for (Integer itemToCheck : valueList)
                        stringBuilder.append(itemToCheck + ",");
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                } else if (checkItemType.equals("java.lang.Long")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Long min = ( Long ) checkQuery.get(0);
                    Long max = ( Long ) checkQuery.get(1);
                    if (max < min) {
                        Long temp = max;
                        max = min;
                        min = temp;
                    }
                    stringBuilder.append(query + "$");
                    stringBuilder.append(min + "," + max);
                } else {
                    ArrayList<String> valueList = new ArrayList<>();
                    for (Object checkItem : checkQuery)
                        valueList.add(( String ) checkItem);
                    stringBuilder.append(query + "$");
                    for (String string : valueList)
                        stringBuilder.append(string + ",");
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
            } else if (queryValue.getClass().getName().equals("java.lang.Integer")) {
                Integer itemToCheck = ( Integer ) queryValue;
                stringBuilder.append(query + "$");
                stringBuilder.append(itemToCheck);
            } else if (queryValue.getClass().getName().equals("java.lang.Double")) {
                Double itemToCheck = ( Double ) queryValue;
                stringBuilder.append(query + "$");
                stringBuilder.append(itemToCheck);
            } else {
                String queryValueString = ( String ) queryValue;
                if (queryValueString.contains("-")) {
                    Integer min = Integer.parseInt(queryValueString.split("-")[0]);
                    Integer max = Integer.parseInt(queryValueString.split("-")[1]);
                    if (max < min) {
                        Integer temp = max;
                        max = min;
                        min = temp;
                    }
                    stringBuilder.append(query + "$" + min + "," + max);
                } else {
                    String itemToCheck = ( String ) queryValue;
                    stringBuilder.append(query + "$");
                    stringBuilder.append(itemToCheck);
                }
            }
            stringBuilder.append(";");
        }
        result = stringBuilder.substring(0, stringBuilder.length() - 1);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(result + "\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file, true);
                fileWriter.write(result + "\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sortMakeRecord(LocalDateTime dirName, String queryType,
                                      String attributeContent, int sortedWay){
        String path = Testpath.class.getClassLoader().getResource("QueryMake").getPath();
        path += "/" + dirName.toInstant(ZoneOffset.of("+8")).getEpochSecond();
        File dirFile = new File(path);
        if(!dirFile.exists())
            dirFile.mkdirs();
        String result = "";
        File file = new File(path + "/" + queryType + ".txt");
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(attributeContent + "$" + sortedWay + "\n");
        result = stringBuilder.toString();
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(result + "\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file, true);
                fileWriter.write(result + "\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> getQueryOneContent(){
        Map<String, String> queryMap = new HashMap<>();
        String path = Testpath.class.getClassLoader().getResource("QueryMaker").getPath();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path + "/" + QueryFileName.QUERY_ONE + ".txt"))));
            String content = br.readLine();
            queryMap.put(content.split("$")[0], content.split("$")[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryMap;
    }

    public static Map<String, ArrayList<String>> getQueryTwoContent(){
        Map<String, ArrayList<String>> queryMap = new HashMap<>();
        String path = Testpath.class.getClassLoader().getResource("QueryMaker").getPath();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path + "/" + QueryFileName.QUERY_TWO + ".txt"))));
            String content = br.readLine();
            String contentAttr = content.split("$")[0];
            String[] contentValueArray = content.split("$")[1].split(",");
            ArrayList<String> contentValueList = new ArrayList<>();
            for(int i = 0; i < contentValueArray.length; i++)
                contentValueList.add(contentValueArray[i]);
            queryMap.put(contentAttr, contentValueList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryMap;
    }

    public static Map<String, ArrayList<Long>> getQueryThreeContent(){
        Map<String, ArrayList<Long>> queryMap = new HashMap<>();
        String path = Testpath.class.getClassLoader().getResource("QueryMaker").getPath();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path + "/" + QueryFileName.QUERY_THREE + ".txt"))));
            String content = br.readLine();
            String contentAttr = content.split("$")[0];
            String[] contentValueArray = content.split("$")[1].split(",");
            ArrayList<Long> contentValueList = new ArrayList<>();
            for(int i = 0; i < contentValueArray.length; i++)
                contentValueList.add(Long.parseLong(contentValueArray[i]));
            queryMap.put(contentAttr, contentValueList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryMap;
    }

    public static Map<String, String> getQueryFourContent(){
        Map<String, String> queryMap = new HashMap<>();
        String path = Testpath.class.getClassLoader().getResource("QueryMaker").getPath();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path + "/" + QueryFileName.QUERY_FOUR + ".txt"))));
            String content = br.readLine();
            String[] contentArray = content.split(";");
            for(String string : contentArray){
                String contentAttr = string.split("$")[0];
                String contentValue = string.split("$")[1];
                queryMap.put(contentAttr, contentValue);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryMap;
    }

    public static Map<String, Object> getQueryFiveContent(){
        Map<String, Object> queryMap = new HashMap<>();
        ArrayList<String> areaNameContentList = new ArrayList<>();
        String path = Testpath.class.getClassLoader().getResource("QueryMaker").getPath();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path + "/" + QueryFileName.QUERY_FIVE + ".txt"))));
            String content = br.readLine();
            String[] contentArray = content.split(";");
            String areaNameQueryContent = contentArray[0];
            String deviceTypeQueryContent = contentArray[1];
            String[] areaNameQueryValueArray = areaNameQueryContent.split("$")[1].split(",");
            for(String string : areaNameQueryValueArray)
                areaNameContentList.add(string);
            queryMap.put(areaNameQueryContent.split("$")[0], areaNameContentList);
            queryMap.put(deviceTypeQueryContent.split("$")[0], deviceTypeQueryContent.split("$")[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryMap;
    }
}
