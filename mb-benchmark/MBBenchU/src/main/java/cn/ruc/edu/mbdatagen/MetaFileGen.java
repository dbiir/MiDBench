package cn.ruc.edu.mbdatagen;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

public class MetaFileGen {

    public static float[] MMAData(List<Float> list){
        DoubleStream streamList = list.parallelStream()
                .mapToDouble(Float::floatValue);
        float[] data = new float[3];
        data[1] = (float)streamList.min().orElse(0);
        streamList = list.parallelStream()
                .mapToDouble(Float::floatValue);
        data[0] = (float)streamList.max().orElse(0);
        streamList = list.parallelStream()
                .mapToDouble(Float::floatValue);
        data[2] = (float)streamList.average().orElse(0);
        return data;
    }

    public static void BladeMetaGen(
            int id, int filenum, String path,
            float air, int height, int width,
            int length, String area, LocalDateTime date,
            ArrayList<Float> windlist, ArrayList<Float> nlist,
            ArrayList<Float> pwlist, ArrayList<Float> cplist,
            int[] metatype, int fileCopynum) throws IOException, JSONException {
        if(metatype[0] == 1){
            MetaTxtGen txtGen = new MetaTxtGen();
            //生成Txt格式及文件的风扇仿真数据
            txtGen.SetFilenameInfo(id, "%01", "BLADE", filenum);
            txtGen.SetTxtFilepathAndMake(path + "/txt");
            txtGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);
            float[] data = MMAData(windlist);
            txtGen.PutsListArray("WINDSPEED", windlist, data[0], data[1], data[2]);
            data = MMAData(nlist);
            txtGen.PutsListArray("BLADESPEED", nlist, data[0], data[1], data[2]);
            data = MMAData(pwlist);
            txtGen.PutsListArray("ELECTRONICPOWER", pwlist, data[0], data[1], data[2]);
            data = MMAData(cplist);
            txtGen.PutsListArray("CHANGERATE", cplist, data[0], data[1], data[2]);

            txtGen.PutsMetaFile();
        }

        if(metatype[1] == 1){
            MetaJsonGen jsonGen = new MetaJsonGen();
            //生成JSON格式
            jsonGen.SetFilenameInfo(id, "%01", "BLADE", filenum);
            jsonGen.SetJsonFilepathAndMake(path + "/json");
            jsonGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(windlist);
            jsonGen.PutsListArray("WINDSPEED", windlist, data[0], data[1], data[2]);
            data = MMAData(nlist);
            jsonGen.PutsListArray("BLADESPEED", nlist, data[0], data[1], data[2]);
            data = MMAData(pwlist);
            jsonGen.PutsListArray("ELECTRONICPOWER", pwlist, data[0], data[1], data[2]);
            data = MMAData(cplist);
            jsonGen.PutsListArray("CHANGERATE", cplist, data[0], data[1], data[2]);

            jsonGen.PutsMetaFile();
        }

        if(metatype[2] == 1){
            MetaXmlGen xmlGen = new MetaXmlGen();
            //生成XML格式
            xmlGen.SetFilenameInfo(id, "%01", "BLADE", filenum);
            xmlGen.SetXmlFilepathAndMake(path+"/xml");
            xmlGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(windlist);
            xmlGen.PutsListArray("WINDSPEED", windlist, data[0], data[1], data[2]);
            data = MMAData(nlist);
            xmlGen.PutsListArray("BLADESPEED", nlist, data[0], data[1], data[2]);
            data = MMAData(pwlist);
            xmlGen.PutsListArray("ELECTRONICPOWER", pwlist, data[0], data[1], data[2]);
            data = MMAData(cplist);
            xmlGen.PutsListArray("CHANGERATE", cplist, data[0], data[1], data[2]);

            xmlGen.PutsMetaFile();
            //XMLjar包的使用问题
            xmlGen.ClearMetaFile();
            xmlGen = new MetaXmlGen();
        }
    }

    public static void Dynamo(
            int id, int filenum, String path,
            float air, int height, int width,
            int length, String area, LocalDateTime date,
            ArrayList<Float> mtplist, ArrayList<Float> ewlist,
            int[] metatype, int fileCopynum) throws IOException, JSONException {
        if(metatype[0] == 1){
            MetaTxtGen txtGen = new MetaTxtGen();
            //生成Txt格式及文件的风扇仿真数据
            txtGen.SetFilenameInfo(id, "%02", "DYNAMO", filenum);
            txtGen.SetTxtFilepathAndMake(path + "/txt");
            txtGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);
            float[] data = MMAData(mtplist);
            txtGen.PutsListArray("TRANSRATE", mtplist, data[0], data[1], data[2]);
            data = MMAData(ewlist);
            txtGen.PutsListArray("ELECTRICITY", ewlist, data[0], data[1], data[2]);
            txtGen.PutsMetaFile();
        }

        if(metatype[1] == 1){
            MetaJsonGen jsonGen = new MetaJsonGen();
            //生成JSON格式
            jsonGen.SetFilenameInfo(id, "%02", "DYNAMO", filenum);
            jsonGen.SetJsonFilepathAndMake(path + "/json");
            jsonGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(mtplist);
            jsonGen.PutsListArray("TRANSRATE", mtplist, data[0], data[1], data[2]);
            data = MMAData(ewlist);
            jsonGen.PutsListArray("ELECTRICITY", ewlist, data[0], data[1], data[2]);
            jsonGen.PutsMetaFile();
        }

        if(metatype[2] == 1){
            MetaXmlGen xmlGen = new MetaXmlGen();
            //生成XML格式
            xmlGen.SetFilenameInfo(id, "%02", "DYNAMO", filenum);
            xmlGen.SetXmlFilepathAndMake(path + "/xml");
            xmlGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(mtplist);
            xmlGen.PutsListArray("TRANSRATE", mtplist, data[0], data[1], data[2]);
            data = MMAData(ewlist);
            xmlGen.PutsListArray("ELECTRICITY", ewlist, data[0], data[1], data[2]);
            xmlGen.PutsMetaFile();
            //XMLjar包的使用问题
            xmlGen.ClearMetaFile();
            xmlGen = new MetaXmlGen();
        }
    }

    public static void TransMetaGen(
            int id, int filenum, String path,
            float air, int height, int width,
            int length, String area, LocalDateTime date,
            ArrayList<Float> twlist, ArrayList<Float> twratelist,
            int[] metatype, int fileCopynum) throws IOException, JSONException {

        if(metatype[0] == 1){
            MetaTxtGen txtGen = new MetaTxtGen();
            //生成Txt格式及文件的风扇仿真数据
            txtGen.SetFilenameInfo(id, "%03", "TRANSMISSION", filenum);
            txtGen.SetTxtFilepathAndMake(path + "/txt");
            txtGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(twlist);
            txtGen.PutsListArray("TRANSRATE", twlist, data[0], data[1], data[2]);
            data = MMAData(twratelist);
            txtGen.PutsListArray("TRANSELECRATE", twratelist, data[0], data[1], data[2]);

            txtGen.PutsMetaFile();
        }

        if(metatype[1] == 1){
            MetaJsonGen jsonGen = new MetaJsonGen();
            //生成JSON格式
            jsonGen.SetFilenameInfo(id, "%03", "TRANSMISSION", filenum);
            jsonGen.SetJsonFilepathAndMake(path + "/json");
            jsonGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(twlist);
            jsonGen.PutsListArray("TRANSRATE", twlist, data[0], data[1], data[2]);
            data = MMAData(twratelist);
            jsonGen.PutsListArray("TRANSELECRATE", twratelist, data[0], data[1], data[2]);

            jsonGen.PutsMetaFile();
        }

        if(metatype[2] == 1){
            MetaXmlGen xmlGen = new MetaXmlGen();
            //生成XML格式
            xmlGen.SetFilenameInfo(id, "%03", "TRANSMISSION", filenum);
            xmlGen.SetXmlFilepathAndMake(path + "/xml");
            xmlGen.PutsDeviceInfo(air, height, length, width, area, date, fileCopynum);

            float[] data = MMAData(twlist);
            xmlGen.PutsListArray("TRANSRATE", twlist, data[0], data[1], data[2]);
            data = MMAData(twratelist);
            xmlGen.PutsListArray("TRANSELECRATE", twratelist, data[0], data[1], data[2]);
            xmlGen.PutsMetaFile();
            //XMLjar包的使用问题
            xmlGen.ClearMetaFile();
            xmlGen = new MetaXmlGen();
        }

    }

}
