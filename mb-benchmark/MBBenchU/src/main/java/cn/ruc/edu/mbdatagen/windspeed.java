package cn.ruc.edu.mbdatagen;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class windspeed {
	public float basewind;
	public float maxzhenwind;
	public float maxjianwind;
	public float maxshunwind;
	
	public float zhenstarttime;
	public float zhencycletime;
	public float jianstarttime;
	public float jianendtime;
	public float jiankeeptime;
	
	public windspeed(float ba, float mz, float mj, float ms)
	{
		this.basewind = ba;
		this.maxzhenwind = mz;
		this.maxjianwind = mj;
		this.maxshunwind = ms;
	}
	
	public void SetwindtimeParameter(float zs, float zc, float js, float je, float jk)
	{
		this.zhenstarttime = zs;
		this.zhencycletime = zc;
		this.jianstarttime = js;
		this.jianendtime = je;
		this.jiankeeptime = jk;
	}
	
	public float GetShunWind(){
		return this.maxshunwind;
	}
	
	public void SetShunWind(float sw){
		this.maxshunwind = sw;
	}
	
	public ArrayList<Float> GetWindBySecond(int n) throws IOException
	{
		int t;
		float zhenwind, jianwind, shunwind;
		ArrayList<Float> windlist = new ArrayList<Float>();
		for(t = 1; t <= n; t++)
		{
			if(t < this.zhenstarttime || t > this.zhenstarttime + this.zhencycletime)
				zhenwind = 0;
			else
				zhenwind = (float) (this.maxzhenwind / 2 * (1- Math.sin(2 * Math.PI) *
						((t - this.zhenstarttime) / this.zhencycletime)));
			if( t < this.jianstarttime || t > this.jianendtime + this.jiankeeptime)
				jianwind = 0;
			else if(t < this.jianendtime)
				jianwind = this.maxjianwind * ((t - this.jianstarttime) / 
						(this.jianendtime - this.jianstarttime));
			else
				jianwind = this.maxjianwind;

			Random ra = new Random();
			float ranum = ra.nextFloat();
			float ram = ranum * 2 - 1;
			float onigam = (float) (ram * Math.PI + 0.5);
			if(onigam > Math.PI)
			{
				onigam -= 0.5;
			}
			float faim = ranum * 2;
			
			shunwind = (float) (this.maxshunwind * ram * Math.cos(onigam * t + faim));
			float zong = this.basewind + jianwind + zhenwind + shunwind;
			windlist.add(zong);

		}
		return windlist;
	}	
}
