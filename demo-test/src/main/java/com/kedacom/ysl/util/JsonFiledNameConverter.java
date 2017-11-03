package com.kedacom.ysl.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.PropertyNameProcessor;
import net.sf.json.processors.PropertyNameProcessorMatcher;

import org.apache.commons.lang.StringUtils;

/**
 * @author ysl
 * @version V0.1
 */
public class JsonFiledNameConverter {

    private static final JsonConfig jsonConfig = jsonConfig();

    private final static JsonConfig jsonConfig() {
	JsonConfig config = new JsonConfig();

	config.setJsonPropertyNameProcessorMatcher(new PropertyNameProcessorMatcher() {
	    @SuppressWarnings("rawtypes")
	    @Override
	    public Object getMatch(Class target, Set set) {
		return Object.class;
	    }
	});

	config.registerJsonPropertyNameProcessor(Object.class, new PropertyNameProcessor() {
	    @SuppressWarnings("rawtypes")
	    public String processPropertyName(Class beanClass, String name) {
		String _name = TextUtils.convertCamelCaseToUnderscore(name);

		if (StringUtils.equals(_name, "success")) {
		    _name = "is_success";
		}

		return _name;
	    }
	});

	config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {

	    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		if (value == null) {
		    return null;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

		Date date = (Date) value;

		return df.format(date);
	    }

	    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return null;
	    }
	});

	return config;
    }

    /**
     * @return
     */
    public static JsonConfig getJsonConfig() {
	return jsonConfig;
    }

}
