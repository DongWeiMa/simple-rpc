package com.dongweima.rpc.server;

import com.dongweima.rpc.common.ByteSocketReadUtil;
import com.dongweima.rpc.common.RpcInterfaceRegister;
import com.dongweima.rpc.common.RpcRegiester;
import com.dongweima.rpc.common.dto.RpcDTO;
import com.dongweima.rpc.common.thread.ExecutorFactory;
import com.dongweima.rpc.serializer.Serialize;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * @author dongweima
 */
public class SimpleRpcServerImpl implements RpcServer<Socket> {

  private RpcRegiester rpcRegiester;
  private Executor executor;
  private Serialize serialize = SerializeFactory.getSerialize(SerializeEnum.FASTJSON);

  @Override
  public void register(int port, RpcRegiester rpcRegiester) {
    this.rpcRegiester = rpcRegiester;
    executor = ExecutorFactory.getDefaultExecutor();
    try {
      ServerSocket server = new ServerSocket();
      server.bind(new InetSocketAddress("localhost",port));
      System.out.println("rpc server start");
      while (true) {
        resolveRpcClientRequest(server.accept());
      }
    }catch (Exception e){
      e.printStackTrace();
    }finally {

    }
  }

  @Override
  public void resolveRpcClientRequest(Socket socket) {
    executor.execute(new ExporterTask(socket));
  }

  private class ExporterTask implements Runnable {

    private Socket client;

    ExporterTask(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {
      InputStream in = null;
      OutputStream out = null;
      try {
        in = client.getInputStream();
        Object result = dealInput(in);
        out = client.getOutputStream();
        dealOutput(out, result);
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

    private Object dealInput(InputStream in) throws Exception {
      byte[] bs = ByteSocketReadUtil.read(in);
      RpcDTO dto = serialize.deserialize(bs, RpcDTO.class);
      RpcInterfaceRegister reg = rpcRegiester
          .getRegiesterMessage(dto.getInterfaceName(), dto.getGroup(), dto.getVersion());
      Class<?> service = Class.forName(reg.getImplementClassName());
      //这里有个问题 就是该rpc能够访问所有的服务端的class的方法
      //而真正的rpc仅能访问自己配置的接口
      Method method = service.getMethod(dto.getMethodName(), dto.getParameterTypes());
      return method.invoke(service.newInstance(), dto.getParams());
    }

    private void dealOutput(OutputStream out, Object result) throws Exception {
      out.write(serialize.serialize(result));
      out.flush();
    }
  }
}
