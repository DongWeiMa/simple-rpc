package com.dongweima.rpc.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcExporter {

  static Executor executor;

  public static void exporter(String hostName, int port) throws Exception {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(20);
    ThreadFactory threadFactory = new ThreadFactory() {
      private int count = 0;

      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("rpc-thread" + count);
        synchronized (this) {
          count++;
        }
        return thread;
      }
    };
    executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, queue, threadFactory);
    ServerSocket server = new ServerSocket();
    server.bind(new InetSocketAddress(hostName, port));
    try {
      while (true) {
        executor.execute(new ExporterTask(server.accept()));
      }
    } finally {

    }
  }

  private static class ExporterTask implements Runnable {

    Socket client = null;

    ExporterTask(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {
      ObjectInputStream in = null;
      ObjectOutputStream out = null;
      try {
        in = new ObjectInputStream(client.getInputStream());
        
        String interfaceName = in.readUTF();

        Class<?> service = Class.forName(interfaceName);

        String methodName = in.readUTF();
        Class<?>[] parameterTypes = (Class<?>[]) in.readObject();


        Object[] params = (Object[]) in.readObject();
        Method method = service.getMethod(methodName, parameterTypes);
        Object result = method.invoke(service.newInstance(), params);
        out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(result);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        if (out != null) {
          try {
            out.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        if (client != null) {
          try {
            client.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }
    }
  }
}
