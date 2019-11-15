package com.selfimpr.map;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Map 相关操作工具类
 */
public class CustomMapUtils {

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
