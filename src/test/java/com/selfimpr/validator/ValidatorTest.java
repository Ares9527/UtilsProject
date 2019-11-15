//package com.selfimpr.validator;
//
//import org.apache.commons.lang3.StringUtils;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class ValidatorTest {
//
//    @Test
//    public void test() {
//        String tt = " ";
//        if (StringUtils.isEmpty(tt)) {
//            System.out.println(123);
//        }
//    }
//
//    @Test
//    public void checkIdCard() {
//        System.out.println(IdCardUtils.convertIdCardBy15Bit("440421951016815"));
//    }
//
//    @Test
//    public void getAgeByIdCard() {
//        System.out.println(IdCardUtils.getAgeByIdCard("440421951016815"));
//    }
//
//    @Test
//    public void getLunarBirthdayByIdCard() {
//        System.out.println(IdCardUtils.getLunarBirthdayByIdCard("440421951016815"));
//    }
//
//    @Test
//    public void t1() {
//        System.out.println("星座为：" + IdCardUtils.getConstellation("440421951016815"));
//        System.out.println("属相为:" + IdCardUtils.getYear("440421951016815"));
//    }
//
//}
