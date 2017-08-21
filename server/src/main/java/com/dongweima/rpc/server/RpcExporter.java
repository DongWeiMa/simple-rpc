package com.dongweima.rpc.server;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RpcExporter  {
  static  Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public static void exporter(String hostName,int port)throws Exception{
    ServerSocket server  = new ServerSocket();
    server.bind(new InetSocketAddress(hostName,port));
    try{
      while (true){
        executor.execute(new ExporterTask(server.accept()));
      }
    }finally {

    }
  }
  
  private static class ExporterTask implements Runnable{

    Socket client = null;
    public ExporterTask(Socket client){
      this.client = client;
    }
  
    public void run() {
      ObjectInputStream in = null;
      ObjectOutputStream out = null;
      try{
        in = new ObjectInputStream(client.getInputStream());
        String interfaceName = in.readUTF();
        Class<?> service = Class.forName(interfaceName);
        String methodName = in.readUTF();
        Class<?>[] parameterTypes = (Class<?>[])in.readObject();
        Object[] params = (Object[]) in.readObject();
        Method method = service.getMethod(methodName,parameterTypes);
        Object result = method.invoke(service.newInstance(),params);
        out = new ObjectOutputStream(client.getOutputStream()) ;
        out.writeObject(result);
      } catch (Exception e){
        e.printStackTrace();
      }finally {
        if(in!=null){
          try{
            in.close();
          } catch (Exception e){
            e.printStackTrace();
          }
        }
        if(out!=null){
          try{
            out.close();
          } catch (Exception e){
            e.printStackTrace();
          }
        }
        if (client!=null){
          try{
            client.close();
          }catch (Exception e){
            e.printStackTrace();
          }
        }

      }
    }
  }
}
