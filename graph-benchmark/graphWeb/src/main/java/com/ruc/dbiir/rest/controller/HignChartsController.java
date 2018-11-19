package com.ruc.dbiir.rest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruc.dbiir.rest.utils.Config;
import com.ruc.dbiir.rest.utils.ParseUtil;
import com.ruc.dbiir.rest.utils.ReadFileUtil;


@Controller
@RequestMapping(value = "/highEchart")
public class HignChartsController {


	
	
	/**
	 * 获取节点文件
	 * author：mark   
	 * createTime：Sep 7, 2018 4:54:46 PM
	 * @return
	 */
	@RequestMapping(value = "/getGraphNode")
	@ResponseBody
	public Object getGraphNode(@RequestParam(value = "graphName") String graphName) {
		
		System.out.println("getGraphNode:"+graphName);
		String filePath = Config.RESULT_PATH+graphName+"/g1_new_nodes_as_files_item.csv";
//		String filePath = "/Users/mark/Documents/MasterLife/GraphDB/neo4j-community-3.4.6/import/Wechatbom_item.csv";
		List<String> list  = ReadFileUtil.readTxtFileIntoStringArrList(filePath);
//		System.out.println(list.toString());
		return list;
	}

	
	/**
	 * 获取边数据
	 * author：mark   
	 * createTime：Sep 7, 2018 4:54:58 PM
	 * @return
	 */
	@RequestMapping(value = "/getGraphEdge")
	@ResponseBody
	public Object getGraphEdge(@RequestParam(value = "graphName") String graphName) {
		String filePath = Config.RESULT_PATH+graphName+"/g1_new_nodes_as_files_relation.csv";
//		String filePath = "/Users/mark/Documents/MasterLife/GraphDB/neo4j-community-3.4.6/import/Wechatbom_relation.csv";
		List<String> list  = ReadFileUtil.readTxtFileIntoStringArrList(filePath);
//		System.out.println(list.toString());
		return list;
	}


	/**
	 * 向生成器发送请求进行数据生成
	 * author：mark   
	 * createTime：Sep 7, 2018 4:55:58 PM
	 * @param username
	 * @param passwd
	 * @param nodetype
	 * @param edgetype
	 * @param querysize
	 * @param graphsize
	 * @return
	 */
	@RequestMapping(value = "/startGen")
	@ResponseBody
	public Object urlcon( @RequestParam(value = "username") String username,  @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "nodetype") String nodetype,@RequestParam(value = "edgetype") String edgetype,
			@RequestParam(value = "querysize") String querysize,@RequestParam(value = "graphsize") String graphsize) {
	
		HttpURLConnection urlCon = null;
		StringBuilder result = null;
		try {
			String urls = Config.PYSERVER_ADDR+"/startGen?"+username+"&"+passwd
					+"&"+nodetype+"&"+edgetype+"&"+querysize+"&"+graphsize;
			URL url = new URL(urls);
			urlCon = (HttpURLConnection)url.openConnection();
			urlCon.setConnectTimeout(300 * 1000);
			urlCon.setReadTimeout(300 * 1000);
			urlCon.setRequestMethod("GET");
			InputStream is =  urlCon.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String inputLine=null;
			result = new StringBuilder("");
			if((inputLine = br.readLine()) != null){
				System.out.println(inputLine);
				result.append(inputLine+"<br>");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		urlCon.disconnect();
		return ParseUtil.toObject(result.toString());
	}

	
	/**
	 * 获取内容列表
	 * author：mark   
	 * createTime：Sep 7, 2018 4:56:20 PM
	 * @return
	 */
	@RequestMapping(value = "/getContentList")
	@ResponseBody
	public Object getContentList() {
		
		String command = "sh " + Config.CMD_ADDR_LIST;

		final List<String> processList = new ArrayList<String>(); 

		try {  
			
			final Process process = Runtime.getRuntime().exec(command);  	
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));  
			String line = "";  
			while ((line = input.readLine()) != null) {  
				line = line+"/suf";System.out.println(line);
				processList.add(line.split("/")[0]+"&"+line.split("/")[1]); 
			}  
			input.close(); 
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		for (String line : processList) {  
			System.out.println(line);  
		}  
		return processList;
//		return ParseUtil.toObject("sss");
	}
	

	/**
	 * 自动化进行测试
	 * author：mark   
	 * createTime：Sep 7, 2018 4:56:20 PM
	 * @return
	 */
	@RequestMapping(value = "/testPerformance")
	@ResponseBody
	public Object testPerformance(@RequestParam(value = "graphName") String graphName) {
		
//		String shpath="/Users/mark/program/proWorkspace/pyCharmSrc/GenGraph/result/Execute_Command.sh";//.sh文件的绝对路径
		String command = "sh " + Config.RESULT_PATH+graphName+"/Execute_Command.sh";

		final List<String> processList = new ArrayList<String>(); 

		try {  
			
			final Process process = Runtime.getRuntime().exec(command);  	
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));  
			String line = "";  
			while ((line = input.readLine()) != null) {  
				processList.add(line); 
			}  
			input.close(); 
			
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		for (String line : processList) {  
			System.out.println(line);  
		}  
		return processList;
//		return ParseUtil.toObject("sss");
	}
	




	
}
