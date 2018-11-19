package cn.ruc.edu.mbdatagen;

import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReadtest {
	
	private ArrayList<String> area_name = new ArrayList<String>();
	private ArrayList<Float> area_basewind = new ArrayList<Float>();
	private ArrayList<Float> area_maxzhenwind = new ArrayList<Float>();
	private ArrayList<Float> area_maxjianwind = new ArrayList<Float>();
	private ArrayList<Float> area_maxshunwind = new ArrayList<Float>();
	
	private int alltime;
	private float zhenstarttime;
	private float zhencycletime;
	private float jianstarttime;
	private float jianendtime;
	private float jiankeeptime;
	private int uv;
	
	private int areasnum;
	
	public int getalltime()
	{
		return this.alltime;
	}
	
	public float getzhenstart()
	{
		return this.zhenstarttime;
	}
	
	public float getzhencycle()
	{
		return this.zhencycletime;
	}
	
	public float getjianstart()
	{
		return this.jianstarttime;
	}
	
	public float getjianend()
	{
		return this.jianendtime;
	}
	
	public float getjiankeep()
	{
		return this.jiankeeptime;
	}
	
	public int getuv()
	{
		return this.uv;
	}
	
	public ArrayList<String> getAreaName()
	{
		return this.area_name;
	}
	
	public ArrayList<Float> getBasewind()
	{
		return this.area_basewind;
	}
	
	public ArrayList<Float> getzhenwind()
	{
		return this.area_maxzhenwind;
	}
	
	public ArrayList<Float> getjianwind()
	{
		return this.area_maxjianwind;
	}
	
	public ArrayList<Float> getshunwind()
	{
		return this.area_maxshunwind;
	}
	
	public void setareanum(int n)
	{
		this.areasnum = n;
	}
	
	public int getareanum()
	{
		return this.areasnum;
	}
	
	public void ReadXMLFiles() throws SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//File file = new File("rr.txt");
			//String filepath = file.getAbsolutePath();
			//System.out.println(filepath);
			DocumentBuilder db = dbf.newDocumentBuilder();	
			//Document document = db.parse("D:/peizhi.xml");
			//String path = Testpath.class.getClass().getResource("/").getPath().substring(1);
			String path = Testpath.class.getClassLoader().getResource("conf/peizhi.xml").getPath();
			Document document = db.parse(path);
			//Document document = db.parse("../webapps/GHBigBench/Settings/peizhi.xml");
			Element root = document.getDocumentElement();
			NodeList setting = root.getChildNodes();		
			for(int n = 0; n < setting.getLength(); n++)
			{
				Node node = setting.item(n);
				if(node instanceof Element)
				{	
					NodeList contentList = node.getChildNodes();
					for(int i = 0; i < contentList.getLength(); i++)
					{
						Node contentNode = contentList.item(i);
						if(contentNode instanceof Element)
						{
							if(contentNode.getNodeName().equals("areaname"))
							{
								NodeList areaList = document.getElementsByTagName("areaname");
								int count = 0;
								for(int j = 0; j < areaList.getLength(); j++)
								{
									count++;
									NodeList areaNode = areaList.item(j).getChildNodes();
									for(int m = 0; m < areaNode.getLength(); m++)
									{
										Node areaContent = areaNode.item(m);
										if(areaContent instanceof Element)
										{
											String s = areaContent.getNodeName();
											if ("name".equals(s)) {
												this.area_name.add(areaContent.getFirstChild().getNodeValue());
											} else if ("bw".equals(s)) {
												this.area_basewind.add(Float.parseFloat(areaContent.getFirstChild().getNodeValue()));
											} else if ("mzw".equals(s)) {
												this.area_maxzhenwind.add(Float.parseFloat(areaContent.getFirstChild().getNodeValue()));
											} else if ("mjw".equals(s)) {
												this.area_maxjianwind.add(Float.parseFloat(areaContent.getFirstChild().getNodeValue()));
											} else if ("msw".equals(s)) {
												this.area_maxshunwind.add(Float.parseFloat(areaContent.getFirstChild().getNodeValue()));
											}
											System.out.println(areaContent.getNodeName() +
													":" + areaContent.getFirstChild().getNodeValue());																			
										}
									}
								}
								System.out.println(count);
								this.setareanum(count);
								break;
							}
							else
							{
								String s = contentNode.getNodeName();
								if ("time".equals(s)) {
									this.alltime = Integer.parseInt(contentNode.getFirstChild().getNodeValue());
								} else if ("zs".equals(s)) {
									this.zhenstarttime = Float.parseFloat(contentNode.getFirstChild().getNodeValue());
								} else if ("zc".equals(s)) {
									this.zhencycletime = Float.parseFloat(contentNode.getFirstChild().getNodeValue());
								} else if ("js".equals(s)) {
									this.jianstarttime = Float.parseFloat(contentNode.getFirstChild().getNodeValue());
								} else if ("je".equals(s)) {
									this.jianendtime = Float.parseFloat(contentNode.getFirstChild().getNodeValue());
								} else if ("jk".equals(s)) {
									this.jiankeeptime = Float.parseFloat(contentNode.getFirstChild().getNodeValue());
								} else if ("uv".equals(s)) {
									this.uv = Integer.parseInt(contentNode.getFirstChild().getNodeValue());
								}
								System.out.println(contentNode.getNodeName() + ":" + contentNode.getFirstChild().getNodeValue());
							}
						}
					}			
				}				
			}			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
