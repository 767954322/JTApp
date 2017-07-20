package com.homechart.app.commont;

/**
 * @author gumenghao .
 * @version v1.0 .
 * @date 2017.5.27
 * @file RegexUtil.java .
 * @brief 项目中的正则工具类 .
 */
public class RegexUtil {
    private RegexUtil() {
    }

    /**
     * 小区地址校验：无特殊字符，2-32位
     */
    public static final String ADDRESS_REGEX_NAME = "^[\\u4e00-\\u9fa5_a-zA-Z0-9_]{2,15}$";//账号
    public static final String ADDRESS_REGEX_PASS = "^[\\u4e00-\\u9fa5_a-zA-Z0-9_]{6,20}$";//密码
    public static final String ADDRESS_ZHONGWEN = "^[\\u4e00-\\u9fa5]{2,32}$";

    public static final String ADDRESS_NUM = "^[1-9]+(.[0-9]{2})?$";

    /**
     * 手机号码校验
     */
    public static final String PHONE_REGEX = "^1[3|4|5|7|8]\\d{9}$";

    /**
     * 银行卡号验证
     */
//    public static final String PHONE_BLANK = "^(\\d{16}|\\d{23})$";
    public static final String PHONE_BLANK = "^[0-9]{16,19}$";
    /**
     * 量房面积校验： 整数四位，小数2位
     */
    public static final String AREA_REGEX = "^[0-9]{1,4}?(\\.[0-9]{0,2})?$";

    /**
     * 量房面积校验： 整数四位,不能全部为0
     */

    public static final String AREA_REGEX_ZERO = "^(?!0{2,})(?:\\d{1,4}(\\.\\d+)?|10000)$";

    /**
     * 量房费验证：整数位可以有0
     */
    public static final String MEASURE_FEE_REGEX = "^[0-9]{0,4}?(\\.[0-9]{0,2})?$";

    /**
     * 验证姓名
     */
    public static final String NAME_REGEX = "^([\u4e00-\u9fa5]{2,10})$"; // 合同里的姓名判断有用到
    public static final String NAME_REGEX1 = "^[A-Za-z\\u4e00-\\u9fa5]{2,12}+$";

    /**
     * 验证邮编
     */
    public static final String POST_NUMBER_REGEX = "^[0-9]{6}$";

    /**
     * 验证昵称
     */
    public static final String NICK_NAME_REGEX = "^\\S[a-zA-Z\\s\\d\\u4e00-\\u9fa5]{1,19}$"; // 中英文数据空格适配正则  +\S
//    public static final String CONTRACT_NAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\-]{2,20}$";

    /**
     * 验证邮箱
     */
    public static final String EMAIL_REGEX = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";

    public static final String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    /**
     * 验证正整数
     */
    public static final String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*|0";

    /**
     * 身份号码
     */
    public static final String ID_CARD_REGEX = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";


}
