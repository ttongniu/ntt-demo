package com.example.ntt.demorocketmq.mq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 *1、交易状态
 *
 * 事务性消息有三种状态:
 * (1) TransactionStatus。CommitTransaction:提交事务，表示允许使用者使用此消息。
 * (2) TransactionStatus。RollbackTransaction:回滚事务，这意味着消息将被删除，不允许使用。
 * (3) TransactionStatus。Unknown:中间状态，这意味着MQ需要回检查以确定状态。
 *
 * 发送事务性消息
 *
 * (1)创建事务性生产者
 * 使用TransactionMQProducer类来创建生产者客户机，并指定惟一的producerGroup，您可以设置一个自定义线程池来处理检查请求。执行本地事务后，需要根据执行结果对MQ进行应答，应答状态在上一节中进行了说明。
 *
 * @author niutongtong
 */
public class TransactionProducer {

  public static void main(String[] args) throws InterruptedException {

    TransactionListener transactionListener = new TransactionListenerImpl();
    TransactionMQProducer producer = new TransactionMQProducer("please_rename_unique_group_name");
    ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("client-transaction-msg-check-thread");
        return thread;
      }
    });

    producer.setExecutorService(executorService);
    producer.setTransactionListener(transactionListener);
    try {
      producer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }

    String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
    for (int i = 0; i < 10; i++) {
      try {
        Message msg =
          new Message("TopicTest1234", tags[i % tags.length], "KEY" + i,
            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = producer.sendMessageInTransaction(msg, null);
        System.out.printf("%s%n", sendResult);

        Thread.sleep(10);
      } catch (MQClientException | UnsupportedEncodingException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    for (int i = 0; i < 100000; i++) {
      Thread.sleep(1000);
    }
    producer.shutdown();


  }

}
