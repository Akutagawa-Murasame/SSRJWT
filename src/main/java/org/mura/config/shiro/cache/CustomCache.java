package org.mura.config.shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.mura.config.jwt.JWTUtil;
import org.mura.config.redis.JedisUtil;
import org.mura.model.common.Constant;
import org.mura.util.PropertiesUtil;
import org.mura.util.convert.SerializeUtil;

import java.util.*;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/3 8:21
 *
 * 调用JedisUtil进行redis操作
 */
@SuppressWarnings("unchecked")
@Slf4j
public class CustomCache<K, V> implements Cache<K, V> {
    /**
     * 缓存的key名称获取为shiro_cache:account
     * 传入参数的时候不用带上前缀
     */
    private String getKey(K key) {
        return Constant.PREFIX_SHIRO_CACHE + JWTUtil.getAccount(key.toString());
    }

    /**
     * 获取缓存
     */
    @Override
    public V get(K key) throws CacheException {
        log.info("get data from redis");
        return (V) JedisUtil.getObject(this.getKey(key));
    }

    /**
     * 保存缓存，当从缓存中获取数据的时候会调用这个函数，所以在redis中会看到以shiro:cache:开头的键
     */
    @Override
    public V put(K key, V value) throws CacheException {
        PropertiesUtil.readProperties("config.properties");
        String shiroCacheExpireTime = PropertiesUtil.getProperty("shiroCacheExpireTime");

        log.info("cache data:" + value.toString() + "for expire:" + shiroCacheExpireTime + "s");

        // 设置shiro的redis过期时间
        return (V) JedisUtil.setObject(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime));
    }

    /**
     * 移除缓存
     */
    @Override
    public V remove(K key) throws CacheException {
        JedisUtil.delKey(this.getKey(key));

        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        Objects.requireNonNull(JedisUtil.getJedis()).flushDB();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        Long size = Objects.requireNonNull(JedisUtil.getJedis()).dbSize();

        return size.intValue();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set<K> keys() {
        Set<byte[]> keys = Objects.requireNonNull(JedisUtil.getJedis()).
                keys("*".getBytes());

        Set<K> set = new HashSet<>();
        for (byte[] key : keys) {
            set.add((K) SerializeUtil.unserialize(key));
        }
        return set;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection<V> values() {
        Set<K> keys = this.keys();
        List<V> values = new ArrayList<>();

        for (K key : keys) {
            values.add((V) JedisUtil.getObject(this.getKey(key)));
        }

        return values;
    }
}
