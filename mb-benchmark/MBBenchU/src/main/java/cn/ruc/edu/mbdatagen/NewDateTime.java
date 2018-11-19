package cn.ruc.edu.mbdatagen;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NewDateTime {

    public static LocalDateTime PlusBySeconds(LocalDateTime localDateTime, int seconds){
        return localDateTime.plusSeconds(seconds);
    }

    public static long GetAllSeconds(LocalDateTime start, LocalDateTime end){
        Duration period = Duration.between(start, end);
        return (period.toMillis() / 1000);
    }

    public static String GetTimeFormat(LocalDateTime date){

        if(date.toLocalTime().getSecond() == 0){
            return date.toLocalDate() + " " + date.toLocalTime() + ":00";
        }
        else
            return  date.toLocalDate() + " " + date.toLocalTime();
    }
}
