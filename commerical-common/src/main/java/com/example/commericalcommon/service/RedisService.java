package com.example.commericalcommon.service;

public interface RedisService {
    void hset(String key, String field, String value, Integer ttlSeconds);

    <T> T hget(String key, String field, Class<T> clazz);

    void hdel(String key, String field);
}
