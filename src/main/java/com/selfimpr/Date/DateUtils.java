package com.selfimpr.Date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * 基于java8 LocalDate，LocalTime，LocalDateTime封装的日期工具
 * <p>
 * 在新的Java 8中，日期和时间被明确划分为LocalDate和LocalTime，
 * LocalDate无法包含时间，
 * LocalTime无法包含日期，
 * LocalDateTime才能同时包含日期和时间。
 * <p>
 * 备注：
 * 1，实体接收字段前面要加上@DateTimeFormat(pattern=“pattern1”)注解
 */
public class DateUtils {

    /**
     * 自定义日期格式pattern
     *
     * pattern格式：yyyy-MM-dd HH:mm:ss
     * 1.M m是为了区分“月”与“分”，M:月，m:分
     * 2.H h是为了区分12小时制与24小时制,H是24小时制,h是12小时制。
     */
    public static final String pattern1 = "yyyy-MM-dd HH:mm:ss";
    public static final String pattern2 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String pattern3 = "yyyy/MM/dd HH:mm:ss";
    public static final String pattern4 = "yyyy年MM月dd日 HH:mm:ss";

    // TODO ########################################## 获取相关 开始 ##########################################

    /**
     * 获取指定 LocalDateTime 的毫秒
     *
     * @param time LocalDateTime
     * @return long
     */
    public static long getMilli4LocalDateTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定 LocalDateTime 的秒
     *
     * @param time LocalDateTime
     * @return long
     */
    public static long getSeconds4LocalDateTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取两个 LocalDateTime 相距多少
     *
     * @param startTime LocalDateTime
     * @param endTime   LocalDateTime
     * @param unit      单位(年&月&日&时&分&秒&毫秒) ChronoUnit.*
     * @return long
     */
    public static long gaps4LocalDateTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit unit) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (unit == ChronoUnit.YEARS) return period.getYears();
        if (unit == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return unit.between(startTime, endTime);
    }

    /**
     * 获取两个 LocalDate 相距多少
     *
     * @param startDate LocalDate
     * @param endDate   LocalDate
     * @param unit      单位(年&月&日) ChronoUnit.*
     * @return long
     */
    public static long gaps4LocalDate(LocalDate startDate, LocalDate endDate, ChronoUnit unit) {
        Period period = Period.between(startDate, endDate);
        if (unit == ChronoUnit.YEARS) return period.getYears();
        if (unit == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return startDate.until(endDate, unit);
    }

    /**
     * LocalDateTime 加上一个数，根据unit不同加不同值
     *
     * @param time   LocalDateTime
     * @param number LocalDateTime
     * @param unit   单位(年&月&日&时&分&秒&毫秒) ChronoUnit.*
     * @return LocalDateTime
     */
    public static LocalDateTime plus4LocalDateTime(LocalDateTime time, long number, ChronoUnit unit) {
        return time.plus(number, unit);
    }

    /**
     * LocalDate 加上一个数，根据unit不同加不同值
     *
     * @param time   LocalDate
     * @param number LocalDate
     * @param unit   单位(年&月&日) ChronoUnit.*
     * @return LocalDate
     */
    public static LocalDate plus4LocalDate(LocalDate time, long number, ChronoUnit unit) {
        return time.plus(number, unit);
    }

    /**
     * LocalDateTime 减去一个数，根据unit不同减不同值
     *
     * @param time   LocalDateTime
     * @param number long
     * @param unit   单位(年&月&日&时&分&秒&毫秒) ChronoUnit.*
     * @return LocalDateTime
     */
    public static LocalDateTime minus4LocalDateTime(LocalDateTime time, long number, TemporalUnit unit) {
        return time.minus(number, unit);
    }

    /**
     * LocalDate 减去一个数，根据unit不同减不同值
     *
     * @param time   LocalDate
     * @param number long
     * @param unit   单位(年&月&日) ChronoUnit.*
     * @return LocalDate
     */
    public static LocalDate minus4LocalDate(LocalDate time, long number, ChronoUnit unit) {
        return time.minus(number, unit);
    }

    /**
     * 根据LocalDateTime 获取一天的开始时间：2019-11-13T00:00
     *
     * @param time LocalDateTime
     * @return LocalDateTime
     */
    public static LocalDateTime getDayStart4LocalDateTime(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 根据LocalDateTime 获取一天的结束时间：2019-11-13T23:59:59.999999999
     *
     * @param time LocalDateTime
     * @return LocalDateTime
     */
    public static LocalDateTime getDayEnd4LocalDateTime(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    // TODO ########################################## 获取相关 结束 ##########################################


    // TODO ########################################## 转换相关 开始 ##########################################

    /**
     * Date 转换为 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date 转换为 LocalDate
     *
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * long类型的timestamp 转为 LocalDateTime
     *
     * @param timestamp long
     * @return LocalDateTime
     */
    public static LocalDateTime timestamp2LocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * LocalDateTime 转为 long类型的timestamp
     *
     * @param localDateTime LocalDateTime
     * @return long
     */
    public static long localDateTime2Timestamp(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * LocalDateTime 转换为 Date
     *
     * @param time LocalDateTime
     * @return Date
     */
    public static Date localDateTime2Date(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 当前 LocalDateTime 转为自定义时间格式的 字符串
     *
     * @param pattern String
     * @return String
     */
    public static String formatNowLocalDateTime2String(String pattern) {
        return formatLocalDateTime2String(LocalDateTime.now(), pattern);
    }

    /**
     * 指定 LocalDateTime 转为 自定义时间格式的 字符串
     *
     * @param time LocalDateTime
     * @param pattern String
     * @return String
     */
    public static String formatLocalDateTime2String(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 指定 LocalDate 转为 自定义时间格式的 字符串
     *
     * @param localDate LocalDate
     * @param pattern String
     * @return String
     */
    public static String formatLocalDate2String(LocalDate localDate, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(fmt);
    }

    /**
     * 某时间字符串 转为 自定义时间格式的 LocalDateTime
     *
     * @param timeString String
     * @param pattern String
     * @return LocalDateTime
     */
    public static LocalDateTime string2LocalDateTime(String timeString, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(timeString, df);
    }

    /**
     * 某时间字符串 转为 自定义时间格式的 LocalDate
     *
     * @param timeString String
     * @param pattern String
     * @return LocalDate
     */
    public static LocalDate string2LocalDate(String timeString, String pattern) {
        return LocalDate.parse(timeString, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDateTime 将工作日英文转成自定义中文
     *
     * @param localDateTime LocalDateTime
     * @return String
     */
    public static String getWeekAlias4LocalDateTime(LocalDateTime localDateTime) {
        return getWeekAlias(String.valueOf(localDateTime.getDayOfWeek()));
    }

    /**
     * LocalDate 将工作日英文转成自定义中文
     *
     * @param localDate LocalDate
     * @return String
     */
    public static String getWeekAlias4localDate(LocalDate localDate) {
        return getWeekAlias(String.valueOf(localDate.getDayOfWeek()));
    }

    // TODO ########################################## 转换相关 结束 ##########################################


    // TODO ########################################## 校验相关 开始 ##########################################


    // TODO ########################################## 校验相关 结束 ##########################################


    /**
     * 将工作日英文转成自定义中文
     *
     * @param week 星期英文
     * @return 星期中文
     */
    protected static String getWeekAlias(String week) {
        switch (String.valueOf(week)) {
            case "SUNDAY":
                week = "星期天";
                break;
            case "MONDAY":
                week = "星期一";
                break;
            case "TUESDAY":
                week = "星期二";
                break;
            case "WEDNESDAY":
                week = "星期三";
                break;
            case "THURSDAY":
                week = "星期四";
                break;
            case "FRIDAY":
                week = "星期五";
                break;
            case "SATURDAY":
                week = "星期六";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + week);
        }
        return week;
    }

}
