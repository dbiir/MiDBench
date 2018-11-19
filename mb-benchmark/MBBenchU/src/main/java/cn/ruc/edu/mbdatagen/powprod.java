package cn.ruc.edu.mbdatagen;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.ListModel;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class powprod {
	/*
	 * 说明：本数据通过读取文件目录下的Settings里的peizhi.xml文件
	 * 每个数据依照位置不同，分别表示为东北-华北、华东-华中、东南、西南、西北和青藏高原
	 * 数据来源来自中国1961——2012年风速变化一级区划特征值
	 * 阵风、缓慢变化风和噪声风的最大风速是每个地区的30%、20%和100%，这个是可以通过修改peizhi.xml文件修改
	 * 阵风、缓慢变化风和噪声风的运行时间和周期依照设定的时间走，其中阵风时间处于时间的20%——60%；
	 * 缓慢变化风处于时间的10%到50%，并持续到80%；噪声风一直保持
	 */
	public ArrayList<String> areaname = new ArrayList<String>();
	public ArrayList<Float> basewindlist = new ArrayList<Float>();
	public ArrayList<Float> maxzhenwindlist = new ArrayList<Float>();
	public ArrayList<Float> maxjianwindlist = new ArrayList<Float>();
	public ArrayList<Float> maxshunwindlist = new ArrayList<Float>();

	public FansDevice[] myThread;

	public List<Float> filesizearray;
	public List<Float> filesizepresentarray;
	public float maxsize;

	public int filenum;
	public int splittime;
	public int changenum;
	public int filepluspar;

	//设置仿真起始时间
	public String sdate = null;
	public int hourstart = 0;
	public int minstart = 0;
	public int secondstart = 0;

	//设置仿真终止时间
	public String edate = null;
	public int hourend = 0;
	public int minend = 0;
	public int secondend = 0;

	public float zhenstarttime;
	public float zhencycletime;
	public float jianstarttime;
	public float jianendtime;
	public float jiankeeptime;

	public void SetWindparameter(float zs, float zc, float js, float je, float jk)
	{
		this.zhenstarttime = zs;
		this.zhencycletime = zc;
		this.jianstarttime = js;
		this.jianendtime = je;
		this.jiankeeptime = jk;
	}

	public windspeed GetWindSpeed(float bw, float jw, float zw, float sw,
								  float zs, float zc, float js, float je, float jk)
	{
		windspeed ws = new windspeed(bw, zw, jw, sw);
		ws.SetwindtimeParameter(zs, zc, js, je, jk);

		return ws;
	}

	public void SetFilesize(List<Float> size, List<Float> present)
	{
		this.filesizearray = size;
		this.filesizepresentarray = present;
		float max = size.get(0);
		for(float s : size){
			if(max < s)
				max = s;
		}
		this.maxsize = max;
	}

	public void SetStartTime(String sd, int h, int m, int s)
	{
		this.sdate = sd;
		this.hourstart = h;
		this.minstart = m;
		this.secondstart = s;
	}

	public void SetEndTime(String ed, int h, int m, int s)
	{
		this.edate =ed;
		this.hourend = h;
		this.minend = m;
		this.secondend = s;
	}

	public void SetTheAreaInfo(ArrayList<String> s, ArrayList<Float> bw, ArrayList<Float> mzw,
							   ArrayList<Float> mjw, ArrayList<Float> msw)
	{
		this.areaname = s;
		this.basewindlist = bw;
		this.maxzhenwindlist = mzw;
		this.maxjianwindlist = mjw;
		this.maxshunwindlist = msw;
	}

	public int GetPresentNum()
	{
		int allcount = 0;
		for(int i = 0; i < this.myThread.length; i++)
		{
			if(this.myThread[i] == null)
				continue;
			else
				allcount += this.myThread[i].pretentfilecount;
		}
		return allcount;
	}

	public float GetPresentSize()
	{
		float size = 0;
		for(int i = 0; i < this.myThread.length; i++)
		{
			if(this.myThread[i] == null)
				continue;
			else {
				size += this.myThread[i].GetPresentSize();
			}
		}
		return size;
	}

	public void GetBladeFile(String filepath, int[] type, int[] towerheight,
							 int[] fanslength, int[] fanswidth, int divnum, float uv,
							 int filenum, int changenum, int filepluspar, String metatype,
							 float unnormal)
	{
		Sigar sigar = new Sigar();
		this.filenum = filenum;
		this.changenum = changenum;
		this.filepluspar = filepluspar;
		int devicenum = type.length;
		int copynum = 1;
		if(filepluspar!= 0){
			File[] dirFile = new File(filepath).listFiles();
			for(File s : dirFile){
				if(s.isDirectory())
					copynum++;
			}
			copynum--;
		}
		System.out.println("The Current CopyNum is :" + copynum);
		CpuInfo[] infos = null;
		boolean usedevice = true;
		try {
			infos = sigar.getCpuInfoList();
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int threadcount = infos.length / 2;
		int[] deviceset = new int[threadcount];
		if(devicenum <= threadcount){
			myThread = new FansDevice[devicenum];
			for(int i = 0; i < devicenum; i++)
				deviceset[i] = 1;
		}
		else{
			usedevice = false;
			int eachdevicenum = devicenum / threadcount;
			int releasedevicenum = devicenum - eachdevicenum * threadcount;
			for(int i = 0, p = 0; i < threadcount; i++){
				if((++p) <= releasedevicenum)
					deviceset[i] = eachdevicenum + 1;
				else
					deviceset[i] = eachdevicenum;
			}
			myThread = new FansDevice[threadcount];
		}
		String[] ss = metatype.split(",");
		int[] typeOfMeta = new int[3];
		for(String as : ss){
			if ("txt".equals(as)) {
				typeOfMeta[0] = 1;

			} else if ("json".equals(as)) {
				typeOfMeta[1] = 1;

			} else if ("xml".equals(as)) {
				typeOfMeta[2] = 1;

			} else {
			}
		}
		if(usedevice){
			for(int i = 0; i < devicenum; i++){
				windspeed wp = this.GetWindSpeed(this.basewindlist.get(i),
						this.maxzhenwindlist.get(i),
						this.maxjianwindlist.get(i),
						this.maxshunwindlist.get(i),
						this.zhenstarttime,
						this.zhencycletime,
						this.jianstarttime,
						this.jianendtime,
						this.jiankeeptime);
				this.myThread[i] = new FansDevice();
				this.myThread[i].SetDeviceParamter(areaname.get(i), towerheight[i],
						fanslength[i], fanswidth[i]);
				this.myThread[i].SetStartTime(this.sdate, this.hourstart, this.minstart, this.secondstart);
				this.myThread[i].SetEndTime(this.edate, this.hourend, this.minend, this.secondend);
				this.myThread[i].FileSetting((i + 1),
						filepath, filenum, divnum,
						changenum, filepluspar);
				this.myThread[i].SetWindParamter(wp, uv);
				this.myThread[i].SetMetaType(typeOfMeta);
				this.myThread[i].SetFilesizes(this.filesizearray, this.filesizepresentarray, this.maxsize);
				this.myThread[i].SetCopyNum(copynum);
				this.myThread[i].SetUnnormalPresent(unnormal);
				this.myThread[i].start();
			}
		}
		else{
			for(int i = 0; i < threadcount; i++){
				List<String> nameList = new ArrayList<String>();
				List<Integer> towerheighList = new ArrayList<Integer>();
				List<Integer> fanslengthList = new ArrayList<Integer>();
				List<Integer> fanswidthList = new ArrayList<Integer>();

				List<windspeed> wps = new ArrayList<windspeed>();
				windspeed wpspeed = null;
				for(int j = 0; j < deviceset[i]; j++){
					if(deviceset[i] > 1){
						int marknum = i + j * threadcount;
						windspeed wp = this.GetWindSpeed(this.basewindlist.get(marknum),
								this.maxzhenwindlist.get(marknum),
								this.maxjianwindlist.get(marknum),
								this.maxshunwindlist.get(marknum),
								this.zhenstarttime,
								this.zhencycletime,
								this.jianstarttime,
								this.jianendtime,
								this.jiankeeptime);
						wps.add(wp);
						nameList.add(areaname.get(marknum));
						towerheighList.add(towerheight[marknum]);
						fanslengthList.add(fanslength[marknum]);
						fanswidthList.add(fanswidth[marknum]);
					}

					else {
						wpspeed = this.GetWindSpeed(this.basewindlist.get(i),
								this.maxzhenwindlist.get(i),
								this.maxjianwindlist.get(i),
								this.maxshunwindlist.get(i),
								this.zhenstarttime,
								this.zhencycletime,
								this.jianstarttime,
								this.jianendtime,
								this.jiankeeptime);
					}
				}
				if(wpspeed == null){
					this.myThread[i] = new FansDevice();
					this.myThread[i].SetDeviceParamters(nameList, towerheighList,
							fanslengthList, fanswidthList);
					this.myThread[i].SetStartTime(this.sdate, this.hourstart, this.minstart, this.secondstart);
					this.myThread[i].SetEndTime(this.edate, this.hourend, this.minend, this.secondend);
					this.myThread[i].FileSetting((i + 1),
							filepath, filenum, divnum,
							changenum, filepluspar);
					this.myThread[i].SetSplitDevice(deviceset[i], threadcount);
					this.myThread[i].SetWindParamters(wps, uv);
					this.myThread[i].SetMetaType(typeOfMeta);
					this.myThread[i].SetFilesizes(this.filesizearray, this.filesizepresentarray, this.maxsize);
					this.myThread[i].SetCopyNum(copynum);
					this.myThread[i].SetUnnormalPresent(unnormal);
					this.myThread[i].start();
				}

				else{
					this.myThread[i] = new FansDevice();
					this.myThread[i].SetDeviceParamter(areaname.get(i), towerheight[i],
							fanslength[i], fanswidth[i]);
					this.myThread[i].SetStartTime(this.sdate, this.hourstart, this.minstart, this.secondstart);
					this.myThread[i].SetEndTime(this.edate, this.hourend, this.minend, this.secondend);
					this.myThread[i].FileSetting((i + 1),
							filepath, filenum, divnum,
							changenum, filepluspar);
					this.myThread[i].SetWindParamter(wpspeed, uv);
					this.myThread[i].SetMetaType(typeOfMeta);
					this.myThread[i].SetFilesizes(this.filesizearray, this.filesizepresentarray, this.maxsize);
					this.myThread[i].SetCopyNum(copynum);
					this.myThread[i].SetUnnormalPresent(unnormal);
					this.myThread[i].start();
				}
			}
		}

		long starttime = new Date().getTime();
		while(true)
		{
			boolean isAllFinish = true;
			for (Thread thread : this.myThread)
			{
				if(thread.isAlive())
				{
					isAllFinish=false;
				}
			}
			if(isAllFinish)
			{
				System.out.println("<== All Finshed ==>");
				new Printformat().jiange();
				break;
			}
		}
		long endtime = new Date().getTime();
		new Printformat().TimePrint((int)((endtime - starttime) / 1000));
	}
}