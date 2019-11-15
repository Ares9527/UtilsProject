package com.selfimpr.bean;

import com.alibaba.fastjson.JSONObject;
import com.selfimpr.base.BaseTools;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Bean 相关操作工具类
 */
public class CustomBeanUtils {

    // TODO #################### *** Bean Copy Relevant Start *** ####################

    /**
     * 由于BeanCopier.create方法：
     * create对象过程：产生sourceClass-》TargetClass的【拷贝代理类】，放入jvm中，所以创建的代理类的时候比较耗时。
     * 最好保证这个对象的单例模式或其他优化方式。
     * 这里将beancopier做成静态变量，仅第一次触发create。
     */
    public static Map<String, BeanCopier> beanCopierMap = new HashMap<>();

    // TODO *** org.springframework.beans.BeanUtils ***

    /**
     * 通过org.springframework.beans.BeanUtils 复制对象
     *
     * @param src
     * @param target
     */
    public static void copyPropertiesByBeanUtils(Object src, Object target) {
        BeanUtils.copyProperties(src, target);
    }

    /**
     * 通过org.springframework.beans.BeanUtils 复制对象的 非空字段
     *
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNullValue(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 获取对象为空的字段
     *
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object beanWrapperValue = wrapper.getPropertyValue(pd.getName());
            if (ObjectUtils.isEmpty(beanWrapperValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    // TODO *** org.springframework.cglib.beans.BeanCopier ***

    /**
     * BeanCopier支持两种方式：
     *  一种是不使用Converter的方式，仅对两个bean间属性名和类型完全相同的变量进行拷贝；
     *  另一种则引入Converter，可以对某些特定属性值进行特殊操作。
     */
    // 不使用Converter的例子1
//    public void testSimple() {
//        // 动态生成用于复制的类,false为不使用Converter类
//        BeanCopier copier = BeanCopier.create(MA.class, MA.class, false);
//        MA source = new MA();
//        source.setIntP(42);
//        MA target = new MA();
//        // 执行source到target的属性复制
//        copier.copy(source, target, null);
//        assertTrue(target.getIntP() == 42);
//    }
    // 使用Converter的例子1
//    public void testConvert() {
//        // 动态生成用于复制的类,并使用Converter类
//        BeanCopier copier = BeanCopier.create(MA.class, MA.class, true);
//        MA source = new MA();
//        source.setIntP(42);
//        MA target = new MA();
//
//        // 执行source到target的属性复制
//        copier.copy(source, target, new Converter() {
//            /**
//             * @param sourceValue source对象属性值
//             * @param targetClass target对象对应类
//             * @param methodName targetClass里属性对应set方法名,eg.setId
//             * @return
//             */
//            public Object convert(Object sourceValue, Class targetClass, Object methodName) {
//                if (targetClass.equals(Integer.TYPE)) {
//                    return new Integer(((Number)sourceValue).intValue() + 1);
//                }
//                return sourceValue;
//            }
//        });
//        assertTrue(target.getIntP() == 43);
//    }
    // 使用Converter的例子2
//    private PeopleDTO trans2DTO(People people) {
//        PeopleDTO dto = new PeopleDTO();
//        BeanCopier beanCopier = BeanCopier.create(People.class, PeopleDTO.class, true);
//        beanCopier.copy(people, dto, (value, paramType, setMethod) -> {
//            if (value.getClass().isAssignableFrom(ZonedDateTime.class)) {
//                // 将实体的ZonedDateTime类型，转换为String类型，dto以String类型接收
//                return DateUtil.format((ZonedDateTime) value);
//            } else {
//                return value;
//            }
//        });
//        return showDTO;
//    }

    /**
     * 通过org.springframework.cglib.beans.BeanCopier 复制对象
     * BeanCopier支持两种方式：
     * 一种是不使用Converter的方式，仅对两个bean间属性名和类型完全相同的变量进行拷贝；
     * 另一种则引入Converter，可以对某些特定属性值进行特殊操作。
     *
     * @param source 资源类
     * @param target 目标类
     */
    public static void copyPropertiesByBeanCopier(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            // 动态生成用于复制的类,false为不使用Converter类
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }


    // TODO #################### *** Bean transform Start *** ####################

    /**
     * 实体类 转 Map ——【反射】
     *
     * @param object
     * @return
     */
    public static Map<String, Object> entity2MapByReflect(Object object) {
        Map<String, Object> map = new HashMap<>(8);
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
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> Map<String, Object> entity2MapByFastJson(E entity) {
        return (Map<String, Object>) JSONObject.toJSON(entity);
    }

    /**
     * List<实体类> 转 List<Map> ——【fastjson】
     *
     * @param entityList
     * @return
     */
    public static <E> List<Map<String, Object>> entityList2MapListByFastJson(List<E> entityList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Object object : entityList) {
            mapList.add(entity2MapByFastJson(object));
        }
        return mapList;
    }

    /**
     * 实体对象 ---> Json字符串
     *
     * @param entity 实体对象
     * @return
     */
    public static <E> String object2Json(E entity) {
        try {
            return BaseTools.objectMapper.writeValueAsString(entity);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
