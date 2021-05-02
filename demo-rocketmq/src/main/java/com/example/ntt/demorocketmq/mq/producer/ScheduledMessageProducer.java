package com.example.ntt.demorocketmq.mq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class ScheduledMessageProducer {


  public static void main(String[] args) {

    DefaultMQProducer producer = new DefaultMQProducer("nttProducerGroup");
    // Launch producer
    try {
      producer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }
    int totalMessagesToSend = 100;
    for (int i = 0; i < totalMessagesToSend; i++) {
      Message message = new Message("NttTopic", ("Hello scheduled message " + i).getBytes());
      // This message will be delivered to consumer 10 seconds later.
      message.setDelayTimeLevel(3);
      // Send the message
      try {
        producer.send(message);
      } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    // Shutdown producer after use.
    producer.shutdown();
  }

}
