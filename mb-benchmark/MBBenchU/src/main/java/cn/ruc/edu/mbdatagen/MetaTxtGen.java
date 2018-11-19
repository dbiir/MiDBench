package cn.ruc.edu.mbdatagen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;


public class MetaTxtGen {
	
	private int id;
	private String type;
	private String typename;
	private int num;
	
	private FileWriter fw = null;
	
	public void SetFilenameInfo(int id, String type, String typename, int num){
		this.id = id;
		this.type = type;
		this.typename = typename;
		this.num = num;
	}
	
	public void SetTxtFilepathAndMake(String p) throws IOException{
		File txtFile = new File(p + "/" + this.id + "_" +
				this.num + this.type + ".txt" );
		txtFile.createNewFile();
		
		this.fw = new FileWriter(txtFile);
	}
	
	public void PutsDeviceInfo(float air, int height, int length,
                               int width, String area, LocalDateTime date, int fileCopynum) throws IOException{
		fw.write("FILENAME\t:\t" + this.id + "_" + this.num + 
				"$" + this.type.substring(1) + ".mbruc");
		fw.write("\nDEVICEID\t:\t" + this.id);
		fw.write("\nDEVICETPE\t:\t" + this.typename);
		fw.write("\nAIRDENSITY\t:\t" + air);
		fw.write("\nTOWERHEIGHT\t:\t" + height);
		fw.write("\nBLADELENGTH\t:\t" + length);
		fw.write("\nBLADEWIDTH\t:\t" + width);
		fw.write("\nAREANAME\t:\t" + area);
		fw.write("\nSIMULATIONTIME\t:\t" +
						NewDateTime.GetTimeFormat(date)
				);
		fw.write("\nFILECOPYNUM\t:\t" + fileCopynum);
	}
	
	public void PutsListArray(String contentname, ArrayList<Float> list,
			float max, float min, float average) throws IOException{
		fw.write("\n" + contentname + "INFO\t:\n");
		int huan = 0;
		for(float s : list){
			if(huan++ == 10)
			{
				fw.write("\n");
				huan = 1;
			}
			fw.write(s + "\t");
		}
		fw.write("\nMAX" + contentname + "\t:\t" + max + "\n");
		fw.write("MIN" + contentname + "\t:\t" + min + "\n");
		fw.write("AVG" + contentname + "\t:\t" + average + "\n");
	}
	
	public void PutsMetaFile() throws IOException{
		fw.close();
	}
	
}
