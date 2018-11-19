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
public class SimpleQueryResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DecimalFormat fnum  =  new  DecimalFormat("##0.00");

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

			String copynum = filecount.split(":")[0];
			filecount = filecount.split(":")[1];
			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_ONE + ".txt");
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			Map<String, String> query1Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query1Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_TWO + ".txt");
			br = new BufferedReader(reader);

			Map<String, String> query2Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query2Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_THREE + ".txt");
			br = new BufferedReader(reader);

			Map<String, String> query3Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query3Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_FOUR + ".txt");
			br = new BufferedReader(reader);
			Map<String, String> query4Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query4Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_FIVE + ".txt");
			br = new BufferedReader(reader);
			Map<String, String> query5Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query5Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_SIX + ".txt");
			br = new BufferedReader(reader);
			Map<String, String> query6Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query6Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_SEVEN + ".txt");
			br = new BufferedReader(reader);

			Map<String, String> query7Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query7Info.put(str.split(";")[0], str.split(";")[1]);
			}

			reader = new FileReader(dbLogFilePath + "/" + copynum + "/" + filecount + "/" + QueryFileName.QUERY_VERISON + ".txt");
			br = new BufferedReader(reader);
			Map<String, String> query8Info = new HashMap<>();
			while((str = br.readLine()) != null) {
				query8Info.put(str.split(";")[0], str.split(";")[1]);
			}

			response.getWriter().write("{\"devicetype_querycontent\":" + "\"" + query1Info.get("querycontent") +"\"" + ","
					+ "\"devicetype_count\":" + "\"" + query1Info.get("count") +"\"" + ","
					+ "\"devicetype_speed\":"+ "\""+ query1Info.get("speed") + "\""+ ","
					+ "\"devicetype_time\":"+ "\""+ query1Info.get("time") + "\""+ ","
					+ "\"devicetype_rate\":"+ "\""+ query1Info.get("rate") + "\""+ ","
					+ "\"area_querycontent\":" + "\""+ query2Info.get("querycontent") + "\""+ ","
					+ "\"area_count\":" + "\""+ query2Info.get("count") + "\""+ ","
					+ "\"area_speed\":"+ "\""+ query2Info.get("speed") + "\""+ ","
					+ "\"area_time\":"+ "\""+ query2Info.get("time") + "\""+ ","
					+ "\"area_rate\":"+ "\""+ query2Info.get("rate") + "\""+ ","
					+ "\"simulation_count\":"+ "\"" + query3Info.get("count") + "\"" + ","
					+ "\"simulation_speed\":"+ "\""+ query3Info.get("speed") + "\"" + ","
					+ "\"simulation_time\":"+ "\""+ query3Info.get("time") + "\""	+ ","
					+ "\"simulation_rate\":"+ "\""+ query3Info.get("rate") + "\""	+ ","
					+ "\"sett\":"+ "\"" +  query3Info.get("querytime") + "\"" + ","
					+ "\"query4_querycontent\":"+ "\"" + query4Info.get("querycontent") + "\"" + ","
					+ "\"query4_count\":"+ "\"" + query4Info.get("count") + "\"" + ","
					+ "\"query4_speed\":"+ "\""+ query4Info.get("speed") + "\""+ ","
					+ "\"query4_time\":"+ "\""+ query4Info.get("time") + "\""+ ","
					+ "\"query4_rate\":"+ "\""+ query4Info.get("rate") + "\""+ ","
					+ "\"query5_querycontent\":" + "\""+ query5Info.get("querycontent") + "\""+ ","
					+ "\"query5_count\":" + "\""+ query5Info.get("count") + "\""+ ","
					+ "\"query5_speed\":"+ "\""+ query5Info.get("speed") + "\""+ ","
					+ "\"query5_time\":"+ "\""+ query5Info.get("time") + "\""+ ","
					+ "\"query5_rate\":"+ "\""+ query5Info.get("rate") + "\""+ ","
					+ "\"query6_querycontent\":"+ "\"" + query6Info.get("querycontent")+ "\"" + ","
					+ "\"query6_count\":"+ "\"" + query6Info.get("count")+ "\"" + ","
					+ "\"query6_speed\":"+ "\""+ query6Info.get("speed")+ "\"" + ","
					+ "\"query6_time\":"+ "\""+ query6Info.get("time")+ "\"" + ","
					+ "\"query6_rate\":"+ "\""+ query6Info.get("rate")+ "\"" + ","
					+ "\"query7_querycontent\":"+ "\"" + query7Info.get("querycontent")+ "\"" + ","
					+ "\"query7_count\":"+ "\"" + query7Info.get("count")+ "\"" + ","
					+ "\"query7_speed\":"+ "\""+ query7Info.get("speed")+ "\"" + ","
					+ "\"query7_time\":"+ "\""+ query7Info.get("time")+ "\"" + ","
					+ "\"query7_rate\":"+ "\""+ query7Info.get("rate")+ "\"" + ","
					+ "\"query8_result\":" + "\"" + query8Info.get("type") + "\"" + ","
					+ "\"query8_count\":"+ "\"" + query8Info.get("count")+ "\"" + ","
					+ "\"query8_speed\":"+ "\""+ query8Info.get("speed")+ "\"" + ","
					+ "\"query8_time\":"+ "\""+ query8Info.get("time")+ "\"" + ","
					+ "\"query8_rate\":"+ "\""+ query8Info.get("rate")+ "\""
					+"}");
		}

		if(request.getParameter("type").equals("draw"))
		{
            int interval =  Integer.parseInt(request.getParameter("content"));
			String dbWithQuery = request.getParameter("copynum");
			String dbName = dbWithQuery.split(":")[0];
			String queryName = dbWithQuery.split(":")[1];
            String fileName = request.getParameter("charttype");
			FileReader reader = null;	

			if(fileName.equals("query1"))
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_ONE + ".txt");
			else if (fileName.equals("query2"))
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_TWO + ".txt");
			else if (fileName.equals("query3"))
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_THREE + ".txt");
			else if (fileName.equals("query4"))
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_FOUR + ".txt");
			else if (fileName.equals("query5"))
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_FIVE + ".txt");
			else if (fileName.equals("query6"))
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_SIX + ".txt");
			else
				reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.DOWN_QUERY_SEVEN + ".txt");
			StringBuffer speed = new StringBuffer("");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(reader);
			String str = null;

			speed.append("[");
			int i = 0;

			ArrayList<Double> dataList = new ArrayList<>();
			if(interval == 1)
			{
				while((str = br.readLine()) != null) {
					dataList.add(Double.parseDouble(str));
					speed.append("{\"speed\":" + str + "},");
				}
			}
			else
			{
				while((str = br.readLine()) != null)
				{
					boolean enough = true;
					double sum = 0;
					for(i = 0; i < interval; i++, str = br.readLine())
					{
						if(str == null)
						{
							enough = false;
							break;
						}
						sum += Float.parseFloat(str);
					}
					if(enough){
						speed.append("{\"speed\":" + sum / interval + "},");
						dataList.add(sum / interval);
					}

					else{
						speed.append("{\"speed\":" + sum / i + "},");
						dataList.add(sum / i);
					}

				}
			}
			double allAvg = dataList.parallelStream().collect(Collectors.averagingDouble(Double::doubleValue));
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

		if(request.getParameter("type").equals("querya")){
		    System.out.println(dbLogFilePath);
			String dbWithQuery = request.getParameter("copynum");
			String dbName = dbWithQuery.split(":")[0];
			String queryName = dbWithQuery.split(":")[1];
			System.out.println(dbWithQuery);
			FileReader reader = null;
            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_ONE + ".txt");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            Map<String, String> query1Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query1Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_TWO + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> query2Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query2Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_THREE + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> query3Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query3Info.put(str.split(";")[0], str.split(";")[1]);
            }

            System.out.println("{\"devicetype_querycontent\":" + "\"" + query1Info.get("querycontent") +"\"" + ","
					+ "\"devicetype_count\":" + "\"" + query1Info.get("count") +"\"" + ","
					+ "\"devicetype_speed\":"+ "\""+ query1Info.get("speed") + "\""+ ","
					+ "\"devicetype_time\":"+ "\""+ query1Info.get("time") + "\""+ ","
					+ "\"devicetype_rate\":"+ "\""+ query1Info.get("rate") + "\""+ ","
					+ "\"area_querycontent\":" + "\""+ query2Info.get("querycontent") + "\""+ ","
					+ "\"area_count\":" + "\""+ query2Info.get("count") + "\""+ ","
					+ "\"area_speed\":"+ "\""+ query2Info.get("speed") + "\""+ ","
					+ "\"area_time\":"+ "\""+ query2Info.get("time") + "\""+ ","
					+ "\"area_rate\":"+ "\""+ query2Info.get("rate") + "\""+ ","
					+ "\"simulation_count\":"+ "\"" + query3Info.get("count") + "\"" + ","
					+ "\"simulation_speed\":"+ "\""+ query3Info.get("speed") + "\"" + ","
					+ "\"simulation_time\":"+ "\""+ query3Info.get("time") + "\""	+ ","
					+ "\"simulation_rate\":"+ "\""+ query3Info.get("rate") + "\""	+ ","
					+ "\"sett\":"+ "\"" +  query3Info.get("querytime") + "\"" +
                            "}");

            response.getWriter().write("{\"devicetype_querycontent\":" + "\"" + query1Info.get("querycontent") +"\"" + ","
					+ "\"devicetype_count\":" + "\"" + query1Info.get("count") +"\"" + ","
					+ "\"devicetype_speed\":"+ "\""+ query1Info.get("speed") + "\""+ ","
					+ "\"devicetype_time\":"+ "\""+ query1Info.get("time") + "\""+ ","
					+ "\"devicetype_rate\":"+ "\""+ query1Info.get("rate") + "\""+ ","
					+ "\"area_querycontent\":" + "\""+ query2Info.get("querycontent") + "\""+ ","
					+ "\"area_count\":" + "\""+ query2Info.get("count") + "\""+ ","
					+ "\"area_speed\":"+ "\""+ query2Info.get("speed") + "\""+ ","
					+ "\"area_time\":"+ "\""+ query2Info.get("time") + "\""+ ","
					+ "\"area_rate\":"+ "\""+ query2Info.get("rate") + "\""+ ","
					+ "\"simulation_count\":"+ "\"" + query3Info.get("count") + "\"" + ","
					+ "\"simulation_speed\":"+ "\""+ query3Info.get("speed") + "\"" + ","
					+ "\"simulation_time\":"+ "\""+ query3Info.get("time") + "\""	+ ","
					+ "\"simulation_rate\":"+ "\""+ query3Info.get("rate") + "\""	+ ","
					+ "\"sett\":"+ "\"" +  query3Info.get("querytime") + "\"" +
                    "}");

		}

		if(request.getParameter("type").equals("queryb")){
			System.out.println(dbLogFilePath);
			String dbWithQuery = request.getParameter("copynum");
			String dbName = dbWithQuery.split(":")[0];
			String queryName = dbWithQuery.split(":")[1];
			FileReader reader = null;
			//System.out.println(dbLogFilePath + "/" + copynum + "/" + filecount + "/query_devicetype_blade.txt");
			String str = "";
			System.out.println(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_FOUR + ".txt");
            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_FOUR + ".txt");
            BufferedReader br = new BufferedReader(reader);
            Map<String, String> query4Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query4Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_FIVE + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query5Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query5Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_SIX + ".txt");
            br = new BufferedReader(reader);
            Map<String, String> query6Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query6Info.put(str.split(";")[0], str.split(";")[1]);
            }

            reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_SEVEN + ".txt");
            br = new BufferedReader(reader);

            Map<String, String> query7Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query7Info.put(str.split(";")[0], str.split(";")[1]);
            }

			response.getWriter().write("{\"query4_querycontent\":"+ "\"" + query4Info.get("querycontent") + "\"" + ","
					+ "\"query4_count\":"+ "\""+ query4Info.get("count") + "\""+ ","
                    + "\"query4_speed\":"+ "\""+ query4Info.get("speed") + "\""+ ","
                    + "\"query4_time\":"+ "\""+ query4Info.get("time") + "\""+ ","
                    + "\"query4_rate\":"+ "\""+ query4Info.get("rate") + "\""+ ","
                    + "\"query5_querycontent\":" + "\""+ query5Info.get("querycontent") + "\""+ ","
                    + "\"query5_count\":" + "\""+ query5Info.get("count") + "\""+ ","
                    + "\"query5_speed\":"+ "\""+ query5Info.get("speed") + "\""+ ","
                    + "\"query5_time\":"+ "\""+ query5Info.get("time") + "\""+ ","
                    + "\"query5_rate\":"+ "\""+ query5Info.get("rate") + "\""+ ","
                    + "\"query6_querycontent\":"+ "\"" + query6Info.get("querycontent")+ "\"" + ","
                    + "\"query6_count\":"+ "\"" + query6Info.get("count")+ "\"" + ","
                    + "\"query6_speed\":"+ "\""+ query6Info.get("speed")+ "\"" + ","
                    + "\"query6_time\":"+ "\""+ query6Info.get("time")+ "\""	+ ","
                    + "\"query6_rate\":"+ "\""+ query6Info.get("rate")+ "\""	+ ","
                    + "\"query7_querycontent\":"+ "\"" + query7Info.get("querycontent")+ "\"" + ","
                    + "\"query7_count\":"+ "\"" + query7Info.get("count")+ "\"" + ","
                    + "\"query7_speed\":"+ "\""+ query7Info.get("speed")+ "\"" + ","
                    + "\"query7_time\":"+ "\""+ query7Info.get("time")+ "\"" + ","
                    + "\"query7_rate\":"+ "\""+ query7Info.get("rate")+ "\""
					+"}");
		}

		if(request.getParameter("type").equals("queryc")){
			System.out.println(dbLogFilePath);
			String dbWithQuery = request.getParameter("copynum");
			String dbName = dbWithQuery.split(":")[0];
			String queryName = dbWithQuery.split(":")[1];
			FileReader reader;
			String str;
			reader = new FileReader(dbLogFilePath + "/" + dbName + "/" + queryName + "/" + QueryFileName.QUERY_VERISON + ".txt");
			BufferedReader br = new BufferedReader(reader);
            Map<String, String> query8Info = new HashMap<>();
            while((str = br.readLine()) != null) {
                query8Info.put(str.split(";")[0], str.split(";")[1]);
            }

			response.getWriter().write("{\"query8_result\":" + "\"" + query8Info.get("type") + "\"" + ","
                    + "\"query8_count\":"+ "\"" + query8Info.get("count")+ "\"" + ","
                    + "\"query8_speed\":"+ "\""+ query8Info.get("speed")+ "\"" + ","
                    + "\"query8_time\":"+ "\""+ query8Info.get("time")+ "\"" + ","
                    + "\"query8_rate\":"+ "\""+ query8Info.get("rate")+ "\""
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
