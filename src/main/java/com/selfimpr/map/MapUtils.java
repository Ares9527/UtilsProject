package com.selfimpr.map;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map工具
 */
public class MapUtils {

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

    /**
     * 实体类 转 Map ——【反射】
     *
     * @param object
     * @return
     */
    public static Map<String, Object> entity2MapByReflect(Object object) {
        Map<String, Object> map = new HashMap(8);
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 实体类 转 Map ——【fastjson】
     *
     * @param object
     * @return
     */
    public static Map<String, Object> entity2MapByFastJson(Object object) {
        return (Map<String, Object>) JSONObject.toJSON(object);
    }

    /**
     * List<实体类> 转 List<Map> ——【fastjson】
     *
     * @param objectList
     * @return
     */
    public static <T> List<Map<String, T>> entityList2MapListByFastJson(List<T> objectList) {
        List<Map<String, T>> mapList = new ArrayList<>();
        for (Object object : objectList) {
            mapList.add((Map<String, T>) entity2MapByFastJson(object));
        }
        return mapList;
    }

    /**
     * Map 转 实体类 ——【反射】
     *
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param cls 需要转化成的实体类
     * @return
     */
    public static <T> T map2EntityByReflect(Map<String, Object> map, Class<T> cls) {
        T t = null;
        try {
            t = cls.newInstance();
            for (Field field : cls.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object != null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Map 转 实体类 ——【fastjson】
     *
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param cls 需要转化成的实体类
     * @return
     */
    public static <T> T map2EntityByFastJson(Map<String, Object> map, Class<T> cls) {
        return JSONObject.parseObject(JSONObject.toJSONString(map), cls);
    }

    /**
     * List<Map> 转 List<实体类> ——【fastjson】
     *
     * @param mapList 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param cls     需要转化成的实体类
     * @return
     */
    public static <T> List<T> mapList2EntityListByFastJson(List<Map<String, Object>> mapList, Class<T> cls) {
        List<T> tList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            tList.add(map2EntityByFastJson(map, cls));
        }
        return tList;
    }
}
