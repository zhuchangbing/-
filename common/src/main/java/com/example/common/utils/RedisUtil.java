package com.example.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.util.Set;

/**
 * 这是一个用来访问Redis数据库的工具类
 * 提供保存数据、查询数据、更新数据、删除数据
 * 缓存数据主要就是Java中的对象，采用JSON的方式进行缓存
 */

@Component
public class RedisUtil {
    /**
     * redisTemplate.opsForValue(); //操作字符串
     * redisTemplate.opsForHash();
     * redisTemplate.opsForList();
     * redisTemplate.opsForSet();
     * redisTemplate.opsForZSet();
     */
    @Autowired   //自动注入 根据
    private RedisTemplate redisTemplate;

    /**
     * 写入Redis数据库
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        // 尝试写入value
        try {
            ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
            ops.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean set(String key, Object value, Duration duration) {
        try {
            ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
            ops.set(key, value, duration);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Redis setnx
     */
    public boolean setnx(String key, Object value) {
        try {
            ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
            ops.setIfAbsent(key, value, Duration.ofSeconds(5));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    /**
     * @param key
     * @return
     */

    public Object get(String key) {
        ValueOperations<Serializable, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public boolean deletePattern(String pattern) {
        //根据规则获取所有符合的key
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
            return true;
        }
        return false;
    }

}
















