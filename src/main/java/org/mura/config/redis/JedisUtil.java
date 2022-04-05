package org.mura.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.mura.model.common.Constant;
import org.mura.util.StringUtil;
import org.mura.util.convert.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/3 9:50
 *
 * JedisUtil(推荐存Byte数组，存Json字符串效率更慢)
 *
 * 连接操作redis
 */
@Slf4j
@Component
public class JedisUtil {
    /**
     * 静态注入JedisPool连接池
     * 本来是正常注入JedisUtil，可以在Controller和Service层使用，但是重写Shiro的CustomCache无法注入JedisUtil（因为不在容器中）
     * 现在改为静态注入JedisPool连接池，JedisUtil直接调用静态方法即可
     * https://blog.csdn.net/W_Z_W_888/article/details/79979103
     */
    private static JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        JedisUtil.jedisPool = jedisPool;
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
            log.error("get jedis instance cause an exception:" + e.getMessage());
        }

        return null;
    }

    /**
     * 释放Jedis资源
     */
    public static void closePool() {
        try {
            if (jedisPool != null && !jedisPool.isClosed()) {
                jedisPool.close();

                log.info("jedisPool is closed");
            }
        } catch (Exception e) {
            log.error("release jedis cause an exception" + e.getMessage());
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

            if (Constant.OK.equals(result)) {
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

            if (Constant.OK.equals(result)) {
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

    /**
     * 模糊查询获取key集合（字符串），用于获取在线用户信息
     */
    public static Set<String> keysS(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key);
        } catch (Exception e) {
            log.error("模糊查询key值:" + key + "异常:" + e.getMessage());
        }

        return null;
    }

    /**
     * 模糊查询获取key集合（字节），用于获取在线用户信息
     */
    public static Set<byte[]> keysB(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key.getBytes());
        } catch (Exception e) {
            log.error("模糊查询key值:" + key + "异常:" + e.getMessage());
        }

        return null;
    }
}
