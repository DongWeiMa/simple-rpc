package com.dongweima.rpc.server;

import static com.dongweima.registry.center.api.ZookeeperRegistry.PROVIDER;

import com.dongweima.registry.center.api.bean.Entity;
import com.dongweima.rpc.common.AddressUtil;
import com.dongweima.rpc.common.ByteSocketReadUtil;
import com.dongweima.rpc.common.RpcInterfaceRegister;
import com.dongweima.rpc.common.RpcRegiester;
import com.dongweima.rpc.common.thread.ExecutorFactory;
import io.netty.util.internal.ConcurrentSet;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

/**
 * @author dongweima
 */
public class SimpleRpcServerImpl extends AbstractRpcServer<Socket> {

  private Set<Integer> ports = new ConcurrentSet<>();
  @Override
  public void register(int threadNumber, RpcRegiester rpcRegiester) {
    this.setRpcRegiester(rpcRegiester);
    this.setExecutor(ExecutorFactory.getExecutor(threadNumber));
    for (int i = 0; i < threadNumber; i++) {
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          //多线程去跑
          try {
            ServerSocket server = new ServerSocket();
            Collection<RpcInterfaceRegister> interfaceRegisters = getRpcRegiester().getAll();
            //port 随机生成 在10000-20000
            Random random = new Random();
            for (RpcInterfaceRegister interfaceRegister : interfaceRegisters) {
              Entity entity = new Entity();
              String host = AddressUtil.getIntranetIp();
              Integer port = random.nextInt(20000);
              while (ports.contains(port) || port < 10000) {
                port = random.nextInt(20000);
              }
              server.bind(new InetSocketAddress(host, port));

              entity.setType(PROVIDER);
              entity.setNode(host + ":" + port);
              entity.setService(
                  interfaceRegister.getIntefaceName() + ":"
                      + interfaceRegister.getGroup() + ":"
                      + interfaceRegister.getVersion());
              getZookeeperRegistry().register(entity);
              Thread.currentThread().setName(host + ":" + port);
              System.out.println("rpc server start in " + host + ":" + port);
            }

            while (true) {
              resolveRpcClientRequest(server.accept());
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      getExecutor().execute(runnable);
    }

  }

  @Override
  public void resolveRpcClientRequest(Socket socket) {
    this.getExecutor().execute(new ExporterTask(socket));
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
        byte[] bs = ByteSocketReadUtil.read(in);
        Object result = dealInput(bs);
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

    private void dealOutput(OutputStream out, Object result) throws Exception {
      out.write(getSerialize().serialize(result));
      out.flush();
    }
  }
}
