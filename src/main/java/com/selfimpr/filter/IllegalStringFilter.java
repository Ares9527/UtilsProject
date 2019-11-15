package com.selfimpr.filter;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 非法字符串过滤
 */
public class IllegalStringFilter {

    private static IllegalStringFilter stringFilter;
    private static String sytFilterString = "";

    public static IllegalStringFilter getInit() {
        if (ObjectUtils.isEmpty(stringFilter)) {
            stringFilter = new IllegalStringFilter();
            getFilterStr();
        }
        return stringFilter;
    }

    /**
     * 读取配置文件，取得需要过滤的字符
     *
     * @return
     */
    private static void getFilterStr() {
        try {
//            XMLConfiguration config = new XMLConfiguration("stringfilter.xml");
            XMLConfiguration config = new XMLConfiguration();
            config.load("stringfilter.xml");
            System.out.println(config.getBasePath());
            String[] filterStrings = config.getStringArray("filter.value");
            StringBuilder sb = new StringBuilder();

            if (filterStrings != null && filterStrings.length > 0) {
                for (String str : filterStrings) {
                    sb.append(str.trim()).append("|");
                }
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sytFilterString = sb.toString();
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    public static String checkFilterString(String content) {
        @SuppressWarnings("unused")
        String filterStr = IllegalStringFilter.getInit().getSytFilterString();
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(filterStr).matcher(content);
//		return matcher.find() ? true : false;
        if (matcher.find()) {
            content = matcher.replaceAll("*");
            System.out.println("检测输出：存在不合法字符串，替换为：" + content);
        }
        return content;
    }

    public static String getSytFilterString() {
        return sytFilterString;
    }

    public static void main(String[] args) {
        String filterString = IllegalStringFilter.checkFilterString("煞笔，我是全能神");
        System.out.println("过滤后： " + filterString);
    }
}