package com.kedacom.ysl.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public interface CacheService {

    /**
     * 获取JedisClient
     * 
     * @return Jedis redis连接
     */
    Jedis getJedisClient();

    /**
     * 关闭redis连接
     * 
     * @param jedis
     */
    void closeQuietly(Jedis jedis);

    /**
     * 根据key获取对应的Value值
     * 
     * @param key
     * @return
     */
    String get(final String key);

    /**
     * 设置键值对
     * 
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, String value);

    public boolean set(final String key, Object value);

    /**
     * 键值是否存在
     * 
     * @param key
     * @return
     */
    boolean exists(final String key);

    /**
     * 设置键值对，键值对在有效期有效
     * 
     * @param key
     * @param expireinseconds
     *            ：有效期时间，单位：秒
     * @param value
     * @return
     */
    boolean set(final String key, int expireinseconds, String value);

    /**
     * 删除键值对
     * 
     * @param key
     * @return
     */
    Long del(final String key);

    void delByPattern(final String pattern);

    /**
     * 以pattern模式返回key值，以Set形式返回
     * 
     * @param pattern
     *            key的模式，支持
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 获取所有key
     * 
     * @return
     */
    Set<String> allKeys();

    /**
     * 添加缓存，支持对对象以JSON格式序列化
     * 
     * @param key
     * @param expireinseconds
     *            数据缓存超时时间
     * @param data
     */
    boolean set(final String key, int expireinseconds, Object data);

    /**
     * 获取缓存值， 支持对对象以JSON格式反序列化的接口
     * 
     * @param key
     * @return Object
     */
    <T> T getObject(final String key);

    /**
     * 获取缓存值， 支持对对象以JSON格式反序列化的接口
     * 
     * @param key
     * @return Object
     */
    @SuppressWarnings("rawtypes")
    <T> T getObject(final String key, Map<String, Class> propClassMap);

    /**
     * map映射数据缓存添加接口
     * 
     * @param key
     *            键
     * @param field
     *            字段
     * @param data
     *            值
     * @return Long If the field already exists, and the HSET just produced an
     *         update of the value, 0 is returned, otherwise if a new field is
     *         created 1 is returned.
     */
    Long hset(final String key, String field, String data);

    /**
     * hash值自增
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincr(final String key, String field, long value);

    /**
     * map映射数据缓存添加接口
     * 
     * @param key
     *            键
     * @param field
     *            字段
     * @param data
     *            值 need to serialization
     * @return Long If the field already exists, and the HSET just produced an
     *         update of the value, 0 is returned, otherwise if a new field is
     *         created 1 is returned.
     */
    Long hset(final String key, String field, Object data);

    /**
     * map映射数据缓存获取接口
     * 
     * @param key
     *            键值
     * @param field
     *            map映射的字段
     * @param data
     *            对应field的值
     */
    String hget(final String key, String field);

    /**
     * map映射数据缓存获取接口
     * 
     * @param key
     *            键值
     * @param field
     *            map映射的字段
     * @param data
     *            对应field的值
     * @param claszz
     *            泛型类的类型，与返回值类型匹配
     * @return T 泛型类
     */
    public <T> T hget(final String key, String field, Class<T> claszz);

    /**
     * 删除map映射中的字段缓存
     * 
     * @param key
     * @param field
     * @return Long 如果存在并删除则返回 1 否则返回 0
     */
    public Long hdel(final String key, String... fields);

    /**
     * 判断key所关联的map中是否存在filed字段
     * 
     * @param key
     *            键值
     * @param field
     *            字段
     * @return boolean 存在：true， 不存在：false
     */
    public boolean hexists(final String key, String field);

    /**
     * map映射数据缓存获取全部接口
     * 
     * @param key
     * @param filed
     * @param data
     */
    Map<String, String> hgetAll(final String key);

    /**
     * 添加map数据
     * 
     * @param key
     * @param map
     * @return
     */
    boolean hmset(final String key, Map<String, String> map);

    /**
     * 获取map数据中的多个字段值。
     * 
     * @param key
     * @param fields
     * @return
     */
    List<String> hmget(final String key, String... fields);

    /**
     * 向key中以集合方式添加一个或多个成员
     * 
     * @param key
     * @param members
     * @return Long
     */
    Long sadd(final String key, String... members);

    /**
     * 获取key中以集合的所有值
     * 
     * @param key
     * @return
     */
    Set<String> sgetAll(final String key);

    /**
     * 在key的集合中删除一个或多个成员
     * 
     * @param key
     * @param members
     * @return Long
     */
    Long sremove(final String key, String... members);

    /**
     * 向列表中的头部增加一个元素
     * 
     * @param key
     * @param value
     * @return
     */
    Long lpush(final String key, String value);

    /**
     * 向列表中的尾部增加一个元素
     * 
     * @param key
     * @param value
     * @return
     */
    Long rpush(final String key, String value);

    /**
     * 从列表头部移除一个元素并返回该元素
     * 
     * @param key
     * @return
     */
    String lpop(final String key);

    /**
     * 移除所有元素并返回所有元素，并去重
     * 
     * @param key
     * @return
     */
    List<String> lpopAll(final String key);

}
