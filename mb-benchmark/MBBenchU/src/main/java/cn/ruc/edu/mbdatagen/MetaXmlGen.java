package cn.ruc.edu.mbdatagen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.dom4j.io.XMLWriter;

public class MetaXmlGen {
	private int id;
	private String type;
	private String typename;
	private int num;
	
	private Element root = null;
	private XMLWriter xw = null;
	
	private Document document = null;
	
	public MetaXmlGen(){
		this.document = DocumentHelper.createDocument();
		this.root = document.addElement("Files");
	}
	
	public void SetFilenameInfo(int id, String type, String typename, int num){
		this.id = id;
		this.type = type;
		this.typename = typename;
		this.num = num;
	}
	
	public void SetXmlFilepathAndMake(String p) throws IOException{
		File xmlFile = new File(p + "/" + this.id + "_" +
				this.num + this.type + ".xml" ); 
		xmlFile.createNewFile();
		FileWriter fw = new FileWriter(xmlFile);
		
		this.xw = new XMLWriter(fw);
	}
	
	public void PutsDeviceInfo(float air, int height, int length,
                               int width, String area, LocalDateTime date, int fileCopynum){
		Element deviceinfoElement = this.root.addElement("DEVICEINFO");
		Element nameElement = deviceinfoElement.addElement("FILENAME");
		nameElement.setText(this.id + "_" +
				this.num + "$" + this.type.substring(1) + ".mbruc");
		Element deviceidElement = deviceinfoElement.addElement("DEVICEID");
		deviceidElement.setText(this.id + "");
		Element typeElement = deviceinfoElement.addElement("DEVICETYPE");
		typeElement.setText(this.typename);
		Element airElement = deviceinfoElement.addElement("AIRDENSITY");
		airElement.setText(Float.toString(air));
		Element towhElement = deviceinfoElement.addElement("TOWERHEIGHT");
		towhElement.setText(Integer.toString(height));
		Element bladelElement = deviceinfoElement.addElement("BLADELENGTH");
		bladelElement.setText(Integer.toString(height));
		Element bladewElement = deviceinfoElement.addElement("BLADEWIDTH");
		bladewElement.setText(Integer.toString(width));
		Element areaElement = deviceinfoElement.addElement("AREANAME");
		areaElement.setText(area);
		Element timeElement = deviceinfoElement.addElement("SIMULATIONTIME");
		timeElement.setText(NewDateTime.GetTimeFormat(date));
		Element copynumElement = deviceinfoElement.addElement("FILECOPYNUM");
		copynumElement.setText(Integer.toString(fileCopynum));
	}
	
	public void PutsListArray(String contentname, ArrayList<Float> list,
			float max, float min, float average){
		Element content = this.root.addElement(contentname + "INFO");
		Element contentlist = content.addElement(contentname);
		for(int i = 1; i <= list.size(); i++){
			Element contentcurtent = contentlist.addElement(Integer.toString(i));
			contentcurtent.setText(Float.toString(list.get(i-1)));
		}
		Element maxElement = content.addElement("MAX" + contentname);
		maxElement.setText(Float.toString(max));
		Element minElement = content.addElement("MIN" + contentname);
		minElement.setText(Float.toString(min));
		Element avgElement = content.addElement("AVG" + contentname);
		avgElement.setText(Float.toString(average));	
	}
	
	public void PutsMetaFile() throws IOException{
		this.xw.write(this.document);
		this.xw.close();	
	}
	
	public void ClearMetaFile(){
		this.root = null;
		this.document = null;
		this.xw = null;
	}
}
