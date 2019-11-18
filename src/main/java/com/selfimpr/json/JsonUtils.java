package com.selfimpr.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Json 相关工具类
 */
public class JsonUtils {

    /**
     * 获取resources下的bank.json文件
     *
     * @return
     * @throws IOException
     */
    public static String getBankJson() throws IOException {
//        JsonUtils.class.getResource("/").getPath()+"config.json";  // 本地是可以的，但是打包成jar包之后再服务器中打印出的路径是一串数字。
//        String configPath = ResourceUtils.getFile("classpath:config.json").getAbsolutePath(); // 也是不行

        // 改为使用getResourceAsStream()方法可行
        InputStream is = JsonUtils.class.getResourceAsStream("/bank.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String temp;
        String configContentStr = "";
        try {
            while ((temp = br.readLine()) != null) {
                configContentStr = configContentStr + temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
            br.close();
        }
        return configContentStr;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getBankJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
