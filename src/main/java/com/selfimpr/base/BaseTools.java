package com.selfimpr.base;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.text.SimpleDateFormat;

/**
 * 通用工具合集
 */
public class BaseTools {

    /**
     * 自定义日期格式pattern
     * <p>
     * pattern格式：yyyy-MM-dd HH:mm:ss
     * 1.M m是为了区分“月”与“分”，M:月，m:分
     * 2.H h是为了区分12小时制与24小时制,H是24小时制,h是12小时制。
     */
    public static final String pattern1 = "yyyy-MM-dd HH:mm:ss";
    public static final String pattern2 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String pattern3 = "yyyy/MM/dd HH:mm:ss";
    public static final String pattern4 = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * Jackson 高性能的JSON处理 ObjectMapper
     */
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            // 以下可以自定义ObjectMapper的一些属性
            objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
            // 设置日期格式化(24小时)
            objectMapper.setDateFormat(new SimpleDateFormat(pattern1));
            // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性，若不忽略会报错：Unrecognized field "xxx"
//            objectMapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false); 已过时
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 允许出现特殊字符和转义符
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            // 允许出现单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            // Inclusion.ALWAYS 创建输出全部属性到Json字符串
            // Include.NON_DEFAULT 属性为默认值不序列化
            // Inclusion.NON_DEFAULT 属性为：空（""），或者为NULL，都不序列化
            // Include.NON_NULL 属性为NULL，不序列化
            // tips: 注意：只对VO起作用；对Map List不起作用，序列化Map List时，属性为null依旧会被序列化。
            //              这和实体上单独增加注解功能一致：
            //                  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL) —— 已过时
            //                  @JsonInclude(JsonInclude.Include.NON_NULL)
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            // 字段保留，将null值转为""
//            objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()
//            {
//                @Override
//                public void serialize(Object o, JsonGenerator jsonGenerator,
//                                      SerializerProvider serializerProvider)
//                        throws IOException
//                {
//                    jsonGenerator.writeString("");
//                }
//            });
            // 设置可以序列化集合
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
