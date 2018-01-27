package com.dongweima.rpc.client;

import com.dongweima.rpc.common.ByteSocketReadUtil;
import com.dongweima.rpc.common.RpcBaseParams;
import com.dongweima.rpc.common.dto.RpcDTO;
import com.dongweima.rpc.serializer.Serialize;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author dongweima
 */
public class SimpleRpcClient extends AbstractRpcClient {

  private Serialize serialize = SerializeFactory.getSerialize(SerializeEnum.FASTJSON);

  @SuppressWarnings("unchecked")
  @Override
  public <T> T register(Class<T> interfaceClass, String group, String version) {
    System.out.println("rpc client start");
    return (T) Proxy.newProxyInstance(
        interfaceClass.getClassLoader(),
        new Class<?>[]{
            interfaceClass
        }, new InterfaceProxy(group, version, interfaceClass.getName()));
  }

  @Override
  public SocketAddress getServerAddress(RpcBaseParams params) {
    return new InetSocketAddress("localhost", 8080);
  }

  @Override
  public Object communicateWithRpcServer(RpcDTO rpcDTO) {
    Socket socket;
    OutputStream out = null;
    InputStream in = null;
    try {
      socket = new Socket();
      socket.connect(getServerAddress(rpcDTO));
      out = socket.getOutputStream();
      out.write(serialize.serialize(rpcDTO));
      out.flush();
      in = socket.getInputStream();
      return serialize.deserialize(ByteSocketReadUtil.read(in), rpcDTO.getReturnType());
    } catch (Exception e) {

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
