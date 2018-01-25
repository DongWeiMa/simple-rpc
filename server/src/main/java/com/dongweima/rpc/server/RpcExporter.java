package com.dongweima.rpc.server;

import com.dongweima.rpc.common.ByteSocketReadUtil;
import com.dongweima.rpc.common.RpcDTO;
import com.dongweima.rpc.serializer.Serialize;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
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

/**
 * @author dongweima
 */
public class RpcExporter {

  public static void exporter(String hostName, int port) throws Exception {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
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
    Executor executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, queue,
        threadFactory);
    ServerSocket server = new ServerSocket();
    server.bind(new InetSocketAddress(hostName, port));
    try {
      while (true) {
        Serialize serialize = SerializeFactory.getSerialize(SerializeEnum.FASTJSON);
        executor.execute(new ExporterTask(server.accept(), serialize));
      }
    } finally {

    }
  }

  private static class ExporterTask implements Runnable {

    private Serialize serialize;
    private Socket client = null;
    private static byte[] a = {(byte)-1} ;

    ExporterTask(Socket client, Serialize serialize) {
      this.client = client;
      this.serialize = serialize;
    }

    @Override
    public void run() {
      InputStream in = null;
      OutputStream out = null;
      try {
        in = client.getInputStream();
        byte[] bs = ByteSocketReadUtil.read(in);
        RpcDTO dto = serialize.deserialize(bs, RpcDTO.class);
        Class<?> service = Class.forName( dto.getInterfaceName());
        Method method = service.getMethod(dto.getMethodName(), dto.getParameterTypes());
        Object result = method.invoke(service.newInstance(), dto.getParams());
        out = client.getOutputStream();
        out.write(serialize.serialize(result));
        out.flush();
        
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
