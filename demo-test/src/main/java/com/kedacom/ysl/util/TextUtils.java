package com.kedacom.ysl.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author John Lee
 */
public class TextUtils {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    /**
     * 正则验证手机号
     */
    private static final String mobileType = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /**
     * 正则验证身份证
     */
    private static final String numberIdType = "\\d{17}[\\d|x|X]|\\d{15}";
    /**
     * 正则验证银行卡(验证全数字)
     */
    private static final String cardNoType = "^[0-9]*$";
    /**
     * 正则验证时间 yyyy-MM-dd
     */
    private static final String dateType = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

    /**
     * 验证http请求网址
     */
    private static final String httpType = "(http://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*";

    /**
     * 正则验验证全数字
     */
    private static final String digitType = "^[0-9]*$";

    /**
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
	if (StringUtils.isBlank(email)) {
	    return false;
	}

	Pattern pattern = Pattern.compile(EMAIL_REGEX);
	Matcher matcher = pattern.matcher(email);
	if (matcher.find()) {
	    return true;
	}
	return false;
    }

    /**
     * 验证数字
     * 
     * @param digit
     * @return
     */
    public static boolean isDigit(String digit) {
	return isCheck(digitType, digit);
    }

    /**
     * 验证时间
     * 
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
	return isCheck(dateType, date);
    }

    /**
     * 验证BankCard(验证全数字)
     * 
     * @param cardNo
     * @return
     */
    public static boolean isCardNo(String cardNo) {
	return isCheck(cardNoType, cardNo);
    }

    /**
     * 验证手机
     * 
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
	return isCheck(mobileType, mobile);
    }

    /**
     * 验证身份证
     * 
     * @param numberId
     * @return
     */
    public static boolean isNumberId(String numberId) {
	return isCheck(numberIdType, numberId);
    }

    /**
     * 验证http请求
     * 
     * @param numberId
     * @return
     */
    public static boolean isHttp(String http) {
	return isCheck(httpType, http);
    }

    public static void main(String[] args) {
	String[] strArr = new String[] { "111", "222" };
	List<String> list = Arrays.asList(strArr);
	System.out.println(list);
    }

    /**
     * 正则验证
     * 
     * @param type
     *            类型
     * @param str
     *            值
     * @return
     */
    private static boolean isCheck(String type, String str) {
	Pattern p = Pattern.compile(type);
	Matcher m = p.matcher(str);
	return m.matches();
    }

    /**
     * 获取一个随机字符串。
     * 
     * @param length
     *            随机字符串的长度
     * @param model
     *            0 数字 1 字母 2 数字加字母
     * @return 一个随机字符串
     */
    public static String getRandomString(int length, int model) {
	if (length < 1) {
	    return "";
	}

	if (model < 0 || model > 2) {
	    model = 0;
	}

	StringBuffer sb = new StringBuffer(length);

	for (int i = 0; i < length; i++) {
	    if (model == 0) {
		sb.append(genRandomDigit(i != 0));
	    } else if (model == 1) {
		sb.append(genRandomLetter());
	    } else if (model == 2) {
		sb.append(genRandomChar(i != 0));
	    }

	}

	return sb.toString();
    }

    /**
     * 获取一个随机字符（允许数字0-9和小写字母）。
     * 
     * @return 一个随机字符
     */
    private static char genRandomChar(boolean allowZero) {
	int randomNumber = 0;
	do {
	    Random r = new Random();
	    randomNumber = r.nextInt(36);
	} while ((randomNumber == 0) && !allowZero);

	if (randomNumber < 10) {
	    return (char) (randomNumber + '0');
	} else {
	    return (char) (randomNumber - 10 + 'a');
	}
    }

    private static char genRandomLetter() {
	int randomNumber = 0;
	Random r = new Random();
	randomNumber = r.nextInt(26);
	return (char) (randomNumber + 'a');
    }

    /**
     * 获取一个随机字符（只允许数字0-9）。
     * 
     * @return 一个随机字符
     */
    private static char genRandomDigit(boolean allowZero) {
	int randomNumber = 0;

	do {
	    Random r = new Random();
	    randomNumber = r.nextInt(10);
	} while ((randomNumber == 0) && !allowZero);

	return (char) (randomNumber + '0');
    }

    public static String convertCamelCaseToUnderscore(String str) {
	String[] strings = StringUtils.splitByCharacterTypeCamelCase(str);

	String _str = StringUtils.join(strings, "_");

	_str = StringUtils.lowerCase(_str);

	return _str;
    }

    /**
     * 替换规则前四后四
     * 
     * @param message
     * @return
     */
    public static String replaceNumber(String message) {
	StringBuffer str = new StringBuffer();
	for (int i = 0; message.length() - 8 > i; i++) {
	    str = str.append("*");
	}
	StringBuffer result = new StringBuffer();
	result.append(message.substring(0, 4)).append(str.toString()).append(message.substring(message.length() - 4));
	return result.toString();
    }

    /**
     * 替换规则全部
     * 
     * @param message
     * @return
     */
    public static String replaceNumberAll(String message) {
	return "******";
    }

    /**
     * 构建查询查询条件列表。
     * 
     */
    /*
     * public static String constructInCondition(String[] conditonSet){
     * List<String> sets = Arrays.asList(conditonSet) ; return
     * constructInCondition(sets) ; }
     */

    /**
     * 构建查询查询条件列表。
     */
    public static String constructInCondition(Collection<String> conditonSet) {
	StringBuilder sb = new StringBuilder(" in ('");
	boolean first = true;
	for (String userId : conditonSet) {
	    if (first) {
		sb.append(userId);
		first = false;
	    } else {
		sb.append("','").append(userId);
	    }
	}

	sb.append("')");
	return sb.toString();
    }

    /**
     * 构建整型类字段查询条件列表。
     * 
     */
    /*
     * public static String constructIntegerInCondition(String[] conditonSet){
     * List<String> sets = Arrays.asList(conditonSet) ; return
     * constructInCondition(sets) ; }
     */
}
