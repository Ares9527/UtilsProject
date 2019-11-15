package com.selfimpr.date;

import org.apache.commons.lang3.StringUtils;

import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

/**
 * 基于ZonedDateTime封装的 相关操作工具类
 * <p>
 * 描述：
 * 1，LocalDateTime总是表示本地日期和时间，要表示一个带时区的日期和时间，我们就需要ZonedDateTime。(ZonedDateTime = LocalDateTime + ZoneId)
 * 可以简单地把ZonedDateTime理解成LocalDateTime加ZoneId。
 * 2，ZoneId是java.time引入的新的时区类，注意和旧的java.util.TimeZone区别。
 * 3，ZonedDateTime是带时区的日期和时间，可用于时区转换；ZonedDateTime和LocalDateTime可以相互转换。
 */
public class ZonedDateTimeUtils {

    public static final String pattern1 = "yyyy-MM-dd HH:mm:ss";

    // TODO ########################################## 获取相关 开始 ##########################################

    /**
     * 用指定时区获取当前时间
     *
     * @param zoneString 时区，例：Asia/Shanghai
     * @return ZonedDateTime
     */
    public static ZonedDateTime getTime4Zone(String zoneString) {
        return ZonedDateTime.now(ZoneId.of(zoneString));
    }

    /**
     * 转换为系统默认时区
     *
     * @param time ZonedDateTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime convertZone(ZonedDateTime time) {
        return time.withZoneSameInstant(ZoneId.systemDefault());
    }

    /**
     * 转换时区
     *
     * @param time       ZonedDateTime
     * @param zoneString 时区，例：Asia/Shanghai
     * @return ZonedDateTime
     */
    public static ZonedDateTime convertZone(ZonedDateTime time, String zoneString) {
        return time.withZoneSameInstant(ZoneId.of(zoneString));
    }

    /**
     * 根据时间获取当天最晚时间
     *
     * @param time ZonedDateTime 当天时间
     * @return ZonedDateTime 当天最晚时间
     */
    public static ZonedDateTime getLastMomentInDay(ZonedDateTime time) {
        if (Objects.isNull(time)) {
            return null;
        }
        return time.plusDays(1).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plus(-1L, ChronoUnit.MILLIS);
    }

    /**
     * 根据时间获取当月第一天的起始时间
     *
     * @param time ZonedDateTime 查询时间
     * @return ZonedDateTime 当月第一天起始时间
     */
    public static ZonedDateTime getStartDayInMonth(ZonedDateTime time) {
        // 将时区转为本机时间
        return time.withZoneSameInstant(ZoneId.systemDefault()).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * 根据时间获取当月最后一天的最晚时间
     *
     * @param time 查询时间
     * @return ZonedDateTime 当月第一天起始时间
     */
    public static ZonedDateTime getLastDayInMonth(ZonedDateTime time) {
        return getLastMomentInDay(time.plusMonths(1L).plus(-1L, ChronoUnit.MILLIS));
    }

    /**
     * 获取两个 ZonedDateTime 相距多少, 现在会出现负数，后面看看要不要调整
     *
     * @param startDate
     * @param endDate
     * @param unit
     * @return
     */
    public static long gaps4ZonedDateTime(ZonedDateTime startDate, ZonedDateTime endDate, ChronoUnit unit) {
        // ZonedDateTime 转为 LocalDate 后再比较时间相距
        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (unit == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (unit == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return startDate.until(endDate, unit);
    }

    /**
     * ZonedDateTime裁减
     *
     * @param unit
     */
    public static ZonedDateTime truncatedTo(ChronoUnit unit) {
        return ZonedDateTime.now().truncatedTo(unit);
    }

    /**
     * ZonedDateTime 加上一个数，根据unit不同加不同值
     *
     * @param time   ZonedDateTime
     * @param number ZonedDateTime
     * @param unit   单位(年&月&日&时&分&秒&毫秒) ChronoUnit.*
     * @return ZonedDateTime
     */
    public static ZonedDateTime plus4LocalDateTime(ZonedDateTime time, long number, ChronoUnit unit) {
        return time.plus(number, unit);
    }

    /**
     * 获取当前时间是周几
     *
     * @return 1~7
     */
    public static int getNowWeekInt() {
        return ZonedDateTime.now().getDayOfWeek().getValue();
    }

    /**
     * ZonedDateTime 将工作日英文转成自定义中文
     *
     * @param time ZonedDateTime
     * @return String
     */
    public static String getWeekAlias4ZonedDateTime(ZonedDateTime time) {
        return CustomDateUtils.getWeekAlias(String.valueOf(time.getDayOfWeek()));
    }


    // TODO ########################################## 转换相关 开始 ##########################################

    /**
     * 指定 ZonedDateTime 转为 自定义时间格式的 字符串
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String format(ZonedDateTime time, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(time);
    }

    /**
     * 日期字符串 转为 ZonedDateTime 指定格式日期 默认Asia/Shanghai
     *
     * @param timeString String
     * @return ZonedDateTime
     */
    public static ZonedDateTime dateStr2ZonedDateTime(String timeString) {
        if (StringUtils.isEmpty(timeString)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern1).withZone(ZoneId.of("Asia/Shanghai"));
        return ZonedDateTime.parse(timeString, formatter);
    }

    /**
     * 根据日期字符串 和 DateTimeFormatter格式 转为 ZonedDateTime
     *
     * @param timeString String
     * @return ZonedDateTime
     */
    public static ZonedDateTime dateStr2ZonedDateTime(String timeString, String formatterString) {
        if (StringUtils.isEmpty(timeString)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern1).withZone(ZoneId.of(formatterString));
        return ZonedDateTime.parse(timeString, formatter);
    }

    /**
     * 将ZonedDateTime转成Date格式，自动转换时区
     *
     * @param time 时间
     * @return 时间
     */
    public static Date convert2Date(ZonedDateTime time) {
        return Date.from(time.toInstant());
    }

}
