package com.dongweima.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author dongweima
 */
public class RpcImporter<S> {

  @SuppressWarnings("unchecked")
  public S getProxyService(final Class<?> serviceClass) {
    return (S) Proxy.newProxyInstance(
        serviceClass.getClassLoader(),
        new Class<?>[]{
            serviceClass.getInterfaces()[0]
        }, new ServiceProxy(serviceClass.getName()));
  }

  class ServiceProxy implements InvocationHandler {

    private String interfaceName;
    private InetSocketAddress address;
    /**
     * todo 以后可以加上
     */
    private String group;
    private String version;


    ServiceProxy(String interfaceName) {
      this.interfaceName = interfaceName;
      this.address = address;
      this.address = new InetSocketAddress("localhost", 8080);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Socket socket;
      ObjectOutputStream out = null;
      ObjectInputStream in = null;
      try {
        socket = new Socket();
        socket.connect(address);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeUTF(interfaceName);
        out.writeUTF(method.getName());
        out.writeObject(method.getParameterTypes());
        out.writeObject(args);
        in = new ObjectInputStream(socket.getInputStream());
        return in.readObject();
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
      }
      return null;
    }
  }
}
