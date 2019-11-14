//package com.selfimpr.Date;
//
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//
////@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
////@WebAppConfiguration
//public class LocaDateUtilsTest {
//
//    @Test
//    public void twoTimeGaps() {
//        // 获取两个日期的差距
//        LocalDate start = LocalDate.of(2018, 10, 13);
//        LocalDate end = LocalDate.of(2019, 11, 13);
//        System.out.println("年:" + LocalDateUtils.twoTimeGaps(start, end, ChronoUnit.YEARS));
//        System.out.println("月:" + LocalDateUtils.twoTimeGaps(start, end, ChronoUnit.MONTHS));
//        System.out.println("日:" + LocalDateUtils.twoTimeGaps(start, end, ChronoUnit.DAYS));
//    }
//
//    @Test
//    public void plus() {
//        // 增加10天
//        System.out.println(LocalDateUtils.localDate2String(LocalDateUtils
//                .plus(LocalDate.now(), 10, ChronoUnit.DAYS), LocalDateUtils.pattern1));
//        // 增加10年
//        System.out.println(LocalDateUtils.localDate2String(LocalDateUtils
//                .plus(LocalDate.now(), 10, ChronoUnit.YEARS), LocalDateUtils.pattern1));
//
//    }
//}
