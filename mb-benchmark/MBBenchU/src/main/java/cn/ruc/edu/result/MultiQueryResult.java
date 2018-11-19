package cn.ruc.edu.result;

import cn.ruc.edu.basecore.QueryFileName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SimpleQueryResult
 */
public class MultiQueryResult extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DecimalFormat fnum  =  new  DecimalFormat("##0.00")
            ;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String dbLogFilePath = "";
        if(session.getAttribute("transLogPath") != null){
            dbLogFilePath = (String) session.getAttribute("transLogPath");
            dbLogFilePath = dbLogFilePath.substring(0, dbLogFilePath.lastIndexOf("/"));
        }
        else
            dbLogFilePath = (String) session.getAttribute("logpath") +
                    "/" + (String) session.getAttribute("databasetype");


        if(request.getParameter("type").equals("info")){
            String filecount = request.getParameter("copynum");
            FileReader reader = null;
            int c=0;
            String copynum = filecount.split(":")[0];
            filecount = filecount.split(":")[1];
            //复杂负载
            //排序
            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_NINE + ".txt");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            Map<String, String> sort1info = new HashMap<>();
            while((str = br.readLine()) != null) {
                sort1info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_TEN + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> sort2info = new HashMap<>();
            while((str = br.readLine()) != null) {
                sort2info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_ELEVEN + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> sort3info = new HashMap<>();
            while((str = br.readLine()) != null) {
                sort3info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_TWELVE + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query1Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query1Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_THIRTEEN + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query2Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query2Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_FOURTEEN + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query3Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query3Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_ABNORMAL + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> query4Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query4Info.put(str.split(";")[0], str.split(";")[1]);
            }

            System.out.println("{\"sort1_querycontent\":"+ "\"" + sort1info.get("querycontent")+ "\"" + ","
            		+ "\"sort1_ability\":"+ "\""+ sort1info.get("ability") + "\""+ ","
            		+ "\"sort1_count\":"+ "\""+ sort1info.get("count") + "\""+ ","
                    + "\"sort1_speed\":"+ "\""+ sort1info.get("speed") + "\""+ ","
                    + "\"sort1_time\":"+ "\""+ sort1info.get("time") + "\""+ ","
                    + "\"sort2_querycontent\":"+ "\""+ sort2info.get("querycontent") + "\""+ ","
                    + "\"sort2_ability\":"+ "\""+ sort2info.get("ability") + "\""+ ","
                    + "\"sort2_count\":"+ "\"" + sort2info.get("count")+ "\"" + ","
                    + "\"sort2_speed\":"+ "\""+ sort2info.get("speed") + "\""+ ","
                    + "\"sort2_time\":"+ "\""+ sort2info.get("time") + "\""+ ","
                    + "\"sort3_querycontent\":"+ "\"" + sort3info.get("querycontent")+ "\"" + ","
                    + "\"sort3_ability\":"+ "\"" + sort3info.get("ability")+ "\"" + ","
                    + "\"sort3_count\":"+ "\"" + sort3info.get("count")+ "\"" + ","
                    + "\"sort3_speed\":"+ "\""+ sort3info.get("speed") + "\""+ ","
                    + "\"sort3_time\":"+ "\""+ sort3info.get("time") + "\"" +","

                    +"\"query1_count\":"+ "\"" + query1Info.get("count")+ "\"" + ","
                    + "\"query1_speed\":"+ "\""+ query1Info.get("speed") + "\""+ ","
                    + "\"query1_time\":"+ "\""+ query1Info.get("time") + "\""+ ","
                    + "\"query1_rate\":"+ "\""+ query1Info.get("rate") + "\""+ ","
                    + "\"query2_count\":"+ "\"" + query2Info.get("count")+ "\"" + ","
                    + "\"query2_speed\":"+ "\""+ query2Info.get("speed") + "\""+ ","
                    + "\"query2_time\":"+ "\""+ query2Info.get("time") + "\""+ ","
                    + "\"query2_rate\":"+ "\"" + query2Info.get("rate")+ "\"" + ","
                    + "\"query3_count\":"+ "\"" + query3Info.get("count")+ "\"" + ","
                    + "\"query3_speed\":"+ "\""+ query3Info.get("speed") + "\""+ ","
                    + "\"query3_time\":"+ "\""+ query3Info.get("time") + "\"" +","
                    + "\"query3_rate\":"+ "\""+ query3Info.get("rate") + "\"" +","

                    //+ "\"query4_result\":"+ "\"" + query4Info.get("type") + "\"" + ","
                    + "\"query4_count\":"+ "\"" + query4Info.get("count")+ "\"" + ","
                    + "\"query4_speed\":"+ "\"" + query4Info.get("speed")+ "\"" + ","
                    + "\"query4_time\":"+ "\""+ query4Info.get("time") + "\""+ ","
                    + "\"query4_findcount\":"+ "\""+ query4Info.get("findcount") + "\""+ ","
                    + "\"query4_truecount\":\"" + query4Info.get("truecount") + "\""+ ","
                    + "\"query4_acc\":"+ "\"" + query4Info.get("rate")+ "\""
                    +"}");

            response.getWriter().write(
            		"{\"sort1_querycontent\":"+ "\"" + sort1info.get("querycontent")+ "\"" + ","
                    		+ "\"sort1_ability\":"+ "\""+ sort1info.get("ability") + "\""+ ","
                    		+ "\"sort1_count\":"+ "\""+ sort1info.get("count") + "\""+ ","
                            + "\"sort1_speed\":"+ "\""+ sort1info.get("speed") + "\""+ ","
                            + "\"sort1_time\":"+ "\""+ sort1info.get("time") + "\""+ ","
                            + "\"sort2_querycontent\":"+ "\""+ sort2info.get("querycontent") + "\""+ ","
                            + "\"sort2_ability\":"+ "\""+ sort2info.get("ability") + "\""+ ","
                            + "\"sort2_count\":"+ "\"" + sort2info.get("count")+ "\"" + ","
                            + "\"sort2_speed\":"+ "\""+ sort2info.get("speed") + "\""+ ","
                            + "\"sort2_time\":"+ "\""+ sort2info.get("time") + "\""+ ","
                            + "\"sort3_querycontent\":"+ "\"" + sort3info.get("querycontent")+ "\"" + ","
                            + "\"sort3_ability\":"+ "\"" + sort3info.get("ability")+ "\"" + ","
                            + "\"sort3_count\":"+ "\"" + sort3info.get("count")+ "\"" + ","
                            + "\"sort3_speed\":"+ "\""+ sort3info.get("speed") + "\""+ ","
                            + "\"sort3_time\":"+ "\""+ sort3info.get("time") + "\"" +","

                            +"\"query1_count\":"+ "\"" + query1Info.get("count")+ "\"" + ","
                            + "\"query1_speed\":"+ "\""+ query1Info.get("speed") + "\""+ ","
                            + "\"query1_time\":"+ "\""+ query1Info.get("time") + "\""+ ","
                            + "\"query1_rate\":"+ "\""+ query1Info.get("rate") + "\""+ ","
                            + "\"query2_count\":"+ "\"" + query2Info.get("count")+ "\"" + ","
                            + "\"query2_speed\":"+ "\""+ query2Info.get("speed") + "\""+ ","
                            + "\"query2_time\":"+ "\""+ query2Info.get("time") + "\""+ ","
                            + "\"query2_rate\":"+ "\"" + query2Info.get("rate")+ "\"" + ","
                            + "\"query3_count\":"+ "\"" + query3Info.get("count")+ "\"" + ","
                            + "\"query3_speed\":"+ "\""+ query3Info.get("speed") + "\""+ ","
                            + "\"query3_time\":"+ "\""+ query3Info.get("time") + "\"" +","
                            + "\"query3_rate\":"+ "\""+ query3Info.get("rate") + "\"" +","

                            //+ "\"query4_result\":"+ "\"" + query4Info.get("type") + "\"" + ","
                            + "\"query4_count\":"+ "\"" + query4Info.get("count")+ "\"" + ","
                            + "\"query4_speed\":"+ "\"" + query4Info.get("speed")+ "\"" + ","
                            + "\"query4_time\":"+ "\""+ query4Info.get("time") + "\""+ ","
                            + "\"query4_findcount\":"+ "\""+ query4Info.get("findcount") + "\""+ ","
                            + "\"query4_truecount\":\"" + query4Info.get("truecount") + "\""+ ","
                            + "\"query4_acc\":"+ "\"" + query4Info.get("rate")+ "\""
                            +"}");
        }

        if(request.getParameter("type").equals("draw"))
        {
            System.out.println("进入画图");
            String dbWithQuery = request.getParameter("copynum");
            String dbName = dbWithQuery.split(":")[0];
            String queryName = dbWithQuery.split(":")[1];
            FileReader reader;

            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_HOTFILE + ".txt");
            System.out.println(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_HOTFILE + ".txt");
            StringBuffer speed = new StringBuffer("");
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(reader);
            String str;
            speed.append("[");
            ArrayList<Double> dataList = new ArrayList<>();
            while((str = br.readLine()) != null) {
                dataList.add(Double.parseDouble(str));
                speed.append("{\"speed\":" + str + "},");
            }
            double allAvg = dataList.parallelStream().collect(Collectors.averagingDouble(Double::doubleValue));
            System.out.println(allAvg);
            double allVar = 0;
            for(Double data : dataList)
                allVar += (data - allAvg) * (data - allAvg);
            allVar = Math.sqrt(allVar / dataList.size());

            String result = speed.toString().substring(0, speed.length() - 1) + "]";
            System.out.println("{\"avg\":\""+ fnum.format(allAvg) + "\","
                    + "\"var\":" + fnum.format(allVar) + ","
                    + "\"dataarray\":"+ result
                    + "}");
            response.getWriter().write("{\"avg\":\""+ fnum.format(allAvg) + "\","
                    + "\"var\":" + fnum.format(allVar) + ","
                    + "\"dataarray\":"+ result
                    + "}");
        }

        if(request.getParameter("type").equals("queryd")){
            String filecount = request.getParameter("copynum");
            FileReader reader = null;
            int c=0;
            String copynum = filecount.split(":")[0];
            filecount = filecount.split(":")[1];
            //复杂负载
            //排序
            System.out.println(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_NINE + ".txt");
            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_NINE + ".txt");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            Map<String, String> sort1info = new HashMap<>();
            while((str = br.readLine()) != null) {
                sort1info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_TEN + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> sort2info = new HashMap<>();
            while((str = br.readLine()) != null) {
                sort2info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_ELEVEN + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> sort3info = new HashMap<>();
            while((str = br.readLine()) != null) {
                sort3info.put(str.split(";")[0], str.split(";")[1]);
            }

            response.getWriter().write(
            		"{\"sort1_querycontent\":"+ "\"" + sort1info.get("querycontent")+ "\"" + ","
                    		+ "\"sort1_ability\":"+ "\""+ sort1info.get("ability") + "\""+ ","
                    		+ "\"sort1_count\":"+ "\""+ sort1info.get("count") + "\""+ ","
                            + "\"sort1_speed\":"+ "\""+ sort1info.get("speed") + "\""+ ","
                            + "\"sort1_time\":"+ "\""+ sort1info.get("time") + "\""+ ","
                            + "\"sort2_querycontent\":"+ "\""+ sort2info.get("querycontent") + "\""+ ","
                            + "\"sort2_ability\":"+ "\""+ sort2info.get("ability") + "\""+ ","
                            + "\"sort2_count\":"+ "\"" + sort2info.get("count")+ "\"" + ","
                            + "\"sort2_speed\":"+ "\""+ sort2info.get("speed") + "\""+ ","
                            + "\"sort2_time\":"+ "\""+ sort2info.get("time") + "\""+ ","
                            + "\"sort3_querycontent\":"+ "\"" + sort3info.get("querycontent")+ "\"" + ","
                            + "\"sort3_ability\":"+ "\"" + sort3info.get("ability")+ "\"" + ","
                            + "\"sort3_count\":"+ "\"" + sort3info.get("count")+ "\"" + ","
                            + "\"sort3_speed\":"+ "\""+ sort3info.get("speed") + "\""+ ","
                            + "\"sort3_time\":"+ "\""+ sort3info.get("time") + "\""
                            +"}");
        }

        if(request.getParameter("type").equals("querye")){
            String filecount = request.getParameter("copynum");
            FileReader reader = null;

            String copynum = filecount.split(":")[0];
            filecount = filecount.split(":")[1];
            //最值平均值查询
            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_TWELVE + ".txt");
            BufferedReader br = new BufferedReader(reader);
            String str;
            Map<String, String> query1Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query1Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_THIRTEEN + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query2Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query2Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_FOURTEEN + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query3Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query3Info.put(str.split(";")[0], str.split(";")[1]);
            }

            response.getWriter().write(
                            "{\"query1_count\":"+ "\"" + query1Info.get("count")+ "\"" + ","
                            + "\"query1_speed\":"+ "\""+ query1Info.get("speed") + "\""+ ","
                            + "\"query1_time\":"+ "\""+ query1Info.get("time") + "\""+ ","
                            + "\"query1_rate\":"+ "\""+ query1Info.get("rate") + "\""+ ","
                            + "\"query2_count\":"+ "\"" + query2Info.get("count")+ "\"" + ","
                            + "\"query2_speed\":"+ "\""+ query2Info.get("speed") + "\""+ ","
                            + "\"query2_time\":"+ "\""+ query2Info.get("time") + "\""+ ","
                            + "\"query2_rate\":"+ "\""+ query2Info.get("rate") + "\""+ ","
                            + "\"query3_count\":"+ "\"" + query3Info.get("count")+ "\"" + ","
                            + "\"query3_speed\":"+ "\""+ query3Info.get("speed") + "\""+ ","
                            + "\"query3_time\":"+ "\""+ query3Info.get("time") + "\""+ ","
                            + "\"query3_rate\":"+ "\""+ query3Info.get("rate") + "\""
                            +"}");
        }

        if(request.getParameter("type").equals("queryf")){
            String filecount = request.getParameter("copynum");
            FileReader reader = null;
            String copynum = filecount.split(":")[0];
            filecount = filecount.split(":")[1];
            reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_ABNORMAL + ".txt");
            BufferedReader br = new BufferedReader(reader);
            String str;
            Map<String, String> query4Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query4Info.put(str.split(";")[0], str.split(";")[1]);
            }

            System.out.println("{" +
                    //"\"query4_result\":"+ "\"" + query4Info.get("type") + "\"" + ","
                    "\"query4_count\":"+ "\"" + query4Info.get("count")+ "\"" + ","
                    + "\"query4_speed\":"+ "\"" + query4Info.get("speed")+ "\"" + ","
                    + "\"query4_time\":"+ "\""+ query4Info.get("time") + "\""+ ","
                    + "\"query4_findcount\":"+ "\""+ query4Info.get("findcount") + "\""+ ","
                    + "\"query4_truecount\":\"" + query4Info.get("truecount") + "\""+ ","
                    + "\"query4_acc\":"+ "\"" + query4Info.get("rate")+ "\""
                    +"}");

            response.getWriter().write(
                    "{" +
                            //"\"query4_result\":"+ "\"" + query4Info.get("type") + "\"" + ","
                             "\"query4_count\":"+ "\"" + query4Info.get("count")+ "\"" + ","
                            + "\"query4_speed\":"+ "\"" + query4Info.get("speed")+ "\"" + ","
                            + "\"query4_time\":"+ "\""+ query4Info.get("time") + "\""+ ","
                            + "\"query4_findcount\":"+ "\""+ query4Info.get("findcount") + "\""+ ","
                            + "\"query4_truecount\":\"" + query4Info.get("truecount") + "\""+ ","
                            + "\"query4_acc\":"+ "\"" + query4Info.get("rate")+ "\""
                            +"}");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
