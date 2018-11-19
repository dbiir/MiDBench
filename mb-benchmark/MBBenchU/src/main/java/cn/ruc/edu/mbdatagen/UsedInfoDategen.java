package cn.ruc.edu.mbdatagen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UsedInfoDategen
 */
public class UsedInfoDategen extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsedInfoDategen() {
        super();
        // TODO Auto-generated constructor stub
    }

    powprod datagen = null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int filenum = 0;
        int allnum = 0;
        if(Integer.parseInt(request.getParameter("width")) == 0)
        {
            String filepath = request.getParameter("filepath");
            System.out.println(filepath);
            String filename = request.getParameter("filename");
            String path = Testpath.class.getClassLoader().getResource("HistorySettings").getPath();
            File ss = new File(path);
            File[] filelist = ss.listFiles();
            File file = null;
            for(File f : filelist)
            {
                if(f.getName().split(".txt")[0].equals(filename))
                    file = f;
            }
            BufferedReader bf = null;
            System.out.println("File Name:" + file.getName());
            try {
                bf = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                        "GB2312"));
                String str = null;
                String strline = null;
                strline = bf.readLine();
                System.out.println(strline);
                int device = Integer.parseInt(strline.split("\t")[1]);
                System.out.println(device);
                strline = bf.readLine();
                String sdate = strline.split("\t")[1];
                String stime = strline.split("\t")[2];
                int shour = Integer.parseInt(stime.split(":")[0]);
                int smin = Integer.parseInt(stime.split(":")[1]);
                int ssec = Integer.parseInt(stime.split(":")[2]);
                System.out.println(strline);
                System.out.println(sdate + shour + smin + ssec);
                strline = bf.readLine();
                String edate = strline.split("\t")[1];
                stime = strline.split("\t")[2];
                int ehour = Integer.parseInt(stime.split(":")[0]);
                int emin = Integer.parseInt(stime.split(":")[1]);
                int esec = Integer.parseInt(stime.split(":")[2]);
                System.out.println(strline);
                System.out.println(edate + ehour + emin + esec);
                int[] type = new int[device];
                int[] towerheight = new int[device];
                int[] fanslength = new int[device];
                int[] fanswidth = new int[device];
                ArrayList<String> areaname = new ArrayList<String>();
                ArrayList<Float> base = new ArrayList<Float>();
                ArrayList<Float> maxzhen = new ArrayList<Float>();
                ArrayList<Float> maxjian = new ArrayList<Float>();
                ArrayList<Float> maxshun = new ArrayList<Float>();
                strline = bf.readLine();
                int divtime = Integer.parseInt(strline.split("\t")[1]);
                int uv = Integer.parseInt(strline.split("\t")[2]);
                float zhenstart = Float.parseFloat(strline.split("\t")[3]);
                float zhencycle = Float.parseFloat(strline.split("\t")[4]);
                float jianstart = Float.parseFloat(strline.split("\t")[5]);
                float jianend = Float.parseFloat(strline.split("\t")[6]);
                float jiankeep = Float.parseFloat(strline.split("\t")[7]);
                System.out.println(divtime);
                File f = new File(filepath);
                f.mkdirs();
                for(int i = 0; i < device; i++)
                {
                    strline = bf.readLine();
                    System.out.println(strline);
                    type[i] = Integer.parseInt(strline.split("\t")[1]);
                    towerheight[i] = Integer.parseInt(strline.split("\t")[2]);
                    fanslength[i] = Integer.parseInt(strline.split("\t")[3]);
                    fanswidth[i] = Integer.parseInt(strline.split("\t")[4]);
                    areaname.add(strline.split("\t")[5]);
                    base.add(Float.parseFloat(strline.split("\t")[6]));
                    maxzhen.add(Float.parseFloat(strline.split("\t")[7]));
                    maxjian.add(Float.parseFloat(strline.split("\t")[8]));
                    maxshun.add(Float.parseFloat(strline.split("\t")[9]));
                }
                strline = bf.readLine();
                int sizelength = strline.split("\t").length - 1;
                ArrayList<Float> sizearray = new ArrayList<Float>();
                ArrayList<Float> presentarray = new ArrayList<Float>();

                for(int i = 1; i <= sizelength; i++)
                    sizearray.add(Float.parseFloat(strline.split("\t")[i]));
                System.out.println(sizearray.size());
                strline = bf.readLine();
                for(int i = 1; i <= sizelength; i++)
                    presentarray.add(Float.parseFloat(strline.split("\t")[i]) / 100);
                strline = bf.readLine();
                int eachnum = 0;
                int eldernum = Integer.parseInt(strline.split("\t")[1]);
                System.out.println(eldernum);
                strline = bf.readLine();
                float unnormal = Float.parseFloat(strline.split("\t")[1]);
                if(request.getParameter("type").equals("usual"))
                    eachnum = eldernum;
                else
                    eachnum = Integer.parseInt(request.getParameter("filenum"));
                System.out.println(eachnum);

                String metatype = request.getParameter("metatype");

                datagen = new powprod();
                datagen.SetStartTime(sdate, shour, smin, ssec);
                datagen.SetEndTime(edate, ehour, emin, esec);
                datagen.SetWindparameter(zhenstart, zhencycle,
                        jianstart, jianend, jiankeep);
                datagen.SetTheAreaInfo(areaname, base, maxzhen, maxjian, maxshun);
                datagen.SetFilesize(sizearray, presentarray);

                datagen.GetBladeFile(filepath, type, towerheight, fanslength,
                        fanswidth, divtime, uv, eldernum, 0, eachnum, metatype, unnormal
                                / 100);//这里的eldernum为原设置生成的文件数量

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
        {
            filenum = datagen.GetPresentNum();
            allnum = datagen.filepluspar * datagen.areaname.size() * 3;
            System.out.println(allnum);
            //使用json格式返回数值
            float pretentsize = datagen.GetPresentSize();
            System.out.println("{\"presentnum\":" + filenum + ","
                    + "\"pretentsize\":"+ pretentsize + ","
                    + "\"allnum\":"+ allnum
                    + "}");
            response.getWriter().write("{\"presentnum\":" + filenum + ","
                    + "\"pretentsize\":"+ pretentsize + ","
                    + "\"allnum\":"+ allnum
                    + "}");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
