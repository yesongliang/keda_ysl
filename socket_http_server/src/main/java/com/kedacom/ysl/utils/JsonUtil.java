package com.kedacom.ysl.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * json字符串转换
 * 
 * @author 95488
 * @createDate 2019年6月13日
 *
 */
public class JsonUtil {

	public static String toJson(Object object) {
		String jsonString = JSONObject.toJSONString(object);
		return jsonString;
	}

	public static JSONObject toJSONObject(String json) {
		JSONObject parseObject = null;
		try {
			parseObject = JSONObject.parseObject(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseObject;
	}

	public static <T> T getObject(String json, Class<T> clazz) {
		T object = null;
		try {
			object = JSONObject.parseObject(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

}
