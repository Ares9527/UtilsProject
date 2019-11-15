package com.selfimpr.encode;

import com.selfimpr.file.PropertiesUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

@SpringBootTest
public class EncodeTest {

    @Test
    public void hex() {
        byte[] saltByte = EncodeUtils.buildEntryptSalt();
        String pwd = EncodeUtils.buildEntryptPassword("123456", saltByte);
        String salt = EncodeUtils.encodeHex(saltByte);
        System.out.println("salt: " + salt);
        System.out.println("pwd: " + pwd);

        // 校验时：输入密码后，根据password 和 用户的salt，再次进行加密获取到的结果和用户的密码字段一致即可
        System.out.println(EncodeUtils.buildEntryptPassword("123456", EncodeUtils.decodeHex(salt)));
    }

    @Test
    public void base64() {
        String passowrd = "12345";
        String encodeBase64 = EncodeUtils.encodeBase64(passowrd.getBytes());
        System.out.println("encodeBase64: " + encodeBase64);

        String encodeUrlSafeBase64 = EncodeUtils.encodeUrlSafeBase64(passowrd.getBytes());
        System.out.println("encodeUrlSafeBase64: " + encodeUrlSafeBase64);

        byte[] bytes = EncodeUtils.decodeBase64(encodeBase64);
        try {
            String s = new String((bytes), "UTF-8");
            System.out.println(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readProperties() {
        PropertiesUtils propertiesUtils = new PropertiesUtils();
        propertiesUtils.readProperties();
    }

}
