package com.ruc.dbiir.rest.utils;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.List;  

/**
 * mac cmd exe
* author：mark   
* createTime：Aug 30, 2018 8:06:52 AM   
* @version
 */

public class RemoteExeCmd {

	
	public static void executeCmd() {
		String shpath="/software/mark/GenGraph/result/Execute_Command.sh";//.sh文件的绝对路径
		String command = "sh " + shpath;

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
	}
	
	public static void main(String[] args) {
///Users/mark/program/proWorkspace/pyCharmSrc/GenGraph
		
		String shpath="/software/mark/GenGraph/result/Execute_Command.sh";//.sh文件的绝对路径
		String command = "sh " + shpath;

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

	}  



}
