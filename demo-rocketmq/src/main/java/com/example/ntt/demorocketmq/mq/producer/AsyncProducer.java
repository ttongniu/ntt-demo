package com.example.ntt.demorocketmq.mq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * 异步发送消息
 * @author niutongtong
 */
public class AsyncProducer {

  public static final String GROUP_NAME="ntt2_group";
  public static final String NAME_ADDR="localhost:9876";
  public static final String TOPIC="TopicTest";
  public static final String TAG="TagB";

  public static void main(String[] args) {
    DefaultMQProducer producer = new DefaultMQProducer(GROUP_NAME);
    // Specify name server addresses.
    producer.setNamesrvAddr(NAME_ADDR);
    //Launch the instance.
    try {
      producer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }
    producer.setRetryTimesWhenSendAsyncFailed(0);

    int messageCount = 100;
    final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
    for (int i = 0; i < messageCount; i++) {
      try {
        final int index = i;
        Message msg = new Message(TOPIC,
          TAG,
          "OrderID188",
          "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.send(msg, new SendCallback() {
          @Override
          public void onSuccess(SendResult sendResult) {
            countDownLatch.countDown();
            System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
          }

          @Override
          public void onException(Throwable e) {
            countDownLatch.countDown();
            System.out.printf("%-10d Exception %s %n", index, e);
            e.printStackTrace();
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      countDownLatch.await(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }finally {
      producer.shutdown();
    }

  }


}
