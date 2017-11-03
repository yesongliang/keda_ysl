package com.kedacom.ysl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 时间工具类
 */
public class DateUtil {

    /** 时间格式:yyyyMMdd */
    public static final String YYYYMMDD = "yyyyMMdd";

    /** 时间格式:yyyyMMddHHmmss */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /** 时间格式:yyyy-MM-dd */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /** 时间格式:yyyyMMdd HH:mm:ss */
    public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HH:mm:ss";

    /** 时间格式:yyyy-MM-dd HH:mm:ss */
    public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式转换
     * 
     * @param dateStr
     * @param srcFormat
     * @param destFormat
     * @return
     * @throws ParseException
     */
    public static String convertDateFormat(String dateStr, String srcFormat, String destFormat) throws ParseException {
	SimpleDateFormat dateFormat = new SimpleDateFormat(srcFormat);
	Date date = dateFormat.parse(dateStr);
	SimpleDateFormat dateFormat2 = new SimpleDateFormat(destFormat);
	return dateFormat2.format(date);
    }

    /**
     * 获取系统当前日期
     * 
     * @param format
     * @return 返回字符串
     */
    public static String getCurrentDate(String format) {
	if (StringUtils.isEmpty(format)) {
	    format = YYYYMMDD;
	}

	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	return dateFormat.format(new Date());
    }

    public static String getTimestamp() {
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	return dateFormat.format(new Date());
    }

    /**
     * 
     * 根据天，计算时间
     * 
     * @param date
     * @param format
     * @param day
     * @return
     */
    public static String calcuateDate(Date date, String format, int day) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.DATE, day);
	Date time = calendar.getTime();
	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	return dateFormat.format(time);
    }

    /**
     * 
     * 根据月，计算时间
     * 
     * @param date
     * @param format
     * @param month
     * @return
     */
    public static String calcuateMonth(Date date, String format, int month) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.MONTH, month);
	Date time = calendar.getTime();
	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	return dateFormat.format(time);
    }

    /**
     * 
     * 根据年，计算时间
     * 
     * @param date
     * @param format
     * @param month
     * @return
     */
    public static String calcuateYear(Date date, String format, int year) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.YEAR, year);
	Date time = calendar.getTime();
	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	return dateFormat.format(time);
    }

    /**
     * 
     * String转换成date
     * 
     * @param date
     *            格式：YYYYMMDD
     * @return
     */
    public static Date date2String(String date, String format) {
	try {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	    return dateFormat.parse(date);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static void main(String[] args) {
	System.out.println(System.currentTimeMillis());
    }

    /**
     * 格式化日期，默认使用yyyyMMdd格式
     * 
     * @param date
     * */
    public static String date2String(Date date) {
	return date2String(date, YYYYMMDD);
    }

    /**
     * 格式化日期，使用指定的日期格式
     * 
     * @param date
     * @param format
     * */
    public static String date2String(Date date, String format) {
	if (date != null) {
	    if (StringUtils.isBlank(format)) {
		format = YYYYMMDD;
	    }
	    return new SimpleDateFormat(format).format(date);
	}
	return null;
    }

    /**
     * 
     * 格式化日期，使用指定的日期格式
     * 
     * @param dateStr
     * @param format
     * @throws ParseException
     * */
    public static Date string2Date(String dateStr, String format) throws ParseException {
	if (dateStr != null) {
	    return new SimpleDateFormat(format).parse(dateStr);
	}
	return null;
    }
}
