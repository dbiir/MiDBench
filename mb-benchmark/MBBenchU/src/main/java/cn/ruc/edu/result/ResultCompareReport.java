package cn.ruc.edu.result;

import cn.ruc.edu.basecore.FileFunction;
import cn.ruc.edu.basecore.QueryFileName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ResultCompareReport extends HttpServlet {
    DecimalFormat fnum  =  new  DecimalFormat("##0.00");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        if(type.equals("envinfo")){
            File file = new File(session.getAttribute("logpath") + "/" + "CompareResult.txt");
            if(!file.exists()){
                response.getWriter().write("{\"type\":\"false\"}");
            }
            BufferedReader envBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            Map<String, String> envMap = new TreeMap<>();
            String str;
            while((str = envBufferedReader.readLine()) != null)
                envMap.put(str.split(";")[0], str.split(";")[1]);

            Iterator<Map.Entry<String, String>> envIterator = envMap.entrySet().iterator();
            ArrayList<String> leftEnvInfoList = new ArrayList<>();
            ArrayList<String> rightEnvInfoList = new ArrayList<>();

            while(envIterator.hasNext()){
                Map.Entry<String, String> envObject = envIterator.next();
                if(envObject.getKey().contains("left"))
                    leftEnvInfoList.add(envObject.getValue());
                else
                    rightEnvInfoList.add(envObject.getValue());
            }


            StringBuffer leftEnvInfoString = new StringBuffer();
            leftEnvInfoString.append("[");
            for(int i = 0; i < leftEnvInfoList.size(); i++)
                leftEnvInfoString.append("{\"content\":\"" + leftEnvInfoList.get(i) + "\"},");
            String leftContent = leftEnvInfoString.substring(0, leftEnvInfoString.length() - 1);
            System.out.println(leftContent);
            leftContent = leftContent + "]";

            StringBuffer rightEnvInfoString = new StringBuffer();
            rightEnvInfoString.append("[");
            for(int i = 0; i < rightEnvInfoList.size(); i++)
                rightEnvInfoString.append("{\"content\":\"" + rightEnvInfoList.get(i) + "\"},");
            String rightContent = rightEnvInfoString.substring(0, rightEnvInfoString.length() - 1);
            System.out.println(rightContent);
            rightContent = rightContent + "]";

            System.out.println("{" +
                    "\"left\":" + leftContent + "," +
                    "\"leftcount\":\"" + leftEnvInfoList.size() + "\"," +
                    "\"right\":" + rightContent + "," +
                    "\"rightcount\":\"" + rightEnvInfoList.size() + "\"" +
                    "}");

            response.getWriter().write("{" +
                    "\"left\":" + leftContent + "," +
                    "\"leftcount\":\"" + leftEnvInfoList.size() + "\"," +
                    "\"right\":" + rightContent + "," +
                    "\"rightcount\":\"" + rightEnvInfoList.size() + "\"" +
                    "}");

        }

        if(type.equals("infoandtrans")){
            String leftPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + "dbinfo.txt";
            String rightPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + "dbinfo.txt";

            String leftDBName = ( String ) session.getAttribute("leftDBName");
            String rightDBName = ( String ) session.getAttribute("rightDBName");

            BufferedReader leftBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(leftPath))));
            BufferedReader rightBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rightPath))));

            String content = null;
            ArrayList<String> leftInfo = new ArrayList<>();
            ArrayList<String> rightInfo = new ArrayList<>();

            while((content = leftBR.readLine()) != null)
                leftInfo.add(content);

            while((content = rightBR.readLine()) != null)
                rightInfo.add(content);

            String[] leftTime = new String[4];
            int[] leftTimeInt = new int[4];
            for(int i = 0; i < 4; i++){
                leftTimeInt[i] = Integer.parseInt(leftInfo.get(i).split(",")[0]);
                leftTime[i] = FileFunction.getTime(leftTimeInt[i]);
            }

            String[] rightTime = new String[4];
            int[] rightTimeInt = new int[4];
            for(int i = 0; i < 4; i++){
                rightTimeInt[i] = Integer.parseInt(rightInfo.get(i).split(",")[0]);
                rightTime[i] = FileFunction.getTime(rightTimeInt[i]);
            }

            StringBuilder result = new StringBuilder("");
            boolean firstBigger;
            for(int i = 0; i < 4; i++){
                firstBigger = true;
                if(leftTimeInt[i] < rightTimeInt[i])
                    firstBigger = false;
                switch (i){
                    case 0:{
                        if (firstBigger) {
                            result.append("在文件上传上，" + rightDBName +
                                    "的传输速度比" + leftDBName +
                                    "的传输速度快" + (leftTimeInt[i] - rightTimeInt[i])
                                    + "秒，相对效率而言，" + rightDBName
                                    + "是" + leftDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        } else {
                            result.append("在文件上传上，" + leftDBName +
                                    "的传输速度比" + rightDBName +
                                    "的传输速度快" + (rightTimeInt[i] - leftTimeInt[i])
                                    + "秒，相对效率而言，" + leftDBName
                                    + "是" + rightDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        }
                        break;
                    }
                    case 1:{
                        if (firstBigger) {
                            result.append("在文档上传上，" + rightDBName +
                                    "的传输速度比" + leftDBName +
                                    "的传输速度快" + (leftTimeInt[i] - rightTimeInt[i])
                                    + "秒，相对效率而言，" + rightDBName
                                    + "是" + leftDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        } else {
                            result.append("在文档上传上，" + leftDBName +
                                    "的传输速度比" + rightDBName +
                                    "的传输速度快" + (rightTimeInt[i] - leftTimeInt[i])
                                    + "秒，相对效率而言，" + leftDBName
                                    + "是" + rightDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        }
                        break;
                    }
                    case 2:{
                        if (firstBigger) {
                            result.append("在文件下载上，" + rightDBName +
                                    "的传输速度比" + leftDBName +
                                    "的传输速度快" + (leftTimeInt[i] - rightTimeInt[i])
                                    + "秒，相对效率而言，" + rightDBName
                                    + "是" + leftDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        } else {
                            result.append("在文件下载上，" + leftDBName +
                                    "的传输速度比" + rightDBName +
                                    "的传输速度快" + (rightTimeInt[i] - leftTimeInt[i])
                                    + "秒，相对效率而言，" + leftDBName
                                    + "是" + rightDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        }
                        break;
                    }
                    case 3:{
                        if (firstBigger) {
                            result.append("在文档下载上，" + rightDBName +
                                    "的传输速度比" + leftDBName +
                                    "的传输速度快" + (leftTimeInt[i] - rightTimeInt[i])
                                    + "秒，相对效率而言，" + rightDBName
                                    + "是" + leftDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        } else {
                            result.append("在文档下载上，" + leftDBName +
                                    "的传输速度比" + rightDBName +
                                    "的传输速度快" + (rightTimeInt[i] - leftTimeInt[i])
                                    + "秒，相对效率而言，" + leftDBName
                                    + "是" + rightDBName +
                                    "的" + FileFunction.floatToPresentString(( float ) rightTimeInt[i] / leftTimeInt[i])
                                    + "%;");
                        }
                        break;
                    }
                }
            }

            System.out.println(result.toString());


            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write("{\"transtableleft1\":\"Time: " + leftTime[0] + " Speed: " + leftInfo.get(0).split(",")[1] + "\","
                    + "\"transtableleft2\":\"Time: " + leftTime[1] + " Speed: " + leftInfo.get(1).split(",")[1] + "\"," +
                    "\"transtableleft3\":\"Time: " + leftTime[2] + " Speed: " + leftInfo.get(2).split(",")[1] + "\"," +
                    "\"transtableleft4\":\"Time: " + leftTime[3] + " Speed: " + leftInfo.get(3).split(",")[1] + "\"," +
                    "\"transtableright1\":\"Time: " + rightTime[0] + " Speed: " + rightInfo.get(0).split(",")[1] + "\"," +
                    "\"transtableright2\":\"Time: " + rightTime[1] + " Speed: " + rightInfo.get(1).split(",")[1] + "\"," +
                    "\"transtableright3\":\"Time: " + rightTime[2] + " Speed: " + rightInfo.get(2).split(",")[1] + "\"," +
                    "\"transtableright4\":\"Time: " + rightTime[3] + " Speed: " + rightInfo.get(3).split(",")[1] + "\"," +
                    "\"leftinfo1\":\""+ leftInfo.get(5) + "\"," +
                    "\"leftinfo2\":\""+ leftInfo.get(7) + "GB\"," +
                    "\"leftinfo3\":\""+ leftInfo.get(4) + "%\"," +
                    "\"rightinfo1\":\""+ rightInfo.get(5) + "\"," +
                    "\"rightinfo2\":\""+ rightInfo.get(7) + "GB\"," +
                    "\"rightinfo3\":\""+ rightInfo.get(4) + "%\"," +
                    "\"result\":\"" + result.toString() + "\"" +
                    "}");
        }

        if(type.equals("transload")){
            String leftPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + "dbinfo.txt";
            String rightPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + "dbinfo.txt";

            BufferedReader leftBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(leftPath))));
            BufferedReader rightBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rightPath))));

            String content = null;
            ArrayList<String> leftInfo = new ArrayList<>();
            ArrayList<String> rightInfo = new ArrayList<>();

            while((content = leftBR.readLine()) != null)
                leftInfo.add(content);

            while((content = rightBR.readLine()) != null)
                rightInfo.add(content);

            response.getWriter().write("{\"transtableleft1\":\"" + leftInfo.get(0) + "\","
                    + "\"transtableleft2\":\""+ leftInfo.get(1) + "\"," +
                    "\"transtableleft3\":\""+ leftInfo.get(2) + "\"," +
                    "\"transtableleft4\":\""+ leftInfo.get(3) + "\"," +
                    "\"transtableright1\":\""+ rightInfo.get(0) + "\"," +
                    "\"transtableright2\":\""+ rightInfo.get(1) + "\"," +
                    "\"transtableright3\":\""+ rightInfo.get(2) + "\"," +
                    "\"transtableright4\":\""+ rightInfo.get(3) + "\"," +
                    "\"leftinfo1\":\""+ leftInfo.get(5) + "\"," +
                    "\"leftinfo2\":\""+ leftInfo.get(7) + "GB\"," +
                    "\"leftinfo3\":\""+ leftInfo.get(4) + "%\"," +
                    "\"rightinfo1\":\""+ rightInfo.get(5) + "\"," +
                    "\"rightinfo2\":\""+ rightInfo.get(7) + "GB\"," +
                    "\"rightinfo3\":\""+ rightInfo.get(4) + "%\"" +
                    "}");
        }

        if(type.equals("transchart")){
            System.out.println("进入transchart");
            String content = null;

            String leftDBName = ( String ) session.getAttribute("leftDBName");
            String rightDBName = ( String ) session.getAttribute("rightDBName");

            File leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + QueryFileName.LOAD_FILE_SEC + ".txt");

            File rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + QueryFileName.LOAD_FILE_SEC + ".txt");

            BufferedReader leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            BufferedReader rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));

            ArrayList<Float> leftList = new ArrayList<Float>();
            ArrayList<Float> rightList = new ArrayList<Float>();

            while((content = leftBr.readLine()) != null)
                leftList.add(Float.parseFloat(content));

            while((content = rightBr.readLine()) != null)
                rightList.add(Float.parseFloat(content));

            float[] leftLoadSec = new float[4];
            float[] rightLoadSec = new float[4];

            double leftAvg = leftList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            double rightAvg = rightList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            long leftCount = leftList.parallelStream().count();
            long rightCount = leftList.parallelStream().count();

            float leftSumVar = 0;
            float leftMax = 0;
            float leftMin = 10000;
            for(Float s : leftList){
                if(leftMax < s)
                    leftMax = s;
                if(leftMin > s)
                    leftMin = s;
                leftSumVar += (s - leftAvg) * (s - leftAvg);
            }
            leftSumVar = (float) Math.sqrt(leftSumVar / leftCount);

            float rightSumVar = 0;
            float rightMax = 0;
            float rightMin = 10000;
            for(Float s : rightList){
                if(rightMax < s)
                    rightMax = s;
                if(rightMin > s)
                    rightMin = s;
                rightSumVar += (s - rightAvg) * (s - rightAvg);
            }
            rightSumVar = (float) Math.sqrt(rightSumVar / rightCount);

            String fileLoadResult = ResultCompareResultFunction.transCharResult(leftDBName, rightDBName,
                    leftAvg, rightAvg, leftSumVar, rightSumVar, leftMax, rightMax, leftMin, rightMin,
                    "文件上传");

            leftLoadSec[0] = (float) leftAvg;
            leftLoadSec[1] = leftSumVar;
            leftLoadSec[2] = leftMax;
            leftLoadSec[3] = leftMin;
            rightLoadSec[0] = (float) rightAvg;
            rightLoadSec[1] = rightSumVar;
            rightLoadSec[2] = rightMax;
            rightLoadSec[3] = rightMin;

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + QueryFileName.LOAD_META_SEC + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + QueryFileName.LOAD_META_SEC + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));

            leftList = new ArrayList<Float>();
            rightList = new ArrayList<Float>();

            while((content = leftBr.readLine()) != null)
                leftList.add(Float.parseFloat(content));

            while((content = rightBr.readLine()) != null)
                rightList.add(Float.parseFloat(content));

            float[] leftLoadMetaSec = new float[4];
            float[] rightLoadMetaSec = new float[4];

            leftAvg = leftList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            rightAvg = rightList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            leftCount = leftList.parallelStream().count();
            rightCount = leftList.parallelStream().count();

            leftSumVar = 0;
            leftMax = 0;
            leftMin = 10000;
            for(Float s : leftList){
                if(leftMax < s)
                    leftMax = s;
                if(leftMin > s)
                    leftMin = s;
                leftSumVar += (s - leftAvg) * (s - leftAvg);
            }
            leftSumVar = (float) Math.sqrt(leftSumVar / leftCount);

            rightSumVar = 0;
            rightMax = 0;
            rightMin = 10000;
            for(Float s : rightList){
                if(rightMax < s)
                    rightMax = s;
                if(rightMin > s)
                    rightMin = s;
                rightSumVar += (s - rightAvg) * (s - rightAvg);
            }
            rightSumVar = (float) Math.sqrt(rightSumVar / rightCount);

            String metaLoadResult = ResultCompareResultFunction.transCharResult(leftDBName, rightDBName,
                    leftAvg, rightAvg, leftSumVar, rightSumVar, leftMax, rightMax, leftMin, rightMin,
                    "文档上传");

            leftLoadMetaSec[0] = (float) leftAvg;
            leftLoadMetaSec[1] = leftSumVar;
            leftLoadMetaSec[2] = leftMax;
            leftLoadMetaSec[3] = leftMin;
            rightLoadMetaSec[0] = (float) rightAvg;
            rightLoadMetaSec[1] = rightSumVar;
            rightLoadMetaSec[2] = rightMax;
            rightLoadMetaSec[3] = rightMin;

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + QueryFileName.DOWN_FILE_SEC + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + QueryFileName.DOWN_FILE_SEC + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));

            leftList = new ArrayList<Float>();
            rightList = new ArrayList<Float>();

            while((content = leftBr.readLine()) != null)
                leftList.add(Float.parseFloat(content));

            while((content = rightBr.readLine()) != null)
                rightList.add(Float.parseFloat(content));

            float[] leftDownSec = new float[4];
            float[] rightDownSec = new float[4];

            leftAvg = leftList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            rightAvg = rightList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            leftCount = leftList.parallelStream().count();
            rightCount = leftList.parallelStream().count();

            leftSumVar = 0;
            leftMax = 0;
            leftMin = 10000;
            for(Float s : leftList){
                if(leftMax < s)
                    leftMax = s;
                if(leftMin > s)
                    leftMin = s;
                leftSumVar += (s - leftAvg) * (s - leftAvg);
            }
            leftSumVar = (float) Math.sqrt(leftSumVar / leftCount);

            rightSumVar = 0;
            rightMax = 0;
            rightMin = 10000;
            for(Float s : rightList){
                if(rightMax < s)
                    rightMax = s;
                if(rightMin > s)
                    rightMin = s;
                rightSumVar += (s - rightAvg) * (s - rightAvg);
            }
            rightSumVar = (float) Math.sqrt(rightSumVar / rightCount);

            String fileDownResult = ResultCompareResultFunction.transCharResult(leftDBName, rightDBName,
                    leftAvg, rightAvg, leftSumVar, rightSumVar, leftMax, rightMax, leftMin, rightMin,
                    "文件下载");

            leftDownSec[0] = (float) leftAvg;
            leftDownSec[1] = leftSumVar;
            leftDownSec[2] = leftMax;
            leftDownSec[3] = leftMin;
            rightDownSec[0] = (float) rightAvg;
            rightDownSec[1] = rightSumVar;
            rightDownSec[2] = rightMax;
            rightDownSec[3] = rightMin;

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + QueryFileName.DOWN_META_SEC + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + QueryFileName.DOWN_META_SEC + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));

            leftList = new ArrayList<Float>();
            rightList = new ArrayList<Float>();

            while((content = leftBr.readLine()) != null)
                leftList.add(Float.parseFloat(content));

            while((content = rightBr.readLine()) != null)
                rightList.add(Float.parseFloat(content));

            float[] leftDownMetaSec = new float[4];
            float[] rightDownMetaSec = new float[4];

            leftAvg = leftList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            rightAvg = rightList.parallelStream().collect(Collectors.averagingDouble(Float::doubleValue));
            leftCount = leftList.parallelStream().count();
            rightCount = leftList.parallelStream().count();

            leftSumVar = 0;
            leftMax = 0;
            leftMin = 10000;
            for(Float s : leftList){
                if(leftMax < s)
                    leftMax = s;
                if(leftMin > s)
                    leftMin = s;
                leftSumVar += (s - leftAvg) * (s - leftAvg);
            }
            leftSumVar = (float) Math.sqrt(leftSumVar / leftCount);

            rightSumVar = 0;
            rightMax = 0;
            rightMin = 10000;
            for(Float s : rightList){
                if(rightMax < s)
                    rightMax = s;
                if(rightMin > s)
                    rightMin = s;
                rightSumVar += (s - rightAvg) * (s - rightAvg);
            }
            rightSumVar = (float) Math.sqrt(rightSumVar / rightCount);

            String metaDownResult = ResultCompareResultFunction.transCharResult(leftDBName, rightDBName,
                    leftAvg, rightAvg, leftSumVar, rightSumVar, leftMax, rightMax, leftMin, rightMin,
                    "文档下载");

            leftDownMetaSec[0] = (float) leftAvg;
            leftDownMetaSec[1] = leftSumVar;
            leftDownMetaSec[2] = leftMax;
            leftDownMetaSec[3] = leftMin;
            rightDownMetaSec[0] = (float) rightAvg;
            rightDownMetaSec[1] = rightSumVar;
            rightDownMetaSec[2] = rightMax;
            rightDownMetaSec[3] = rightMin;

            System.out.println("信息获取完毕");
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write("{"
                    + "\"leftDBType\":\""+ session.getAttribute("leftDBName") + "\","
                    + "\"rightDBType\":\""+ session.getAttribute("rightDBName") + "\","

                    + "\"leftLoadFileSec_Avg\":\"" + fnum.format(leftLoadSec[0]) + "\","
                    + "\"leftLoadFileSec_Var\":\"" + fnum.format(leftLoadSec[1]) + "\","
                    + "\"leftLoadFileSec_Max\":\"" + fnum.format(leftLoadSec[2]) + "\","
                    + "\"leftLoadFileSec_Min\":\"" + fnum.format(leftLoadSec[3]) + "\","
                    + "\"rightLoadFileSec_Avg\":\"" + fnum.format(rightLoadSec[0]) + "\","
                    + "\"rightLoadFileSec_Var\":\"" + fnum.format(rightLoadSec[1]) + "\","
                    + "\"rightLoadFileSec_Max\":\"" + fnum.format(rightLoadSec[2]) + "\","
                    + "\"rightLoadFileSec_Min\":\"" + fnum.format(rightLoadSec[3]) + "\","
                    + "\"loadFileResult\":\"" + fileLoadResult + "\","

                    + "\"leftLoadMetaSec_Avg\":\"" + fnum.format(leftLoadMetaSec[0]) + "\","
                    + "\"leftLoadMetaSec_Var\":\"" + fnum.format(leftLoadMetaSec[1]) + "\","
                    + "\"leftLoadMetaSec_Max\":\"" + fnum.format(leftLoadMetaSec[2]) + "\","
                    + "\"leftLoadMetaSec_Min\":\"" + fnum.format(leftLoadMetaSec[3]) + "\","
                    + "\"rightLoadMetaSec_Avg\":\"" + fnum.format(rightLoadMetaSec[0]) + "\","
                    + "\"rightLoadMetaSec_Var\":\"" + fnum.format(rightLoadMetaSec[1]) + "\","
                    + "\"rightLoadMetaSec_Max\":\"" + fnum.format(rightLoadMetaSec[2]) + "\","
                    + "\"rightLoadMetaSec_Min\":\"" + fnum.format(rightLoadMetaSec[3]) + "\","
                    + "\"loadMetaResult\":\"" + metaLoadResult + "\","

                    + "\"leftDownFileSec_Avg\":\"" + fnum.format(leftDownSec[0]) + "\","
                    + "\"leftDownFileSec_Var\":\"" + fnum.format(leftDownSec[1]) + "\","
                    + "\"leftDownFileSec_Max\":\"" + fnum.format(leftDownSec[2]) + "\","
                    + "\"leftDownFileSec_Min\":\"" + fnum.format(leftDownSec[3]) + "\","
                    + "\"rightDownFileSec_Avg\":\"" + fnum.format(rightDownSec[0]) + "\","
                    + "\"rightDownFileSec_Var\":\"" + fnum.format(rightDownSec[1]) + "\","
                    + "\"rightDownFileSec_Max\":\"" + fnum.format(rightDownSec[2]) + "\","
                    + "\"rightDownFileSec_Min\":\"" + fnum.format(rightDownSec[3]) + "\","
                    + "\"downFileResult\":\"" + fileDownResult + "\","

                    + "\"downMetaResult\":\"" + metaDownResult + "\","
                    + "\"leftDownMetaSec_Avg\":\"" + fnum.format(leftDownMetaSec[0]) + "\","
                    + "\"leftDownMetaSec_Var\":\"" + fnum.format(leftDownMetaSec[1]) + "\","
                    + "\"leftDownMetaSec_Max\":\"" + fnum.format(leftDownMetaSec[2]) + "\","
                    + "\"leftDownMetaSec_Min\":\"" + fnum.format(leftDownMetaSec[3]) + "\","
                    + "\"rightDownMetaSec_Avg\":\"" + fnum.format(rightDownMetaSec[0]) + "\","
                    + "\"rightDownMetaSec_Var\":\"" + fnum.format(rightDownMetaSec[1]) + "\","
                    + "\"rightDownMetaSec_Max\":\"" + fnum.format(rightDownMetaSec[2]) + "\","
                    + "\"rightDownMetaSec_Min\":\"" + fnum.format(rightDownMetaSec[3])
                    + "\"}");

        }
        //烂尾：用于绘制折线图
        //原因：有些数值数量过小时，300的定值绘制不好看
        /*
        if(type.equals("transchart")){
            String transType = request.getParameter("transtyle");
            String transPathSec = null;
            String transPathPer = null;

            if(transType.equals("loadfile")){
                transPathPer = QueryFileName.LOAD_FILE_PER + ".txt";
                transPathSec = QueryFileName.DOWN_FILE_SEC + ".txt";
            }else if(transType.equals("downfile")){
                transPathPer = QueryFileName.DOWN_FILE_PER + ",txt";
                transPathSec = QueryFileName.DOWN_FILE_SEC + ".txt";
            }else if(transType.equals("loadmeta")){
                transPathSec = QueryFileName.LOAD_META_SEC + ".txt";
            }else{
                transPathSec = QueryFileName.DOWN_META_SEC + ".txt";
            }

            if(transPathPer != null){
                File leftTransPer = new File(session.getAttribute("logpath") + "/" +
                        session.getAttribute("leftDBName") + "/"
                        + session.getAttribute("leftTransName") + "/"
                        + session.getAttribute("leftQueryName") + "/"
                        + transPathPer);

                File leftTransSec = new File(session.getAttribute("logpath") + "/" +
                        session.getAttribute("leftDBName") + "/"
                        + session.getAttribute("leftTransName") + "/"
                        + session.getAttribute("leftQueryName") + "/"
                        + transPathSec);

                File rightTransPer = new File(session.getAttribute("logpath") + "/" +
                        session.getAttribute("rightDBName") + "/"
                        + session.getAttribute("rightTransName") + "/"
                        + session.getAttribute("rightQueryName") + "/"
                        + transPathPer);

                File rightTransSec = new File(session.getAttribute("logpath") + "/" +
                        session.getAttribute("rightDBName") + "/"
                        + session.getAttribute("rightTransName") + "/"
                        + session.getAttribute("rightQueryName") + "/"
                        + transPathSec);

                BufferedReader leftTransPerBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftTransPer)));
                BufferedReader rightTransPerBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightTransPer)));
                BufferedReader leftTransSecBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftTransSec)));
                BufferedReader rightTransSecBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightTransSec)));

                String content;
                ArrayList<Float> leftTransPerList = new ArrayList<Float>();
                ArrayList<Float> rightTransPerList = new ArrayList<Float>();
                ArrayList<Float> leftTransSecList = new ArrayList<Float>();
                ArrayList<Float> rightTransSecList = new ArrayList<Float>();

                while((content = leftTransPerBr.readLine()) != null)
                    leftTransPerList.add(Float.parseFloat(content));

                while((content = rightTransPerBr.readLine()) != null)
                    rightTransPerList.add(Float.parseFloat(content));

                while((content = leftTransSecBr.readLine()) != null)
                    leftTransSecList.add(Float.parseFloat(content));

                while((content = rightTransSecBr.readLine()) != null)
                    rightTransSecList.add(Float.parseFloat(content));


            }
            else{

            }

        }
        */
        if(type.equals("querychart")){
            int queryType = Integer.parseInt(request.getParameter("querytype"));
            String queryPath = null;

            switch (queryType){
                case 1 : queryPath = QueryFileName.DOWN_QUERY_ONE + ".txt";break;
                case 2 : queryPath = QueryFileName.DOWN_QUERY_TWO + ".txt";break;
                case 3 : queryPath = QueryFileName.DOWN_QUERY_THREE + ".txt";break;
                case 4 : queryPath = QueryFileName.DOWN_QUERY_FOUR + ".txt";break;
                case 5 : queryPath = QueryFileName.DOWN_QUERY_FIVE + ".txt";break;
                case 6 : queryPath = QueryFileName.DOWN_QUERY_SIX + ".txt";break;
                case 7 : queryPath = QueryFileName.DOWN_QUERY_SEVEN + ".txt";break;
            }

            File leftQuery = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + queryPath);
            File rightQuery = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + queryPath);

            BufferedReader leftQueryBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftQuery)));
            BufferedReader rightQueryBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightQuery)));

            String content;
            ArrayList<Float> leftQueryList = new ArrayList<Float>();
            ArrayList<Float> rightQueryList = new ArrayList<Float>();

            while((content = leftQueryBr.readLine()) != null)
                leftQueryList.add(Float.parseFloat(content));

            while((content = rightQueryBr.readLine()) != null)
                rightQueryList.add(Float.parseFloat(content));

            if(leftQueryList.size() > 300){
                int filter = leftQueryList.size() / 300;
                ArrayList<Float> leftQuery300List = new ArrayList<>();
                float sum = 0;
                int i = 0;
                if(filter == 1){
                    int checksum = leftQueryList.size() - 300;
                    Iterator<Float> haha = leftQueryList.iterator();
                    int j = 0;
                    while(haha.hasNext()){
                        if(j < checksum){
                            leftQuery300List.add((haha.next() + haha.next()) / 2);
                            j++;
                        }
                        else
                            leftQuery300List.add(haha.next());
                    }

                }
                else
                    for(; i < leftQueryList.size() && leftQuery300List.size() < 300; i++){
                        if((i+1) % filter == 0){
                         leftQuery300List.add(sum / filter);
                         sum = 0;
                        }
                        else
                            sum += leftQueryList.get(i);
                    }
                leftQueryList = leftQuery300List;
            }

            if(rightQueryList.size() > 300){
                int filter = rightQueryList.size() / 300;
                ArrayList<Float> rightQuery300List = new ArrayList<Float>();
                float sum = 0;
                int i = 0;
                if(filter == 1){
                    int checksum = rightQueryList.size() - 300;
                    Iterator<Float> haha = rightQueryList.iterator();
                    int j = 0;
                    while(haha.hasNext()){
                        if(j < checksum){
                            rightQuery300List.add((haha.next() + haha.next()) / 2);
                            j++;
                        }
                        else
                            rightQuery300List.add(haha.next());
                    }

                }
                else
                    for(; i < rightQueryList.size() && rightQuery300List.size() < 300; i++){
                        if((i+1) % filter == 0){
                            rightQuery300List.add(sum / filter);
                            sum = 0;
                        }
                        else
                            sum += rightQueryList.get(i);
                    }
                rightQueryList = rightQuery300List;
            }

            String left = "[";
            for(float s : leftQueryList)
                left += "{\"speed\":" + fnum.format(s) + "},";
            left = left.substring(0, left.length() - 1) + "]";

            String right = "[";
            for(float s : rightQueryList)
                right += "{\"speed\":" + fnum.format(s) + "},";
            right = right.substring(0, right.length() - 1) + "]";

            response.getWriter().write("{\"leftdb\":\""+ session.getAttribute("leftDBName") + "\","
                    + "\"rightdb\":\""+ session.getAttribute("rightDBName") + "\","
                    + "\"leftarray\":" + left + ","
                    + "\"rightarray\":"+ right
                    + "}");
        }

        if(type.equals("simplequeryinfo")){
            System.out.println("进入simplequeryinfo");
            String strl, strr;
            String leftDBName = ( String ) session.getAttribute("leftDBName");
            String rightDBName = ( String ) session.getAttribute("rightDBName");

            File leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_ONE + ".txt");

            File rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_ONE + ".txt");

            BufferedReader leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            BufferedReader rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl1 = new HashMap<>();
            Map<String, String> simplequeryr1 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl1.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr1.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_TWO + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_TWO + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl2 = new HashMap<>();
            Map<String, String> simplequeryr2 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl2.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr2.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_THREE + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_THREE + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl3 = new HashMap<>();
            Map<String, String> simplequeryr3 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl3.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr3.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_FOUR + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_FOUR + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl4 = new HashMap<>();
            Map<String, String> simplequeryr4 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl4.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr4.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_FIVE + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_FIVE + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl5 = new HashMap<>();
            Map<String, String> simplequeryr5 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl5.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr5.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_SIX + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_SIX + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl6 = new HashMap<>();
            Map<String, String> simplequeryr6 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl6.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr6.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_SEVEN + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_SEVEN + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> simplequeryl7 = new HashMap<>();
            Map<String, String> simplequeryr7 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                simplequeryl7.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                simplequeryr7.put(strr.split(";")[0], strr.split(";")[1]);

            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write("{"
                    + "\"simplequeryl11\":\"" + simplequeryl1.get("count") + "\","
                    + "\"simplequeryr11\":\"" + simplequeryr1.get("count") + "\","
                    + "\"simplequeryl21\":\"" + simplequeryl1.get("speed") + "\","
                    + "\"simplequeryr21\":\"" + simplequeryr1.get("speed") + "\","
                    + "\"simplequeryl31\":\"" + simplequeryl1.get("time") + "\","
                    + "\"simplequeryr31\":\"" + simplequeryr1.get("time") + "\","
                    + "\"simplequery1result\":\"" + ResultCompareResultFunction.queryResult("单属性单数值查询",
                    leftDBName, rightDBName, simplequeryl1.get("speed"), simplequeryr1.get("speed")) + "\","

                    + "\"simplequeryl12\":\"" + simplequeryl2.get("count") + "\","
                    + "\"simplequeryr12\":\"" + simplequeryr2.get("count") + "\","
                    + "\"simplequeryl22\":\"" + simplequeryl2.get("speed") + "\","
                    + "\"simplequeryr22\":\"" + simplequeryr2.get("speed") + "\","
                    + "\"simplequeryl32\":\"" + simplequeryl2.get("time") + "\","
                    + "\"simplequeryr32\":\"" + simplequeryr2.get("time") + "\","
                    + "\"simplequery2result\":\"" + ResultCompareResultFunction.queryResult("单属性离散数值查询",
                    leftDBName, rightDBName, simplequeryl2.get("speed"), simplequeryr2.get("speed")) + "\","

                    + "\"simplequeryl13\":\"" + simplequeryl3.get("count") + "\","
                    + "\"simplequeryr13\":\"" + simplequeryr3.get("count") + "\","
                    + "\"simplequeryl23\":\"" + simplequeryl3.get("speed") + "\","
                    + "\"simplequeryr23\":\"" + simplequeryr3.get("speed") + "\","
                    + "\"simplequeryl33\":\"" + simplequeryl3.get("time") + "\","
                    + "\"simplequeryr33\":\"" + simplequeryr3.get("time") + "\","
                    + "\"simplequery3result\":\"" + ResultCompareResultFunction.queryResult("单属性连续数值查询",
                    leftDBName, rightDBName, simplequeryl3.get("speed"), simplequeryr3.get("speed")) + "\","

                    + "\"simplequeryl14\":\"" + simplequeryl4.get("count") + "\","
                    + "\"simplequeryr14\":\"" + simplequeryr4.get("count") + "\","
                    + "\"simplequeryl24\":\"" + simplequeryl4.get("speed") + "\","
                    + "\"simplequeryr24\":\"" + simplequeryr4.get("speed") + "\","
                    + "\"simplequeryl34\":\"" + simplequeryl4.get("time") + "\","
                    + "\"simplequeryr34\":\"" + simplequeryr4.get("time") + "\","
                    + "\"simplequery4result\":\"" + ResultCompareResultFunction.queryResult("多属性单数值查询",
                    leftDBName, rightDBName, simplequeryl4.get("speed"), simplequeryr4.get("speed")) + "\","

                    + "\"simplequeryl15\":\"" + simplequeryl5.get("count") + "\","
                    + "\"simplequeryr15\":\"" + simplequeryr5.get("count") + "\","
                    + "\"simplequeryl25\":\"" + simplequeryl5.get("speed") + "\","
                    + "\"simplequeryr25\":\"" + simplequeryr5.get("speed") + "\","
                    + "\"simplequeryl35\":\"" + simplequeryl5.get("time") + "\","
                    + "\"simplequeryr35\":\"" + simplequeryr5.get("time") + "\","
                    + "\"simplequery5result\":\"" + ResultCompareResultFunction.queryResult("多属性离散数值查询",
                    leftDBName, rightDBName, simplequeryl5.get("speed"), simplequeryr5.get("speed")) + "\","

                    + "\"simplequeryl16\":\"" + simplequeryl6.get("count") + "\","
                    + "\"simplequeryr16\":\"" + simplequeryr6.get("count") + "\","
                    + "\"simplequeryl26\":\"" + simplequeryl6.get("speed") + "\","
                    + "\"simplequeryr26\":\"" + simplequeryr6.get("speed") + "\","
                    + "\"simplequeryl36\":\"" + simplequeryl6.get("time") + "\","
                    + "\"simplequeryr36\":\"" + simplequeryr6.get("time") + "\","
                    + "\"simplequery6result\":\"" + ResultCompareResultFunction.queryResult("多属性连续数值查询",
                    leftDBName, rightDBName, simplequeryl6.get("speed"), simplequeryr6.get("speed")) + "\","

                    + "\"simplequery7result\":\"" + ResultCompareResultFunction.queryResult("多属性多类型数值查询",
                    leftDBName, rightDBName, simplequeryl7.get("speed"), simplequeryr7.get("speed")) + "\","
                    + "\"simplequeryl17\":\"" + simplequeryl7.get("count") + "\","
                    + "\"simplequeryr17\":\"" + simplequeryr7.get("count") + "\","
                    + "\"simplequeryl27\":\"" + simplequeryl7.get("speed") + "\","
                    + "\"simplequeryr27\":\"" + simplequeryr7.get("speed") + "\","
                    + "\"simplequeryl37\":\"" + simplequeryl7.get("time") + "\","
                    + "\"simplequeryr37\":\"" + simplequeryr7.get("time")
                    + "\"}");
        }

        if(type.equals("querymakecolumn")){
            int queryType = Integer.parseInt(request.getParameter("querytype"));
            String queryPath = null;

            switch (queryType){
                case 1 : queryPath = QueryFileName.DOWN_QUERY_ONE + ".txt";break;
                case 2 : queryPath = QueryFileName.DOWN_QUERY_TWO + ".txt";break;
                case 3 : queryPath = QueryFileName.DOWN_QUERY_THREE + ".txt";break;
                case 4 : queryPath = QueryFileName.DOWN_QUERY_FOUR + ".txt";break;
                case 5 : queryPath = QueryFileName.DOWN_QUERY_FIVE + ".txt";break;
                case 6 : queryPath = QueryFileName.DOWN_QUERY_SIX + ".txt";break;
                case 7 : queryPath = QueryFileName.DOWN_QUERY_SEVEN + ".txt";break;
            }

            File leftQuery = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + queryPath);
            File rightQuery = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + queryPath);

            BufferedReader leftQueryBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftQuery)));
            BufferedReader rightQueryBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightQuery)));

            String content;
            ArrayList<Double> leftQueryList = new ArrayList<>();
            ArrayList<Double> rightQueryList = new ArrayList<>();

            while((content = leftQueryBr.readLine()) != null)
                leftQueryList.add(Double.parseDouble(content));

            while((content = rightQueryBr.readLine()) != null)
                rightQueryList.add(Double.parseDouble(content));

            double leftAvg = leftQueryList.parallelStream().collect(Collectors.averagingDouble(Double::doubleValue));
            double rightAvg = rightQueryList.parallelStream().collect(Collectors.averagingDouble(Double::doubleValue));

            double leftMin = 100000, leftMax = 0;
            double rightMin = 100000, rightMax = 0;

            double leftVar = 0;
            for (Double data : leftQueryList){
                leftVar += (data - leftAvg) * (data - leftAvg);
                if(leftMin > data)  leftMin = data;
                if(leftMax < data)  leftMax = data;
            }
            leftVar = Math.sqrt(leftVar / leftQueryList.size());

            double rightVar = 0;
            for (Double data : rightQueryList){
                rightVar += (data - rightAvg) * (data - rightAvg);
                if(rightMin > data)  rightMin = data;
                if(rightMax < data)  rightMax = data;
            }
            rightVar = Math.sqrt(rightVar / rightQueryList.size());

            System.out.println("{"
                    + "\"leftDBType\":\""+ session.getAttribute("leftDBName") + "\","
                    + "\"rightDBType\":\""+ session.getAttribute("rightDBName") + "\","
                    + "\"leftFilePer_Avg\":\"" + fnum.format(leftAvg) + "\","
                    + "\"leftFilePer_Var\":\"" + fnum.format(leftVar) + "\","
                    + "\"leftFilePer_Max\":\"" + fnum.format(leftMax) + "\","
                    + "\"leftFilePer_Min\":\"" + fnum.format(leftMin) + "\","
                    + "\"rightFilePer_Avg\":\"" + fnum.format(rightAvg) + "\","
                    + "\"rightFilePer_Var\":\"" + fnum.format(rightVar) + "\","
                    + "\"rightFilePer_Max\":\"" + fnum.format(rightMax) + "\","
                    + "\"rightFilePer_Min\":\"" + fnum.format(rightMin) + "\""
                    + "}");

            response.getWriter().write("{"
                    + "\"leftDBType\":\""+ session.getAttribute("leftDBName") + "\","
                    + "\"rightDBType\":\""+ session.getAttribute("rightDBName") + "\","
                    + "\"leftFilePer_Avg\":\"" + fnum.format(leftAvg) + "\","
                    + "\"leftFilePer_Var\":\"" + fnum.format(leftVar) + "\","
                    + "\"leftFilePer_Max\":\"" + fnum.format(leftMax) + "\","
                    + "\"leftFilePer_Min\":\"" + fnum.format(leftMin) + "\","
                    + "\"rightFilePer_Avg\":\"" + fnum.format(rightAvg) + "\","
                    + "\"rightFilePer_Var\":\"" + fnum.format(rightVar) + "\","
                    + "\"rightFilePer_Max\":\"" + fnum.format(rightMax) + "\","
                    + "\"rightFilePer_Min\":\"" + fnum.format(rightMin) + "\""
                    + "}");
        }

        if(type.equals("copyinfo")){
            System.out.println("进入copyinfo");
            String leftDBName = ( String ) session.getAttribute("leftDBName");
            String rightDBName = ( String ) session.getAttribute("rightDBName");
            String leftPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_VERISON + ".txt";
            String rightPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_VERISON + ".txt";

            BufferedReader leftBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(leftPath))));
            BufferedReader rightBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rightPath))));

            Map<String, String> copyl1 = new HashMap<>();
            Map<String, String> copyr1 = new HashMap<>();

            String strl, strr;

            while ((strl = leftBR.readLine()) != null)
                copyl1.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBR.readLine()) != null)
                copyr1.put(strr.split(";")[0], strr.split(";")[1]);

            System.out.println("{"
                    + "\"copytableleft1\":\""+ copyl1.get("type") + "\","
                    + "\"copytableleft2\":\""+ copyl1.get("count") + "\","
                    + "\"copytableleft3\":\"" + copyl1.get("time") + "\","
                    + "\"copytableleft4\":\"" + copyl1.get("speed") + "\","
                    + "\"copytableleft5\":\"" + copyl1.get("rate") + "\","

                    + "\"copyresult\":\"" + ResultCompareResultFunction.copyQueryResult(leftDBName, rightDBName,
                        copyl1, copyr1) + "\","

                    + "\"copytableright1\":\"" + copyr1.get("type") + "\","
                    + "\"copytableright2\":\"" + copyr1.get("count") + "\","
                    + "\"copytableright3\":\"" + copyr1.get("time") + "\","
                    + "\"copytableright4\":\"" + copyr1.get("speed") + "\","
                    + "\"copytableright5\":\"" + copyr1.get("rate") + "\""
                    + "}");

            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write("{"
                    + "\"copytableleft1\":\""+ copyl1.get("type") + "\","
                    + "\"copytableleft2\":\""+ copyl1.get("count") + "\","
                    + "\"copytableleft3\":\"" + copyl1.get("time") + "\","
                    + "\"copytableleft4\":\"" + copyl1.get("speed") + "\","
                    + "\"copytableleft5\":\"" + copyl1.get("rate") + "\","

                    + "\"copyresult\":\"" + ResultCompareResultFunction.copyQueryResult(leftDBName, rightDBName,
                    copyl1, copyr1) + "\","

                    + "\"copytableright1\":\"" + copyr1.get("type") + "\","
                    + "\"copytableright2\":\"" + copyr1.get("count") + "\","
                    + "\"copytableright3\":\"" + copyr1.get("time") + "\","
                    + "\"copytableright4\":\"" + copyr1.get("speed") + "\","
                    + "\"copytableright5\":\"" + copyr1.get("rate") + "\""
                    + "}");
        }

        if(type.equals("complexqueryinfo")){
            String leftDBName = ( String ) session.getAttribute("leftDBName");
            String rightDBName = ( String ) session.getAttribute("rightDBName");

            File leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_NINE + ".txt");

            File rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_NINE + ".txt");

            BufferedReader leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            BufferedReader rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            String strl, strr;

            Map<String, String> complexqueryl1 = new HashMap<>();
            Map<String, String> complexqueryr1 = new HashMap<>();
            while ((strl = leftBr.readLine()) != null)
                complexqueryl1.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                complexqueryr1.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_TEN + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_TEN + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> complexqueryl2 = new HashMap<>();
            Map<String, String> complexqueryr2 = new HashMap<>();
            while ((strl = leftBr.readLine()) != null)
                complexqueryl2.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                complexqueryr2.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_ELEVEN + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_ELEVEN + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> complexqueryl3 = new HashMap<>();
            Map<String, String> complexqueryr3 = new HashMap<>();
            while ((strl = leftBr.readLine()) != null)
                complexqueryl3.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                complexqueryr3.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_TWELVE + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_TWELVE + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> complexqueryl4 = new HashMap<>();
            Map<String, String> complexqueryr4 = new HashMap<>();
            while ((strl = leftBr.readLine()) != null)
                complexqueryl4.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                complexqueryr4.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_THIRTEEN + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_THIRTEEN + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> complexqueryl5 = new HashMap<>();
            Map<String, String> complexqueryr5 = new HashMap<>();
            while ((strl = leftBr.readLine()) != null)
                complexqueryl5.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                complexqueryr5.put(strr.split(";")[0], strr.split(";")[1]);

            leftInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_FOURTEEN + ".txt");

            rightInfoFile = new File(session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_FOURTEEN + ".txt");

            leftBr = new BufferedReader(new InputStreamReader(new FileInputStream(leftInfoFile)));
            rightBr = new BufferedReader(new InputStreamReader(new FileInputStream(rightInfoFile)));
            Map<String, String> complexqueryl6 = new HashMap<>();
            Map<String, String> complexqueryr6 = new HashMap<>();

            while ((strl = leftBr.readLine()) != null)
                complexqueryl6.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBr.readLine()) != null)
                complexqueryr6.put(strr.split(";")[0], strr.split(";")[1]);

            System.out.println("{"
                    + "\"complexqueryl11\":\"" + complexqueryl1.get("count") + "\","
                    + "\"complexqueryr11\":\"" + complexqueryr1.get("count") + "\","
                    + "\"complexqueryl21\":\"" + complexqueryl1.get("speed") + "\","
                    + "\"complexqueryr21\":\"" + complexqueryr1.get("speed") + "\","
                    + "\"complexqueryl31\":\"" + complexqueryl1.get("time") + "\","
                    + "\"complexqueryr31\":\"" + complexqueryr1.get("time") + "\","
                    + "\"complexqueryresult1\":\"" + ResultCompareResultFunction.queryResult("数值排序", leftDBName,
                    rightDBName, complexqueryl1.get("speed"), complexqueryr1.get("speed")) + "\","

                    + "\"complexqueryl12\":\"" + complexqueryl2.get("count") + "\","
                    + "\"complexqueryr12\":\"" + complexqueryr2.get("count") + "\","
                    + "\"complexqueryl22\":\"" + complexqueryl2.get("speed") + "\","
                    + "\"complexqueryr22\":\"" + complexqueryr2.get("speed") + "\","
                    + "\"complexqueryl32\":\"" + complexqueryl2.get("time") + "\","
                    + "\"complexqueryr32\":\"" + complexqueryr2.get("time") + "\","
                    + "\"complexqueryresult2\":\"" + ResultCompareResultFunction.queryResult("字符串排序", leftDBName,
                    rightDBName, complexqueryl2.get("speed"), complexqueryr2.get("speed")) + "\","

                    + "\"complexqueryl13\":\"" + complexqueryl3.get("count") + "\","
                    + "\"complexqueryr13\":\"" + complexqueryr3.get("count") + "\","
                    + "\"complexqueryl23\":\"" + complexqueryl3.get("speed") + "\","
                    + "\"complexqueryr23\":\"" + complexqueryr3.get("speed") + "\","
                    + "\"complexqueryl33\":\"" + complexqueryl3.get("time") + "\","
                    + "\"complexqueryr33\":\"" + complexqueryr3.get("time") + "\","
                    + "\"complexqueryresult3\":\"" + ResultCompareResultFunction.queryResult("日期排序", leftDBName,
                    rightDBName, complexqueryl3.get("speed"), complexqueryr3.get("speed")) + "\","

                    + "\"complexqueryl14\":\"" + complexqueryl4.get("count") + "\","
                    + "\"complexqueryr14\":\"" + complexqueryr4.get("count") + "\","
                    + "\"complexqueryl24\":\"" + complexqueryl4.get("speed") + "\","
                    + "\"complexqueryr24\":\"" + complexqueryr4.get("speed") + "\","
                    + "\"complexqueryl34\":\"" + complexqueryl4.get("time") + "\","
                    + "\"complexqueryr34\":\"" + complexqueryr4.get("time") + "\","
                    + "\"complexqueryresult4\":\"" + ResultCompareResultFunction.queryResult("平均值计算", leftDBName,
                    rightDBName, complexqueryl4.get("speed"), complexqueryr4.get("speed")) + "\","

                    + "\"complexqueryl15\":\"" + complexqueryl5.get("count") + "\","
                    + "\"complexqueryr15\":\"" + complexqueryr5.get("count") + "\","
                    + "\"complexqueryl25\":\"" + complexqueryl5.get("speed") + "\","
                    + "\"complexqueryr25\":\"" + complexqueryr5.get("speed") + "\","
                    + "\"complexqueryl35\":\"" + complexqueryl5.get("time")+ "\","
                    + "\"complexqueryr35\":\"" + complexqueryr5.get("time") + "\","
                    + "\"complexqueryresult5\":\"" + ResultCompareResultFunction.queryResult("最小值计算", leftDBName,
                    rightDBName, complexqueryl5.get("speed"), complexqueryr5.get("speed")) + "\","

                    + "\"complexqueryresult6\":\"" + ResultCompareResultFunction.queryResult("最大值计算", leftDBName,
                    rightDBName, complexqueryl6.get("speed"), complexqueryr6.get("speed")) + "\","
                    + "\"complexqueryl16\":\"" + complexqueryl6.get("count") + "\","
                    + "\"complexqueryr16\":\"" + complexqueryr6.get("count") + "\","
                    + "\"complexqueryl26\":\"" + complexqueryl6.get("speed") + "\","
                    + "\"complexqueryr26\":\"" + complexqueryr6.get("speed") + "\","
                    + "\"complexqueryl36\":\"" + complexqueryl6.get("time") + "\","
                    + "\"complexqueryr36\":\"" + complexqueryr6.get("time")
                    + "\"}");

            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write("{"
                    + "\"complexqueryl11\":\"" + complexqueryl1.get("count") + "\","
                    + "\"complexqueryr11\":\"" + complexqueryr1.get("count") + "\","
                    + "\"complexqueryl21\":\"" + complexqueryl1.get("speed") + "\","
                    + "\"complexqueryr21\":\"" + complexqueryr1.get("speed") + "\","
                    + "\"complexqueryl31\":\"" + complexqueryl1.get("time") + "\","
                    + "\"complexqueryr31\":\"" + complexqueryr1.get("time") + "\","
                    + "\"complexqueryresult1\":\"" + ResultCompareResultFunction.queryResult("数值排序", leftDBName,
                    rightDBName, complexqueryl1.get("speed"), complexqueryr1.get("speed")) + "\","

                    + "\"complexqueryl12\":\"" + complexqueryl2.get("count") + "\","
                    + "\"complexqueryr12\":\"" + complexqueryr2.get("count") + "\","
                    + "\"complexqueryl22\":\"" + complexqueryl2.get("speed") + "\","
                    + "\"complexqueryr22\":\"" + complexqueryr2.get("speed") + "\","
                    + "\"complexqueryl32\":\"" + complexqueryl2.get("time") + "\","
                    + "\"complexqueryr32\":\"" + complexqueryr2.get("time") + "\","
                    + "\"complexqueryresult2\":\"" + ResultCompareResultFunction.queryResult("字符串排序", leftDBName,
                    rightDBName, complexqueryl2.get("speed"), complexqueryr2.get("speed")) + "\","

                    + "\"complexqueryl13\":\"" + complexqueryl3.get("count") + "\","
                    + "\"complexqueryr13\":\"" + complexqueryr3.get("count") + "\","
                    + "\"complexqueryl23\":\"" + complexqueryl3.get("speed") + "\","
                    + "\"complexqueryr23\":\"" + complexqueryr3.get("speed") + "\","
                    + "\"complexqueryl33\":\"" + complexqueryl3.get("time") + "\","
                    + "\"complexqueryr33\":\"" + complexqueryr3.get("time") + "\","
                    + "\"complexqueryresult3\":\"" + ResultCompareResultFunction.queryResult("日期排序", leftDBName,
                    rightDBName, complexqueryl3.get("speed"), complexqueryr3.get("speed")) + "\","

                    + "\"complexqueryl14\":\"" + complexqueryl4.get("count") + "\","
                    + "\"complexqueryr14\":\"" + complexqueryr4.get("count") + "\","
                    + "\"complexqueryl24\":\"" + complexqueryl4.get("speed") + "\","
                    + "\"complexqueryr24\":\"" + complexqueryr4.get("speed") + "\","
                    + "\"complexqueryl34\":\"" + complexqueryl4.get("time") + "\","
                    + "\"complexqueryr34\":\"" + complexqueryr4.get("time") + "\","
                    + "\"complexqueryresult4\":\"" + ResultCompareResultFunction.queryResult("平均值计算", leftDBName,
                    rightDBName, complexqueryl4.get("speed"), complexqueryr4.get("speed")) + "\","

                    + "\"complexqueryl15\":\"" + complexqueryl5.get("count") + "\","
                    + "\"complexqueryr15\":\"" + complexqueryr5.get("count") + "\","
                    + "\"complexqueryl25\":\"" + complexqueryl5.get("speed") + "\","
                    + "\"complexqueryr25\":\"" + complexqueryr5.get("speed") + "\","
                    + "\"complexqueryl35\":\"" + complexqueryl5.get("time")+ "\","
                    + "\"complexqueryr35\":\"" + complexqueryr5.get("time") + "\","
                    + "\"complexqueryresult5\":\"" + ResultCompareResultFunction.queryResult("最小值计算", leftDBName,
                    rightDBName, complexqueryl5.get("speed"), complexqueryr5.get("speed")) + "\","

                    + "\"complexqueryresult6\":\"" + ResultCompareResultFunction.queryResult("最大值计算", leftDBName,
                    rightDBName, complexqueryl6.get("speed"), complexqueryr6.get("speed")) + "\","
                    + "\"complexqueryl16\":\"" + complexqueryl6.get("count") + "\","
                    + "\"complexqueryr16\":\"" + complexqueryr6.get("count") + "\","
                    + "\"complexqueryl26\":\"" + complexqueryl6.get("speed") + "\","
                    + "\"complexqueryr26\":\"" + complexqueryr6.get("speed") + "\","
                    + "\"complexqueryl36\":\"" + complexqueryl6.get("time") + "\","
                    + "\"complexqueryr36\":\"" + complexqueryr6.get("time")
                    + "\"}");
        }

        if(type.equals("abnormalinfo")){
            System.out.println("进入abnormalinfo");
            String leftDBName = ( String ) session.getAttribute("leftDBName");
            String rightDBName = ( String ) session.getAttribute("rightDBName");
            String leftPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("leftDBName") + "/"
                    + session.getAttribute("leftTransName") + "/"
                    + session.getAttribute("leftQueryName") + "/"
                    + QueryFileName.QUERY_ABNORMAL + ".txt";
            String rightPath = session.getAttribute("logpath") + "/" +
                    session.getAttribute("rightDBName") + "/"
                    + session.getAttribute("rightTransName") + "/"
                    + session.getAttribute("rightQueryName") + "/"
                    + QueryFileName.QUERY_ABNORMAL + ".txt";

            BufferedReader leftBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(leftPath))));
            BufferedReader rightBR = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rightPath))));

            String strl, strr;
            Map<String, String> abl1 = new HashMap<>();
            Map<String, String> abr1 = new HashMap<>();

            while ((strl = leftBR.readLine()) != null)
                abl1.put(strl.split(";")[0], strl.split(";")[1]);
            while((strr = rightBR.readLine()) != null)
                abr1.put(strr.split(";")[0], strr.split(";")[1]);

            System.out.println("{"
                    + "\"abnormalleft1\":\""+ abl1.get("type") + "\","
                    + "\"abnormalleft2\":\""+ abl1.get("count") + "\","
                    + "\"abnormalleft3\":\"" + abl1.get("time") + "\","
                    + "\"abnormalleft4\":\"" + abl1.get("speed") + "\","
                    + "\"abnormalleft5\":\"" + abl1.get("findcount") + "\","
                    + "\"abnormalleft6\":\"" + abl1.get("truecount") + "\","
                    + "\"abnormalleft7\":\"" + abl1.get("rate") + "\","

                    + "\"abnormalresult\":\"" + ResultCompareResultFunction.abnormalQueryResult(leftDBName, rightDBName,
                        abl1, abr1) + "\","

                    + "\"abnormalright1\":\""+ abr1.get("type") + "\","
                    + "\"abnormalright2\":\""+ abr1.get("count") + "\","
                    + "\"abnormalright3\":\"" + abr1.get("time") + "\","
                    + "\"abnormalright4\":\"" + abr1.get("speed") + "\","
                    + "\"abnormalright5\":\"" + abr1.get("findcount") + "\","
                    + "\"abnormalright6\":\"" + abr1.get("truecount") + "\","
                    + "\"abnormalright7\":\"" + abr1.get("rate") + "\""
                    + "}");

            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write("{"
                    + "\"abnormalleft1\":\""+ abl1.get("type") + "\","
                    + "\"abnormalleft2\":\""+ abl1.get("count") + "\","
                    + "\"abnormalleft3\":\"" + abl1.get("time") + "\","
                    + "\"abnormalleft4\":\"" + abl1.get("speed") + "\","
                    + "\"abnormalleft5\":\"" + abl1.get("findcount") + "\","
                    + "\"abnormalleft6\":\"" + abl1.get("truecount") + "\","
                    + "\"abnormalleft7\":\"" + abl1.get("rate") + "\","

                    + "\"abnormalresult\":\"" + ResultCompareResultFunction.abnormalQueryResult(leftDBName, rightDBName,
                    abl1, abr1) + "\","

                    + "\"abnormalright1\":\""+ abr1.get("type") + "\","
                    + "\"abnormalright2\":\""+ abr1.get("count") + "\","
                    + "\"abnormalright3\":\"" + abr1.get("time") + "\","
                    + "\"abnormalright4\":\"" + abr1.get("speed") + "\","
                    + "\"abnormalright5\":\"" + abr1.get("findcount") + "\","
                    + "\"abnormalright6\":\"" + abr1.get("truecount") + "\","
                    + "\"abnormalright7\":\"" + abr1.get("rate") + "\""
                    + "}");
        }

        if(type.equals("hotinfo")){
            String charttype = request.getParameter("charttype");
            String filePath;
            if(charttype.equals("left"))
                filePath = session.getAttribute("logpath") + "/" +
                        session.getAttribute("leftDBName") + "/"
                        + session.getAttribute("leftTransName") + "/"
                        + session.getAttribute("leftQueryName") + "/"
                        + QueryFileName.QUERY_HOTFILE + ".txt";
            else
                filePath = session.getAttribute("logpath") + "/" +
                        session.getAttribute("rightDBName") + "/"
                        + session.getAttribute("rightTransName") + "/"
                        + session.getAttribute("rightQueryName") + "/"
                        + QueryFileName.QUERY_HOTFILE + ".txt";

            FileReader reader = new FileReader(filePath);
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
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
