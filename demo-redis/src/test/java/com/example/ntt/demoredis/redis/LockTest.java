package com.example.ntt.demoredis.redis;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LockTest {

  @Test
  public void lock() {
    //加锁
    LockUtil.lock("KEY");
    try {
      //TODO 干事情
    } catch (Exception e) {
      //异常处理
    }finally{
      //释放锁
      LockUtil.unlock("KEY");
    }
  }

}
