package com.kedacom.ysl.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.kedacom.ysl.redis.serializer.RedisSerializer;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    private static Logger logger = LoggerFactory.getLogger("ysl_test");

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisSerializer jsonRedisSerializer;

    public String get(final String key) {
	Jedis jedis = getJedisClient();
	String value = jedis.get(key);
	closeQuietly(jedis);
	return value;
    }

    public boolean set(final String key, String value) {
	Jedis jedis = getJedisClient();
	String replyCode = jedis.set(key, value);
	closeQuietly(jedis);
	return replyCode != null ? replyCode.equalsIgnoreCase("OK") : false;
    }

    public boolean set(String key, Object value) {
	Jedis jedis = getJedisClient();
	String jsonVal = jsonRedisSerializer.serialize(value);
	String replyCode = jedis.set(key, jsonVal);
	closeQuietly(jedis);
	return replyCode != null ? replyCode.equalsIgnoreCase("OK") : false;
    }

    public boolean exists(String key) {
	Jedis jedis = getJedisClient();
	boolean ret = jedis.exists(key);
	closeQuietly(jedis);
	return ret;
    }

    public boolean set(final String key, int expireinseconds, String value) {
	Jedis jedis = getJedisClient();
	String isOk = jedis.setex(key, expireinseconds, value);
	closeQuietly(jedis);
	return isOk != null ? isOk.equalsIgnoreCase("OK") : false;
    }

    public Long del(final String key) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.del(key);
	closeQuietly(jedis);
	return ret;
    }

    public void delByPattern(final String pattern) {
	Jedis jedis = getJedisClient();
	Set<String> keys = this.keys(pattern);
	for (String key : keys) {
	    jedis.del(key);
	}
	closeQuietly(jedis);
    }

    public boolean set(final String key, int expireinseconds, Object object) {
	String value = jsonRedisSerializer.serialize(object);
	return this.set(key, expireinseconds, value);
    }

    public <T> T getObject(String key) {
	String value = this.get(key);
	return jsonRedisSerializer.deserialize(value);
    }

    @SuppressWarnings("rawtypes")
    public <T> T getObject(String key, Map<String, Class> propClassMap) {
	String value = this.get(key);
	return jsonRedisSerializer.deserialize(value, propClassMap);
    }

    public Set<String> keys(String pattern) {
	Jedis jedis = getJedisClient();
	Set<String> keys = jedis.keys(pattern);
	closeQuietly(jedis);
	return keys;
    }

    public Set<String> allKeys() {
	return this.keys("*");
    }

    public Long hset(final String key, String field, String data) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.hset(key, field, data);
	closeQuietly(jedis);
	return ret;
    }

    public Long hincr(final String key, String field, long value) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.hincrBy(key, field, value);
	closeQuietly(jedis);
	return ret;
    }

    public Long hset(final String key, String field, Object data) {
	Jedis jedis = getJedisClient();
	String value = jsonRedisSerializer.serialize(data);
	Long ret = jedis.hset(key, field, value);
	closeQuietly(jedis);
	return ret;
    }

    public String hget(final String key, String field) {
	Jedis jedis = getJedisClient();
	String value = jedis.hget(key, field);
	closeQuietly(jedis);
	return value;
    }

    public <T> T hget(final String key, String field, Class<T> claszz) {
	Jedis jedis = getJedisClient();
	String value = jedis.hget(key, field);
	T t = jsonRedisSerializer.deserialize(value);
	closeQuietly(jedis);
	return t;
    }

    public Long hdel(String key, String... fields) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.hdel(key, fields);
	closeQuietly(jedis);
	return ret;
    }

    public boolean hexists(String key, String field) {
	Jedis jedis = getJedisClient();
	boolean exixsts = jedis.hexists(key, field);
	closeQuietly(jedis);
	return exixsts;
    }

    public Map<String, String> hgetAll(final String key) {
	Jedis jedis = getJedisClient();
	Map<String, String> map = jedis.hgetAll(key);
	closeQuietly(jedis);
	return map;
    }

    public boolean hmset(String key, Map<String, String> map) {
	Jedis jedis = getJedisClient();
	String replyCode = jedis.hmset(key, map);
	closeQuietly(jedis);
	return replyCode != null ? replyCode.equalsIgnoreCase("OK") : false;
    }

    public List<String> hmget(String key, String... fields) {
	Jedis jedis = getJedisClient();
	List<String> values = jedis.hmget(key, fields);
	closeQuietly(jedis);
	return values;
    }

    public Long sadd(String key, String... members) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.sadd(key, members);
	closeQuietly(jedis);
	return ret;
    }

    public Set<String> sgetAll(String key) {
	Jedis jedis = null;
	try {
	    jedis = getJedisClient();
	    Set<String> values = jedis.smembers(key);
	    return values;
	} finally {
	    closeQuietly(jedis);
	}
    }

    public Long sremove(String key, String... members) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.srem(key, members);
	closeQuietly(jedis);
	return ret;
    }

    public Long lpush(String key, String value) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.lpush(key, value);
	closeQuietly(jedis);
	return ret;
    }

    public Long rpush(String key, String value) {
	Jedis jedis = getJedisClient();
	Long ret = jedis.rpush(key, value);
	closeQuietly(jedis);
	return ret;
    }

    public String lpop(String key) {
	Jedis jedis = getJedisClient();
	String value = jedis.lpop(key);
	closeQuietly(jedis);
	return value;
    }

    public List<String> lpopAll(String key) {
	Jedis jedis = getJedisClient();
	List<String> values = new ArrayList<String>();
	String value = null;
	do {
	    value = jedis.lpop(key);
	    if (value != null) {
		if (!values.contains(value)) {
		    values.add(value);
		}
	    }
	} while (value != null);
	closeQuietly(jedis);
	return values;
    }

    /**
     * 如果获取Jedis失败，如果处理
     * 
     * @return
     */
    public Jedis getJedisClient() {
	return getJedisClient(true);
    }

    public Jedis getJedisClient(boolean defaultDatabase) {
	Jedis jedis = null;
	try {
	    if (defaultDatabase) {
		jedis = jedisPool.getResource();
	    }
	    return jedis;
	} catch (Throwable t) {
	    logger.error(t.getMessage(), t);
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    if (defaultDatabase) {
		jedis = jedisPool.getResource();
	    }
	}
	return jedis;
    }

    public void closeQuietly(Jedis jedis) {
	try {
	    if (jedis != null) {
		jedis.close();
	    }
	} catch (Throwable t) {
	    t.printStackTrace();
	    logger.error(t.getMessage(), t);
	}
    }
}
