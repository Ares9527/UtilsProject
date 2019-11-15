package com.selfimpr.file;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties配置文件 操作工具类
 *
 * Java 开发中，需要将一些易变的配置参数放置再 XML 配置文件或者 properties 配置文件中。
 * 然而 XML 配置文件需要通过 DOM 或 SAX 方式解析，而读取 properties 配置文件就比较容易。
 */
public class PropertiesUtils {

    /**
     * 读取Properties配置文件
     */
    public void readProperties() {
        Map<String, String> activityMsgMaps = new HashMap<>();
        //1.解析配置文件
        Properties properties = new Properties();
        try {
            // 乱码处理：properties文件默认的用的GBK编码的，设置成GBK仍乱码，可能更IDEA设置的编码有关，这里设置为UTF-8
            // 法一 读取为null
//            InputStream in = this.getClass().getClassLoader().getResourceAsStream("customMsg1.properties");
//            properties.load(new InputStreamReader(in, "UTF-8"));

            properties = PropertiesLoaderUtils.loadAllProperties("customMsg1.properties");

            activityMsgMaps.put("msg1", new String(properties.getProperty("msg1").getBytes("iso-8859-1"), "UTF-8"));
            activityMsgMaps.put("msg2", new String(properties.getProperty("msg2").getBytes("iso-8859-1"), "UTF-8"));

            System.out.println(activityMsgMaps);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
