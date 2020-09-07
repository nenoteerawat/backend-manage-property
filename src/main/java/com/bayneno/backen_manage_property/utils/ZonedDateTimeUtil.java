package com.bayneno.backen_manage_property.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeUtil {
    public static final String YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final ZoneId bangkokAsiaZoneId = ZoneId.of("Asia/Bangkok");

    public static String zonedDateTimeToString(ZonedDateTime dateValue, String formant, ZoneId zoneId) {
        if(dateValue == null) return null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formant)
                .withZone(zoneId);
        return dateValue.format(dateTimeFormatter);
    }

    public static ZonedDateTime stringToZonedDateTime(String strDate, String formant, ZoneId zoneId){
        if(strDate == null || "".equals(strDate)) return null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formant).withZone(zoneId);
        return LocalDateTime.parse(strDate, dateTimeFormatter).atZone(zoneId);
    }

    public static ZonedDateTime now(ZoneId zoneId){
        return ZonedDateTime.now(zoneId);
    }

    public static ZonedDateTime now(){
        return now(bangkokAsiaZoneId);
    }

    public static String nowString(String format, ZoneId zoneId){
        return zonedDateTimeToString(now(zoneId), format, zoneId);
    }

    public static String nowString(String format){
        return nowString(format, bangkokAsiaZoneId);
    }

    public static String nowString(){
        return nowString(YYYYMMDDTHHMMSS);
    }
}
