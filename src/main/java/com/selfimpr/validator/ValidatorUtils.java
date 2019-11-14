package com.selfimpr.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 通用校验相关工具
 */
public class ValidatorUtils {

    /**
     * 待细化
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1+(2|3|4|5|6|7|8|9)\\d{9}$");

    /**
     * 校验字符串是不是 手机号码
     *
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 校验字符串是不是 全是数字
     *
     * @param str
     * @return
     */
    public static boolean isDigital(String str) {
        return str.matches("^[0-9]*$");
    }


}
