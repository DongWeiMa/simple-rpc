package com.dongweima.rpc.common.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dongweima create on 2018/1/26 0:49.
 */
public class ExecutorFactory {

  public static Executor getDefaultExecutor() {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
    ThreadFactory threadFactory = new RpcThreadFactory();
    return new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, queue,
        threadFactory);
  }
}
