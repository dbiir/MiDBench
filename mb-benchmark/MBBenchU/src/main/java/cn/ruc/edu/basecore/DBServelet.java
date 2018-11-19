package cn.ruc.edu.basecore;

import cn.ruc.edu.mbdatagen.Testpath;

import java.io.*;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DBServelet extends HttpServlet{

    FileFunction fileFunction = new FileFunction();
    UnStrDBBase database = null;
    int allFileCount = 0;
    DecimalFormat decimalFormat=new DecimalFormat(".00");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        /*
        Each time will check if there is a new result dir
         */
        if(session.getAttribute("transdir") != null)
            fileFunction.SetLogDataPathforTrans((String) session.getAttribute("transdir"));
        if(session.getAttribute("querydir") != null)
            fileFunction.SetLogDataPathforQuery((String) session.getAttribute("querydir"));

        String functionType = request.getParameter("functiontype");

        if(functionType.equals("dbconnect")){
            String ip = request.getParameter("dbip");
            int port = Integer.valueOf(request.getParameter("dbport"));
            String dbName = request.getParameter("dbname");
            session.setAttribute("dbname", dbName);
            String metaDbName = request.getParameter("metadbname");
            System.out.println(metaDbName);
            session.setAttribute("metadbname", metaDbName);
            String dataType = String.valueOf(session.getAttribute("databasetype"));
            try {
                Class<?> databaseClass = Class.forName("cn.ruc.edu.dbfunction." + dataType);
                database = (UnStrDBBase) databaseClass.newInstance();
                System.out.println(database.getClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            boolean ifLink = false;
            if(database != null){
                database.SetDbIp(ip);
                database.SetDbPort(port);
                database.SetDbName(dbName);
                database.SetCollectionName(metaDbName);
                database.SetHttpSession(session);
                ifLink = database.LinkDb();
            }
            if(ifLink)
                response.getWriter().write("{\"result\":\"true\"}");
            else
                response.getWriter().write("{\"result\":\"false\"}");
        }

        if(functionType.equals("dbdisconnect")){
            if(database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                boolean ifDisLink = database.DislinkDb();
                if(ifDisLink)
                    response.getWriter().write("{\"result\":\"true\"}");
                else
                    response.getWriter().write("{\"result\":\"false\"}");
            }
        }

        if(functionType.equals("loadfile")){
            if(database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if(stateType.equals("init")){
                    System.out.println(stateType);
                    String upLoadPath = request.getParameter("uploadpath");
                    allFileCount = FileFunction.GetFilesNumByPath(upLoadPath);
                    boolean ifupload = database.UpLoadFile(upLoadPath);
                    System.out.println("执行结束");
                    if(!ifupload)
                        response.getWriter().write("{\"result\":\"The Path is not correct!\"}");
                }
                else{
                    System.out.println(stateType);
                    int currentLoadCount = database.GetFileLoadCount();
                    long tempLoadCount = database.GetTempFileLoadSpread();

                    System.out.println("{\"loadfile\":" + currentLoadCount+ ","
                            + "\"loadfilespread\":"+ tempLoadCount+ ","
                            + "\"zongfile\":"+  allFileCount
                            + "}");

                    response.getWriter().write("{\"loadfile\":" + currentLoadCount+ ","
                            + "\"loadfilespread\":"+ tempLoadCount+ ","
                            + "\"zongfile\":"+ allFileCount
                            +"}");
                }
            }
        }

        if(functionType.equals("loadmeta")) {
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else {
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String upLoadPath = request.getParameter("uploadpath");
                    allFileCount = FileFunction.GetFilesNumByPath(upLoadPath);
                    boolean ifupload = database.UpLoadMetafile(upLoadPath);
                    if (!ifupload)
                        response.getWriter().write("{\"result\":\"The Path is not correct!\"}");
                } else {
                    int currentLoadCount = database.GetMetaLoadCount();
                    long tempLoadCount = database.GetTempMetaLoadSpread();

                    System.out.println("{\"loadmeta\":" + currentLoadCount + ","
                            + "\"loadmetaspread\":" + tempLoadCount + ","
                            + "\"zongmeta\":" + allFileCount
                            + "}");

                    response.getWriter().write("{\"loadmeta\":" + currentLoadCount + ","
                            + "\"loadmetaspread\":" + tempLoadCount + ","
                            + "\"zongmeta\":" + allFileCount
                            + "}");
                }
            }
        }

        if(functionType.equals("downfile")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else {
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String downPath = request.getParameter("downpath");
                    if(allFileCount == 0)
                        allFileCount = (int) database.GetDbFilesCount();

                    boolean ifDown = database.DownLoadFile(downPath);
                    if (!ifDown)
                        response.getWriter().write("{\"result\":\"Database may not have data!\"}");
                } else {
                    int currentDownCount = database.GetFileDownCount();
                    long tempDownCount = database.GetTempFileDownSpread();

                    System.out.println("{\"downfile\":" + currentDownCount + ","
                            + "\"downfilespread\":" + tempDownCount + ","
                            + "\"downfileall\":" + allFileCount
                            + "}");

                    response.getWriter().write("{\"downfile\":" + currentDownCount + ","
                            + "\"downfilespread\":" + tempDownCount + ","
                            + "\"downfileall\":" + allFileCount
                            + "}");
                }
            }
        }

        if(functionType.equals("downmeta")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else {
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String downPath = request.getParameter("downpath");
                    if(allFileCount == 0)
                        allFileCount = (int) database.GetDbFilesCount();

                    boolean ifDown = database.DownLoadMetaFile(downPath);
                    if (!ifDown)
                        response.getWriter().write("{\"result\":\"Database may not have data!\"}");
                } else {
                    int currentDownCount = database.GetMetaDownCount();
                    long tempDownCount = database.GetTempMetaDownSpread();

                    System.out.println("{\"downmeta\":" + currentDownCount + ","
                            + "\"downmetaspread\":" + tempDownCount + ","
                            + "\"downmetaall\":" + allFileCount
                            + "}");
                    response.getWriter().write("{\"downmeta\":" + currentDownCount + ","
                            + "\"downmetaspread\":" + tempDownCount + ","
                            + "\"downmetaall\":" + allFileCount
                            + "}");
                }
            }
        }

        if(functionType.equals("querySet")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            String fileName = request.getParameter("filename") + ".txt";

            database.newGetValue(fileName);
        }

        if(functionType.equals("queryDelete")){
            String path = Testpath.class.getClassLoader().getResource("QueryMaker").getPath();
            File[] fileArray = new File(path).listFiles();
            fileArray[0].delete();
        }
        if(functionType.equals("querya")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String downPath = request.getParameter("downpath");
                    database.SingleAttQuerry(downPath);
                } else {
                    if(!database.GetFinish()) {
                        database.activecount++;
                        String result = "Single Attribute Query is Doing, Please wait";
                        if(database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for(int i = 0; i < database.activecount; i++){
                                result = "--" + result + "--";

                            }
                            if(database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result +"\"}");
                    }
                    else{
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Single attribute query has been finished\"}");
                    }

                }
            }
        }

        if(functionType.equals("queryb")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String downPath = request.getParameter("downpath");
                    database.MultiAttQuery(downPath);
                } else {
                    if(!database.GetFinish()) {
                        database.activecount++;
                        String result = "Multi Attribute Query is Doing, Please wait";
                        if(database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for(int i = 0; i < database.activecount; i++){
                                result = "--" + result + "--";

                            }
                            if(database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result +"\"}");
                    }
                    else{
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Multi attribute query has been finished\"}");
                    }
                }
            }
        }

        if(functionType.equals("queryc")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String datapath = request.getParameter("datapath");
                    database.VersionQuery(datapath);
                } else {
                    if(!database.GetFinish()) {
                        database.activecount++;
                        String result = "Version Query is Doing, Please wait";
                        if(database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for(int i = 0; i < database.activecount; i++){
                                result = "--" + result + "--";

                            }
                            if(database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result +"\"}");
                    }
                    else{
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Version query has been finished\"}");
                    }

                }
            }
        }

        if(functionType.equals("queryd")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    database.SortQuery();
                } else {
                    if(!database.GetFinish()) {
                        database.activecount++;
                        String result = "Sort Query is Doing, Please wait";
                        if(database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for(int i = 0; i < database.activecount; i++){
                                result = "--" + result + "--";

                            }
                            if(database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result +"\"}");
                    }
                    else{
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Sort query has been finished\"}");
                    }

                }
            }
        }

        if(functionType.equals("querye")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    database.SatisticsQuery();
                } else {
                    if(!database.GetFinish()) {
                        database.activecount++;
                        String result = "Satistics Query is Doing, Please wait";
                        if(database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for(int i = 0; i < database.activecount; i++){
                                result = "--" + result + "--";

                            }
                            if(database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result +"\"}");
                    }
                    else{
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Satistics query has been finished\"}");
                    }

                }
            }
        }

        if(functionType.equals("queryf")){
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else{
                String stateType = request.getParameter("statetype");
                if (stateType.equals("init")) {
                    String datapath = request.getParameter("datapath");
                    database.AbnormalQuery(datapath);
                } else {
                    if(!database.GetFinish()) {
                        database.activecount++;
                        String result = "Abnormal Query is Doing, Please wait";
                        if(database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for(int i = 0; i < database.activecount; i++){
                                result = "--" + result + "--";

                            }
                            if(database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result +"\"}");
                    }
                    else{
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Abnormal query has been finished\"}");
                    }

                }
            }
        }

        if(functionType.equals("queryg")) {
            int queryCount = Integer.valueOf(request.getParameter("chaxunnum"));
            int interval = Integer.valueOf(request.getParameter("interval"));
            if (database == null)
                response.getWriter().write("{\"result\":\"Please connect the database!\"}");
            else {
                String stateType = request.getParameter("statetype");
                System.out.println(stateType);
                if (stateType.equals("init")) {
                    database.HotFileQuery(queryCount, interval);
                } else {
                    if (!database.GetFinish()) {
                        database.activecount++;
                        String result = "Hot-File Query is Doing, Please wait";
                        if (database.activecount > 6)
                            database.activecount = 0;
                        else {
                            for (int i = 0; i < database.activecount; i++) {
                                result = "--" + result + "--";

                            }
                            if (database.activecount == 6)
                                result = "<" + result + ">";
                        }
                        response.getWriter().write("{\"result\":\"" + result + "\"}");
                    } else {
                        database.activecount = 0;
                        response.getWriter().write("{\"result\":\"" + "Hot-File query has been finished\"}");
                    }

                }
            }
        }

        if(functionType.equals("getdbfileinfo")){
            long filenum = database.GetDbFilesCount();
            double datasize = database.GetDataSize();
            datasize = datasize / (1024 * 1024 * 1024);
            if(filenum == 0 || datasize == 0)
                response.getWriter().write("{\"filenum\":\"" + "NAN" + "\","
                        + "\"datasize\":\""+ "NAN"
                        + "\"}");
            else
                response.getWriter().write("{\"filenum\":\"" + filenum + "\","
                        + "\"datasize\":\""+ decimalFormat.format(datasize)
                        + "\"}");
        }

        if(functionType.equals("setfilepathsession")){
            session.setAttribute("filepath", request.getParameter("filepath"));

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        doGet(request, response);
    }

}
