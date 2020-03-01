package cn.mrs47.util;

import java.text.SimpleDateFormat;

/**
 * @author mrs47
 */
public class TimeUtil {
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String TimeStampToDate(Long timestampString){
        Long timestamp = timestampString;
        String date = new SimpleDateFormat(STANDARD_FORMAT).format(new java.util.Date(timestamp));
        return date;
    }

}
