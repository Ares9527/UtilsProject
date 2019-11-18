package com.selfimpr.encode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("encode")
public class EncodeController {

    @Value("${custom.path.rsa.phonePublicKeyPath}")
    private String phonePublicKeyPath;

    @Value("${custom.path.rsa.phonePrivateKeyPath}")
    private String phonePrivateKeyPath;

    /**
     * rsa加解密手机测试
     *
     * @param phone 手机号
     */
    @GetMapping(value = "/rsaTest")
    public void rsaTest(@RequestParam("phone") String phone) {
        synchronized (EncodeController.class) {
            RsaUtil.setPublicKeyPath(phonePublicKeyPath);
            RsaUtil.setPrivateKeyPath(phonePrivateKeyPath);
            // RSA加密
            String encryptData;
            try {
                encryptData = RsaUtil.encrypt(phone);
                System.out.println("加密后内容:" + encryptData);
                // RSA解密
                String decryptData = RsaUtil.decrypt(encryptData);
                System.out.println("解密后内容:" + decryptData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
