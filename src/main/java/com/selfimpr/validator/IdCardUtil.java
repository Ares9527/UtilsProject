package com.selfimpr.validator;

import cn.hutool.core.lang.Validator;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 身份证合法性校验 & 港澳台湾居民来往内地通行证
 * <p>
 * --15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
 * --18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。最后一位为校验位
 */
public class IdCardUtil {

    private static final int FIFTEEN = 15;

    private static final int EIGHTEEN = 18;

    /**
     * 省、直辖市代码表：
     * 11 : 北京  12 : 天津  13 : 河北    14 : 山西  15 : 内蒙古
     * 21 : 辽宁  22 : 吉林  23 : 黑龙江   31 : 上海  32 : 江苏
     * 33 : 浙江  34 : 安徽  35 : 福建    36 : 江西  37 : 山东
     * 41 : 河南  42 : 湖北  43 : 湖南    44 : 广东  45 : 广西    46 : 海南
     * 50 : 重庆  51 : 四川  52 : 贵州    53 : 云南  54 : 西藏
     * 61 : 陕西  62 : 甘肃  63 : 青海    64 : 宁夏  65 : 新疆
     * 71 : 台湾
     * 81 : 香港  82 : 澳门
     * 91 : 国外
     */
    private static String[] cityCode = {"11", "12", "13", "14", "15", "21", "22", "23",
            "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46",
            "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

    /**
     * 每位加权因子
     */
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 港澳居民来往内地通行证校验正则表达式
     * 以H或者M开头（H代表香港、M代表澳门）且包含10位数字的号码均认为是合法的
     */
    private static final String HONG_KONG_REGEX = "^[H][0-9]{8,10}";

    /**
     * 港澳居民来往内地通行证校验正则表达式
     * 以H或者M开头（H代表香港、M代表澳门）且包含10位数字的号码均认为是合法的
     */
    private static final String MACAU_REGEX = "^[M][0-9]{8,10}";

    /**
     * 台湾居民来往大陆通行证校验正则表达式
     * 只要前八位为数字的号码均认为是合法号码
     */
    private static final String TAI_WAN_REGEX = "[0-9]{8}(.*)";

    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23,
            23, 23, 24, 23, 22};

    private final static String[] constellationArr = new String[]{"摩羯座",
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座"};

    private final static String[] years = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊",
            "猴", "鸡", "狗", "猪"};

    /**
     * 验证所有的身份证的合法性
     *
     * @param idCard 身份证
     * @return 合法返回true，否则返回false
     */
    public static boolean validatedAllIdcard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        if (idCard.length() == FIFTEEN) {
            return validate15IdCard(idCard);
        }
        if (idCard.length() == EIGHTEEN) {
            return validate18IdCard(idCard);
        }
        return false;
    }

    // TODO ################################ 校验15位身份证 ################################

    /**
     * 校验15位身份证
     * 只校验省份和出生年月日
     *
     * @param idCard
     * @return 合法返回true，否则返回false
     */
    private static boolean validate15IdCard(String idCard) {
        if (idCard == null) {
            return false;
        }
        // 非15位为假
        if (idCard.length() != FIFTEEN) {
            return false;
        }
        // 15全部为数字
        if (!ValidatorUtils.isDigital(idCard)) {
            return false;
        }
        String provinceId = idCard.substring(0, 2);
        // 校验省份
        if (!checkProvinceId(provinceId)) {
            return false;
        }
        String birthday = idCard.substring(6, 12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            // 身份证日期错误
            if (!tmpDate.equals(birthday)) {
                return false;
            }
        } catch (ParseException e1) {
            return false;
        }
        return true;
    }


    // TODO ################################ 校验18位身份证 ################################

    /**
     * <p>
     * 判断18位身份证的合法性
     * </p>
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号 码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * </p>
     * <p>
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     * </p>
     * <p>
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     * </p>
     * <p>
     * 2.将这17位数字和系数相乘的结果相加。
     * </p>
     * <p>
     * 3.用加出来和除以11，看余数是多少
     * </p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
     * 2。
     * <p>
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     * </p>
     *
     * @param idCard 身份证号
     * @return 合法返回true，否则返回false
     */
    private static boolean validate18IdCard(String idCard) {
        if (idCard == null) {
            return false;
        }
        // 非18位为假
        if (idCard.length() != EIGHTEEN) {
            return false;
        }
        // 获取前17位
        String idCard17 = idCard.substring(0, 17);
        // 前17位全部为数字
        if (!ValidatorUtils.isDigital(idCard17)) {
            return false;
        }
        String provinceId = idCard.substring(0, 2);
        // 校验省份
        if (!checkProvinceId(provinceId)) {
            return false;
        }
        // 校验出生日期
        String birthday = idCard.substring(6, 14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            // 出生年月日不正确
            if (!tmpDate.equals(birthday)) {
                return false;
            }
        } catch (ParseException e1) {
            return false;
        }
        // 获取第18位
        String idCard18Code = idCard.substring(17, 18);
        char[] c = idCard17.toCharArray();
        int[] bit = convertCharToInt(c);
        int sum17 = getPowerSum(bit);
        // 将和值与11取模得到余数进行校验码判断
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode) {
            return false;
        }
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        return idCard18Code.equalsIgnoreCase(checkCode);
    }

    /**
     * 校验18位身份证 —— hutool
     *
     * @param idCard
     * @return
     */
    private static boolean validate18IdCard2(String idCard) {
        return Validator.isCitizenId(idCard);
    }

    /**
     * 校验省份
     *
     * @param provinceId provinceId
     * @return 合法返回true，否则返回false
     */
    private static boolean checkProvinceId(String provinceId) {
        for (String id : cityCode) {
            if (id.equals(provinceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param c c
     * @return int[]
     * @throws NumberFormatException 1
     */
    private static int[] convertCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit bit
     * @return 值
     */
    private static int getPowerSum(int[] bit) {

        int sum = 0;

        if (power.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17 sum17
     * @return 校验位
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
            default:
                break;
        }
        return checkCode;
    }

    // TODO ################################ 15位 转为 18位 ################################

    /**
     * 将15位的身份证转成18位身份证
     * 18位身份证：去掉最后一位,再去掉年份的前两位,即1981去掉19，即为15位身份证
     *
     * @param idCard
     * @return
     */
    public static String convertIdCardBy15Bit(String idCard) {
        if (idCard == null) {
            return null;
        }
        // 非15位身份证
        if (idCard.length() != FIFTEEN) {
            return null;
        }
        // 15全部为数字
        if (!ValidatorUtils.isDigital(idCard)) {
            return null;
        }

        String provinceId = idCard.substring(0, 2);
        // 校验省份
        if (!checkProvinceId(provinceId)) {
            return null;
        }

        String birthday = idCard.substring(6, 12);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

        Date birthdate;
        try {
            birthdate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthdate);
            // 身份证日期错误
            if (!tmpDate.equals(birthday)) {
                return null;
            }
        } catch (ParseException e1) {
            return null;
        }
        Calendar cday = Calendar.getInstance();
        cday.setTime(birthdate);
        String year = String.valueOf(cday.get(Calendar.YEAR));
        String idCard17 = idCard.substring(0, 6) + year + idCard.substring(8);

        char[] c = idCard17.toCharArray();
        String checkCode;

        // 将字符数组转为整型数组
        int bit[] = convertCharToInt(c);

        int sum17;
        sum17 = getPowerSum(bit);

        // 获取和值与11取模得到余数进行校验码
        checkCode = getCheckCodeBySum(sum17);

        // 获取不到校验位
        if (null == checkCode) {
            return null;
        }
        // 将前17位与第18位校验码拼接
        idCard17 += checkCode;
        return idCard17;
    }

    /**
     * 港澳居民来往内地通行证校验，
     * 通行证号码组成规则：通行证证件号码共11位。第1位为字母，“H”字头签发给香港居民，“M”字头签发给澳门居民；第2位至第11位为数字（摘自百度百科）
     *
     * @param idCardNo 身份证号
     * @return 合法返回true，否则返回false
     */
    public static boolean checkHongKongPass(String idCardNo) {
        return Pattern.matches(HONG_KONG_REGEX, idCardNo);
    }

    /**
     * 港澳居民来往内地通行证校验，
     * 通行证号码组成规则：通行证证件号码共11位。第1位为字母，“H”字头签发给香港居民，“M”字头签发给澳门居民；第2位至第11位为数字（摘自百度百科）
     *
     * @param idCardNo 身份证号
     * @return 合法返回true，否则返回false
     */
    public static boolean checkMacauPass(String idCardNo) {
        return Pattern.matches(MACAU_REGEX, idCardNo);
    }

    /**
     * 台湾居民来往大陆通行证校验
     * 台湾居民来往大陆通行证的号码校验规则,五年期台胞证证件号码编号规则为8位阿拉伯数字
     * http://www.mps.gov.cn/n2255079/n4876594/n4974590/n4974592/n5116774/index.html
     * http://www.cdcrj.gov.cn/exitentry/bsdt/723.htm
     *
     * @param idCardNo 身份证号
     * @return 合法返回true，否则返回false
     */
    public static boolean checkTaiWanPass(String idCardNo) {
        return Pattern.matches(TAI_WAN_REGEX, idCardNo);
    }


    // TODO ################################ 获取相关 ################################

    /**
     * 前置检查 IdCard
     *
     * @param idCard
     * @return
     */
    public static String preCheckIdCard(String idCard) {
        if (StringUtils.isBlank(idCard) ||
                (idCard.length() != FIFTEEN && idCard.length() != EIGHTEEN)) {
            return null;
        }
        if (idCard.length() == FIFTEEN) {
            idCard = convertIdCardBy15Bit(idCard);
        }
        return idCard;
    }

    /**
     * 根据身份证号（15 or 18位）获取生日 月/日
     *
     * @param idCard
     * @return
     */
    public static String getBirthdayByIdCard(String idCard) {
        return preCheckIdCard(idCard).substring(10, 14);
    }

    /**
     * 根据身份证号（15 or 18位）计算农历生日
     *
     * @param idCard
     * @return
     */
    public static String getLunarBirthdayByIdCard(String idCard) {
        idCard = preCheckIdCard(idCard);
        String birthday = idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        try {
            return CalendarUtil.solarToLunar(birthday, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据身份证号（15 or 18位）计算年龄
     *
     * @param idCard
     * @return
     */
    public static Integer getAgeByIdCard(String idCard) {
        idCard = preCheckIdCard(idCard);
        String bornYear = idCard.substring(6, 10);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(new Date());
        return Integer.parseInt(year) - Integer.parseInt(bornYear);
    }

    /**
     * 根据月日计算星座
     *
     * @param idCard
     * @return
     */
    public static String getConstellation(String idCard) {
        idCard = preCheckIdCard(idCard);
        Integer month = Integer.valueOf(idCard.substring(10, 12));
        Integer day = Integer.valueOf(idCard.substring(12, 14));
        return day < dayArr[month - 1] ? constellationArr[month - 1]
                : constellationArr[month];
    }

    /**
     * 根据年份计算属相
     *
     * @param idCard
     * @return
     */
    public static String getYear(String idCard) {
        idCard = preCheckIdCard(idCard);
        Integer year = Integer.valueOf(idCard.substring(6, 10));
        if (year < 1900) {
            return "未知";
        }
        int start = 1900;
        return years[(year - start) % years.length];
    }

}
