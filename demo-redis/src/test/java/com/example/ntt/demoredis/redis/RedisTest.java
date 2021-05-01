package com.example.ntt.demoredis.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class RedisTest {

  @Resource
  private RedisTemplate<String,Object> redisTemplate;

  @Resource
  private StringRedisTemplate stringRedisTemplate;

  @Test
  public void  redis2() {
    ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
    stringStringValueOperations.set("key01","ntt");
    stringStringValueOperations.set("dsakdmas","asdasd",1000);

    System.out.println(stringStringValueOperations.get("key01"));
  }

  @Test
  public void  redis1() {

  }

}
