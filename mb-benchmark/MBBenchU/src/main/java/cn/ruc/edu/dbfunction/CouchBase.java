package cn.ruc.edu.dbfunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONObject;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import cn.ruc.edu.basecore.FileFunction;
import cn.ruc.edu.basecore.QueryFileName;
import cn.ruc.edu.basecore.UnStrDBBase;

public class CouchBase extends UnStrDBBase<Cluster>{

	@Override
	public boolean LinkDb() throws UnknownHostException {
		// TODO Auto-generated method stub
		System.out.println("现在连接的数据库是：CouchBase");
        if(this.ip == null || this.port == -1){
            return false;
        }
        else {
        	 // Initialize the Connection       	 
        	 this.dbBase = CouchbaseCluster.create(this.ip, String.valueOf(this.port));
        	 this.dbBase.authenticate("cmq", "123456");
            Bucket bucket = this.dbBase.openBucket("filedb");            
			this.session.setAttribute("dbport", this.port);
            return true;
        }
		
	}

	@Override
	public boolean DislinkDb() {
		// TODO Auto-generated method stub
		 if(this.dbBase == null)
	            return false;
	        else
	            this.dbBase.disconnect();
	        return true;
	}

	@Override
	public long GetDbFilesCount() {
		// TODO Auto-generated method stub
		 Bucket bucket = this.dbBase.openBucket("filedb");
		 return (long)bucket.bucketManager().info().raw().getObject("basicStats").getInt("itemCount");		
	}

	@Override
	public double GetDataSize() {
		// TODO Auto-generated method stub
		Bucket bucket = this.dbBase.openBucket("filedb");
		return bucket.bucketManager().info().raw().getObject("basicStats").getDouble("dataUsed");
	
	}

	@Override
	public double GetStorageSize() {
		// TODO Auto-generated method stub
		Bucket bucket = this.dbBase.openBucket("filedb");
		return bucket.bucketManager().info().raw().getObject("basicStats").getDouble("diskUsed");
	}

	@Override
	public boolean UpLoadFile(String filepath) {
		// TODO Auto-generated method stub
		this.SetTransLogPath();
		Cluster cluster = CouchbaseCluster.create("localhost", "8091");
	    cluster.authenticate("cmq", "123456"); 
	    Bucket bucket = cluster.openBucket("filedb");
		List<File> allLoadFileList = new ArrayList<File>();
	    File rootFile = new File(filepath);
	    File[] rootFileList = rootFile.listFiles();
	    if(rootFileList.length != 0) {     	
	        for(File files : rootFileList) {
	        if (!files.isDirectory())
	            continue;
	        	File dataFile = new File(files.getAbsolutePath() + "/file" );                
	        	if (dataFile.isDirectory()) {            	             
	        		allLoadFileList.addAll(Arrays.asList(dataFile.listFiles()));
	        	}            
	        }
	    } 
	    String sid = "";      
	    for(int i=0;i<allLoadFileList.size();i++) {
	    	String spath[] = allLoadFileList.toArray()[i].toString().split("\\\\");
	    	sid = sid + "," + spath[spath.length-1];
	    }
	    JsonObject arthur =JsonObject.create();
	    int count = 1;             
	    long length = 0;
	    long starttime = new Date().getTime();
	    if(allLoadFileList.size() == 0)
	        return false;
	    else{
	        for(File file : allLoadFileList){
	        	InputStream in = null;
	        	String sfile = "";
				try {
					in = new FileInputStream(file);
					byte b[]=new byte[(int)file.length()];     //创建合适文件大小的数组   		             
					in.read(b);					 
		            in.close();
		            sfile = new sun.misc.BASE64Encoder().encodeBuffer(b);//编码
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	        	
	            //System.out.println(all);
	            long ofilestime = System.nanoTime();
	            length += file.length();	            
	            long time=System.currentTimeMillis();//当前毫秒数
				arthur.put("FILENAME", sid.split(",")[count] )          			
					  .put("CURRENTTIME", time)				
					  .put("content",sfile);
				bucket.upsert(JsonDocument.create("file"+String.valueOf(count),arthur));//id            
				long ofileetime = System.nanoTime();
				count++;
	            this.filesloadcount++;
	            this.tempfilesloadspread += file.length();
	            double speed = ((double)file.length()/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
	            fileFunction.WriteLogforTrans(speed + "" , QueryFileName.LOAD_FILE_PER);            
					           
	        }                    
	    }	
	    long endtime = new Date().getTime();
        int seconds = (int)((endtime - starttime) / 1000);
        FileFunction.TimePrint(seconds);
        System.out.println("The average of translation is " + ((double)length/(1024*1024))/((float)(endtime - starttime)/1000) + "Mb/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                ((double)length/(1024*1024))/(float)seconds + "Mb/s", QueryFileName.DBINFO);
        return true;  
	}

	@Override
	public boolean UpLoadMetafile(String metapath) {
		// TODO Auto-generated method stub
		this.SetTransLogPath();
		List<File> allLoadMetaList = FileFunction.GetUpLoadMetaFileList(metapath, "json");
        int count = 0;           
        Cluster cluster = CouchbaseCluster.create("localhost", "8091");
        cluster.authenticate("cmq", "123456"); 
        Bucket bucket = cluster.openBucket("filemeta");
        long starttime = new Date().getTime();
        for(File file : allLoadMetaList){
        	try {
            	BufferedReader fjson = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String all = "";
                String s = null;
                while((s = fjson.readLine()) != null) {
                	all += s;	
                }  
                System.out.println(all);
                //id不能重复，否则导入的数据会覆盖
                JSONObject myjObject = new JSONObject(all); 
                JsonObject arthur =JsonObject.create();
                Set<String>  sname = myjObject.keySet();
                for(String key:sname) {
             	   if(key.equals("WINDSPEED")||key.equals("BLADESPEED")||key.equals("ELECTRONICPOWER")||key.equals("CHANGERATE")
             			   ||key.equals("TRANSRATE")||key.equals("ELECTRICITY")||key.equals("TRANSELECRATE")) {
             		   arthur.put(key,JsonArray.fromJson(myjObject.get(key).toString()) );  
             	   }else {
             		   arthur.put(key,myjObject.get(key) );  
             	   }   	  
                }     
                bucket.upsert(JsonDocument.create("meta"+String.valueOf(count+1),arthur));//id唯一  			
    			count++;			
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} 
        	 this.metasloadcount++;
             this.tempfilemetaloadcount ++;
        }       
        long endtime = new Date().getTime();
        int seconds = (int)((endtime - starttime) / 1000);
        FileFunction.TimePrint(seconds);
        System.out.println("The average of document translation is " + (this.metasloadcount)/((float)(endtime - starttime)/1000) + "per/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                (this.metasloadcount)/(float)seconds + "per/s", QueryFileName.DBINFO);
        return true;
	}

	@Override
	public boolean DownLoadFile(String downpath) {
		// TODO Auto-generated method stub
		this.SetTransLogPath();
		Cluster cluster = CouchbaseCluster.create("localhost", "8091");
	    cluster.authenticate("cmq", "123456"); 
	    Bucket bucket = cluster.openBucket("filedb");
        File logdir = new File(downpath + "/files" );
        if( !logdir.exists())
        {
            logdir.mkdirs();
        }            
        String filename = null;
        long filesize = 0;     
        long allsize = 0;
        long filestime = new Date().getTime();     
        OutputStream out = null ;        
        for(int i=0;i<bucket.bucketManager().info().raw().getObject("basicStats").getInt("itemCount");i++) {
        	JsonDocument jsond = bucket.get("file"+String.valueOf(i+1));
        	String filecontent = jsond.content().getString("content");        	       	
        	filename = jsond.content().getString("FILENAME").substring(0, jsond.content().getString("FILENAME").indexOf("."));//得到每条数据的FILENAME属性的值
        	String time = jsond.content().get("CURRENTTIME").toString();      	
			try {
				out = new FileOutputStream(downpath + "/files/" + filename + "_" + time + ".mbruc");	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							 
			byte[] bt = null;
        	long ofilestime = System.nanoTime();
        	try {
        		bt = new sun.misc.BASE64Decoder().decodeBuffer(filecontent);        		
				out.write(bt);
				out.flush();
	        	out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        	 
        	long ofileetime = System.nanoTime();
        	filesize = bt.length;        	
            allsize += filesize;
        	this.filesdowncount++;
            this.tempfilesdownspread += filesize;
            double speed = ((double)filesize/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
            fileFunction.WriteLogforTrans(speed + "" , QueryFileName.DOWN_FILE_PER);
            System.out.println("The translation of the " + filesdowncount + ",  the speed is" + speed + "Mb/s\n");  
        }    
        long fileetime = new Date().getTime();
        int seconds = (int)((fileetime - filestime) / 1000);
        FileFunction.TimePrint(seconds);
        System.out.println("The average of translation speed is " + ((double)allsize/(1024*1024))/((float)(fileetime - filestime)/1000) + "Mb/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                ((double)allsize/(1024*1024))/(float)seconds + "MB/s", QueryFileName.DBINFO);      
        return true;
	}

	@Override
	public boolean DownLoadMetaFile(String downpath) {
		// TODO Auto-generated method stub
		this.SetTransLogPath();
		Cluster cluster = CouchbaseCluster.create("localhost", "8091");
	    cluster.authenticate("cmq", "123456"); 
	    Bucket bucket = cluster.openBucket("filemeta");
        File logdir = new File(downpath + "/metadata");
        if( !logdir.exists())
        {
            logdir.mkdirs();
        }                       
        BufferedWriter out = null ; 
        long filestime = new Date().getTime();  
        for(int i=0;i<bucket.bucketManager().info().raw().getObject("basicStats").getInt("itemCount");i++) {
        	JsonDocument jsond = bucket.get("meta"+String.valueOf(i+1));
        	String metacontent = jsond.content().toString();      	        	
        	String sname = jsond.content().getString("FILENAME");//得到每条数据的FILENAME属性的值
        	long time=System.currentTimeMillis();//当前毫秒数     	
			try {
				out = new BufferedWriter(new FileWriter(downpath + "/metadata/" + sname.split("\\.")[0]							
						+ "_" + String.valueOf(time)
				        + ".json"));
			out.write(metacontent);
			out.write("\r\n");
            out.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			this.metasdowncount++;
            this.tempfilemetadowncount ++;       	            
        }     
        long fileetime = new Date().getTime();
        int seconds = (int)((fileetime - filestime) / 1000);
        FileFunction.TimePrint(seconds);
        System.out.println("The average of document translation speed is " +
                (this.metasdowncount)/((float)(fileetime - filestime)/1000) + "per/s\n");
        fileFunction.WriteLogforTrans(seconds + "," +
                (this.metasdowncount)/(float)seconds + "per/s", QueryFileName.DBINFO);
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        long filesnum = this.GetDbFilesCount();
        double datasize = (double)(this.GetDataSize() / (1024 * 1024));   
        double storagesize = (double)(this.GetStorageSize() / (1024 * 1024));
        double rates = ( datasize / storagesize ) * 100;
        storagesize = storagesize / 1024;
        fileFunction.WriteLogforTrans(decimalFormat.format(rates), QueryFileName.DBINFO);
        System.out.println(decimalFormat.format(rates));
        fileFunction.WriteLogforTrans(filesnum + "", QueryFileName.DBINFO);
        System.out.println(filesnum + "");
        fileFunction.WriteLogforTrans(this.GetDBName() + "", QueryFileName.DBINFO);
        System.out.println(this.GetDBName() + "");
        fileFunction.WriteLogforTrans(decimalFormat.format(storagesize), QueryFileName.DBINFO);
        System.out.println(decimalFormat.format(storagesize));       
        return true;
	}

	@Override
	public void DownloadFileByList(List<String> downList, String downfilename, String downfilepath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void VersionQuery(String datapath) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AbnormalQuery(String datapath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HotFileQuery(int queryCount, int interval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> QueryOne() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> QueryTwo() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> QueryThree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> QueryFour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> QueryFive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> QuerySix() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> QuerySeven() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void QueryNine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void QueryTen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void QueryEleven() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void QueryTwelve() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void QueryThirteen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void QueryFourteen() {
		// TODO Auto-generated method stub
		
	}

}
