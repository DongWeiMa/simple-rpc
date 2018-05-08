package com.dongweima.rpc.common.thread;

import java.util.concurrent.ThreadFactory;

/**
 * @author dongweima
 */
public class RpcThreadFactory implements ThreadFactory {

  private static volatile int count = 0;

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r);
    thread.setName("rpc-thread" + count);
    synchronized (this) {
      count++;
    }
    return thread;
  }
}
