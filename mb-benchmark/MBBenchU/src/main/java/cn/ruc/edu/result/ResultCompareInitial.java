package cn.ruc.edu.result;

import cn.ruc.edu.basecore.FileFunction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultCompareInitial extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String requestType = request.getParameter("requestType");

        if(requestType.equals("dataTypeLeft")){
            String dbName = request.getParameter("DBName");
            session.setAttribute("leftDBName", dbName);

            File[] transFileCopyList = new File(session.getAttribute("logpath")
                    + "/" + dbName).listFiles();
            String[] transCopyName = new String[transFileCopyList.length];
            for(int i = 0; i < transCopyName.length; i++)
                transCopyName[i] = transFileCopyList[i].getName();
            String[] sortedDbName = FileFunction.sortedFileName(transCopyName);
            String result = "[";
            for(String content : sortedDbName)
                result += "{\"transc\":\"" + content + "\"},";
            result = result.substring(0, result.length() - 1) + "]";

            response.getWriter().write("{\"arrays\":" + result + "}");
        }

        if(requestType.equals("dataTypeRight")){
            String dbName = request.getParameter("DBName");
            session.setAttribute("rightDBName", dbName);
            File[] transFileCopyList = new File((String) session.getAttribute("logpath")
                    + "/" + dbName).listFiles();
            String[] transCopyName = new String[transFileCopyList.length];
            for(int i = 0; i < transCopyName.length; i++)
                transCopyName[i] = transFileCopyList[i].getName();
            String[] sortedDbName = FileFunction.sortedFileName(transCopyName);
            String result = "[";
            for(String content : sortedDbName)
                result += "{\"transc\":\"" + content + "\"},";
            result = result.substring(0, result.length() - 1) + "]";

            response.getWriter().write("{\"arrays\":" + result + "}");
        }

        if(requestType.equals("queryTypeLeft")){
            String transName = request.getParameter("TransName");
            System.out.println(transName);
            session.setAttribute("leftTransName", transName);
            ArrayList<String> transCopyList = new ArrayList<>();
            File[] transFileCopyList = new File((String) session.getAttribute("logpath")
                    + "/" + (String) session.getAttribute("leftDBName") +
                    "/" + transName).listFiles();
            for(File file : transFileCopyList)
                if(file.isDirectory())
                    transCopyList.add(file.getName());
            String[] transCopyName = new String[transCopyList.size()];
            for(int i = 0; i < transCopyName.length; i++)
                transCopyName[i] = transCopyList.get(i);
            String[] sortedDbName = FileFunction.sortedFileName(transCopyName);
            String result = "[";
            for(String content : sortedDbName)
                result += "{\"queryc\":\"" + content + "\"},";
            result = result.substring(0, result.length() - 1) + "]";

            response.getWriter().write("{\"arrays\":" + result + "}");
        }

        if(requestType.equals("queryTypeRight")){
            String transName = request.getParameter("TransName");
            System.out.println(transName);
            session.setAttribute("rightTransName", transName);
            ArrayList<String> transCopyList = new ArrayList<>();
            File[] transFileCopyList = new File((String) session.getAttribute("logpath")
                    + "/" + (String) session.getAttribute("rightDBName") +
                    "/" + transName).listFiles();
            for(File file : transFileCopyList)
                if(file.isDirectory())
                    transCopyList.add(file.getName());
            String[] transCopyName = new String[transCopyList.size()];
            for(int i = 0; i < transCopyName.length; i++)
                transCopyName[i] = transCopyList.get(i);
            String[] sortedDbName = FileFunction.sortedFileName(transCopyName);
            String result = "[";
            for(String content : sortedDbName)
                result += "{\"queryc\":\"" + content + "\"},";
            result = result.substring(0, result.length() - 1) + "]";

            response.getWriter().write("{\"arrays\":" + result + "}");
        }

        if(requestType.equals("Create")){
            String left = request.getParameter("LeftName");
            String right = request.getParameter("RightName");
            System.out.println(session.getAttribute("leftDBName") + " " + session.getAttribute("leftTransName")
                    + " " + left + " pk "
                    + session.getAttribute("rightDBName") + " " + session.getAttribute("rightTransName")
                    + " " + right);
            session.setAttribute("leftQueryName", left);
            session.setAttribute("rightQueryName", right);

            String leftCPUName = request.getParameter("LeftCpuName");
            String leftCoreNum = request.getParameter("LeftCoreNum");
            String leftProcessNum = request.getParameter("LeftProcessNum");
            String leftMemVal = request.getParameter("LeftMemVal");
            String leftDiskVal = request.getParameter("LeftDiskVal");
            String leftNetSpeed = request.getParameter("LeftNetSpeed");

            String[] leftCPUNameArray = leftCPUName.substring(0, leftCPUName.length() - 1).split(",");
            String[] leftCoreNumArray = leftCoreNum.substring(0, leftCoreNum.length() - 1).split(",");
            String[] leftProcessNumArray = leftProcessNum.substring(0, leftProcessNum.length() - 1).split(",");
            String[] leftMemValArray = leftMemVal.substring(0, leftMemVal.length() - 1).split(",");
            String[] leftDiskValArray = leftDiskVal.substring(0, leftDiskVal.length() - 1).split(",");
            String[] leftNetSpeedArray = leftNetSpeed.substring(0, leftNetSpeed.length() - 1).split(",");

            String rightCPUName = request.getParameter("RightCpuName");
            String rightCoreNum = request.getParameter("RightCoreNum");
            String rightProcessNum = request.getParameter("RightProcessNum");
            String rightMemVal = request.getParameter("RightMemVal");
            String rightDiskVal = request.getParameter("RightDiskVal");
            String rightNetSpeed = request.getParameter("RightNetSpeed");

            String[] rightCPUNameArray = rightCPUName.substring(0, rightCPUName.length() - 1).split(",");
            String[] rightCoreNumArray = rightCoreNum.substring(0, rightCoreNum.length() - 1).split(",");
            String[] rightProcessNumArray = rightProcessNum.substring(0, rightProcessNum.length() - 1).split(",");
            String[] rightMemValArray = rightMemVal.substring(0, rightMemVal.length() - 1).split(",");
            String[] rightDiskValArray = rightDiskVal.substring(0, rightDiskVal.length() - 1).split(",");
            String[] rightNetSpeedArray = rightNetSpeed.substring(0, rightNetSpeed.length() - 1).split(",");

            ArrayList<DeviceInfo> leftObjectList = new ArrayList<>();
            DeviceInfo[] leftObjectArray = new DeviceInfo[leftCPUNameArray.length];
            for(int i = 0; i < leftObjectArray.length; i++){
                leftObjectArray[i] = new DeviceInfo(leftCPUNameArray[i], leftCoreNumArray[i],
                        leftProcessNumArray[i], leftMemValArray[i], leftDiskValArray[i],
                        leftNetSpeedArray[i]);
                leftObjectList.add(new DeviceInfo(leftCPUNameArray[i], leftCoreNumArray[i],
                        leftProcessNumArray[i], leftMemValArray[i], leftDiskValArray[i],
                        leftNetSpeedArray[i]));
            }

            ArrayList<DeviceInfo> rightObjectList = new ArrayList<>();
            DeviceInfo[] rightObjectArray = new DeviceInfo[rightCPUNameArray.length];
            for(int i = 0; i < rightObjectArray.length; i++){
                rightObjectArray[i] = new DeviceInfo(rightCPUNameArray[i], rightCoreNumArray[i],
                        rightProcessNumArray[i], rightMemValArray[i], rightDiskValArray[i],
                        rightNetSpeedArray[i]);
                rightObjectList.add(new DeviceInfo(rightCPUNameArray[i], rightCoreNumArray[i],
                        rightProcessNumArray[i], rightMemValArray[i], rightDiskValArray[i],
                        rightNetSpeedArray[i]));
            }


            File file = new File(session.getAttribute("logpath")
                    + "/" + "CompareResult.txt");
            if(file.exists())
                file.delete();
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(file);
            Map<String, Long> ll = leftObjectList.stream().collect(Collectors.groupingBy(DeviceInfo::toString, Collectors.counting()));
            Iterator<Map.Entry<String, Long>> llSet = ll.entrySet().iterator();
            int count = 1;
            while(llSet.hasNext()){
                Map.Entry<String, Long> temp = llSet.next();
                fileWriter.write("leftDevice" + count + ";" + temp.getKey() + "," + temp.getValue() + "\n");
                count ++;
            }

            count = 1;
            Map<String, Long> rr = rightObjectList.stream().collect(Collectors.groupingBy(DeviceInfo::toString, Collectors.counting()));
            Iterator<Map.Entry<String, Long>> rrSet = rr.entrySet().iterator();
            while(rrSet.hasNext()){
                Map.Entry<String, Long> temp = rrSet.next();
                fileWriter.write("rightDevice" + count + ";" + temp.getKey() + "," + temp.getValue() + "\n");
                count ++;
            }
            fileWriter.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
