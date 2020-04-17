package com.shusaku.thread.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author liuzi
 */
public class LocalDateUtils {

    public static void main(String[] args){
        /*Date expTime = parseStringToDateTime("2020-12-12 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        int i = nowDate.compareTo(expTime);
        System.out.println(i);*/
        //获取当前日期指定时间之前的日期
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1L);
        Date date = localDateTimeToDate(localDateTime);
        String s = parseDateTimeToString(date, "yyyy-MM-dd HH:mm:ss");
        System.out.println(s);
    }

    /**
     * Instant：表示时刻，不直接对应年月日信息，需要通过时区转换
     * LocalDateTime: 表示与时区无关的日期和时间信息，不直接对应时刻，需要通过时区转换
     * LocalDate：表示与时区无关的日期，与LocalDateTime相比，只有日期信息，没有时间信息
     * LocalTime：表示与时区无关的时间，与LocalDateTime相比，只有时间信息，没有日期信息
     * ZonedDateTime： 表示特定时区的日期和时间
     * ZoneId/ZoneOffset：表示时区
     */
    private LocalDateUtils(){}

    /**
     *  字符串转为指定格式的Date
     */
    public static Date parseStringToDateTime(String dateStr, String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = ldt.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     *  Date转为指定格式的字符串
     */
    public static String parseDateTimeToString(Date date, String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime time = LocalDateTime.ofInstant(instant, zone);
        String dateStr = df.format(time);
        return dateStr;
    }

    /**
     * Date转为LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTime;
    }

    /**
     * LocalDateTime转为Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * LocalDateTime转为LocalDate
     */
    public static LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime){

        return localDateTime.toLocalDate();
    }

    /**
     * LocalDate转为Date
     */
    public static Date localDateToDate(LocalDate localDate){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay(zoneId).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 两个LocalDateTime的时间间隔
     */
    public static Duration smallRangeInterval(LocalDateTime localDateTime1, LocalDateTime localDateTime2){
        Duration duration = Duration.between(localDateTime1,localDateTime2);
        return duration;
    }

    /**
     * 两个LocalDateTime的时间间隔
     */
    public static Period bigRangeInterval(LocalDateTime localDateTime1, LocalDateTime localDateTime2){
        Period period2 = Period.between(localDateTime1.toLocalDate(),localDateTime2.toLocalDate());
        return period2;
    }

}
