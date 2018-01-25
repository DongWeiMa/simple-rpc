package com.dongweima.rpc.client;

import com.dongweima.rpc.common.ByteSocketReadUtil;
import com.dongweima.rpc.common.RpcDTO;
import com.dongweima.rpc.serializer.Serialize;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
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
  public S getProxyService(final Class<?> serviceClass, Serialize serialize) {

    return (S) Proxy.newProxyInstance(
        serviceClass.getClassLoader(),
        new Class<?>[]{
            serviceClass.getInterfaces()[0]
        }, new ServiceProxy(serviceClass.getName(), serialize));
  }

  class ServiceProxy implements InvocationHandler {
    private byte[] a ={(byte)-1};
    private String interfaceName;
    private InetSocketAddress address;
    private Serialize serialize;
    /**
     * todo 以后可以加上
     */
    private String group;
    private String version;


    ServiceProxy(String interfaceName, Serialize serialize) {
      this.interfaceName = interfaceName;
      this.serialize = serialize;
      this.address = new InetSocketAddress("localhost", 8080);
    }

    ServiceProxy(String interfaceName, String group, String version, Serialize serialize) {
      this.group = group;
      this.version = version;
      this.interfaceName = interfaceName;
      this.serialize = serialize;
      this.address = new InetSocketAddress("localhost", 8080);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
      Socket socket;
      OutputStream out = null;
      InputStream in = null;
      try {
        socket = new Socket();
        socket.connect(address);
        out = socket.getOutputStream();
        RpcDTO rpcDTO = new RpcDTO();
        rpcDTO.setInterfaceName(interfaceName);
        rpcDTO.setMethodName(method.getName());
        rpcDTO.setParameterTypes(method.getParameterTypes());
        rpcDTO.setParams(params);
        out.write(serialize.serialize(rpcDTO));
        out.flush();
        in = socket.getInputStream();
        return serialize.deserialize(ByteSocketReadUtil.read(in),method.getReturnType());
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
