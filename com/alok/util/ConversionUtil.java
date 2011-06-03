package com.alok.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversionUtil {
    private static final String ISO_UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public String toISOUtcString(long time){
        Date date = new Date(time);
        return toISOUtcString(date);
    }

    public String toISOUtcString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(ISO_UTC_PATTERN);
        return format.format(date);
    }

    public Date fromISOUtcString(String utcDate){
        SimpleDateFormat format = new SimpleDateFormat(ISO_UTC_PATTERN);
        try {
            return format.parse(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int intValue(String property, int defaultValue) {
        try{
            return Integer.parseInt(property);
        }catch (Exception e){
        }
        return defaultValue;
    }

}


