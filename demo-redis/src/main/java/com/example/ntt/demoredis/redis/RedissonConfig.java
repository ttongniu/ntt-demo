package com.example.ntt.demoredis.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author niutongtong
 */
@Configuration
public class RedissonConfig {

  @Value("${spring.redis.password}")
  private String password;

  /**
   * RedissonClient,单机模式
   * @return
   * @throws IOException
   */
  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson() throws IOException {
    RedissonClient redisson = Redisson.create(
      Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
      return redisson;
  }

  @Bean
  public RedissonLocker redissonLocker(RedissonClient redissonClient){
    RedissonLocker locker = new RedissonLocker(redissonClient);
    //设置LockUtil的锁处理对象
    LockUtil.setLocker(locker);
    return locker;
  }


}
