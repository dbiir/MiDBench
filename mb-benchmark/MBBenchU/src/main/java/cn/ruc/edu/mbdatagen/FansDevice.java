package cn.ruc.edu.mbdatagen;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

public class FansDevice extends Thread{
    /*
     * 下面的参数是用来记录当前设备的环境条件数值和设备本身的基础数值
     */
    public int id; //设备的id值，在多设备时表示生成的序号
    public int tower_height; //风力发电的塔高，和风速相关
    public List<Integer> towerheightList = null;
    public int blade_length; //叶片的长度，与面积相关
    public List<Integer> bladelengthList = null;
    public int blade_width; //叶片的宽度，与面积相关
    public List<Integer> bladewidthList = null;
    public float air_density = (float) 1.2933; //空气密度，为定值
    public String area_name; //录入地区名称
    public List<String> areanameList = null;

    public boolean usedevicenum = false;
    public windspeed sp;
    public List<windspeed> sps;

    public int divtime; //录入每个时刻文件生成的数量
    public int changenum;

    public float uv;
    public List<Float> uvs;

    public int filepluspar;
    public int copynum = 0;

    //设置仿真起始时间
    String sdate = null;
    LocalDateTime newStartTime;

    //设置仿真终止时间
    String edate = null;
    LocalDateTime newEndTime;
    //设置中间取值时间
    LocalDateTime newAddTime;

    //设置时间间隔和文件数量
    public int splittime;
    public int filenum;

    public List<Float> sizes;
    public List<Float> presents;

    //设置文件存储路径
    public String filepath;
    public int pretentfilecount = 0;
    public float size = 0;

    public byte[] filecontent;
    public float maxsize;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //设置元数据生成类型
    public int[] metatype;

    //设置生成设备的分批
    public int splitdevice = 1;
    public int threadcount = 0;

    public float normal = 0;
    public float unnormal = 0;
    public float unnormalfactor = 5;

    public void SetSplitDevice(int num, int tc){
        this.splitdevice = num;
        this.threadcount = tc;
    }

    public void FileSetting(int id, String fpath, int fnum,
                            int dnum, int ch, int fipp)
    {
        this.id = id;
        this.filepath = fpath;
        this.filenum =fnum;
        this.divtime = dnum;
        this.changenum = ch;
        this.filepluspar = fipp;
    }

    public void SetDeviceParamter(String an, int th, int bl, int bw)
    {
        this.area_name = an;
        this.tower_height = th;
        this.blade_length = bl;
        this.blade_width = bw;
    }

    public void SetDeviceParamters(List<String> an, List<Integer>th, List<Integer> bl, List<Integer> bw)
    {
        this.areanameList = an;
        this.towerheightList = th;
        this.bladelengthList = bl;
        this.bladewidthList = bw;
    }

    public void SetWindParamter(windspeed w, float uv)
    {
        this.sp = w;
        this.uv = uv;
    }

    public void SetWindParamters(List<windspeed> w, float uv){
        this.sps = w;
        this.uv = uv;
    }

    public void SetCopyNum(int cn){
        this.copynum = cn - 2;
    }

    public void SetStartTime(String sd, int h, int m, int s)
    {
        this.sdate = sd;
        LocalDate date = LocalDate.parse(sd);
        LocalTime time = LocalTime.of(h, m, s);
        this.newStartTime = date.atTime(time);
        this.newAddTime = date.atTime(time);
    }

    public void SetEndTime(String ed, int h, int m, int s)
    {
        this.edate = ed;
        LocalDate date = LocalDate.parse(ed);
        LocalTime time = LocalTime.of(h, m, s);
        this.newEndTime = date.atTime(time);
    }

    public void SetByteContent(){
        byte[] content = new byte[(int)(1024 * 1024 * this.maxsize)];
        Random rnd = new Random();
        rnd.nextBytes(content);
        this.filecontent = content;
    }

    public void SetUnnormalPresent(float un){
        this.unnormal = un;
    }

    public void SetSplittime()
    {
        long allsecond = NewDateTime.GetAllSeconds(this.newStartTime, this.newEndTime);
        this.splittime = (int) (allsecond / (this.filenum - 1));
    }

    public void SetUsedSplittime()
    {
        long allsecond = NewDateTime.GetAllSeconds(this.newStartTime, this.newEndTime);
        this.splittime = (int) (allsecond / (this.filepluspar - 1));
    }

    public LocalDateTime GetTimeBySplittime()
    {
        this.newAddTime = NewDateTime.PlusBySeconds(this.newAddTime, this.splittime);
        return this.newAddTime;
    }

    public void resetAddTime(){
        this.newAddTime = this.newStartTime;
    }

    public LocalDateTime GetTimeForUpdate(int i)
    {
        LocalDate date = LocalDate.parse(this.sdate);
        LocalTime time = LocalTime.of(this.newStartTime.getHour(), this.newStartTime.getMinute(), this.newStartTime.getSecond());
        LocalDateTime toDateTime = date.atTime(time);
        return NewDateTime.PlusBySeconds(toDateTime, this.splittime * i);
    }

    public void SetFilesizes(List<Float> s, List<Float> p, float max)
    {
        this.sizes = s;
        this.presents = p;
        this.maxsize = max;
    }

    public float GetPresentSize()
    {
        float sizes = this.size;
        this.size = 0;
        return sizes;
    }

    public void FileGener(String filepath, int num, String type, float size) throws IOException {
        File disable = new File(filepath + "/" + this.id + "_" +
                num + type + ".mbruc");
        disable.createNewFile();
        FileOutputStream fp = new FileOutputStream(disable);
        if(size == this.maxsize)
            fp.write(this.filecontent);
        else
            fp.write(this.filecontent, 0, (int) (1024 * 1024 * size));
        fp.flush();
        fp.close();
    }

    public void FileGener(String filepath, int deviceid, int num, String type, float size) throws IOException{
        File disable = new File(filepath + "/" + deviceid + "_" +
                num + type + ".mbruc");
        disable.createNewFile();
        FileOutputStream fp = new FileOutputStream(disable);
        if(size == this.maxsize)
            fp.write(this.filecontent);
        else
            fp.write(this.filecontent, 0, (int) (1024 * 1024 * size));
        fp.flush();
        fp.close();
    }

    public void SetMetaType(int[] meta){
        this.metatype = meta;
    }

    public void NewFileCount() throws IOException{
        File sfile = new File(this.filepath + "/.copynum");
        if(!sfile.exists())
            sfile.mkdirs();
        File filescheck = new File(this.filepath  + "/.copynum/" + this.id + ".txt");
        try {
            filescheck.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        FileWriter fw = new FileWriter(filescheck.getAbsolutePath());

        if(splitdevice == 1){
            for(int i = 1; i <= this.filenum; i++)
            {

                fw.write(this.id + "_" + i + ":" + 1 + "\n");
                fw.flush();
            }
            fw.close();
        }
        else{
            for(int i = 0; i < splitdevice; i++)
                for(int j = 1; j <= this.filenum; j++)
                {
                    fw.write((this.id + i * threadcount) + "_" + j + ":" + 1 + "\n");
                    fw.flush();
                }
        }
        fw.close();
    }

    public List<Integer> UseFileCount(boolean[] file) throws IOException{
        List<Integer> fileCopynum = new ArrayList<>();
        File sfile = new File(this.filepath + "/.copynum");
        if(!sfile.exists())
            sfile.mkdirs();
        RandomAccessFile raFile = new RandomAccessFile(this.filepath  + "/.copynum/" + this.id + ".txt", "rw");
        String check = null;
        long oldmark = 0;
        for(int i = 0; i < file.length; i++){
            if(file[i]){
                boolean finish = false;
                while((check = raFile.readLine()) != null){
                    if(check.contains(this.id + "_" + (i+1)))
                    {
                        int count = Integer.parseInt(check.split(":")[1]);
                        raFile.seek(oldmark);
                        raFile.writeBytes(this.id + "_" + (i + 1) + ":" + (count+1) + "\n");
                        finish = true;
                        fileCopynum.add(count + 1);
                    }
                    oldmark = raFile.getFilePointer();
                    if(finish)
                        break;
                }
            }
        }
        raFile.close();
        return fileCopynum;
    }

    public List<Integer> UseFileCount(int deid, boolean[] file) throws IOException{
        List<Integer> fileCopynum = new ArrayList<>();
        File sfile = new File(this.filepath + "/.copynum");
        if(!sfile.exists())
            sfile.mkdirs();
        RandomAccessFile raFile = new RandomAccessFile(this.filepath  + "/.copynum/" + this.id + ".txt", "rw");
        String check = null;
        long oldmark = 0;
        for(int i = 0; i < file.length; i++){
            if(file[i]){
                boolean finish = false;
                while((check = raFile.readLine()) != null)
                {
                    if(check.split(":")[0].equals(deid + "_" + (i+1)))
                    {
                        int count = Integer.parseInt(check.split(":")[1]);


                        raFile.seek(oldmark);
                        raFile.writeBytes(deid + "_" + (i + 1) + ":" + (count+1) + "\n");
                        finish = true;
                        fileCopynum.add(count + 1);
                    }
                    oldmark = raFile.getFilePointer();
                    if(finish){
                        //raFile.seek(0);
                        break;
                    }
                }
            }
        }
        raFile.close();
        return fileCopynum;
    }

    public void AbnormalRecord(String filename, int copynum) throws IOException {
        File sfile = new File(this.filepath + "/.abnormal");
        if(!sfile.exists())
            sfile.mkdirs();
        File recordFile = new File(this.filepath + "/.abnormal/" + this.id + ".txt");
        FileWriter fw;
        if(recordFile.exists()){
            fw = new FileWriter(recordFile,true);
            fw.write("\n" + filename + ":" + copynum);
        }
        else{
            recordFile.createNewFile();
            fw = new FileWriter(recordFile);
            fw.write(filename + ":" + copynum);
        }
        fw.close();
    }
    //用于给第一版数据获取异常文件列表
    public List<Integer> GetAbnormalList(int allFileNum, int currentFileNum){
        List<Integer> abnormallist = new ArrayList<>();
        Random ra = new Random();
        int abnormalNum = (int)(currentFileNum * this.unnormal);
        for(int i = 0; i < abnormalNum; i++){
            int num = ra.nextInt(allFileNum) + 1;
            boolean check = false;
            for(int j = 0; abnormallist.size() != 0 && j < abnormallist.size(); j++)
                if(num == abnormallist.get(j)){
                    check = true;
                    break;
                }
            if(check){
                i--;
                continue;
            }
            else
                abnormallist.add(num);
        }
        abnormallist.sort((a1, a2) -> a1.compareTo(a2));
        return abnormallist;
    }
    //用于给非第一版数据获取异常文件表
    public List<Integer> GetAbnormalList(boolean[] filename){
        List<Integer> readylist = new ArrayList<>();
        List<Integer> abnormallist = new ArrayList<>();
        int filenameLength = filename.length;
        for(int i = 0; i < filenameLength; i++)
            if(filename[i])
                readylist.add(i+1);
        Random ra = new Random();
        int currentFileNum = readylist.size();
        int abnormalNum = (int)(currentFileNum * this.unnormal);
        for(int i = 0; i < abnormalNum; i++) {
            int chooseFileIndex = ra.nextInt(currentFileNum);
            int chooseFilename = readylist.get(chooseFileIndex);
            boolean check = false;
            for (int j = 0; abnormallist.size() != 0 && j < abnormallist.size(); j++)
                if (chooseFilename == abnormallist.get(j)) {
                    check = true;
                    break;
                }
            if (check) {
                i--;
                continue;
            } else
                abnormallist.add(chooseFilename);
        }
        abnormallist.sort((a1, a2) -> a1.compareTo(a2));
        return abnormallist;
    }

    public void run()
    {
        try{
            System.out.println("Device " + this.id + " Start!");
            //生成二进制数组
            this.SetByteContent();
            //表示当前线程只生成一个设备
            if(this.splitdevice == 1){
                int[] sizess = new int[this.filenum];
                for(int i = 1; i < this.sizes.size(); i++)
                {
                    int curentnum = (int) (this.filenum * this.presents.get(i));
                    for(int j = 1; j <= curentnum; j++)
                    {
                        int rs = new Random().nextInt(this.filenum);
                        if(sizess[rs] == i)
                        {
                            j--;
                            continue;
                        }
                        else
                        {
                            sizess[rs] = i;
                        }
                    }
                }

                //检测当前是否为按照已有文件生成
                if(this.changenum == 0 && this.filepluspar != 0)
                {
                    this.SetUsedSplittime();

                    boolean[] allfile = new boolean[this.filenum];
                    for(int i = 0; i < this.filenum; i++)
                        allfile[i] = false;

                    if(this.filenum == this.filepluspar)
                    {
                        for(int i = 0; i < this.filepluspar; i++)
                            allfile[i] = true;
                    }
                    else
                    {
                        for(int i = 0; i < this.filepluspar; i++)
                        {
                            int random = new Random().nextInt(this.filenum);
                            if(allfile[random])
                            {
                                i--;
                                continue;
                            }
                            else {
                                allfile[random] = true;
                            }
                        }
                    }



                    File sfile = new File(this.filepath + "/updata" + this.copynum + "/file");
                    sfile.mkdirs();
                    String loaddatapath = sfile.getAbsolutePath();
                    File mjfile = new File(this.filepath + "/updata" + this.copynum + "/metadata/json");
                    mjfile.mkdirs();
                    String jsondatapath = mjfile.getAbsolutePath();
                    File mxfile = new File(this.filepath + "/updata" + this.copynum + "/metadata/xml");
                    mxfile.mkdirs();
                    String xmldatapath = mxfile.getAbsolutePath();
                    File mtfile = new File(this.filepath + "/updata" + this.copynum + "/metadata/txt");
                    mtfile.mkdirs();
                    String txtdatapath = mtfile.getAbsolutePath();

                    List<Integer> fileCopynum = this.UseFileCount(allfile);
                    List<Integer> abnormallist = this.GetAbnormalList(allfile);
                    int index = 0;
                    int abnormalIndex = 0;
                    int abnormallistSize = abnormallist.size();
                    for(int i = 1; i <= this.filenum;i++)
                    {
                        if(!allfile[i-1])
                            continue;
                        ArrayList<Float> windlist;
                        if(abnormalIndex < abnormallistSize && abnormallist.get(abnormalIndex) == i){
                            sp.SetShunWind(sp.GetShunWind() * this.unnormalfactor);
                            windlist = sp.GetWindBySecond(this.divtime);
                            sp.SetShunWind(sp.GetShunWind() / this.unnormalfactor);
                            this.AbnormalRecord(id + "_" + i, fileCopynum.get(index));
                            abnormalIndex++;
                        }else
                            windlist = sp.GetWindBySecond(this.divtime);

                        float size = this.sizes.get(sizess[i-1]);
                        ArrayList<Float> nlist = new ArrayList<Float>();
                        ArrayList<Float> cplist = new ArrayList<Float>();
                        ArrayList<Float> pwlist = new ArrayList<Float>();

                        ArrayList<Float> mtplist = new ArrayList<Float>();
                        ArrayList<Float> ewlist = new ArrayList<Float>();

                        ArrayList<Float> twlist = new ArrayList<Float>();
                        ArrayList<Float> twratelist = new ArrayList<Float>();

                        for(int j = 0; j < this.divtime; j++)
                        {
                            float n =  (float) (windlist.get(j) / (2 * Math.PI * this.blade_length));
                            nlist.add(n);
                            float cp = (float) (-0.3656 * (Math.pow(1 / windlist.get(j), 2)) + 0.6505 / windlist.get(j));
                            cplist.add(cp);
                            float pw = (float) (0.5 * 1.2933 * Math.PI * Math.pow(this.blade_length, 2)
                                    * Math.pow(windlist.get(j), 3) * cp);
                            pwlist.add(pw);

                            float mtprate = (float) (new Random().nextFloat() + 9) / 10;
                            mtplist.add(mtprate);
                            float ew = pw * mtprate;
                            ewlist.add(ew);

                            //15表示交联聚乙烯绝缘电缆100KM的电阻阻值
                            float ewtrue = ew - (ew / this.uv) * (ew / this.uv) * 15;
                            twlist.add(ewtrue);
                            twratelist.add(ewtrue / ew);
                        }

                        LocalDateTime date = this.GetTimeForUpdate(i-1);

                        MetaFileGen.BladeMetaGen(this.id, i, this.filepath + "/updata" + this.copynum + "/metadata",
                                air_density, tower_height, blade_width, blade_length, area_name, date, windlist,nlist,
                                pwlist, cplist, metatype, fileCopynum.get(index));

                        this.FileGener(loaddatapath, i, "$01", size);
                        this.pretentfilecount ++;
                        this.size += size;
                        size = size / 2;

                        MetaFileGen.Dynamo(this.id, i, this.filepath + "/updata" + this.copynum + "/metadata",
                                air_density, tower_height, blade_width, blade_length, area_name, date, mtplist,
                                ewlist, metatype, fileCopynum.get(index));
                        this.FileGener(loaddatapath, i, "$02", size);

                        this.pretentfilecount ++;
                        this.size += size;

                        MetaFileGen.TransMetaGen(this.id, i, this.filepath + "/updata" + this.copynum + "/metadata",
                                air_density, tower_height, blade_width, blade_length, area_name, date, twlist,
                                twratelist, metatype, fileCopynum.get(index));

                        this.FileGener(loaddatapath, i, "$03", size);

                        this.pretentfilecount ++;
                        this.size += size;
                        index++;
                    }
                }
                else
                {
                    this.SetSplittime();
                    File lsfile = new File(this.filepath + "/" + "loaddata/file");
                    lsfile.mkdirs();
                    String loaddatapath = lsfile.getAbsolutePath();
                    File ltfile = new File(this.filepath + "/" + "loaddata/metadata/txt");
                    ltfile.mkdirs();
                    String loadtxtpath = ltfile.getAbsolutePath();
                    File ljfile = new File(this.filepath + "/" + "loaddata/metadata/json");
                    ljfile.mkdirs();
                    String loadjsonpath = ljfile.getAbsolutePath();
                    File lxfile = new File(this.filepath + "/" + "loaddata/metadata/xml");
                    lxfile.mkdirs();
                    String loadxmlpath = lxfile.getAbsolutePath();

                    this.NewFileCount();

                    List<Integer> abnormallist = this.GetAbnormalList(this.filenum, this.filenum);
                    int abnormalIndex = 0;
                    int abnormallistSize = abnormallist.size();
                    for(int i = 1; i <= this.filenum; i++)
                    {
                        ArrayList<Float> windlist;
                        if(abnormalIndex < abnormallistSize && abnormallist.get(abnormalIndex) == i){
                            sp.SetShunWind(sp.GetShunWind() * this.unnormalfactor);
                            windlist = sp.GetWindBySecond(this.divtime);
                            sp.SetShunWind(sp.GetShunWind() / this.unnormalfactor);
                            this.AbnormalRecord(id + "_" + i, 1);
                            abnormalIndex++;
                        }else
                            windlist = sp.GetWindBySecond(this.divtime);

                        float size = this.sizes.get(sizess[i-1]);
                        ArrayList<Float> nlist = new ArrayList<Float>();
                        ArrayList<Float> cplist = new ArrayList<Float>();
                        ArrayList<Float> pwlist = new ArrayList<Float>();

                        ArrayList<Float> mtplist = new ArrayList<Float>();
                        ArrayList<Float> ewlist = new ArrayList<Float>();

                        ArrayList<Float> twlist = new ArrayList<Float>();
                        ArrayList<Float> twratelist = new ArrayList<Float>();

                        for(int j = 0; j < this.divtime; j++)
                        {
                            float n =  (float) (windlist.get(j) / (2 * Math.PI * this.blade_length));
                            nlist.add(n);
                            float cp = (float) (-0.3656 * (Math.pow(1 / windlist.get(j), 2)) + 0.6505 / windlist.get(j));
                            cplist.add(cp);
                            float pw = (float) (0.5 * 1.2933 * Math.PI * Math.pow(this.blade_length, 2)
                                    * Math.pow(windlist.get(j), 3) * cp);
                            pwlist.add(pw);

                            float mtprate = (float) (new Random().nextFloat() + 9) / 10;
                            mtplist.add(mtprate);
                            float ew = pw * mtprate;
                            ewlist.add(ew);

                            //15表示交联聚乙烯绝缘电缆100KM的电阻阻值
                            float ewtrue = ew - (ew / this.uv) * (ew / this.uv) * 15;
                            twlist.add(ewtrue);
                            twratelist.add(ewtrue / ew);
                        }
                        LocalDateTime date = null;
                        if(i == 1)
                            date = this.newStartTime;
                        else
                            date = this.GetTimeBySplittime();

                        MetaFileGen.BladeMetaGen(this.id, i, this.filepath + "/" + "loaddata/metadata",
                                air_density, tower_height, blade_width, blade_length, area_name, date, windlist,nlist,
                                pwlist, cplist, metatype, 1);

                        this.FileGener(loaddatapath, i, "$01", size);
                        this.pretentfilecount ++;
                        this.size += size;
                        size = size / 2;

                        MetaFileGen.Dynamo(this.id, i, this.filepath + "/" + "loaddata/metadata",
                                air_density, tower_height, blade_width, blade_length, area_name, date, mtplist,
                                ewlist, metatype, 1);
                        this.FileGener(loaddatapath, i, "$02", size);

                        this.pretentfilecount ++;
                        this.size += size;

                        MetaFileGen.TransMetaGen(this.id, i, this.filepath + "/" + "loaddata/metadata",
                                air_density, tower_height, blade_width, blade_length, area_name, date, twlist,
                                twratelist, metatype, 1);

                        this.FileGener(loaddatapath, i, "$03", size);

                        this.pretentfilecount ++;
                        this.size += size;
                    }

                    if(this.changenum == 0)
                    {
                    }
                    else
                    {
                        File usfile = new File(this.filepath + "/" + "updata1/file");
                        usfile.mkdirs();
                        String updatapath = usfile.getAbsolutePath();
                        File utfile = new File(this.filepath + "/" + "updata1/metadata/txt");
                        utfile.mkdirs();
                        String uptxtpath = utfile.getAbsolutePath();
                        File ujfile = new File(this.filepath + "/" + "updata1/metadata/json");
                        ujfile.mkdirs();
                        String upjsonpath = ujfile.getAbsolutePath();
                        File uxfile = new File(this.filepath + "/" + "updata1/metadata/xml");
                        uxfile.mkdirs();
                        String upxmlpath = uxfile.getAbsolutePath();

                        boolean[] checkin = new boolean[this.filenum];

                        if(this.changenum == this.filenum)
                            for(int i = 0; i < this.filenum; i++)
                                checkin[i] = true;
                        else {
                            for (int i = 0; i < this.filenum; i++)
                                checkin[i] = false;
                            for(int i = 1; i <= this.changenum; i++){
                                int choosetype = new Random().nextInt(this.filenum);
                                if(checkin[choosetype])
                                {
                                    i--;
                                    continue;
                                }
                                else
                                    checkin[choosetype] = true;
                            }
                        }

                        List<Integer> copyFile = this.UseFileCount(checkin);
                        abnormallist = this.GetAbnormalList(checkin);
                        int index = 0;
                        abnormalIndex = 0;
                        abnormallistSize = abnormallist.size();
                        for(int i = 0; i < this.filenum; i++)
                        {
                            if(!checkin[i])
                                continue;
                            ArrayList<Float> windlist;
                            if(abnormalIndex < abnormallistSize && abnormallist.get(abnormalIndex) == (i+1)){
                                sp.SetShunWind(sp.GetShunWind() * this.unnormalfactor);
                                windlist = sp.GetWindBySecond(this.divtime);
                                sp.SetShunWind(sp.GetShunWind() / this.unnormalfactor);
                                this.AbnormalRecord(id + "_" + (i+1), copyFile.get(index));
                                abnormalIndex++;
                            }else
                                windlist = sp.GetWindBySecond(this.divtime);
                            float size = this.sizes.get(sizess[i]);

                            ArrayList<Float> nlist = new ArrayList<Float>();
                            ArrayList<Float> cplist = new ArrayList<Float>();
                            ArrayList<Float> pwlist = new ArrayList<Float>();

                            ArrayList<Float> mtplist = new ArrayList<Float>();
                            ArrayList<Float> ewlist = new ArrayList<Float>();

                            ArrayList<Float> twlist = new ArrayList<Float>();
                            ArrayList<Float> twratelist = new ArrayList<Float>();

                            for(int j = 0; j < this.divtime; j++)
                            {
                                float n =  (float) (windlist.get(j) / (2 * Math.PI * this.blade_length));
                                nlist.add(n);
                                float cp = (float) (-0.3656 * (Math.pow(1 / windlist.get(j), 2)) + 0.6505 / windlist.get(j));
                                cplist.add(cp);
                                float pw = (float) (0.5 * 1.2933 * Math.PI * Math.pow(this.blade_length, 2)
                                        * Math.pow(windlist.get(j), 3) * cp);
                                pwlist.add(pw);

                                float mtprate = (float) (new Random().nextFloat() + 9) / 10;
                                mtplist.add(mtprate);
                                float ew = pw * mtprate;
                                ewlist.add(ew);

                                //15表示交联聚乙烯绝缘电缆100KM的电阻阻值
                                float ewtrue = ew - (ew / this.uv) * (ew / this.uv) * 15;
                                twlist.add(ewtrue);
                                twratelist.add(ewtrue / ew);
                            }
                            LocalDateTime date = this.GetTimeForUpdate(i);

                            MetaFileGen.BladeMetaGen(this.id, (i+1), this.filepath + "/" + "updata1/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, windlist,nlist,
                                    pwlist, cplist, metatype, copyFile.get(index));

                            this.FileGener(updatapath, (i+1), "$01", size);
                            this.pretentfilecount ++;
                            this.size += size;
                            size = size / 2;

                            MetaFileGen.Dynamo(this.id, (i+1), this.filepath + "/" + "updata1/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, mtplist,
                                    ewlist, metatype, copyFile.get(index));
                            this.FileGener(updatapath, (i+1), "$02", size);

                            this.pretentfilecount ++;
                            this.size += size;

                            MetaFileGen.TransMetaGen(this.id, (i+1), this.filepath + "/" + "updata1/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, twlist,
                                    twratelist, metatype, copyFile.get(index));

                            this.FileGener(updatapath, (i+1), "$03", size);
                            this.pretentfilecount ++;
                            this.size += size;

                            index++;
                        }
                    }
                }
            }
            //表示该线程需要生成多个设备
            else{
                //初始化只能做一次，因此放到上面
                if(this.changenum == 0 && this.filepluspar != 0){

                }
                else
                    this.NewFileCount();
                for(int sd = 0; sd < this.splitdevice; sd++){
                    this.resetAddTime();
                    windspeed sp = this.sps.get(sd);
                    String area_name = this.areanameList.get(sd);
                    int tower_height = this.towerheightList.get(sd);
                    int blade_length = this.bladelengthList.get(sd);
                    int blade_width = this.bladewidthList.get(sd);
                    int[] sizess = new int[this.filenum];
                    int deviceid = this.id + sd * this.threadcount;
                    for(int i = 1; i < this.sizes.size(); i++)
                    {
                        int curentnum = (int) (this.filenum * this.presents.get(i));
                        for(int j = 1; j <= curentnum; j++)
                        {
                            int rs = new Random().nextInt(this.filenum);
                            if(sizess[rs] == i)
                            {
                                j--;
                                continue;
                            }
                            else
                            {
                                sizess[rs] = i;
                            }
                        }
                    }

                    //检测当前是否为按照已有文件生成
                    if(this.changenum == 0 && this.filepluspar != 0)
                    {
                        this.SetUsedSplittime();
                        boolean[] allfile = new boolean[this.filenum];

                        if(this.filenum == this.filepluspar)
                        {
                            for(int i = 0; i < this.filenum; i++)
                                allfile[i] = true;
                        }
                        else
                        {
                            for(int i = 0; i < this.filenum; i++)
                                allfile[i] = false;
                            for(int i = 0; i < this.filepluspar; i++)
                            {
                                int random = new Random().nextInt(this.filenum);
                                if(allfile[random])
                                {
                                    i--;
                                    continue;
                                }
                                else {
                                    allfile[random] = true;
                                }
                            }
                        }


                        File sfile = new File(this.filepath + "/updata" + this.copynum + "/file");
                        sfile.mkdirs();
                        String loaddatapath = sfile.getAbsolutePath();
                        File mjfile = new File(this.filepath + "/updata" + this.copynum + "/metadata/json");
                        mjfile.mkdirs();
                        String jsondatapath = mjfile.getAbsolutePath();
                        File mxfile = new File(this.filepath + "/updata" + this.copynum + "/metadata/xml");
                        mxfile.mkdirs();
                        String xmldatapath = mxfile.getAbsolutePath();
                        File mtfile = new File(this.filepath + "/updata" + this.copynum + "/metadata/txt");
                        mtfile.mkdirs();
                        String txtdatapath = mtfile.getAbsolutePath();

                        List<Integer> copyfile = new ArrayList<>();

                        try {
                            copyfile = this.UseFileCount(deviceid, allfile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        List<Integer> abnormallist = this.GetAbnormalList(allfile);
                        int index = 0;
                        int abnormalIndex = 0;
                        int abnormallistSize = abnormallist.size();
                        for(int i = 1; i <= this.filenum;i++)
                        {
                            if(!allfile[i-1])
                                continue;
                            ArrayList<Float> windlist;
                            if(abnormalIndex < abnormallistSize && abnormallist.get(abnormalIndex) == i){
                                sp.SetShunWind(sp.GetShunWind() * this.unnormalfactor);
                                windlist = sp.GetWindBySecond(this.divtime);
                                sp.SetShunWind(sp.GetShunWind() / this.unnormalfactor);
                                this.AbnormalRecord(deviceid + "_" + i, copyfile.get(index));
                                abnormalIndex++;
                            }else
                                windlist = sp.GetWindBySecond(this.divtime);

                            float size = this.sizes.get(sizess[i-1]);
                            ArrayList<Float> nlist = new ArrayList<Float>();
                            ArrayList<Float> cplist = new ArrayList<Float>();
                            ArrayList<Float> pwlist = new ArrayList<Float>();

                            ArrayList<Float> mtplist = new ArrayList<Float>();
                            ArrayList<Float> ewlist = new ArrayList<Float>();

                            ArrayList<Float> twlist = new ArrayList<Float>();
                            ArrayList<Float> twratelist = new ArrayList<Float>();

                            for(int j = 0; j < this.divtime; j++)
                            {
                                float n =  (float) (windlist.get(j) / (2 * Math.PI * blade_length));
                                nlist.add(n);
                                float cp = (float) (-0.3656 * (Math.pow(1 / windlist.get(j), 2)) + 0.6505 / windlist.get(j));
                                cplist.add(cp);
                                float pw = (float) (0.5 * 1.2933 * Math.PI * Math.pow(blade_length, 2)
                                        * Math.pow(windlist.get(j), 3) * cp);
                                pwlist.add(pw);

                                float mtprate = (float) (new Random().nextFloat() + 9) / 10;
                                mtplist.add(mtprate);
                                float ew = pw * mtprate;
                                ewlist.add(ew);

                                //15表示交联聚乙烯绝缘电缆100KM的电阻阻值
                                float ewtrue = ew - (ew / this.uv) * (ew / this.uv) * 15;
                                twlist.add(ewtrue);
                                twratelist.add(ewtrue / ew);
                            }

                            LocalDateTime date = this.GetTimeForUpdate(i-1);

                            MetaFileGen.BladeMetaGen(deviceid, i, this.filepath + "/updata" + this.copynum + "/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, windlist,nlist,
                                    pwlist, cplist, metatype, copyfile.get(index));

                            this.FileGener(loaddatapath, deviceid, i, "$01", size);
                            this.pretentfilecount ++;
                            this.size += size;
                            size = size / 2;

                            MetaFileGen.Dynamo(deviceid, i, this.filepath + "/updata" + this.copynum + "/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, mtplist,
                                    ewlist, metatype, copyfile.get(index));
                            this.FileGener(loaddatapath, deviceid, i, "$02", size);

                            this.pretentfilecount ++;
                            this.size += size;

                            MetaFileGen.TransMetaGen(deviceid, i, this.filepath + "/updata" + this.copynum + "/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, twlist,
                                    twratelist, metatype, copyfile.get(index));

                            this.FileGener(loaddatapath, deviceid, i, "$03", size);

                            this.pretentfilecount ++;
                            this.size += size;
                            index++;
                        }
                    }

                    else
                    {
                        MetaTxtGen txtGen = new MetaTxtGen();
                        MetaJsonGen jsonGen = new MetaJsonGen();
                        MetaXmlGen xmlGen = new MetaXmlGen();

                        this.SetSplittime();
                        File lsfile = new File(this.filepath + "/" + "loaddata/file");
                        lsfile.mkdirs();
                        String loaddatapath = lsfile.getAbsolutePath();
                        File ltfile = new File(this.filepath + "/" + "loaddata/metadata/txt");
                        ltfile.mkdirs();
                        String loadtxtpath = ltfile.getAbsolutePath();
                        File ljfile = new File(this.filepath + "/" + "loaddata/metadata/json");
                        ljfile.mkdirs();
                        String loadjsonpath = ljfile.getAbsolutePath();
                        File lxfile = new File(this.filepath + "/" + "loaddata/metadata/xml");
                        lxfile.mkdirs();
                        String loadxmlpath = lxfile.getAbsolutePath();

                        List<Integer> abnormallist = this.GetAbnormalList(this.filenum, this.filenum);
                        int abnormalIndex = 0;
                        int abnormallistSize = abnormallist.size();
                        for(int i = 1; i <= this.filenum; i++)
                        {
                            ArrayList<Float> windlist;
                            if(abnormalIndex < abnormallistSize && abnormallist.get(abnormalIndex) == i){
                                sp.SetShunWind(sp.GetShunWind() + this.unnormalfactor);
                                windlist = sp.GetWindBySecond(this.divtime);
                                sp.SetShunWind(sp.GetShunWind() / this.unnormalfactor);
                                this.AbnormalRecord(deviceid + "_" + i, 1);
                                abnormalIndex++;
                            }else
                                windlist = sp.GetWindBySecond(this.divtime);

                            float size = this.sizes.get(sizess[i-1]);

                            ArrayList<Float> nlist = new ArrayList<Float>();
                            ArrayList<Float> cplist = new ArrayList<Float>();
                            ArrayList<Float> pwlist = new ArrayList<Float>();

                            ArrayList<Float> mtplist = new ArrayList<Float>();
                            ArrayList<Float> ewlist = new ArrayList<Float>();

                            ArrayList<Float> twlist = new ArrayList<Float>();
                            ArrayList<Float> twratelist = new ArrayList<Float>();

                            for(int j = 0; j < this.divtime; j++)
                            {
                                float n =  (float) (windlist.get(j) / (2 * Math.PI * blade_length));
                                nlist.add(n);
                                float cp = (float) (-0.3656 * (Math.pow(1 / windlist.get(j), 2)) + 0.6505 / windlist.get(j));
                                cplist.add(cp);
                                float pw = (float) (0.5 * 1.2933 * Math.PI * Math.pow(blade_length, 2)
                                        * Math.pow(windlist.get(j), 3) * cp);
                                pwlist.add(pw);

                                float mtprate = (float) (new Random().nextFloat() + 9) / 10;
                                mtplist.add(mtprate);
                                float ew = pw * mtprate;
                                ewlist.add(ew);

                                //15表示交联聚乙烯绝缘电缆100KM的电阻阻值
                                float ewtrue = ew - (ew / this.uv) * (ew / this.uv) * 15;
                                twlist.add(ewtrue);
                                twratelist.add(ewtrue / ew);
                            }
                            LocalDateTime date = null;
                            if(i == 1)
                                date = this.newStartTime;
                            else
                                date = this.GetTimeBySplittime();

                            MetaFileGen.BladeMetaGen(deviceid, i, this.filepath + "/" + "loaddata/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, windlist,nlist,
                                    pwlist, cplist, metatype, 1);

                            this.FileGener(loaddatapath, deviceid, i, "$01", size);
                            this.pretentfilecount ++;
                            this.size += size;
                            size = size / 2;

                            MetaFileGen.Dynamo(deviceid, i, this.filepath + "/" + "loaddata/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, mtplist,
                                    ewlist, metatype, 1);
                            this.FileGener(loaddatapath, deviceid, i, "$02", size);

                            this.pretentfilecount ++;
                            this.size += size;

                            MetaFileGen.TransMetaGen(deviceid, i, this.filepath + "/" + "loaddata/metadata",
                                    air_density, tower_height, blade_width, blade_length, area_name, date, twlist,
                                    twratelist, metatype, 1);

                            this.FileGener(loaddatapath, deviceid, i, "$03", size);

                            this.pretentfilecount ++;
                            this.size += size;
                        }
                        if(this.changenum == 0)
                        {
                        }
                        else
                        {
                            File usfile = new File(this.filepath + "/" + "updata1/file");
                            usfile.mkdirs();
                            String updatapath = usfile.getAbsolutePath();
                            File utfile = new File(this.filepath + "/" + "updata1/metadata/txt");
                            utfile.mkdirs();
                            String uptxtpath = utfile.getAbsolutePath();
                            File ujfile = new File(this.filepath + "/" + "updata1/metadata/json");
                            ujfile.mkdirs();
                            String upjsonpath = ujfile.getAbsolutePath();
                            File uxfile = new File(this.filepath + "/" + "updata1/metadata/xml");
                            uxfile.mkdirs();
                            String upxmlpath = uxfile.getAbsolutePath();

                            boolean[] checkin = new boolean[this.filenum];
                            if(this.changenum == this.filenum)
                                for(int i = 0; i < this.filenum; i++)
                                    checkin[i] = true;
                            else {
                                for (int i = 0; i < this.filenum; i++)
                                    checkin[i] = false;
                                for(int i = 1; i <= this.changenum; i++){
                                    int choosetype = new Random().nextInt(this.filenum);
                                    if(checkin[choosetype])
                                    {
                                        i--;
                                        continue;
                                    }
                                    else
                                        checkin[choosetype] = true;
                                }
                            }

                            List<Integer> copyfile = this.UseFileCount(deviceid, checkin);
                            abnormallist = this.GetAbnormalList(checkin);
                            int index = 0;
                            abnormalIndex = 0;
                            abnormallistSize = abnormallist.size();
                            for(int i = 0; i < this.filenum; i++){
                                if(!checkin[i])
                                    continue;
                                ArrayList<Float> windlist;
                                if(abnormalIndex < abnormallistSize && abnormallist.get(abnormalIndex) == (i+1)){
                                    sp.SetShunWind(sp.GetShunWind() * this.unnormalfactor);
                                    windlist = sp.GetWindBySecond(this.divtime);
                                    sp.SetShunWind(sp.GetShunWind() / this.unnormalfactor);
                                    this.AbnormalRecord(deviceid + "_" + (i+1), copyfile.get(index));
                                    abnormalIndex++;
                                }else
                                    windlist = sp.GetWindBySecond(this.divtime);
                                float size = this.sizes.get(sizess[i]);
                                ArrayList<Float> nlist = new ArrayList<Float>();
                                ArrayList<Float> cplist = new ArrayList<Float>();
                                ArrayList<Float> pwlist = new ArrayList<Float>();

                                ArrayList<Float> mtplist = new ArrayList<Float>();
                                ArrayList<Float> ewlist = new ArrayList<Float>();

                                ArrayList<Float> twlist = new ArrayList<Float>();
                                ArrayList<Float> twratelist = new ArrayList<Float>();

                                for(int j = 0; j < this.divtime; j++)
                                {
                                    float n =  (float) (windlist.get(j) / (2 * Math.PI * blade_length));
                                    nlist.add(n);
                                    float cp = (float) (-0.3656 * (Math.pow(1 / windlist.get(j), 2)) + 0.6505 / windlist.get(j));
                                    cplist.add(cp);
                                    float pw = (float) (0.5 * 1.2933 * Math.PI * Math.pow(blade_length, 2)
                                            * Math.pow(windlist.get(j), 3) * cp);
                                    pwlist.add(pw);

                                    float mtprate = (float) (new Random().nextFloat() + 9) / 10;
                                    mtplist.add(mtprate);
                                    float ew = pw * mtprate;
                                    ewlist.add(ew);

                                    //15表示交联聚乙烯绝缘电缆100KM的电阻阻值
                                    float ewtrue = ew - (ew / this.uv) * (ew / this.uv) * 15;
                                    twlist.add(ewtrue);
                                    twratelist.add(ewtrue / ew);
                                }
                                LocalDateTime date = this.GetTimeForUpdate(i);

                                MetaFileGen.BladeMetaGen(deviceid, (i+1), this.filepath + "/" + "updata1/metadata",
                                        air_density, tower_height, blade_width, blade_length, area_name, date, windlist,nlist,
                                        pwlist, cplist, metatype, copyfile.get(index));

                                this.FileGener(updatapath, deviceid, (i+1), "$01", size);
                                this.pretentfilecount ++;
                                this.size += size;
                                size = size / 2;

                                MetaFileGen.Dynamo(deviceid, (i+1), this.filepath + "/" + "updata1/metadata",
                                        air_density, tower_height, blade_width, blade_length, area_name, date, mtplist,
                                        ewlist, metatype, copyfile.get(index));
                                this.FileGener(updatapath, deviceid, (i+1), "$02", size);

                                this.pretentfilecount ++;
                                this.size += size;

                                MetaFileGen.TransMetaGen(deviceid, (i+1), this.filepath + "/" + "updata1/metadata",
                                        air_density, tower_height, blade_width, blade_length, area_name, date, twlist,
                                        twratelist, metatype, copyfile.get(index));

                                this.FileGener(updatapath, deviceid, (i+1), "$03", size);

                                this.pretentfilecount ++;
                                this.size += size;
                                index++;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.run();
        System.out.println("The " + this.id + " Device is Finshed");
    }
}
