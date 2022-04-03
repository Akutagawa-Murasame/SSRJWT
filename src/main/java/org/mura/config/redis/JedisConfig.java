package org.mura.config.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/3 14:11
 *
 * 项目启动注入JedisPool
 */
@Configuration
@PropertySource("classpath:config.properties")
@ConfigurationProperties(prefix = "redis")
@Slf4j
@Data
public class JedisConfig {
    /**
     * Redis服务器IP
     */
    @Value("${redis.pool.host}")
    private String host;

    /**
     * Redis的端口号
     */
    @Value("${redis.pool.port}")
    private int port;

    /**
     * 访问密码
     */
    @Value("${redis.pool.password}")
    private String password;

    /**
     * 连接超时时间
     */
    @Value("${redis.pool.timeout}")
    private int timeout;

    /**
     * 可用连接实例的最大数目，默认值为8
     * 如果赋值为-1，则表示不限制
     * 如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
     */
    @Value("${redis.pool.max-active}")
    private int maxActive;

    /**
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时
     * 如果超过等待时间，则直接抛出JedisConnectionException
     */
    @Value("${redis.pool.max-wait}")
    private int maxWait;

    /**
     * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
     */
    @Value("${redis.pool.max-idle}")
    private int maxIdle;

    /**
     * 控制一个pool最少有多少个状态为idle(空闲的)的jedis实例
     */
    @Value("${redis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWait(Duration.ofMillis(maxWait));
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        log.info("JedisPool injection success");
        log.info("redis location：" + host + ":" + port);
        return jedisPool;
    }
}
