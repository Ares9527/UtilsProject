package com.selfimpr.Date;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class ZonedDateTimeTest {

    @Test
    public void test1() {
        System.out.println(ZonedDateTime.now());
        System.out.println(ZonedDateTime.now().getDayOfWeek());
        System.out.println(ZonedDateTime.now().getDayOfWeek().getValue());

        ZoneId zoneId = ZoneId.of("UTC+1");
        ZonedDateTime dateTime2 = ZonedDateTime.of(2019, 11, 18, 23, 45, 59, 1234, zoneId);
        System.out.println(dateTime2.getDayOfWeek());
        System.out.println(dateTime2.getDayOfWeek().getValue());
    }

    @Test
    public void gaps4ZonedDateTime() {
        ZoneId zoneId = ZoneId.of("UTC+1");
        ZonedDateTime dateTime2 = ZonedDateTime.of(2020, 11, 18, 23, 45, 59, 1234, zoneId);

        System.out.println("年:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.YEARS));
        System.out.println("月:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.MONTHS));
        System.out.println("日:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.DAYS));
        System.out.println("半日:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.HALF_DAYS));
        System.out.println("小时:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.HOURS));
        System.out.println("分钟:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.MINUTES));
        System.out.println("秒:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.SECONDS));
        System.out.println("毫秒:" + ZonedDateTimeUtils.gaps4ZonedDateTime(ZonedDateTime.now(), dateTime2, ChronoUnit.MILLIS));
    }

    @Test
    public void truncatedTo() {
        System.out.println(ZonedDateTime.now());
        System.out.println(ZonedDateTimeUtils.truncatedTo(ChronoUnit.DAYS));
        System.out.println(ZonedDateTimeUtils.truncatedTo(ChronoUnit.HOURS));
        System.out.println(ZonedDateTimeUtils.truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    public void test2() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime);
        System.out.println(ZonedDateTimeUtils.plus4LocalDateTime(zonedDateTime, 1, ChronoUnit.DAYS));
        System.out.println(ZonedDateTimeUtils.plus4LocalDateTime(zonedDateTime, 1, ChronoUnit.MONTHS));
        System.out.println(ZonedDateTimeUtils.plus4LocalDateTime(zonedDateTime, 1, ChronoUnit.YEARS));
    }

    @Test
    public void getWeekAlias4ZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(ZonedDateTimeUtils.getWeekAlias4ZonedDateTime(zonedDateTime));
    }

}