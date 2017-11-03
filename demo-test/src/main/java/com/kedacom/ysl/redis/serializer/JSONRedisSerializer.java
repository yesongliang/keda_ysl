package com.kedacom.ysl.redis.serializer;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class JSONRedisSerializer implements RedisSerializer {

    private ObjectJSONMapper objectMapper = new ObjectJSONMapper();

    public String serialize(Object javaObj) throws SerializationException {
	return objectMapper.writeAsJSONString(javaObj);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialize(String value) throws SerializationException {
	if (StringUtils.isEmpty(value)) {
	    return null;
	}
	try {
	    return (T) objectMapper.read(value);
	} catch (Throwable t) {
	    throw new SerializationException(t.getMessage(), t);
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> T deserialize(String value, Map<String, Class> propClassMap) throws SerializationException {
	if (StringUtils.isEmpty(value)) {
	    return null;
	}

	try {
	    return (T) objectMapper.read(value, propClassMap);
	} catch (Throwable t) {
	    throw new SerializationException(t.getMessage(), t);
	}
    }

}
