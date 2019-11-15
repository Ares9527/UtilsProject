package com.selfimpr.encode;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Calendar;

/**
 * 【对称加密】3DES 加密工具类
 *      DES向AES过渡的加密算法，它使用3条56位的密钥对数据进行三次加密。3DES更为安全。
 */
public class ThreeDesEncryptUtil {
    // 密钥
    private final static String SECRET_KEY = "selfimpr!@#12345678901234567890";

    // 向量
    private final static String IV = "01234567";

    // 加解密统一使用的编码方式
    private final static String ENCODING = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(SECRET_KEY.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
        return EncodeUtils.encodeBase64(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(SECRET_KEY.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(EncodeUtils.decodeBase64(encryptText));

        return new String(decryptData, ENCODING);
    }

    public static void main(String[] args) {
        try {
            //String orgStr = "8e4dcb3ecfab4274ae7bfc248a6c29d8";
            String orgStr = "1234abcd中文@!#全角《？";
            String encryptStr = "";
            String decryptStr = "";

            long startMills = Calendar.getInstance().getTimeInMillis();
            //for(int i=0;i<1000;++i){
            encryptStr = ThreeDesEncryptUtil.encode(orgStr);
            decryptStr = ThreeDesEncryptUtil.decode(encryptStr);
            //}

            System.out.println("耗时：" + (Calendar.getInstance().getTimeInMillis() - startMills));
            System.out.println("加密前的字符：" + orgStr);
            System.out.println("加密前的length:" + orgStr.length());
            //System.out.println("加密前的字符串HEX:"+DesCryptUtil.getInstance().byteArr2HexStr(orgStr.getBytes()));
            System.out.println("加密后的字符：" + encryptStr);
            System.out.println("加密后的length:" + encryptStr.length());
            System.out.println("解密后的字符：" + decryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

