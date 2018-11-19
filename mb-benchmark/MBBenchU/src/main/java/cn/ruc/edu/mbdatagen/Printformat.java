package cn.ruc.edu.mbdatagen;

import java.text.DecimalFormat;

public class Printformat {
	public void jiange()
	{
		for(int i = 0; i < 25; i++)
			System.out.print("*");
		System.out.println();
	}
	public void TimePrint(int seconds)
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
			System.out.println("Time is " + hour + " : " + minus +" : " + seconds);
		else if(minus != 0)
			System.out.println("Time is " +  minus + " : " + seconds);
		else
			System.out.println("Time is " +  seconds);
	}
	
	public String getTime(int seconds)
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
			return "Time is " + hour + " : " + minus +" : " + seconds;
		else if(minus != 0)
			return "Time is " +  minus + " : " + seconds;
		else
			return "Time is " +  seconds;
	}
	
	public String FormetFileSize(long fileS) {//ת���ļ���С
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
	
	public boolean isruinian(int year)
	{
		 if(year%4 == 0 && year%100!=0 || year%400 == 0){//������
             return true;
		 }else{
             return false;
		 }
	}
}
