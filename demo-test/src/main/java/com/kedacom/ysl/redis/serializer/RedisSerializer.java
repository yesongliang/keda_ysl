package com.kedacom.ysl.redis.serializer;

import java.util.Map;

/**
 * 缓存接口Key/Value都是
 * String，所以这里序列化目标是String，让Redis客户端去编码和反序列化以String为输入，由Redis客户端反编码。 如果采用Redis
 * low level api，应该是面向字节数组（流）
 * 
 */
public interface RedisSerializer {

    /**
     * 将给定的对象序列化到字符串。
     * 
     * @param javaObj
     * @throws SerializationException
     */
    String serialize(Object javaObj) throws SerializationException;

    /**
     * 将缓存返回的字符串反序列化为对象。
     * 
     * @param value
     * @throws SerializationException
     */
    <T> T deserialize(String value) throws SerializationException;

    /**
     * 将缓存返回的字符串反序列化为对象。
     * 
     * @param value
     * @throws SerializationException
     */
    @SuppressWarnings("rawtypes")
    <T> T deserialize(String value, Map<String, Class> propClassMap) throws SerializationException;

}
