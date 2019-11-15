package com.selfimpr.file;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties配置文件 操作工具类
 * <p>
 * Java 开发中，需要将一些易变的配置参数放置再 XML 配置文件或者 properties 配置文件中。
 * 然而 XML 配置文件需要通过 DOM 或 SAX 方式解析，而读取 properties 配置文件就比较容易。
 */
public class PropertiesUtils {

    private static Properties properties = new Properties();

    /**
     * 读取Properties配置文件
     */
    public void readProperties(String path) {
        Map<String, String> map = new HashMap<>(8);
        //1.解析配置文件
        try {
            // 乱码处理：properties文件默认的用的GBK编码的，设置成GBK仍乱码，可能更IDEA设置的编码有关，这里设置为UTF-8

            // 法一写法一 有些问题，编码
            // 注意：path ——》 "customMsg1.properties"
//            InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
//            properties.load(new InputStreamReader(in, "iso-8859-1"));

            // 法一写法二 ok
            // 注意：path ——》 "/customMsg1.properties"
//            properties.load(getClass().getResourceAsStream(path));

            // 法二 ok
            // 注意：path ——》 "customMsg1.properties"
            properties = PropertiesLoaderUtils.loadAllProperties(path);
            map.put("msg1", new String(properties.getProperty("msg1").getBytes("iso-8859-1"), "UTF-8"));
            map.put("msg2", new String(properties.getProperty("msg2").getBytes("iso-8859-1"), "UTF-8"));
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
