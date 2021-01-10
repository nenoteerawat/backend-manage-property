package com.bayneno.backen_manage_property.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeUtil {
    public static final String YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DDMMYYHHMMSS = "dd-MM-yyyy HH:mm:ss";
    public static final String DDMMYY = "dd-MM-yyyy";
    public static final String YYYYMMDDTHHMMSSSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DDMMYYYYTHHMMSSSSSZ = "dd-MM-yyyy'T'HH:mm:ss.SSS'Z'";
    public static final ZoneId BANGKOK_ASIA_ZONE_ID = ZoneId.of("Asia/Bangkok");
    public static final ZoneId UTC_ZONE_ID = ZoneId.of("Europe/Berlin");

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
        return now(BANGKOK_ASIA_ZONE_ID);
    }

    public static String nowString(String format, ZoneId zoneId){
        return zonedDateTimeToString(now(zoneId), format, zoneId);
    }

    public static String nowString(String format){
        return nowString(format, BANGKOK_ASIA_ZONE_ID);
    }

    public static String nowString(){
        return nowString(YYYYMMDDTHHMMSS);
    }

    public static String buddhistYear(){
        return ThaiBuddhistDate.now(ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID).format(DateTimeFormatter.ofPattern("yyyy"));
    }

    public static String christianityYear(){
        return now().format(DateTimeFormatter.ofPattern("yyyy"));
    }
}
