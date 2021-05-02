package com.example.ntt.demorocketmq.mq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * 单向发送消息
 * 单向传输用于日志收集等可靠性要求较低的场景。
 * @author niutongtong
 */
public class OnewayProducer {

  public static final String GROUP_NAME="ntt1_group";
  public static final String NAME_ADDR="localhost:9876";
  public static final String TOPIC="TopicTest";
  public static final String TAG="TagC";

  public static void main(String[] args) {
    DefaultMQProducer defaultMQProducer = new DefaultMQProducer(GROUP_NAME);
    defaultMQProducer.setNamesrvAddr(NAME_ADDR);
    try {
      defaultMQProducer.start();
      for (int i = 0; i < 100; i++) {
        //Create a message instance, specifying topic, tag and message body.
        Message msg = new Message(TOPIC,
          TAG,
          ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
        );
        //Call send message to deliver message to one of brokers.
         defaultMQProducer.sendOneway(msg);
         Thread.sleep(5000);
      }
    } catch (MQClientException | UnsupportedEncodingException | RemotingException  | InterruptedException e) {
      e.printStackTrace();
    }finally {
      defaultMQProducer.shutdown();
    }
  }


}
