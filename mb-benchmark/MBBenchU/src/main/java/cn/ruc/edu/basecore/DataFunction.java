package cn.ruc.edu.basecore;

import java.time.LocalDateTime;

public class DataFunction {

    public static String getTimeFormat(LocalDateTime date){

        if(date.toLocalTime().getSecond() == 0){
            return date.toLocalDate() + " " + date.toLocalTime() + ":00";
        }
        else
            return  date.toLocalDate() + " " + date.toLocalTime();
    }
}
