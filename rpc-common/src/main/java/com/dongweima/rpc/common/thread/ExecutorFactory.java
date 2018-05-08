package com.dongweima.rpc.common.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * todo 参数不要写死
 * @author dongweima
 */
public class ExecutorFactory {

  public static Executor getDefaultExecutor() {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
    ThreadFactory threadFactory = new RpcThreadFactory();
    return new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, queue,
        threadFactory);
  }

  //todo 上次 corePoolSize 设置为threadNumber ，max设置为threadNumber+20，发现线程没有自动创建，导致卡死，socker 只接受连接，但是分不出线程处理.
  public static Executor getExecutor(int threadNumber) {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(threadNumber + 30);
    ThreadFactory threadFactory = new RpcThreadFactory();
    return new ThreadPoolExecutor(threadNumber + 20, threadNumber + 30, 1000, TimeUnit.SECONDS,
        queue,
        threadFactory);
  }
}
