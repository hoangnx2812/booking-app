package com.example.commericalcommon.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.Pool;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.database:0}")
    private int database;

    @Value("${spring.data.redis.timeout:3000}")
    private int timeout;

    @Value("${spring.data.redis.connect-timeout:10000}")
    private int connectTimeout;

    // Jedis Pool Configuration
    @Value("${spring.data.redis.jedis.pool.max-total:300}")
    private int maxTotal;

    @Value("${spring.data.redis.jedis.pool.max-idle:8}")
    private int maxIdle;

    @Value("${spring.data.redis.jedis.pool.min-idle:2}")
    private int minIdle;

    @Value("${spring.data.redis.jedis.pool.max-wait:1000}")
    private int maxWaitMillis;

    @Value("${spring.data.redis.jedis.pool.min-evictable-idle-time:60000}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.data.redis.jedis.pool.time-between-eviction-runs:30000}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.data.redis.jedis.pool.num-tests-per-eviction-run:3}")
    private int numTestsPerEvictionRun;

    @Value("${spring.data.redis.jedis.pool.test-on-borrow:true}")
    private boolean testOnBorrow;

    @Value("${spring.data.redis.jedis.pool.test-on-return:false}")
    private boolean testOnReturn;

    @Value("${spring.data.redis.jedis.pool.test-while-idle:true}")
    private boolean testWhileIdle;

    @Value("${spring.data.redis.jedis.pool.block-when-exhausted:true}")
    private boolean blockWhenExhausted;

    @Bean
    public Pool<Jedis> jedisPool() {
        JedisPoolConfig poolConfig = getJedisPoolConfig();

        if (StringUtils.hasText(password)) {
            log.info("Creating JedisPool with authentication - {}:{}", host, port);
            return new JedisPool(poolConfig, host, port, connectTimeout, timeout, password, database, null);
        } else {
            log.info("Creating JedisPool without authentication - {}:{}", host, port);
            return new JedisPool(poolConfig, host, port, connectTimeout, String.valueOf(timeout));
        }
    }

    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        // Basic pool settings
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));

        // Test settings
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);

        // Eviction settings
        poolConfig.setMinEvictableIdleTime(Duration.ofMillis(minEvictableIdleTimeMillis));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRunsMillis));
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);

        // Block when pool exhausted
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);

        // JMX
        poolConfig.setJmxEnabled(false);

        // Fairness - FIFO cho connections
        poolConfig.setFairness(true);

        log.debug("JedisPoolConfig - Max: {}, MaxIdle: {}, MinIdle: {}, MaxWait: {}ms",
                maxTotal, maxIdle, minIdle, maxWaitMillis);

        return poolConfig;
    }
}
