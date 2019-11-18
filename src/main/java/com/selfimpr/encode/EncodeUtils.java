package com.selfimpr.encode;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 封装各种格式的编码解码工具类
 * <p>
 * 对称加密：DES、3DES、AES —— 加密和解密使用的是同一个密钥，加解密双方必须使用同一个密钥才能进行正常的沟通
 * 采用单钥密码系统的加密方法，同一个密钥可以同时用作信息的加密和解密，这种加密方法称为对称加密，也称为单密钥加密。
 * 美国数据加密标准（DES）是对称密码算法，就是加密密钥能够从解密密钥中推算出来，反过来也成立。
 * 密钥较短，加密处理简单，加解密速度快，适用于加密大量数据的场合。
 * <p>
 * 非对称加密：RSA —— 需要两个密钥来进行加密和解密，公开密钥（public key，简称公钥）和私有密钥（private key，简称私钥），公钥加密的信息只有私钥才能解开，私钥加密的信息只有公钥才能解开。
 * 非对称加密为数据的加密与解密提供了一个非常安全的方法，它使用了一对密钥，公钥（public key）和私钥（private key）。
 * 私钥只能由一方安全保管，不能外泄，而公钥则可以发给任何请求它的人。非对称加密使用这对密钥中的一个进行加密，而解密则需要另一个密钥。
 * 问题：非对称加密中，究竟是公钥加密还是私钥加密？
 * 对于加密：公钥加密，私钥加密。毕竟公钥可以公开，但是私钥只有你自已知道，你也同样希望只有你自已才能解密
 * 对于签名：私钥加密，公钥解密。好比你的签名只有你自已签的才是真的，别人签的都是假的。
 * <p>
 * 不可逆加密算法：MD5、SHA1
 * SHA1 和 MD5 是搜索散列算法，将任意大小的数据映射到一个较小的、固定长度的唯一值。
 * 加密性强的散列一定是不可逆的，这就意味着通过散列结果，无法推出任何部分的原始信息。
 * 任何输入信息的变化，哪怕仅一位，都将导致散列结果的明显变化，这称之为雪崩效应。
 * 散列还应该是防冲突的，即找不出具有相同散列结果的两条信息。具有这些特性的散列结果就可以用于验证信息是否被修改。
 * MD5 比 SHA1 大约快 33%。
 * <p>
 * Commons-Codec的 hex/base64 编码
 */
public class EncodeUtils {

    private static final int SALT_SIZE = 8;

    public static final int HASH_INTERATIONS = 1024;

    // TODO #################### *** hex Start *** ####################

    /**
     * 设定安全密码的Salt
     */
    public static byte[] buildEntryptSalt() {
        return Digests.generateSalt(SALT_SIZE);
    }

    /**
     * 设定安全的密码，生成随机的salt并经过 1024次 sha-1 hash
     */
    public static String buildEntryptPassword(String password, byte[] salt) {
        byte[] hashPassword = Digests.sha1(vaildPassword(password).getBytes(), salt, HASH_INTERATIONS);
        return encodeHex(hashPassword);
    }

    private static String vaildPassword(String password) {
        return password;
    }

    /**
     * Hex编码
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    // TODO #################### *** base64 Start *** ####################

    /**
     * Base64编码
     *
     * @param binaryData
     * @return byte[]
     */
    public static byte[] encodeBase64(byte[] binaryData) {
        return Base64.encodeBase64(binaryData);
    }

    /**
     * Base64编码
     *
     * @param binaryData
     * @return String
     */
    public static String encodeBase64String(byte[] binaryData) {
        return Base64.encodeBase64String(binaryData);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)
     *
     * @param binaryData
     * @return byte[]
     */
    public static byte[] encodeBase64URLSafe(byte[] binaryData) {
        return Base64.encodeBase64URLSafe(binaryData);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)
     *
     * @param binaryData
     * @return String
     */
    public static String encodeBase64URLSafeString(byte[] binaryData) {
        return Base64.encodeBase64URLSafeString(binaryData);
    }

    /**
     * Base64解码
     *
     * @param input
     * @return byte[]
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * Base64解码
     *
     * @param binaryData
     * @return byte[]
     */
    public static byte[] decodeBase64(byte[] binaryData) {
        return Base64.decodeBase64(binaryData);
    }
}
