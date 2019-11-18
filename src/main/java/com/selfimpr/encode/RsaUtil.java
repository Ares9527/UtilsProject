package com.selfimpr.encode;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 【对称加密】DES 加密，解密工具
 * RSA加密是一种非对称加密。可以在不直接传递密钥的情况下，完成解密。这能够确保信息的安全性，避免了直接传递密钥所造成的被破解的风险。
 * 是由一对密钥来进行加解密的过程，分别称为公钥和私钥。两者之间有数学相关，该加密算法的原理就是对一极大整数做因数分解的困难性来保证安全性。
 * 通常个人保存私钥，公钥是公开的（可能同时a多人持有）。
 * <p>
 * PS: RSA加密对明文的长度有所限制，规定需加密的明文最大长度=密钥长度-11（单位是字节，即byte），所以在加密和解密的过程中需要分块进行。
 * 而密钥默认是1024位，即1024位/8位-11=128-11=117字节。所以默认加密前的明文最大长度117字节，解密密文最大长度为128字。
 * 那么为啥两者相差11字节呢？是因为RSA加密使用到了填充模式（padding），即内容不足117字节时会自动填满，用到填充模式自然会占用一定的字节，
 * 而且这部分字节也是参与加密的。
 * 密钥长度可自行调整，当然非对称加密随着密钥变长，安全性上升的同时性能也会有所下降。
 */
public class RsaUtil {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 公钥存放地址
     *
     */
    private static String publicKeyPath;

    /**
     * 私钥存放地址
     */
    private static String privateKeyPath;

    /**
     * 公钥
     */
    private static PublicKey publicKey = null;

    /**
     * 私钥
     */
    private static PrivateKey privateKey = null;

    /**
     * 用于转换的密钥工厂
     */
    private static KeyFactory keyFactory = null;

    public static void setPublicKeyPath(String path) {
        publicKeyPath = path;
        try {
            // 法一：公钥对象和私钥 key对象 为持久化文件
//            publicKey = (PublicKey) getKeyByPath(publicKeyPath);

            // 法二：公钥对象和私钥 字符串 为持久化文件
            byte[] publicDecodedKey = getDecodedKey(publicKeyPath);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecodedKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static void setPrivateKeyPath(String path) {
        privateKeyPath = path;
        try {
            // 法一：公钥对象和私钥 key对象 为持久化文件
//            privateKey = (PrivateKey) getKeyByPath(privateKeyPath);

            // 法二：公钥对象和私钥 字符串 为持久化文件
            byte[] privateDecodedKey = getDecodedKey(privateKeyPath);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateDecodedKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建密钥对
     */
    public static void createKeyPair() throws Exception {
        // 使用RSA算法获得密钥对生成器对象keyPairGenerator
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        // 设置密钥长度为1024，可自行调整，当然非对称加密随着密钥变长，安全性上升的同时性能也会有所下降
        generator.initialize(1024);
        // 生成密钥对
        KeyPair keyPair = generator.generateKeyPair();
        // 获取公钥
        PublicKey publicKey = keyPair.getPublic();
        // 获取私钥
        PrivateKey privateKey = keyPair.getPrivate();
        // TODO 存法1：保存公钥对象和私钥 对象 为持久化文件
//        ObjectOutputStream oos1 = null;
//        ObjectOutputStream oos2 = null;
//        try {
//            oos1 = new ObjectOutputStream(new FileOutputStream(publicKeyPath));
//            oos2 = new ObjectOutputStream(new FileOutputStream(privateKeyPath));
//            oos1.writeObject(publicKey);
//            oos2.writeObject(privateKey);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            oos1.close();
//            oos2.close();
//        }

        // TODO 存法2：保存公钥对象和私钥 字符串 为持久化文件
        // 以base64加密
        String privateKeyBase64String = EncodeUtils.encodeBase64String(publicKey.getEncoded());
        String publicKeyBase64String = EncodeUtils.encodeBase64String(privateKey.getEncoded());
        FileWriter publicFw = null;
        FileWriter privateFw = null;
        BufferedWriter pubbw = null;
        BufferedWriter pribw = null;
        try {
            publicFw = new FileWriter(publicKeyPath);
            privateFw = new FileWriter(privateKeyPath);
            pubbw = new BufferedWriter(publicFw);
            pribw = new BufferedWriter(privateFw);
            pubbw.write(privateKeyBase64String);
            pribw.write(publicKeyBase64String);
            pubbw.flush();
            pribw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            pubbw.close();
            publicFw.close();
            pribw.close();
            privateFw.close();
        }
    }

    /**
     * 读取持久化的公钥对象
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Key getKeyByPath(String path) throws Exception {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            return (Key) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ois.close();
        }
    }

    /**
     * 获取公钥/私钥 decodedKey
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] getDecodedKey(String path) throws IOException {
        FileReader fileReader;
        BufferedReader br = null;
        try {
            fileReader = new FileReader(path);
            br = new BufferedReader(fileReader);
            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            // base6解密
            return EncodeUtils.decodeBase64(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取秘钥失败");
        } finally {
            br.close();
        }
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @return
     */
    public static String encrypt(String data) throws Exception {
        // Cipher类为加密和解密提供密码功能,它构成了 Java Cryptographic Extension (JCE) 框架的核心
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return EncodeUtils.encodeBase64String(encryptedData);
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @return
     */
    public static String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = EncodeUtils.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @return 签名
     */
    public static String sign(String data) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param sign    签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static void main(String[] args) {
        try {
            // TODO 生成密钥对，只需生成一次
            createKeyPair();

            // TODO 生成密钥对后打开
//            String publicKeyString = EncodeUtils.encodeBase64String(publicKey.getEncoded());
//            String privateKeyString = EncodeUtils.encodeBase64String(privateKey.getEncoded());
//            System.out.println("公钥:" + publicKeyString);
//            System.out.println("私钥:" + privateKeyString);
//            // RSA加密
//            String data = "待加密的文字内容";
//            String encryptData = encrypt(data);
//            System.out.println("加密后内容:" + encryptData);
//            // RSA解密
//            String decryptData = decrypt(encryptData);
//            System.out.println("解密后内容:" + decryptData);
//
//            // RSA签名
//            String sign = sign(data);
//            // RSA验签
//            boolean result = verify(data, sign);
//            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}