package com.dongweima.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;


public class RpcImporter<S> {
  public  S importer(final Class<?> serviceClass,final InetSocketAddress address){
    return (S) Proxy.newProxyInstance(
        serviceClass.getClassLoader(),
        new Class<?>[]{
            serviceClass.getInterfaces()[0]
        }, new InvocationHandler() {
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Socket socket = null;
            ObjectOutputStream out =  null;
            ObjectInputStream in = null;
            try{
              socket = new Socket();
              socket.connect(address);
              out = new ObjectOutputStream(socket.getOutputStream());
              out.writeUTF(serviceClass.getName());
              out.writeUTF(method.getName());
              out.writeObject(method.getParameterTypes());
              out.writeObject(args);
              in =new ObjectInputStream(socket.getInputStream());
              return in.readObject();
            }catch (Exception e){
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
            }
            return null;
          }
        });

  }
}
