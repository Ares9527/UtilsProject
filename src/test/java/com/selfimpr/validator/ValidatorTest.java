package com.selfimpr.validator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValidatorTest {

    @Test
    public void test() {
        String tt = " ";
        if (StringUtils.isEmpty(tt)) {
            System.out.println(123);
        }
    }

    @Test
    public void checkIdCard() {
        System.out.println(IdCardUtils.convertIdCardBy15Bit("440421951016815"));
    }
}
