package com.kedacom.ysl.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kedacom.ysl.redis.CacheService;
import com.kedacom.ysl.util.JsonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisTest {

    @Autowired
    private CacheService cacheService;

    String listKey = "listKey";

    @Before
    public void before() {
	cacheService.set("comp", "testRedis");

	Map<String, String> map = new HashMap<String, String>();
	map.put("author", "JK.Rolin");
	map.put("bookName", "Harry Potter");
	map.put("protagonist", "哈利波特, 罗恩, 赫敏");
	cacheService.hmset("novel", map);

	cacheService.hset("memstat:123", "e1", 1 + "");
	cacheService.hset("memstat:123", "e2", 2 + "");
	cacheService.hset("memstat:123", "e3", 3 + "");

	cacheService.rpush(listKey, "23");
	cacheService.rpush(listKey, "a");
	cacheService.rpush(listKey, "b");
	cacheService.rpush(listKey, "c");
	cacheService.rpush(listKey, "d");
	cacheService.rpush(listKey, "e");
	cacheService.rpush(listKey, "b");
	cacheService.rpush(listKey, "d");
    }

    @After
    public void after() {
	cacheService.del("comp");
	cacheService.del("novel");
	cacheService.del(listKey);
	cacheService.del("memstat:123");
    }

    @Test
    public void testPush() {
	List<String> list = new ArrayList<String>();
	list.add("111");
	list.add("222");
	String jsonStr = JsonUtils.toJsonFromList(list);
	cacheService.lpush("testJsonList", jsonStr);

	String jsonList = cacheService.lpop("testJsonList");
	@SuppressWarnings("unchecked")
	List<String> list2 = JsonUtils.toBeanFromJsonArray(jsonList, List.class);
	System.out.println(list2);
    }

    @Test
    public void testHIncrease() {
	String key = "testHIncr";
	String field = "times";
	for (int i = 0; i < 10; i++) {
	    cacheService.hincr(key, field, 1L);
	}
	String value = cacheService.hget(key, field);
	System.out.println(value);
    }

    @Test
    public void testList() {
	String value = cacheService.lpop(listKey);
	Assert.assertEquals("23", value);

	List<String> values = cacheService.lpopAll(listKey);
	System.out.println(values);
    }

    @Test
    public void testKeys() {
	Set<String> keys = cacheService.keys("*");
	Iterator<String> ks = keys.iterator();
	while (ks.hasNext()) {
	    System.out.println(ks.next());
	}
    }

    @Test
    public void testHdel() {
	Map<String, String> map = cacheService.hgetAll("memstat:123");
	System.out.println(map.size());

	String[] memberes = new String[] { "e1", "e2", "e3" };
	Long ret = cacheService.hdel("memstat:123", memberes);
	System.out.println("ret = " + ret);

	Map<String, String> map2 = cacheService.hgetAll("memstat:123");
	System.out.println(map2);
    }

    @Test
    public void testExists() {
	String n = cacheService.hget("teclist", "name");
	Assert.assertNull(n);
    }

    // @Test
    public void testSlaveRedis() {
	String v = cacheService.get("size");
	Assert.assertNotNull(v);
	Long ret = cacheService.del("size");
	Assert.assertNotNull(ret);
	System.out.println("value is :" + v);
	System.out.println("ret is :" + ret);
    }

    @Test
    public void testRedis() {
	String v = cacheService.get("comp");
	Assert.assertNotNull(v);
    }

    /**
     * Long 类型无法转换。
     */
    // @Test
    public void testConvert() {
	Long tt = 10000L;
	cacheService.set("6002", 120, tt);
	Long ll = cacheService.getObject("6002");
	Assert.assertNotNull(ll);
	System.out.println(ll);
    }

    @Test
    public void testHSet() {
	Map<String, String> tempMap = cacheService.hgetAll("novel");
	Assert.assertNotNull(tempMap);
	System.out.println(tempMap.toString());
    }

    @Test
    public void testHmset() {
	Map<String, String> vals = cacheService.hgetAll("novel");
	System.out.println(vals);
    }
}
