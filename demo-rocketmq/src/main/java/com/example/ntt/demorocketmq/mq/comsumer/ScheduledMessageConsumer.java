package com.example.ntt.demorocketmq.mq.comsumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
/**
 * @author niutongtong
 */
public class ScheduledMessageConsumer {


  public static void main(String[] args) {
    // Instantiate message consumer
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("nttProducerGroup");
    // Subscribe topics
    try {
      consumer.subscribe("NttTopic", "*");
    } catch (MQClientException e) {
      e.printStackTrace();
    }
    // Register message listener
    consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
      for (MessageExt message : messages) {
        // Print approximate delay time period
        System.out.println("Receive message[msgId=" + message.getMsgId() + "] "
          + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    // Launch consumer
    try {
      consumer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }
  }
}
