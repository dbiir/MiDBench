package cn.ruc.edu.mbdatagen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MetaJsonGen {
	
	private int id;
	private String type;
	private String typename;
	private int num;
	
	//private JSONObject jsonall = new JSONObject();
	private JSONObject jsonfAll = new JSONObject();
	private PrintWriter jw = null;
	
	public void SetFilenameInfo(int id, String type, String typename, int num){
		this.id = id;
		this.type = type;
		this.typename = typename;
		this.num = num;
		//this.jsonall = new JSONObject();
		this.jsonfAll = new JSONObject();
	}
	
	public void SetJsonFilepathAndMake(String p) throws IOException{
		File jsonFile = new File(p + "/" + this.id + "_" +
				this.num + this.type + ".json" );
		jsonFile.createNewFile();
		
		FileWriter fw = new FileWriter(jsonFile);
		this.jw = new PrintWriter(fw);
	}
	
	public void PutsDeviceInfo(float air, int height, int length,
							   int width, String area, LocalDateTime date, int fileCopynum) throws JSONException{
	
		//SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//JSONObject allinfo = new JSONObject();
		//JSONObject baseinfo = new JSONObject();
		this.jsonfAll.put("FILENAME", this.id + "_" +
				this.num + "$" + this.type.substring(1) + ".mbruc");
		this.jsonfAll.put("DEVICEID", this.id);
		this.jsonfAll.put("DEVICETYPE", this.typename);
		this.jsonfAll.put("AIRDENSITY", air);
		this.jsonfAll.put("TOWERHEIGHT", height);
		this.jsonfAll.put("BLADELENGTH", length);
		this.jsonfAll.put("BLADEWIDTH", width);
		this.jsonfAll.put("AREANAME", area);
		this.jsonfAll.put("SIMULATIONTIME", NewDateTime.GetTimeFormat(date));
		this.jsonfAll.put("FILECOPYNUM", fileCopynum);
		//allinfo.put("DEVICEINFO", baseinfo);
		//this.jsonall.put(baseinfo);
	}
	
	public void PutsListArray(String contentname, ArrayList<Float> list,
			float max, float min, float average) throws JSONException{
		JSONObject allinfo = new JSONObject();
		JSONObject listobject = new JSONObject();
		JSONArray listarray = new JSONArray();
		for(float s : list)
			listarray.put(s);
		this.jsonfAll.put(contentname, listarray);
		this.jsonfAll.put("MAX" + contentname, max);
		this.jsonfAll.put("MIN" + contentname, min);
		this.jsonfAll.put("AVG" + contentname, average);
		//allinfo.put(contentname + "INFO", listobject);
		//this.jsonall.put(listobject);
	}
	
	public void PutsMetaFile() throws JSONException{
		//this.jsonfAll.put("Files", this.jsonall);
		//this.jsonfAll.put(this.jsonall);
		this.jw.write(jsonfAll.toString());
		this.jw.close();
	}
}
