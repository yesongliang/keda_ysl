package com.kedacom.ysl.redis.serializer;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kedacom.ysl.util.JsonUtils;

public class ObjectJSONMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger("API");

    private static final String CLASS_NAME = "className";
    private static final String OBJECT_JSON_STRING = "valueString";

    /**
     * {"className":"object class name", "valueString":"object JSON string"}
     */
    public Object read(String jsonString) throws SerializationException {
	return read(jsonString, null);
    }

    @SuppressWarnings("rawtypes")
    public Object read(String jsonString, Map<String, Class> propClassMap) throws SerializationException {
	if (StringUtils.isNotEmpty(jsonString)) {
	    try {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		String className = jsonObject.getString(CLASS_NAME);
		String objectJsonString = jsonObject.getJSONObject(OBJECT_JSON_STRING).toString();

		Class<?> c = null;
		ClassLoader classLoader = this.getClassLoader();
		if (classLoader != null) {
		    c = classLoader.loadClass(className);
		} else {
		    c = Class.forName(className); // Not good.
		}
		if (propClassMap != null) {
		    return JsonUtils.toBean(objectJsonString, c, propClassMap);
		} else {
		    return JsonUtils.toBean(objectJsonString, c);
		}
	    } catch (Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		throw new SerializationException(ex.getMessage(), ex);
	    }
	}
	return null;
    }

    public String writeAsJSONString(Object object) {
	// 存储对象类型名称
	final String className = object.getClass().getName();
	// 对象
	final String jsonString = JSONObject.fromObject(object).toString();
	return getJsonString(className, jsonString);
    }

    private String getJsonString(String className, String jsonString) {
	StringBuilder sb = new StringBuilder();
	sb.append("{\"").append(CLASS_NAME).append("\":\"").append(className).append("\"").append(",\"").append(OBJECT_JSON_STRING).append("\":").append(jsonString).append("}");
	return sb.toString();
    }

    // 这里要斟酌一下！
    private ClassLoader getClassLoader() {

	return Thread.currentThread().getContextClassLoader();
    }
}
