package com.ruc.dbiir.rest.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 用于将查询集进行转换
 * 
 * @author: cyj
 * @date: 2017年3月29日 下午4:04:30
 */
public class ParseUtil {

	

	/**
	 * 将一个字符串转换成java对象
	 * 
	 * @return
	 * @author: cyj
	 * @date: 2017年3月30日 下午3:11:43
	 */
	public static Object toObject(String string) {
		string = "{ value : '" + string + "' } ";
		JSONObject object = JSON.parseObject(string);
		return object;
	}
	public static Object toObject(boolean b) {
		String string;
		string = "{ value : '" + b + "' } ";
		JSONObject object = JSON.parseObject(string);
		return object;
	}
}
