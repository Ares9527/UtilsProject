//package com.selfimpr.Date;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//
////@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
////@WebAppConfiguration
//public class LocaDateTimeUtilsTest {
//
//    @Test
//    public void formatNow() {
//        System.out.println(LocalDateTimeUtils.formatNow2String(LocalDateTimeUtils.pattern1));
//    }
//
//    @Test
//    public void twoTimeGaps() {
//        // 获取两个日期的差距
//        LocalDateTime start = LocalDateTime.of(2018, 10, 13, 13, 15, 35, 654);
//        LocalDateTime end = LocalDateTime.of(2019, 11, 13, 16, 00, 14, 152);
//        System.out.println("年:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.YEARS));
//        System.out.println("月:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.MONTHS));
//        System.out.println("日:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.DAYS));
//        System.out.println("半日:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.HALF_DAYS));
//        System.out.println("小时:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.HOURS));
//        System.out.println("分钟:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.MINUTES));
//        System.out.println("秒:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.SECONDS));
//        System.out.println("毫秒:" + LocalDateTimeUtils.twoTimeGaps(start, end, ChronoUnit.MILLIS));
//    }
//
//    @Test
//    public void plus() {
//        // 增加二十分钟
//        System.out.println(LocalDateTimeUtils.formatNow2String(LocalDateTimeUtils
//                .plus(LocalDateTime.now(), 20, ChronoUnit.MINUTES), LocalDateTimeUtils.pattern1));
//        // 增加两年
//        System.out.println(LocalDateTimeUtils.formatNow2String(LocalDateTimeUtils.plus(LocalDateTime.now(),
//                2,
//                ChronoUnit.YEARS), LocalDateTimeUtils.pattern1));
//    }
//
//    @Test
//    public void minus() {
//        // 减少三十分钟
//        System.out.println(LocalDateTimeUtils.formatNow2String(LocalDateTimeUtils
//                .minus(LocalDateTime.now(), 30, ChronoUnit.MINUTES), LocalDateTimeUtils.pattern1));
//        // 增加两年
//    }
//
//    @Test
//    public void getDayStart() {
//        System.out.println(LocalDateTimeUtils.getDayStart(LocalDateTime.now()));
//    }
//
//    @Test
//    public void getDayEnd() {
//        System.out.println(LocalDateTimeUtils.getDayEnd(LocalDateTime.now()));
//    }
//
//}
