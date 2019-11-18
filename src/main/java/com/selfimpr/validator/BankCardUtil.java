package com.selfimpr.validator;

import com.selfimpr.json.JsonUtils;
import com.selfimpr.string.CustomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class BankCardUtil {

    /**
     * 校验银行卡号格式是否正确, 正确返回true，错误返回false
     *
     * @param bankCardNo 待校验银行卡号
     *                   校验过程：
     *                   1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     *                   2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     *                   3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * @return boolean
     */
    public static boolean validateBankCard(String bankCardNo) {
        int min = 15;
        int max = 19;
        char falseBit = 'N';
        if (StringUtils.isBlank(bankCardNo) || bankCardNo.length() < min || bankCardNo.length() > max) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCardNo.substring(0, bankCardNo.length() - 1));
        if (bit == falseBit) {
            return false;
        }
        return bankCardNo.charAt(bankCardNo.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位（又称模10算法）
     *
     * @param nonCheckCodeBankCard 不含校验位的银行卡卡号
     * @return 校验位
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 调用支付宝的api地址 获取银行卡号信息
     *
     * @param cardNo 银行卡卡号
     * @return {"bank":"CMB","validated":true,"cardType":"DC","key":"(卡号)","messages":[],"stat":"ok"}
     * 2017年5月22日 下午4:35:23
     */
    public static String getCardDetail(String cardNo) {
        // 创建HttpClient实例
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=";
        url += cardNo;
        url += "&cardBinCheck=true";
        StringBuilder sb = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取银行名称
     *
     * @param bank 银行代码
     * @return
     */
    public static String getCardName(String bank) {
        try {
            String bankJson = JsonUtils.getBankJson();
            Map<String, Object> stringObjectMap = CustomStringUtils.json2MapByFastJson(bankJson);
            return (String) stringObjectMap.get(bank);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String cardNo = "6226090000000048";
        System.out.println("银行卡号码校验结果： " + validateBankCard(cardNo));

        // {"cardType":"DC","bank":"CMB","key":"6226090000000048","messages":[],"validated":true,"stat":"ok"}
        System.out.println("调用支付宝的api地址 获取银行卡号信息： " + getCardDetail(cardNo));
        // 获取银行图标：https://apimg.alipay.com/combo.png?d=cashier&t=CMB
        // CMB是银行代码，也就是上面获取到的bank，如果需要多种图片，直接 "," 逗号隔开 ，例如： https://apimg.alipay.com/combo.png?d=cashier&t=ABC,CCB
        System.out.println(getCardName("CMB"));
    }

}
