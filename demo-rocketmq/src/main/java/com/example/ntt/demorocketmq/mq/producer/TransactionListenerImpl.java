package com.example.ntt.demorocketmq.mq.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现TransactionListener接口
 * 当发送一半消息成功时，“executeLocalTransaction”方法用于执行本地事务。它返回前一节中提到的三个事务状态中的一个。
 * “checkLocalTransaction”方法用于检查本地事务状态并响应MQ检查请求。它还返回前一节中提到的三个事务状态中的一个。
 *
 * @author niutongtong
 */
public class TransactionListenerImpl implements TransactionListener {
  private AtomicInteger transactionIndex = new AtomicInteger(0);

  private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

  @Override
  public LocalTransactionState executeLocalTransaction(Message message, Object o) {
    return null;
  }

  @Override
  public LocalTransactionState checkLocalTransaction(MessageExt msg) {
    Integer status = localTrans.get(msg.getTransactionId());
    if (null != status) {
      switch (status) {
        case 0:
          return LocalTransactionState.UNKNOW;
        case 1:
          return LocalTransactionState.COMMIT_MESSAGE;
        case 2:
          return LocalTransactionState.ROLLBACK_MESSAGE;
        default:
          throw new IllegalStateException("Unexpected value: " + status);
      }
    }
    return LocalTransactionState.COMMIT_MESSAGE;
  }

}
