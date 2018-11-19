package cn.ruc.edu.mbdatagen;

import java.io.File;

public class Testpath {
    public static void main (String[] args){
        //String path = Testpath.class.getClass().getResource("/").getPath().substring(1);
        //System.out.println("ss:" + path);
        String path = Testpath.class.getClassLoader().getResource("HistorySettings").getPath();
//        path = path.substring(0, path.length()-1);
        System.out.println("ss:" + path);
    }
}
