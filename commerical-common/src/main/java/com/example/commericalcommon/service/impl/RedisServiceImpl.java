package com.example.commericalcommon.service.impl;

import com.example.commericalcommon.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    ObjectMapper objectMapper;
    Pool<Jedis> jedisPool;

    @Override
    public void hset(String key, String field, String value, Integer ttlSeconds) {
        try (Jedis jedis = getJedis()) {
            String jsonValue = objectMapper.writeValueAsString(value);
            jedis.hset(key, field, jsonValue);
            if (ttlSeconds != null && ttlSeconds > 0) {
                jedis.expire(key, ttlSeconds);
            }
        } catch (Exception e) {
            log.error("Error setting value in Redis", e);
        }
    }

    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        try (Jedis jedis = getJedis()){
            String value = jedis.hget(key, field);
            if (value != null) {
                return objectMapper.convertValue(value, clazz);
            } else return null;
        }catch (Exception e){
            log.error("Error getting value from Redis", e);
            return null;
        }
    }

    @Override
    public void hdel(String key, String field) {
        try (Jedis jedis = getJedis()) {
            jedis.hdel(key, field);
        } catch (Exception e) {
            log.error("Error deleting value from Redis", e);
        }
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }
}
