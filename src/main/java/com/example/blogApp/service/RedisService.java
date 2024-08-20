package com.example.blogApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public void set(String value, Object o, Long ttl){
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            String jsonValue=objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(value, jsonValue, ttl, TimeUnit.SECONDS);
        }catch (Exception e){
//            log.error("Exception found in redis service"+e);
            System.out.print("Exception found in redis Service" +e);
        }
    }
    public<T> T get(String key, Class<T> entityClass){
        try {
            Object o=redisTemplate.opsForValue().get(key);
            if(o==null) return null;
            ObjectMapper mapper=new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

