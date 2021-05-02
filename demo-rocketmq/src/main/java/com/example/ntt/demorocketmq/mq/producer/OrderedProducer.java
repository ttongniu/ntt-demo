package com.example.ntt.demorocketmq.mq.producer;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 *
 *
 * @author niutongtong
 */
public class OrderedProducer {

  public static final String GROUP_NAME="ntt13_group";
  public static final String TOPIC="TopicTest";


  public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

    MQProducer producer = new DefaultMQProducer(GROUP_NAME);
    try {
      producer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }

    String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
    for (int i = 0; i < 100; i++) {
      int orderId = i % 10;
      //Create a message instance, specifying topic, tag and message body.
      Message msg = null;
      try {
        msg = new Message(TOPIC, tags[i % tags.length], "KEY" + i,
          ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      SendResult sendResult = producer.send(msg, (mqs, msg1, arg) -> {
        Integer id = (Integer) arg;
        int index = id % mqs.size();
        return mqs.get(index);
      }, orderId);

      System.out.printf("%s%n", sendResult);
    }
     producer.shutdown();
  }

}
