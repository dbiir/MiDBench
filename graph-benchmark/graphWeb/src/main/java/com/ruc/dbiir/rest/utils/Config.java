package com.ruc.dbiir.rest.utils;

public class Config {

//	public static final String TOOL_PATH = "/home/presentation/RUC/graph_benchmark/GenGraph";
//	public static final String TOOL_PATH = "/software/mark/GenGraph";
	public static final String TOOL_PATH = "/Users/mark/program/proWorkspace/pyCharmSrc/GenGraph";
//	public static final String TOOL_PATH = "/data/zdyfjh_data/neo/GenGraph";
	
	public static final String RESULT_PATH = TOOL_PATH+"/result/";

//	数据生成器server
//	public static final String PYSERVER_ADDR = "http://10.13.30.18:15000";
//	public static final String PYSERVER_ADDR = "http://10.77.50.193:5000";
	public static final String PYSERVER_ADDR = "http://127.0.0.1:8898";
//	public static final String PYSERVER_ADDR = "http://10.13.30.18:23999";
//	public static final String PYSERVER_ADDR = "http://192.168.3.90:19999";
	
	
//	public static final String CMD_ADDR = TOOL_PATH+"/result/Execute_Command.sh";
	public static final String CMD_ADDR_LIST = TOOL_PATH+"/listContent.sh";

	
//	public static final String DEPLOY_PATH = "/Users/mark/Documents/MasterLife/GraphDB/graphdb/code/generator/thesis/result/Query_Neo4j.cypher";
}
