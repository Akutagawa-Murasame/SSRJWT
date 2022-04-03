package org.mura.util;

import lombok.extern.slf4j.Slf4j;
import org.mura.util.convert.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/3 9:50
 *
 * JedisUtil(推荐存Byte数组，存Json字符串效率更慢)
 *
 * 连接操作redis
 */
@Slf4j
public class JedisUtil {
    /**
     * Redis服务器IP
     * TODO 需要改成自己的redis地址，我这里写的是虚拟机地址，不用尝试攻击
     */
    private static final String ADDRESS = "10.184.230.144";

    /**
     * Redis的端口号
     */
    private static final int PORT = 6379;

    /**
     * 访问密码
     */
    private static final String AUTH = "test";

    /**
     * 可用连接实例的最大数目，默认值为8
     * 如果赋值为-1，则表示不限制
     * 如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
     */
    private static final int MAX_ACTIVE = 1024;

    /**
     * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
     */
    private static final int MAX_IDLE = 200;

    /**
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时
     * 如果超过等待时间，则直接抛出JedisConnectionException
     */
    private static final int MAX_WAIT = 10000;

    /**
     * 连接超时时间
     */
    private static final int TIMEOUT = 10000;

    /**
     * 在borrow一个jedis实例时，是否提前进行validate操作
     * 如果为true，则得到的jedis实例均是可用的
     */
    private static final boolean TEST_ON_BORROW = true;

    /**
     * redis过期时间，以秒为单位，一分钟
     */
    public final static int EXPIRE_MINUTE = 60;

    /**
     * redis过期时间，以秒为单位，一小时
     */
    public final static int EXRPIRE_HOUR = 60 * 60;

    /**
     * redis过期时间，以秒为单位，一天
     */
    public final static int EXRPIRE_DAY = 60 * 60 * 24;

    /**
     * redis-OK
     */
    public final static String OK = "OK";

    /**
     * 连接池
     */
    private static JedisPool jedisPool = null;

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWait(Duration.ofMillis(MAX_WAIT));
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDRESS, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            log.error("initialize jedisPool exception" + e.getMessage());
        }
    }

    /**
     * 获取Jedis实例
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("get jedis instance exception:" + e.getMessage());
        }

        return null;
    }

    /**
     * 释放Jedis资源
     */
    public static void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();

            log.info("jedisPool is closed");
        }
    }

    /**
     * 获取redis键值-object
     */
    public static Object getObject(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(key.getBytes());

            if (StringUtil.isNotNull(bytes)) {
                return SerializeUtil.unserialize(bytes);
            }
        } catch (Exception e) {
            log.error("getObject get object exception:key=" + key + " cause:" + e.getMessage());
        }

        return null;
    }

    /**
     * 设置redis键值-object
     */
    public static String setObject(String key, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            log.error("setObject set key exception:key=" + key + " value=" + value + " cause:" + e.getMessage());
        }

        return null;
    }

    /**
     * 设置redis键值和超时时间-object-expire_time
     */
    public static String setObject(String key, Object value, int expireTime) {
        String result = "";
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.set(key.getBytes(), SerializeUtil.serialize(value));

            if (OK.equals(result)) {
                jedis.expire(key.getBytes(), expireTime);
            }

            return result;
        } catch (Exception e) {
            log.error("setObject set key with expire time exception:key=" + key + " value=" + value + " cause:" + e.getMessage());
        }

        return result;
    }

    /**
     * 获取redis键值-Json，redis默认返回的就是json
     */
    public static String getJson(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("getJson get json exception:key=" + key + " cause:" + e.getMessage());
        }

        return null;
    }

    /**
     * 设置redis键值-Json
     */
    public static String setJson(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception e) {
            log.error("setJson set json key exception:key=" + key + " value=" + value + " cause:" + e.getMessage());
        }

        return null;
    }

    /**
     * 设置redis键值-Json-expire_time
     */
    public static String setJson(String key, String value, int expireTime) {
        String result = "";
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.set(key, value);

            if (OK.equals(result)) {
                jedis.expire(key, expireTime);
            }

            return result;
        } catch (Exception e) {
            log.error("setJson设置redis键值异常:key=" + key + " value=" + value + " cause:" + e.getMessage());
        }

        return result;
    }

    /**
     * 删除key
     */
    public static Long delKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key.getBytes());
        } catch (Exception e) {
            log.error("delete key exception:" + key + "cause:" + e.getMessage());
        }
        return null;
    }

    /**
     * key是否存在
     */
    public static Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key.getBytes());
        } catch (Exception e) {
            log.error("search key exception:" + key + "cause:" + e.getMessage());
        }

        return null;
    }
}
