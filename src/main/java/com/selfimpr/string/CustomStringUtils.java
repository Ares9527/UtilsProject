package com.selfimpr.string;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.selfimpr.base.BaseTools;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * String 相关操作工具类
 */
public class CustomStringUtils {

    /**
     * Json字符串 转 Map —— 【native】
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2MapByNative(String json) {
        // 根据逗号截取字符串数组
        String[] json1 = json.split(",");
        // 创建Map对象
        Map<String, Object> map = new HashMap<>();
        // 循环加入map集合
        for (int i = 0; i < json1.length; i++) {
            // 根据":"截取字符串数组
            String[] json2 = json1[i].split(":");
            // json2[0]为KEY,json2[1]为值
            map.put(json2[0], json2[1]);
        }
        return map;
    }

    /**
     * Json字符串 转为 Map ——【fastjson】
     * JSONObject extends JSON implements Map<String, Object>
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2MapByFastJson(String json) {
        return JSONObject.parseObject(json);
    }

    /**
     * Json字符串 转为 Map<String, Object> ——【ObjectMapper】
     * 弃用case：使用 json2CustomFormatByObjectMapper 定制化即可
     *
     * @param json
     * @return
     */
    @Deprecated
    public static Map<String, Object> json2MapByObjectMapper(String json) {
        try {
            return BaseTools.objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Json字符串 转为 List<Map> ——【fastjson】
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> json2MapListByFastJson(String json) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(json);
        for (Object object : jsonArray) {
            mapList.add((Map<String, Object>) JSONObject.toJSON(object));
        }
        return mapList;
    }

    /**
     * Json字符串 转为 List<Map<String, Object>> ——【ObjectMapper】
     * 弃用case：使用 json2CustomFormatByObjectMapper 定制化即可
     *
     * @param json
     * @return
     */
    @Deprecated
    public static List<Map<String, Object>> json2MapListByObjectMapper(String json) {
        try {
            return BaseTools.objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Json字符串 转为 Entity ——【fastjson】
     *
     * @param json
     * @return
     */
    public static <T> T json2EntityByFastJson(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * Json字符串 转为 Entity ——【ObjectMapper】
     *
     * @param json
     * @return
     */
//    @Deprecated 保留，直接传入Entity.class速度更快
    public static <T> T json2EntityByObjectMapper(String json, Class<T> cls) {
        try {
            return BaseTools.objectMapper.readValue(json, cls);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Json字符串 转为 List<Entity> ——【fastjson】
     *
     * @param json
     * @return
     */
    public static <T> List<T> json2EntityListByFastJson(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    /**
     * Json字符串 转为 定制化返回数据结构，例List<bean> or other ——【ObjectMapper】
     * <p>
     * Map<String,String> ext = JSON.parseObject(extStr,new TypeReference<Map<String, String>>(){});
     * new TypeReference<List<Map<String, Object>>>(){}
     * new TypeReference<Entity>() {}
     */
    public static <T> T json2CustomFormatByObjectMapper(String json, TypeReference<T> typeReference) {
        try {
            return BaseTools.objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

}
