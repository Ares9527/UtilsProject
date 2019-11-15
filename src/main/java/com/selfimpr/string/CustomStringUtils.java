package com.selfimpr.string;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * String 相关操作工具类
 */
public class CustomStringUtils {

    /**
     * String 转 Map —— 【native】
     *
     * @param str
     * @return
     */
    public static Map<String, Object> string2MapByNative(String str) {
        // 根据逗号截取字符串数组
        String[] str1 = str.split(",");
        // 创建Map对象
        Map<String, Object> map = new HashMap<>();
        // 循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            // 根据":"截取字符串数组
            String[] str2 = str1[i].split(":");
            // str2[0]为KEY,str2[1]为值
            map.put(str2[0], str2[1]);
        }
        return map;
    }

    /**
     * String 转为 Map ——【fastjson】
     * JSONObject extends JSON implements Map<String, Object>
     *
     * @param str
     * @return
     */
    public static Map<String, Object> string2MapByFastJson(String str) {
        return JSONObject.parseObject(str);
    }

}
