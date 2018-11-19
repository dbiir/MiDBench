package cn.ruc.edu.mbdatagen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.xml.sax.SAXException;

/**
 * Servlet implementation class MBNewDatagen
 */
public class MBNewDatagen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MBNewDatagen() {
		super();
		// TODO Auto-generated constructor stub
	}
	powprod datagen = null;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	float filesize = 0;
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		int devicenum = 10; //设置设备数量（即开线程的数量）
		int filenum = 200; //设置每个设备需要输出的文件数量
		int changenum = 60;
		int shour = 0; //设置起始时间（小时）
		int smin = 0; //设置起始时间（分钟）
		int ssec = 0; //设置起始时间（秒）
		int ehour = 1; //设置终止时间（小时）
		int emin = 0; //设置终止时间（分钟）
		int esec = 0; //设置终止时间（秒）
		String filepath;
		float uv = 10000; //设置传输电压
		String sdate;
		String edate;

		int towerheigthlow = 40;
		int towerheighthigh = 50;
		int fanslengthlow = 60;
		int fanslengthhigh = 80;
		int fanswidthlow = 10;
		int fanswidthheight = 15;
		float filepluspar = 500;

		String metatype = request.getParameter("metatype");

		XMLReadtest read = null;
		int typenum;
		HttpSession session = request.getSession();
		int width = Integer.parseInt(request.getParameter("width"));
		if(width == 0)
		{
			read = new XMLReadtest();
			try {
				read.ReadXMLFiles();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			typenum = read.getareanum();
			uv = read.getuv();
			ArrayList<String> areaname = read.getAreaName();
			ArrayList<Float> basewindlist = read.getBasewind();
			ArrayList<Float> maxzhenwindlist = read.getzhenwind();
			ArrayList<Float> maxjianwindlist = read.getjianwind();
			ArrayList<Float> maxshunwindlist = read.getshunwind();

			ArrayList<String> typeareaname = new ArrayList<String>();
			ArrayList<Float> typebasewindlist = new ArrayList<Float>();
			ArrayList<Float> typemaxzhenwindlist = new ArrayList<Float>();
			ArrayList<Float> typemaxjianwindlist = new ArrayList<Float>();
			ArrayList<Float> typemaxshunwindlist = new ArrayList<Float>();

			System.out.println("执行了");
			filepath = request.getParameter("datadir");
			System.out.println(filepath);
			session.setAttribute("filepath", filepath);
			filenum = Integer.parseInt(request.getParameter("eachfilen"));
			System.out.println(filenum);
			changenum = Integer.parseInt(request.getParameter("changenum"));
			devicenum = Integer.parseInt(request.getParameter("devicenum"));
			System.out.println(devicenum);
			sdate = request.getParameter("sdate");
			edate = request.getParameter("edate");
			shour = Integer.parseInt(request.getParameter("shour"));
			smin = Integer.parseInt(request.getParameter("smins"));
			ssec = Integer.parseInt(request.getParameter("ssecs"));
			ehour = Integer.parseInt(request.getParameter("ehour"));
			emin = Integer.parseInt(request.getParameter("emins"));
			esec = Integer.parseInt(request.getParameter("esecs"));
			System.out.println(sdate + shour + smin + esec);

			int[] type = new int[devicenum];
			int[] towerheight = new int[devicenum];
			int[] fanslength = new int[devicenum];
			int[] fanswidth = new int[devicenum];

			String changes = request.getParameter("towerheight");
			System.out.println(changes);
			String[] ss = changes.split("-");
			towerheigthlow = Integer.parseInt(ss[0]);
			towerheighthigh = Integer.parseInt(ss[1]);
			changes = request.getParameter("bladelength");
            System.out.println(changes);
			ss = changes.split("-");
			fanslengthlow = Integer.parseInt(ss[0]);
			fanslengthhigh = Integer.parseInt(ss[1]);
			changes = request.getParameter("bladewidth");
            System.out.println(changes);
			ss = changes.split("-");
			fanswidthlow = Integer.parseInt(ss[0]);
			fanswidthheight = Integer.parseInt(ss[1]);
			System.out.println(request.getParameter("filesizearray"));
			System.out.println(request.getParameter("filesizepresentarray"));

			String[] filesizearray = request.getParameter("filesizearray").split(",");
			String[] filesizepresentarray = request.getParameter("filesizepresentarray").split(",");

			List<Float> sizes = new ArrayList<Float>();
			List<Float> presents = new ArrayList<Float>();

			for(int i = 0; i < filesizearray.length; i++)
			{
				sizes.add(Float.parseFloat(filesizearray[i]));
				presents.add(Float.parseFloat(filesizepresentarray[i]) / 100);
			}
			System.out.println("sizes:" + sizes);

			float unnormal = Float.parseFloat(request.getParameter("unnormal"));

			System.out.println("unnormal:" + unnormal);
			File f = new File(filepath);
			f.mkdirs();
			boolean[] checkNumArray = new boolean[typenum];
			for(int i = 0; i < devicenum; i++)
			{
				if(i % typenum == 0){
					for(int j = 0; j < typenum; j++)
						checkNumArray[j] = false;
				}
				int randomChoose = new Random().nextInt(typenum);
				while(checkNumArray[randomChoose])
					randomChoose = new Random().nextInt(typenum);
				checkNumArray[randomChoose] = true;
				type[i] = randomChoose;
				towerheight[i] = new Random().nextInt(towerheighthigh - towerheigthlow + 1) + towerheigthlow;
				fanslength[i] = new Random().nextInt(fanslengthhigh - fanslengthlow + 1) + fanslengthlow;
				fanswidth[i] = new Random().nextInt(fanswidthheight - fanswidthlow + 1) + fanswidthlow;
				typeareaname.add(areaname.get(type[i]));
				typebasewindlist.add(basewindlist.get(type[i]));
				typemaxzhenwindlist.add(maxzhenwindlist.get(type[i]));
				typemaxjianwindlist.add(maxjianwindlist.get(type[i]));
				typemaxshunwindlist.add(maxshunwindlist.get(type[i]));
			}
			//String path = Testpath.class.getClass().getResource("/").getPath().substring(1);
			String path = Testpath.class.getClassLoader().getResource("HistorySettings").getPath();
			File ffFile = new File(path);
			//File ffFile = new File("D:/HistorySettings");
			//File ffFile = new File("../webapps/GHBigBench/HistorySettings");
			if(!ffFile.exists())
			{
				ffFile.mkdirs();
			}
			Date ddDate = new Date();
			FileWriter ffWriter = null;
			try {
				ffWriter = new FileWriter(ffFile.getAbsoluteFile() +
                        "/"  + ddDate.getTime() + ".txt");
				ffWriter.write("Simulation Device Num:\t" + devicenum + "\n");
				ffWriter.write("Simulation Start Time:\t" + sdate + "\t" + shour + ":" + smin + ":" + ssec + "\n");
				ffWriter.write("Simulation End Time:\t" + edate + "\t" + ehour + ":" + emin + ":" + esec + "\n");
				ffWriter.write("Each Wind Based Varables:\t" + read.getalltime() + "\t" + read.getuv() + "\t" +
						read.getzhenstart() + "\t" + read.getzhencycle() + "\t" + read.getjianstart() +
						"\t" + read.getjianend() + "\t" + read.getjiankeep() +  "\n");
				for(int i = 0; i < devicenum; i++)
				{
					ffWriter.write("The " + (i+1) + " of Device Varables:\t" + type[i] + "\t" + towerheight[i] + "\t" +
							fanslength[i] + "\t" + fanswidth[i] + "\t" +
							areaname.get(type[i]) + "\t" + basewindlist.get(type[i]) + "\t" +
							maxzhenwindlist.get(type[i]) + "\t" +
							maxjianwindlist.get(type[i]) + "\t" + maxshunwindlist.get(type[i]) + "\n");
					ffWriter.flush();
				}
				ffWriter.write("File Sizes Varables:\t");
				for(int i = 0; i < filesizearray.length; i++)
					ffWriter.write(filesizearray[i] + "\t");
				ffWriter.write("\n");
				ffWriter.write("File Sizes Percents Varables:\t");
				for(int i = 0; i < filesizepresentarray.length; i++)
					ffWriter.write(filesizepresentarray[i] + "\t");
				ffWriter.write("\n");
				ffWriter.write("Time Block:\t" + filenum + "\n");
				ffWriter.write("Unnormal Presents:\t" + unnormal + "\n");
				ffWriter.write("DataGen Start Time:" + (ddDate.getYear() + 1900) + "-" + (ddDate.getMonth() + 1) +
						"-" + ddDate.getDate() + " " + ddDate.getHours() + ":" + ddDate.getMinutes()
						+ ":" + ddDate.getSeconds() + "\n");
				ffWriter.write("Data Storage Path:\t" + filepath + "\n");
				ffWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			datagen = new powprod();
			datagen.SetFilesize(sizes, presents);
			datagen.SetWindparameter(read.getzhenstart(), read.getzhencycle(),
					read.getjianstart(), read.getjianend(), read.getjiankeep());
			datagen.SetTheAreaInfo(typeareaname, typebasewindlist, typemaxzhenwindlist,
					typemaxjianwindlist, typemaxshunwindlist);
			datagen.SetStartTime(sdate, shour, smin, ssec);
			datagen.SetEndTime(edate, ehour, emin, esec);

			datagen.GetBladeFile(filepath, type, towerheight, fanslength, fanswidth,
                        read.getalltime(), uv, filenum, changenum, 0, metatype, unnormal
                            / 100);


        }
		else
		{
			filenum = datagen.GetPresentNum();
			//使用json格式返回数值
			float pretentsize = datagen.GetPresentSize();
			System.out.println(pretentsize);
            try {
                response.getWriter().write("{\"presentnum\":" + filenum + ","
                        + "\"pretentsize\":"+ pretentsize +
                        "}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
