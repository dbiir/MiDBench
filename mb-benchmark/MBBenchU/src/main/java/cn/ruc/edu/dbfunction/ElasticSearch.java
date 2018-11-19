package cn.ruc.edu.dbfunction;

import cn.ruc.edu.basecore.DevicePartNameEnum;
import cn.ruc.edu.basecore.FileFunction;
import cn.ruc.edu.basecore.QueryFileName;
import cn.ruc.edu.basecore.SortedTuple;
import cn.ruc.edu.basecore.UnStrDBBase;
import cn.ruc.edu.mbdatagen.NewDateTime;

import org.elasticsearch.action.admin.indices.stats.CommonStats;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ElasticSearch extends UnStrDBBase<TransportClient>{	
	public static long fileLength=0;
	
	public static void fileList(File file) {
		File[] files = file.listFiles();		
		if(files!=null) {
			for(File f:files) {
				fileLength = fileLength+f.length();
				//System.out.println(f.length());
				fileList(f);
			}
		}		
	}
	  @Override
	  public boolean LinkDb() {
	        System.out.println("现在连接的数据库是：ElasticSearch");
	        if(this.ip == null || this.port == -1){
	            return false;
	        }
	        else {
	        	Settings settings = Settings.builder().put("cluster.name","elasticsearch").put("client.transport.sniff", true).build();				
				try {
					this.dbBase = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(this.ip),this.port));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			//	SearchRequestBuilder srb=client.prepareSearch("filedb").setTypes("filemata");	
				this.session.setAttribute("dbport", this.port);
	            return true;
	        }
	    }

	    @Override
	    public boolean DislinkDb() {
	        if(this.dbBase == null)
	            return false;
	        else
	            this.dbBase.close();
	        return true;
	    }

    @Override
    public long GetDbFilesCount() {
    	IndicesStatsResponse resp = this.dbBase.admin().indices().prepareStats().execute().actionGet();
    	IndexStats commonStats = resp.getIndex(this.dbName);
       // System.out.println(commonStats.docs.getCount());
        return commonStats.getTotal().docs.getCount();
    }

    @Override
    public double GetDataSize() {
    	IndicesStatsResponse resp = this.dbBase.admin().indices().prepareStats().execute().actionGet();
        CommonStats commonStats = resp.getPrimaries();
       // System.out.println((double)commonStats.docs.getTotalSizeInBytes()/1024/1024 + "GB");
        return (double)commonStats.docs.getTotalSizeInBytes();
    	/*File f= new File("D:\\greatdata");    	
    	fileList(f);
    	//System.out.println((double)fileLength/1024/1024/1024 + "GB");
    	return (double)fileLength/1024/1024/1024;*/
    }

    @Override
    public double GetStorageSize() {
    	IndicesStatsResponse resp = this.dbBase.admin().indices().prepareStats().execute().actionGet();
        CommonStats commonStats = resp.getPrimaries();
       // System.out.println((double)commonStats.store.getSizeInBytes()/1024/1024 + "GB");
        return (double)commonStats.store.getSizeInBytes();    
    }

    @Override
    public boolean UpLoadFile(String filepath) {
    	this.SetTransLogPath();
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
        IndexRequestBuilder builder=(this.dbBase).prepareIndex(this.dbName, this.dbName);
        int count = 1;             
        long length = 0;
        long starttime = new Date().getTime();
        if(allLoadFileList.size() == 0)
            return false;
        else{
            for(File file : allLoadFileList)
            {
                long ofilestime = System.nanoTime();
                length += file.length();
                try {
                	long time=System.currentTimeMillis();//当前毫秒数
	            	XContentBuilder xbuilder = XContentFactory.jsonBuilder()
	            			.startObject()
	            			.field("FILENAME", sid.split(",")[count])
	            			.field("CURRENTTIME", time)
						    .field("content", fileFunction.readFile(file.getAbsolutePath()))//上传的是文件转为Byte[]后的Byte数组
						    .endObject();	            	
	            	IndexResponse response = builder.setId(Integer.toString(count)).setSource(xbuilder).execute().actionGet();            	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                count++;
                long ofileetime = System.nanoTime();
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
    	this.SetTransLogPath();
        
        List<File> allLoadMetaList = FileFunction.GetUpLoadMetaFileList(metapath, "json");
        int count = 0;      
        IndexRequestBuilder builder=(this.dbBase).prepareIndex(this.collectionName, this.collectionName);
        long starttime = new Date().getTime();
        for(File file : allLoadMetaList){
        	try {
            	BufferedReader fjson = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String all = "";
                String s = null;
                while((s = fjson.readLine()) != null) {
                	all += s;	
                }         
    			//导入的数据必须为  json字符串的形式！！！（不是json对象）
                //id不能重复，否则导入的数据会覆盖
                IndexResponse response = builder.setId(Integer.toString(count)).setSource(all,XContentType.JSON).execute().actionGet();	//创建索引库（小写），类型,id   			
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
    	this.SetTransLogPath();
        File logdir = new File(downpath + "/files" );
        if( !logdir.exists())
        {
            logdir.mkdirs();
        }
        int timeMillis = 6000;   
        SearchResponse scrollResp = this.dbBase.prepareSearch(this.dbName).setTypes(this.dbName)     		     		
                .setScroll(new TimeValue(timeMillis))//setScroll设置回滚时间，类似游标
                .setQuery(QueryBuilders.matchAllQuery()).setSize(10)//setSize(10)表示一次返回10条数据（es最多允许一次返回10000数据）               
                .execute().actionGet();     
        String filename = null;
        long filesize = 0;     
        long allsize = 0;
        long filestime = new Date().getTime();     
        FileOutputStream out = null ;     
        while (true) {     
        SearchHit[] hits = scrollResp.getHits().getHits();            
        for (int i=0;i<hits.length;i++) {
        	String binary = hits[i].getSourceAsMap().get("content").toString(); 
        	filesize = Base64.decode(binary).length;
            allsize += filesize;        	
        	filename = hits[i].getSourceAsMap().get("FILENAME").toString().substring(0, hits[i].getSourceAsMap().get("FILENAME").toString().indexOf("."));//得到每条数据的FILENAME属性的值
        	String time = hits[i].getSourceAsMap().get("CURRENTTIME").toString();       	
            //以“文件名+时间戳”为名防止覆盖     	      	
			try {
				out = new FileOutputStream(downpath + "/files/" + filename + "_" + time
				        + ".mbruc");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        	long ofilestime = System.nanoTime();
        	try {
				out.write(Base64.decode(binary));
				out.flush();
	        	out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//注意需要进行解码        	 
        	long ofileetime = System.nanoTime();
        	this.filesdowncount++;
            this.tempfilesdownspread += filesize;
            double speed = ((double)filesize/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
        fileFunction.WriteLogforTrans(speed + "" , QueryFileName.DOWN_FILE_PER);
            System.out.println("The translation of the " + filesdowncount + ",  the speed is" + speed + "Mb/s\n");       	       	    	
        }   
        scrollResp = this.dbBase.prepareSearchScroll(scrollResp.getScrollId())
                .setScroll(new TimeValue(timeMillis)).execute().actionGet();
        if (scrollResp.getHits().getHits().length == 0) {
            break;
        }
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
    	this.SetTransLogPath();
        File logdir = new File(downpath + "/metadata");
        if( !logdir.exists())
        {
            logdir.mkdirs();
        }        
        int timeMillis = 6000;	 
        SearchResponse scrollResp = this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName)
                .setScroll(new TimeValue(timeMillis))
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(10).execute().actionGet();                            
        long filestime = new Date().getTime();      
        while (true) {     
            SearchHit[] hits = scrollResp.getHits().getHits();               
            for (int i=0;i<hits.length;i++) {
            	String json = hits[i].getSourceAsString();       
                String sname = hits[i].getSourceAsMap().get("FILENAME").toString();//得到每条数据的FILENAME的值                          
                //以“文件名+版本号”为名防止覆盖  
                long time=System.currentTimeMillis();//当前毫秒数
				try {
				BufferedWriter out = new BufferedWriter(new FileWriter(downpath + "/metadata/" + sname.split("\\.")[0]							
							+ "_" + String.valueOf(time)
					        + ".json"));
				out.write(json);
				out.write("\r\n");
                out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}                      
                this.metasdowncount++;
                this.tempfilemetadowncount ++;
            }             
            scrollResp = this.dbBase.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
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
       /* File f= new File(downpath);    	
    	fileList(f);
    	double datasize = (double)fileLength/1024/1024;*/
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
		File logdir = new File(downfilepath + "/" + downfilename + "/");
        if(!logdir.exists()){
            logdir.mkdirs();
        }            
        int filesdowncount = 0;
        long filesize;
        long allsize = 0;
        long filestime = new Date().getTime();
        FileOutputStream out = null ;
        int count = 0;
        double minspeed = 1E6;double maxspeed = 0;      
        Iterator<String> downFileIterator = downList.iterator();

        while (downFileIterator.hasNext()){
            String content = downFileIterator.next();
            String filename = content.split(":")[0];
            int copynum = Integer.parseInt(content.split(":")[1]);        	       	
        	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.dbName).setTypes(this.dbName);		   	
    		TermQueryBuilder tb = QueryBuilders
    	            .termQuery("FILENAME.keyword",filename.toLowerCase());                           
    		SearchResponse sr=srb  			
    				.setQuery(tb)   				
    				.addSort("CURRENTTIME", SortOrder.ASC) 			
    				.setSize(100)
    				.execute()
    				.actionGet();   		  		
    		SearchHit[] hits=sr.getHits().getHits();  	
    		String currenttime = null;
    		String binary = null;    	  	
    			currenttime = hits[copynum - 1].getSourceAsMap().get("CURRENTTIME").toString();
    			binary = hits[copynum - 1].getSourceAsMap().get("content").toString();				
    			filesize = Base64.decode(binary).length;
                allsize += filesize;
                long ofilestime = System.nanoTime();
                try {
    				out = new FileOutputStream(downfilepath + "/" + downfilename + "/" + filename + "_"
                           + currenttime +".mbruc");
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}             	
            	try {
    				out.write(Base64.decode(binary));
    				out.flush();
    	        	out.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}//注意需要进行解码
            	long ofileetime = System.nanoTime();
            	filesdowncount++; 
            	/*File ss = new File(downfilepath + "/" + downfilename + "/" + filename + "_"
                        + currenttime +".mbruc");
                ss.delete();*/
                double speed = ((double)filesize/(1024*1024))/((float)(ofileetime - ofilestime)/1E9);
                if(speed<minspeed) {
                    minspeed = speed;
                }
                if(speed>maxspeed) {
                    maxspeed = speed;
                }
                fileFunction.WriteLogforQuery(String.valueOf(speed), downfilename);
               // System.out.println("File name is " + filename + ". The current down file count: " + filesdowncount + "; " +
              //          "The curent down file speed: " + speed + "Mb/s\n");		      	
           count++;
        }
        long fileetime = new Date().getTime();
        System.out.println("All down files count: " + count);
        System.out.println("The max speed is: "+maxspeed + "Mb/s");
        System.out.println("The min speed is: "+minspeed + "Mb/s");
        System.out.println("The average speed is" + ((double)allsize/(1024*1024))/((float)(fileetime - filestime)/1000) + "Mb/s");	
	}

    @Override
    public void VersionQuery(String datapath) throws IOException {
    	this.SetQueryLogPath();
        this.finish = false;
        fileFunction.WriteLogforQuery("ability;false", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("rate;0", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("count;0", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("time;0", QueryFileName.QUERY_VERISON);
        fileFunction.WriteLogforQuery("speed;0", QueryFileName.QUERY_VERISON);
    	System.out.println("Elasticsearch can not do VersionQuery!");
    	this.finish = true;
    }

    @Override
    public void AbnormalQuery(String datapath) {
    	this.SetQueryLogPath();
        this.finish = false;
        fileFunction.WriteLogforQuery("ability;false", QueryFileName.QUERY_ABNORMAL);
        /*fileFunction.WriteLogforQuery("rate;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("count;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("time;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("speed;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("findcount;0", QueryFileName.QUERY_ABNORMAL);
        fileFunction.WriteLogforQuery("truecount;0", QueryFileName.QUERY_ABNORMAL);*/
    	System.out.println("Elasticsearch can not do AbnormalQuery!");
    	this.finish = true;

    }

    @Override
    public void HotFileQuery(int queryCount, int interval) {
    	this.SetQueryLogPath();
        this.finish = false;
        int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	Map<String, String> queryMap = getHotQueryContent();
    	String hotQueryAttributeName = (String) queryMap.keySet().toArray()[0];
    	System.out.println("拿到的热点查询的查询值："+hotQueryAttributeName);
    	QueryBuilder qb;
    	if(hotQueryAttributeName.equals("TOWERHEIGHT")){
            String[] queryStringContentArray = queryMap.get(hotQueryAttributeName).split("-");
            int minValue = Integer.parseInt(queryStringContentArray[0]);
            int maxValue = Integer.parseInt(queryStringContentArray[1]);
            qb = QueryBuilders.rangeQuery("TOWERHEIGHT")
    				.gte(minValue)
    				.lte(maxValue);
            /*BasicDBObject min = new BasicDBObject()
                    .append("TOWERHEIGHT",new BasicDBObject("$gte", minValue));
            BasicDBObject max = new BasicDBObject()
                    .append("TOWERHEIGHT",new BasicDBObject("$lte", maxValue));
            //BasicDBObject towerFinalObj = new BasicDBObject("$and",Arrays.asList(min, max));
            query = new BasicDBObject("$and",Arrays.asList(min, max));*/
        } else if(hotQueryAttributeName.equals("BLADELENGTH")){
            String[] queryStringContentArray = queryMap.get(hotQueryAttributeName).split("-");
            int minValue = Integer.parseInt(queryStringContentArray[0]);
            int maxValue = Integer.parseInt(queryStringContentArray[1]);
            qb = QueryBuilders.rangeQuery("BLADELENGTH")
    				.gte(minValue)
    				.lte(maxValue);
            /*BasicDBObject min = new BasicDBObject()
                    .append("BLADELENGTH",new BasicDBObject("$gte", minValue));
            BasicDBObject max = new BasicDBObject()
                    .append("BLADELENGTH",new BasicDBObject("$lte", maxValue));
            //BasicDBObject bladeLengthFinalObj = new BasicDBObject("$and",Arrays.asList(min, max));
            query = new BasicDBObject("$and",Arrays.asList(min, max));*/
        } else if(hotQueryAttributeName.equals("BLADEWIDTH")){
            String[] queryStringContentArray = queryMap.get(hotQueryAttributeName).split("-");
            int minValue = Integer.parseInt(queryStringContentArray[0]);
            int maxValue = Integer.parseInt(queryStringContentArray[1]);
            qb = QueryBuilders.rangeQuery("BLADEWIDTH")
    				.gte(minValue)
    				.lte(maxValue);
            /*BasicDBObject min = new BasicDBObject()
                    .append("BLADEWIDTH",new BasicDBObject("$gte", minValue));
            BasicDBObject max = new BasicDBObject()
                    .append("BLADEWIDTH",new BasicDBObject("$lte", maxValue));
            //BasicDBObject bladeWidthFinalObj = new BasicDBObject("$and",Arrays.asList(min, max));
            query = new BasicDBObject("$and",Arrays.asList(min, max));*/
        }
        else {      	
        	qb = QueryBuilders
    				.termQuery(hotQueryAttributeName,queryMap.get(hotQueryAttributeName).toLowerCase());
        }
           // query.put(hotQueryAttributeName, queryMap.get(hotQueryAttributeName));
		for(int i = 0; i < queryCount; i++) {           
			 long start = System.nanoTime();				 
			SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
					.setQuery(qb)				
					.setSize(100).get();					
			while (true) {   		        	
	            sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
	                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
	            if (sr.getHits().getHits().length == 0) {
	                break;
	            }		            
			}
			long end = System.nanoTime();
			fileFunction.WriteLogforQuery(String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_HOTFILE);//用时          
			System.out.println((float)((end - start) / 1E6));
			try {
                Thread.sleep (interval) ;
            } catch (InterruptedException ie){
            }
        }
		this.finish = true;
    }

    @Override
    public List<String> QueryOne() {
    	//Settings settings = Settings.builder().put("cluster.name","estest").put("client.transport.sniff", true).build();				
    	//TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"),9300));	  
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	String content = getDeviceTypeQueryContent();
    	System.out.println("query-one查询属性DEVICETYPE取的值是："+content);
		long start = System.nanoTime();
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.termQuery("DEVICETYPE", content.toLowerCase()))//"BLADE"需转换为小写形式				
				.setSize(100).get();						
		while (true) {			
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}		
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();			
		Map<String, Object> queryMap = new HashMap<>();  	
        queryMap.put("DEVICETYPE", content);       
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.termQuery("DEVICETYPE", content.toLowerCase()))//"BLADE"需转换为小写形式				
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 			
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }     		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}		
		int rightCount = this.CheckQuery(hits, queryMap);		
		fileFunction.WriteLogforQuery("querycontent;" + "DEVICETYPE:" + content, QueryFileName.QUERY_ONE);
		fileFunction.WriteLogforQuery("count;" + String.valueOf(count), QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("time;" + String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("speed;" + String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_ONE);
        fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_ONE);		
		System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-One is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The rate is " + FileFunction.floatToString((float) rightCount / count * 100) + "%");
        System.out.println("query-one查询属性DEVICETYPE取的值是："+content);
        return fileList;
    }
    
    @Override
    public List<String> QueryTwo() throws IOException {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	System.out.println("query-two开始了1");
    	ArrayList<String> contentQueryList = getAreaNameQueryContentList();
    	System.out.println("query-two开始了2");
    	ArrayList<String> contentQueryList1 = new ArrayList<String>();
    	System.out.println("query-two开始了3");
		for(int i=0;i<contentQueryList.size();i++) {
			contentQueryList1.add(contentQueryList.get(i).toLowerCase());
		}
		System.out.println("query-two查询属性AREANAME取的值是："+contentQueryList1);
		
		//TermsQueryBuilder用于一个字段多个值的查询
		TermsQueryBuilder tb2 = QueryBuilders
				.termsQuery("AREANAME",contentQueryList1);    
		long start = System.nanoTime();                       
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(tb2)				
				.setSize(100).get();				
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();	
		/*ArrayList<String> queryContentList = new ArrayList<>();
        queryContentList.add("XiBei");
        queryContentList.add("XiNan");
        queryContentList.add("QingZangGaoYuan");*/
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("AREANAME", contentQueryList);   			           
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(tb2)				
				.setSize(100).get();				
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}	
		int rightCount = this.CheckQuery(hits, queryMap);
		fileFunction.WriteLogforQuery("querycontent;" + "AREANAME:" + contentQueryList, QueryFileName.QUERY_TWO);
		fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_TWO);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_TWO);
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Two is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The rate is " + FileFunction.floatToString((float) rightCount / count * 100) + "%");
        System.out.println("query-two查询属性AREANAME取的值是："+contentQueryList1);
        return fileList;
    }

    @Override
    public List<String> QueryThree() {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	/*String[] s_time =new String[6];
		s_time[0]=String.valueOf(shour);s_time[1]=String.valueOf(smins);s_time[2]=String.valueOf(ssecs);
		s_time[3]=String.valueOf(ehour);s_time[4]=String.valueOf(emins);s_time[5]=String.valueOf(esecs);
		for(int i=0;i<s_time.length;i++) {
			if(s_time[i].length()==1) {
				s_time[i]="0" + s_time[i];
			}
		}
		String startdate = sdate + " " + s_time[0]+":"+s_time[1]+":"+s_time[2];        
        String enddate = edate + " " + s_time[3]+":"+s_time[4]+":"+s_time[5];*/
    	 LocalDateTime[] queryContentArray = getTimeQueryContentArray();
    	 System.out.println("query-three查询属性SIMULATIONTIME取的值是："+queryContentArray[0]+"--"+queryContentArray[1]);       
    	 String startdate = queryContentArray[0].toString().replaceAll("T", " ");
    	 String enddate = queryContentArray[1].toString().replaceAll("T", " ");
         //long lsdate = queryContentArray[0].toInstant(ZoneOffset.of("+8")).getEpochSecond();
         //long ledate = queryContentArray[1].toInstant(ZoneOffset.of("+8")).getEpochSecond();       
				
		//ES在索引时可自动识别date类型，yyyy-MM-dd HH:mm:ss此形式可被识别。
		QueryBuilder qb = QueryBuilders.rangeQuery("SIMULATIONTIME.keyword")//必须是SIMULATIONTIME.keyword形式，加.keyword
				.gte(startdate)
				.lte(enddate);		
		long start = System.nanoTime();		                             
	    SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))	    	
	    		.setQuery(QueryBuilders.boolQuery()  
                .must(qb))	    		
	    		.setSize(100).get();				
	    while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();	
		ArrayList<LocalDateTime> timeQueryList = new ArrayList<>();
        timeQueryList.add(queryContentArray[0]);
        timeQueryList.add(queryContentArray[1]);
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("SIMULATIONTIME", timeQueryList);
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
	    		.setQuery(QueryBuilders.boolQuery()  
                .must(qb))	    		
	    		.setSize(100).get();				
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits];
		while (true) {    
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }
            sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr.getHits().getHits().length == 0) {
                break;
            }
		}	
		int rightCount = this.CheckQuery(hits, queryMap);
		System.out.println("rightCount " + rightCount);	
		fileFunction.WriteLogforQuery("querytime;" + NewDateTime.GetTimeFormat(queryContentArray[0])
        + "--" + NewDateTime.GetTimeFormat(queryContentArray[1])
        , QueryFileName.QUERY_THREE);
		fileFunction.WriteLogforQuery("rate;" + FileFunction.floatToPresentString((float) rightCount / count), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_THREE);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_THREE);
		System.out.println("The number of files is " + count);	
		System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
		System.out.println("The Avg Speed of Query-Three is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
		return fileList;
    }

    @Override
    public List<String> QueryFour(){
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	String deviceTypeContent = this.getDeviceTypeQueryContent();
        String areaNameContent = this.getAreaNameContent();
        System.out.println("query-four查询属性AREANAME取的值是："+areaNameContent);
        System.out.println("query-four查询属性DEVICETYPE取的值是："+deviceTypeContent);
		
        //and 查询		
		TermQueryBuilder tb1 = QueryBuilders	            
				.termQuery("DEVICETYPE",deviceTypeContent.toLowerCase());
		TermQueryBuilder tb2 = QueryBuilders	            
				.termQuery("AREANAME",areaNameContent.toLowerCase());
		long start = System.nanoTime();
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				//.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.boolQuery()  
	            .must(tb1)
	            .must(tb2))				
				.setSize(100).get();		
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();	
		Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("DEVICETYPE", deviceTypeContent);
        queryMap.put("AREANAME", areaNameContent);    	    
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))			
				.setQuery(QueryBuilders.boolQuery()  
	            .must(tb1)
	            .must(tb2))				
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}	
		int rightCount = this.CheckQuery(hits, queryMap);
		fileFunction.WriteLogforQuery("querycontent;" +"AREANAME:"+areaNameContent+" & DEVICETYPE:"+deviceTypeContent, QueryFileName.QUERY_FOUR);
		fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_FOUR);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_FOUR);
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Four is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The rate is " + FileFunction.floatToString((float) rightCount / count * 100) + "%");
        System.out.println("query-four查询属性AREANAME取的值是："+areaNameContent);
        System.out.println("query-four查询属性DEVICETYPE取的值是："+deviceTypeContent);
        return fileList;
    }

    @Override
    public List<String> QueryFive() {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	ArrayList<String> areaNameContentList = this.getAreaNameQueryContentList();
    	ArrayList<String> areaNameContentList1 = new ArrayList<String>();
		for(int i=0;i<areaNameContentList.size();i++) {
			areaNameContentList1.add(areaNameContentList.get(i).toLowerCase());
		}
		String devicetypeContent = this.getDeviceTypeQueryContent();
		System.out.println("query-five查询属性AREANAME取的值是："+areaNameContentList1);
        System.out.println("query-five查询属性DEVICETYPE取的值是："+devicetypeContent);
    	
		TermsQueryBuilder tb1 = QueryBuilders	            
				.termsQuery("AREANAME",areaNameContentList1);
		TermQueryBuilder tb2 = QueryBuilders	            
				.termQuery("DEVICETYPE",devicetypeContent.toLowerCase());
		long start = System.nanoTime();
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				//.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.boolQuery()  
			    .must(tb1)
			    .must(tb2))				
				.setSize(100).get();			
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();	
		Map<String, Object> queryMap = new HashMap<>();
        /*ArrayList<String> queryContentList = new ArrayList<>();
        queryContentList.add("DongBei");
        queryContentList.add("HuaBei");*/
        queryMap.put("AREANAME", areaNameContentList);
        queryMap.put("DEVICETYPE", devicetypeContent);          
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				//.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.boolQuery()  
			    .must(tb1)
			    .must(tb2))				
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}	
		int rightCount = this.CheckQuery(hits, queryMap);
		fileFunction.WriteLogforQuery("querycontent;" +"AREANAME:"+areaNameContentList+" & DEVICETYPE:"+devicetypeContent, QueryFileName.QUERY_FIVE);
		fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_FIVE);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_FIVE);
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Five is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The rate is " + FileFunction.floatToString((float) rightCount / count * 100) + "%");
        System.out.println("query-five查询属性AREANAME取的值是："+areaNameContentList1);
        System.out.println("query-five查询属性DEVICETYPE取的值是："+devicetypeContent);
        return fileList;
    }

    @Override
    public List<String> QuerySix() throws IOException {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	/*this.getValue();
    	int heightsub = this.towerheigh_max - this.towerheigh_min;
        int lengthsub = this.sylenth_max - this.sylenth_min;*/
    	int[] bladeLengthQueryRange = this.getNumerRange(DevicePartNameEnum.BLADELENGTH.getValue());
        int[] towerHeightQueryRange = this.getNumerRange(DevicePartNameEnum.TOWERHEIGHT.getValue());
        System.out.println("query-six查询属性BLADELENGTH取的值是："+bladeLengthQueryRange[0]+"--"+bladeLengthQueryRange[1]);
        System.out.println("query-six查询属性TOWERHEIGHT取的值是："+towerHeightQueryRange[0]+"--"+towerHeightQueryRange[1]);
		
		QueryBuilder qb1 = QueryBuilders.rangeQuery("BLADELENGTH")
				.gte(bladeLengthQueryRange[0])
				.lte(bladeLengthQueryRange[1]);
		QueryBuilder qb2 = QueryBuilders.rangeQuery("TOWERHEIGHT")
				.gte(towerHeightQueryRange[0])
				.lte(towerHeightQueryRange[1]);
		long start = System.nanoTime();
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				//.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.boolQuery()  
			    .must(qb1)
			    .must(qb2))
				.setSize(100).get();				
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();	
		 Map<String, Object> queryMap = new HashMap<>();
        //ArrayList<Integer> queryContentListOne = new ArrayList<>();
        //ArrayList<Integer> queryContentListTwo = new ArrayList<>();
        //queryContentListOne.add(this.sylenth_min + (int)(lengthsub / 4));
        //queryContentListOne.add(this.sylenth_max - (int)(lengthsub / 4));
        //queryContentListTwo.add(this.towerheigh_min + (int)(heightsub / 4));
        //queryContentListTwo.add(this.towerheigh_max - (int)(heightsub / 4));
        queryMap.put("BLADELENGTH", bladeLengthQueryRange[0] + "-" + bladeLengthQueryRange[1]);
        queryMap.put("TOWERHEIGHT", towerHeightQueryRange[0] + "-" + towerHeightQueryRange[1]);               
        int count=0;
        SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))			
				.setQuery(QueryBuilders.boolQuery()  
			    .must(qb1)
			    .must(qb2))
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}	
		int rightCount = this.CheckQuery(hits, queryMap);
		fileFunction.WriteLogforQuery("querycontent;" +"BLADELENGTH:"+bladeLengthQueryRange[0]+"--"+bladeLengthQueryRange[1]
				+" & TOWERHEIGHT:"+towerHeightQueryRange[0] + "-" + towerHeightQueryRange[1], QueryFileName.QUERY_SIX);
		fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_SIX);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_SIX);
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Six is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The rate is " + FileFunction.floatToString((float) rightCount / count * 100) + "%");
        System.out.println("query-six查询属性BLADELENGTH取的值是："+bladeLengthQueryRange[0]+"--"+bladeLengthQueryRange[1]);
        System.out.println("query-six查询属性TOWERHEIGHT取的值是："+towerHeightQueryRange[0]+"--"+towerHeightQueryRange[1]);
        return fileList;
    }

    @Override
    public List<String> QuerySeven() {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);    	
    	ArrayList<String> areaNameContentList = this.getAreaNameQueryContentList();
    	ArrayList<String> areaNameContentList1 = new ArrayList<String>();
		for(int i=0;i<areaNameContentList.size();i++) {
			areaNameContentList1.add(areaNameContentList.get(i).toLowerCase());
		}
		String deviceTypeContent = this.getDeviceTypeQueryContent();
		int[] towerHeightArray = this.getNumerRange(DevicePartNameEnum.TOWERHEIGHT.getValue());   
		System.out.println("query-seven查询属性AREANAME取的值是："+areaNameContentList1);
        System.out.println("query-seven查询属性DEVICETYPE取的值是："+deviceTypeContent);
        System.out.println("query-seven查询属性TOWERHEIGHT取的值是："+towerHeightArray[0]+"--"+towerHeightArray[1]);
		
		TermsQueryBuilder tb1 = QueryBuilders	            
				.termsQuery("AREANAME",areaNameContentList1);
		TermsQueryBuilder tb2 = QueryBuilders	            
				.termsQuery("DEVICETYPE",deviceTypeContent.toLowerCase());
		QueryBuilder qb = QueryBuilders.rangeQuery("TOWERHEIGHT")
				.gte(towerHeightArray[0])
				.lte(towerHeightArray[1]);	
		long start = System.nanoTime();
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				//.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.boolQuery() 
				.must(tb1)
			    .must(tb2)
			    .must(qb))
				.setSize(100).get();				
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		ArrayList<String> fileList = new ArrayList<>();		
		Map<String, Object> queryMap = new HashMap<>();      
        queryMap.put("DEVICETYPE", deviceTypeContent);
        queryMap.put("AREANAME", areaNameContentList);
        queryMap.put("TOWERHEIGHT", towerHeightArray[0] + "-" + towerHeightArray[1]);             
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				//.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.boolQuery() 
				.must(tb1)
			    .must(tb2)
			    .must(qb))
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			fileList.add(searchHit.getSourceAsMap().get("FILENAME").toString() + ":" + Integer.valueOf(searchHit.getSourceAsMap().get("FILECOPYNUM").toString()));         	
            	count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}	
		int rightCount = this.CheckQuery(hits, queryMap);
		fileFunction.WriteLogforQuery("querycontent;" +"AREANAME:"+areaNameContentList+" & DEVICETYPE:"+deviceTypeContent
				+" & TOWERHEIGHT:"+towerHeightArray[0]+"--"+towerHeightArray[1], QueryFileName.QUERY_SEVEN);
		fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_SEVEN);
        fileFunction.WriteLogforQuery("rate;" +FileFunction.floatToString((float) rightCount / count * 100), QueryFileName.QUERY_SEVEN);
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Seven is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The rate is " + FileFunction.floatToString((float) rightCount / count * 100) + "%");
        System.out.println("towerheigh_min:"+this.towerheigh_min);
        System.out.println("towerheigh_max:"+this.towerheigh_max);
        System.out.println("query-seven查询属性AREANAME取的值是："+areaNameContentList1);
        System.out.println("query-seven查询属性DEVICETYPE取的值是："+deviceTypeContent);
        System.out.println("query-seven查询属性TOWERHEIGHT取的值是："+towerHeightArray[0]+"--"+towerHeightArray[1]);
        return fileList;
    }

    @Override
    public void QueryNine() {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);
    	int c=1;
		SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
		while(c>0) {
			if(!queryContentTuple.sortedDataType.equals("Integer")) {
	    		queryContentTuple = this.getSortAttributeName();
	    		c++;
	    	}else {
	    		c=0;
	    	}				
		}
    	System.out.println("QueryNine选择排序的属性："+queryContentTuple.getSortAttributeContent());
    	SortOrder sortOrder = null;   	
    	String order="";   	
    	if(queryContentTuple.getSortedType()==-1) {
    		sortOrder = SortOrder.DESC;
    		order = "DESC";
    	}else {
    		sortOrder = SortOrder.ASC;
    		order = "ASC";
    	}    	   	
		long start = System.nanoTime();
		//QueryBuilder qb = QueryBuilders.termQuery("DEVICETYPE", "BLADE".toLowerCase());	
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.matchAllQuery())
				.addSort(queryContentTuple.getSortAttributeContent(), sortOrder)//所有设备类型为“BLADE”的文件中，按“BLADEWIDTH”字段倒序查询			
				.setSize(100).get();				
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.matchAllQuery())
				.addSort(queryContentTuple.getSortAttributeContent(), sortOrder)//所有设备类型为“BLADE”的文件中，按“BLADEWIDTH”字段倒序查询			
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}		
		String checkSorted = CheckSorted(hits, queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedDataType(), queryContentTuple.getSortedType());
		fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_NINE);
	    fileFunction.WriteLogforQuery("ability;" + checkSorted, QueryFileName.QUERY_NINE);
	    fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_NINE);//排序文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_NINE);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_NINE);//排序速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Nine is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The ability is " + checkSorted);
    }

    @Override
    public void QueryTen() {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName); 
    	int c=1;
		SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
		while(c>0) {
			if(!queryContentTuple.sortedDataType.equals("String")) {
	    		queryContentTuple = this.getSortAttributeName();
	    		c++;
	    	}else {
	    		c=0;
	    	}				
		}
    	System.out.println("QueryTen选择排序的属性："+queryContentTuple.getSortAttributeContent());
    	SortOrder sortOrder = null;
    	String order="";
    	if(queryContentTuple.getSortedType()==-1) {
    		sortOrder = SortOrder.DESC;
    		order = "DESC";
    	}else {
    		sortOrder = SortOrder.ASC;
    		order = "ASC";
    	}
    	/*if(queryContentTuple.getSortAttributeContent().equals("DEVICETYPE")
    		||queryContentTuple.getSortAttributeContent().equals("FILENAME")
    		||queryContentTuple.getSortAttributeContent().equals("AREANAME")
    		||queryContentTuple.getSortAttributeContent().equals("SIMULATIONTIME")) {
    		sorttype = queryContentTuple.getSortAttributeContent()+".keyword";
    	}else {
    		sorttype = queryContentTuple.getSortAttributeContent();
    	}*/
		long start = System.nanoTime();		
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.matchAllQuery())
				.addSort(queryContentTuple.getSortAttributeContent()+".keyword", sortOrder)//所有设备类型为“BLADE”的文件中，按“AREANAME”字段正序查询				
				.setSize(100).get();				
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();	
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.matchAllQuery())
				.addSort(queryContentTuple.getSortAttributeContent()+".keyword", sortOrder)//所有设备类型为“BLADE”的文件中，按“AREANAME”字段正序查询				
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}	
		String checkSorted = CheckSorted(hits, queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedDataType(), queryContentTuple.getSortedType());
		fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_TEN);
        fileFunction.WriteLogforQuery("ability;" + checkSorted, QueryFileName.QUERY_TEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_TEN);//排序文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_TEN);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf(count / (float)((end - start) / 1E6)), QueryFileName.QUERY_TEN);//排序速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Ten is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The ability is " + checkSorted);
		}

    @Override
    public void QueryEleven() {
    	int timeMillis = 60000;
    	SearchRequestBuilder srb=this.dbBase.prepareSearch(this.collectionName).setTypes(this.collectionName);   
    	int c=1;
		SortedTuple<String, String, Integer> queryContentTuple = this.getSortAttributeName();
		while(c>0) {
			if(!queryContentTuple.sortedDataType.equals("Date")) {
	    		queryContentTuple = this.getSortAttributeName();
	    		c++;
	    	}else {
	    		c=0;
	    	}				
		}
    	System.out.println("QueryEleven选择排序的属性："+queryContentTuple.getSortAttributeContent());
    	SortOrder sortOrder = null;
    	String order="";
    	if(queryContentTuple.getSortedType()==-1) {
    		sortOrder = SortOrder.DESC;
    		order="DESC";
    	}else {
    		sortOrder = SortOrder.ASC;
    		order="ASC";
    	}
    	/*if(queryContentTuple.getSortAttributeContent().equals("DEVICETYPE")
    		||queryContentTuple.getSortAttributeContent().equals("FILENAME")
    		||queryContentTuple.getSortAttributeContent().equals("AREANAME")
    		||queryContentTuple.getSortAttributeContent().equals("SIMULATIONTIME")) {
    		sorttype = queryContentTuple.getSortAttributeContent()+".keyword";
    	}else {
    		sorttype = queryContentTuple.getSortAttributeContent();
    	}*/
		long start = System.nanoTime();
		//QueryBuilder qb = QueryBuilders.termQuery("DEVICETYPE", "BLADE".toLowerCase());						
		SearchResponse sr1=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.matchAllQuery())					
				.addSort(queryContentTuple.getSortAttributeContent()+".keyword", sortOrder)//按时间排序需要时分秒为“HH:MM:SS”的形式							
				.setSize(100).get();			
		while (true) {   		        	
            sr1 = this.dbBase.prepareSearchScroll(sr1.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (sr1.getHits().getHits().length == 0) {
                break;
            }		            
		}
		long end = System.nanoTime();	
		int count=0;
		SearchResponse sr=srb.setScroll(new TimeValue(timeMillis))
				.setQuery(QueryBuilders.matchAllQuery())					
				.addSort(queryContentTuple.getSortAttributeContent()+".keyword", sortOrder)//按时间排序需要时分秒为“HH:MM:SS”的形式							
				.setSize(100).get();
		SearchHit[] hits = new SearchHit[(int) sr.getHits().totalHits]; 		
		while (true) { 		
			Iterator<SearchHit> countSearchHit = sr.getHits().iterator();				
    		while (countSearchHit.hasNext()) {
    			SearchHit searchHit = countSearchHit.next();
    			hits[count]=searchHit;   				
    			count++;
            }   		
    		 sr = this.dbBase.prepareSearchScroll(sr.getScrollId())
                     .setScroll(new TimeValue(timeMillis)).execute().actionGet();            
             if (sr.getHits().getHits().length == 0) {
                 break;
             }           
		}
		String checkSorted = CheckSorted(hits, queryContentTuple.getSortAttributeContent(),
                queryContentTuple.getSortedDataType(), queryContentTuple.getSortedType());
		fileFunction.WriteLogforQuery("querycontent;"+queryContentTuple.getSortAttributeContent()+" : "+order, QueryFileName.QUERY_ELEVEN);
        fileFunction.WriteLogforQuery("ability;" + checkSorted, QueryFileName.QUERY_ELEVEN);
        fileFunction.WriteLogforQuery("count;" +String.valueOf(count), QueryFileName.QUERY_ELEVEN);//排序文件总数
        fileFunction.WriteLogforQuery("time;" +String.valueOf((float)((end - start) / 1E6)), QueryFileName.QUERY_ELEVEN);//用时
        fileFunction.WriteLogforQuery("speed;" +String.valueOf((float)(count / (float)((end - start) / 1E6))), QueryFileName.QUERY_ELEVEN);//排序速度
        System.out.println("The number of files is " + count);
        System.out.println("ALL cost time is " + (float)((end - start) / 1E6) + "ms");
        System.out.println("The Avg Speed of Query-Eleven is " + (float)(count / (float)((end - start) / 1E6)) + "per/ms");
        System.out.println("The ability is " + checkSorted);
    }

    @Override
    public void QueryTwelve() {
    	this.SetQueryLogPath();
        this.finish = false;
        fileFunction.WriteLogforQuery("ability;false", QueryFileName.QUERY_TWELVE);
        //fileFunction.WriteLogforQuery("rate;0" , QueryFileName.QUERY_TWELVE);
        //fileFunction.WriteLogforQuery("count;0" , QueryFileName.QUERY_TWELVE);//文件总数
        //fileFunction.WriteLogforQuery("time;0" , QueryFileName.QUERY_TWELVE);//用时
        //fileFunction.WriteLogforQuery("speed;0" , QueryFileName.QUERY_TWELVE);//速度       
    	System.out.println("Elasticsearch can not do QueryTwelve : Query-Avg!");
    	this.finish = true;
    }

    @Override
    public void QueryThirteen() {
    	this.SetQueryLogPath();
        this.finish = false;
    	fileFunction.WriteLogforQuery("ability;false",QueryFileName.QUERY_THIRTEEN);
    	//fileFunction.WriteLogforQuery("rate;0" , QueryFileName.QUERY_THIRTEEN);
        //fileFunction.WriteLogforQuery("count;0" , QueryFileName.QUERY_THIRTEEN);//文件总数
        //fileFunction.WriteLogforQuery("time;0" , QueryFileName.QUERY_THIRTEEN);//用时
       // fileFunction.WriteLogforQuery("speed;0" , QueryFileName.QUERY_THIRTEEN);//速度
    	System.out.println("Elasticsearch can not do QueryThirteen : Query-Min!");
    	this.finish = true;
    }

    @Override
    public void QueryFourteen() {
    	this.SetQueryLogPath();
        this.finish = false;
    	fileFunction.WriteLogforQuery("ability;false",QueryFileName.QUERY_FOURTEEN);
    	//fileFunction.WriteLogforQuery("rate;0" , QueryFileName.QUERY_FOURTEEN);
       // fileFunction.WriteLogforQuery("count;0" , QueryFileName.QUERY_FOURTEEN);//文件总数
       // fileFunction.WriteLogforQuery("time;0" , QueryFileName.QUERY_FOURTEEN);//用时
       // fileFunction.WriteLogforQuery("speed;0" , QueryFileName.QUERY_FOURTEEN);//速度
    	System.out.println("Elasticsearch can not do QueryForteen : Query-Max!");
    	this.finish = true;
    }
    
    public int CheckQuery(SearchHit[] result, Map<String, Object> queryMap){
        int rightCount = 0;
        boolean checkRight = true;      
        for(int i=0;i<result.length;i++) {
        	SearchHit searchHit = result[i];
            Set<String> queryKeySet = queryMap.keySet();
            for(String query : queryKeySet){
                Object queryValue = queryMap.get(query);
                //判断是否为列表，如果是列表，则不是离散查询就是数值的范围查询
                if(queryValue.getClass().getName().equals("java.util.ArrayList")){
                    ArrayList checkQuery = (ArrayList) queryValue;
                    String checkItemType = checkQuery.get(0).getClass().getName();
                    //System.out.println("checkItemType: "+checkItemType);
                    //首先判断是否为数值类型
                    if(checkItemType.equals("java.lang.Integer")){
                        ArrayList<Integer> valueList = new ArrayList<>();
                        for(Object checkItem : checkQuery)
                            valueList.add((Integer) checkItem);
                        Integer checkAttrValue = (Integer ) searchHit.getSourceAsMap().get(query);
                        boolean isRight = false;
                        for(Integer itemToCheck : valueList)
                            if(checkAttrValue == itemToCheck)
                                isRight = true;
                        if(!isRight){
                            checkRight = false;
                            break;
                        }
                    }

                    else if(checkItemType.equals("java.time.LocalDateTime")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Long min = null;Long max = null;	                      
                        String mintime = checkQuery.get(0).toString().replaceAll("T", " ");
                        String maxtime = checkQuery.get(1).toString().replaceAll("T", " ");                        
                        if(mintime.split(" ")[1]!=null&&mintime.split(" ")[1].split(":")[0]!=null&&mintime.split(" ")[1].split(":")[0].equals("12")) {
                        	try {
								min = sdf.parse(mintime).getTime()+Long.valueOf(43200000);									
							} catch (ParseException e1) {
								 //TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }
                        if(maxtime.split(" ")[1]!=null&&maxtime.split(" ")[1].split(":")[0]!=null&&maxtime.split(" ")[1].split(":")[0].equals("12")) {
                        	try {								
								max = sdf.parse(maxtime).getTime()+Long.valueOf(43200000);								
							} catch (ParseException e1) {
								 //TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }	                                              
                        try {
                        	min = sdf.parse(mintime).getTime();
							max = sdf.parse(maxtime).getTime();							
						} catch (ParseException e1) {
							 //TODO Auto-generated catch block
							e1.printStackTrace();
						}						
                        if(max < min){
                            Long temp = max;
                            max = min;
                            min = temp;
                        }	                        
                        String msdate = searchHit.getSourceAsMap().get("SIMULATIONTIME").toString();
                        Long checkAttrValue = null;
                        if(msdate.split(" ")[1]!=null&&msdate.split(" ")[1].split(":")[0]!=null&&msdate.split(" ")[1].split(":")[0].equals("12")) {
                        	try {
								checkAttrValue = sdf.parse(msdate).getTime()+Long.valueOf(43200000);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }else {
                        	try {
								checkAttrValue = sdf.parse(msdate).getTime();
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }                       	                                                  
                        if(checkAttrValue < min || checkAttrValue > max) {
                        	checkRight = false;
                            break;
                        }                      
                    }
                    else if(checkItemType.equals("java.lang.Long")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Long min = (Long) checkQuery.get(0);
                        Long max = (Long) checkQuery.get(1);
                        if(max < min){
                            Long temp = max;
                            max = min;
                            min = temp;
                        }

                        String msdate = searchHit.getSourceAsMap().get("SIMULATIONTIME").toString();
                        try {
                            Long checkAttrValue = sdf.parse(msdate).getTime();
                            if(checkAttrValue < min || checkAttrValue > max) {
                                checkRight = false;
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        ArrayList<String> valueList = new ArrayList<>();
                        for(Object checkItem : checkQuery)
                            valueList.add((String ) checkItem);
                        String checkAttrValue = (String ) searchHit.getSourceAsMap().get(query);
                        boolean isRight = false;
                        for(String itemToCheck : valueList)
                            if(checkAttrValue.equals(itemToCheck))
                                isRight = true;
                        if(!isRight){
                            checkRight = false;
                            break;
                        }
                    }
                }

                else if(queryValue.getClass().getName().equals("java.lang.Integer")){
                    Integer checkAttrValue = (Integer ) searchHit.getSourceAsMap().get(query);
                    Integer itemToCheck = (Integer ) queryValue;
                    if(checkAttrValue != itemToCheck){
                        checkRight = false;
                        break;
                    }
                }
                else if(queryValue.getClass().getName().equals("java.lang.Double")){
                    Double checkAttrValue = (Double ) searchHit.getSourceAsMap().get(query);
                    Double itemToCheck = (Double ) queryValue;
                    if(checkAttrValue != itemToCheck){
                        checkRight = false;
                        break;
                    }
                }
                else{
                    String queryValueString = (String) queryValue;
                    if(queryValueString.contains("-")){
                        Integer min = Integer.parseInt(queryValueString.split("-")[0]);
                        Integer max = Integer.parseInt(queryValueString.split("-")[1]);
                        if(max < min){
                            Integer temp = max;
                            max = min;
                            min = temp;
                        }
                        Integer checkAttrValue = (Integer ) searchHit.getSourceAsMap().get(query);
                        if(checkAttrValue < min || checkAttrValue > max){
                            checkRight = false;
                            break;
                        }
                    }
                    else{
                        String checkAttrValue = (String ) searchHit.getSourceAsMap().get(query);
                        String itemToCheck = (String ) queryValue;
                        if(!checkAttrValue.equals(itemToCheck)){
                            checkRight = false;
                            break;
                        }
                    }
                }               
            }
            if(checkRight)
                rightCount++;
            else
                checkRight = true;
        }
        return rightCount;
    }
    
    public String CheckSorted(SearchHit[] result, String query, String type, int sortWay){
        boolean check = true;
        ArrayList<Object> toCheckValueList = new ArrayList<>();
        //while(result.hasNext()){
        for(int i=0;i<result.length;i++) {
        	SearchHit searchHit = result[i];
           // DBObject toCheckObject = result.next();
            toCheckValueList.add(searchHit.getSourceAsMap().get(query));
        }

        if(type.equals("Integer")){
            int i;
            for(i = 0; i < toCheckValueList.size() - 1; i++){
                Integer value1 = (Integer) toCheckValueList.get(i);
                Integer value2 = (Integer) toCheckValueList.get(i+1);
                if(sortWay==-1) {
                    if (value1 < value2) {
                        check = false;
                        break;
                    }
                }
                else
                if(value1 > value2){
                    check = false;
                    break;
                }
            }
        }
        else if(type.equals("Double")){
            int i;
            for(i = 0; i < toCheckValueList.size() - 1; i++){
                Double value1 = (Double) toCheckValueList.get(i);
                Double value2 = (Double) toCheckValueList.get(i+1);
                if(sortWay==-1) {
                    if (value1 < value2) {
                        check = false;
                        break;
                    }
                }
                else
                if(value1 > value2){
                    check = false;
                    break;
                }
            }
        }
        else{
            int i;
            for(i = 0; i < toCheckValueList.size() - 1; i++){
                String value1 = (String) toCheckValueList.get(i);
                String value2 = (String) toCheckValueList.get(i+1);
                if(sortWay==1) {
                    if(value1.compareTo(value2) == 1){
                        check = false;
                        break;
                    }
                }
                else
                if(value1.compareTo(value2) == -1){
                    check = false;
                    break;
                }
            }
        }

        if(check)
            return "true";
        else
            return "false";
    }

}
