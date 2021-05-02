package com.example.ntt.demorocketmq.mq.comsumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
/**
 * @author niutongtong
 */
public class Consumer {

  public static void main(String[] args) {
    // Instantiate with specified consumer group name.
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ntt1_group");

    // Specify name server addresses.
    consumer.setNamesrvAddr("localhost:9876");

    // Subscribe one more more topics to consume.
    try {
      consumer.subscribe("TopicTest", "*");
    } catch (MQClientException e) {
      e.printStackTrace();
    }
    // Register callback to execute on arrival of messages fetched from brokers.
    consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
      System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });

    //Launch the consumer instance.
    try {
      consumer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }

    System.out.printf("Consumer Started.%n");
  }


}
