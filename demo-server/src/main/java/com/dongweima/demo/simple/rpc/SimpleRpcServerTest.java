package com.dongweima.demo.simple.rpc;

import com.dongweima.demo.simple.rpc.impl.EchoServiceImpl;
import com.dongweima.rpc.common.RpcInterfaceRegister;
import com.dongweima.rpc.common.RpcRegiester;
import com.dongweima.rpc.common.thread.RpcThreadFactory;
import com.dongweima.rpc.server.AbstractRpcServer;
import com.dongweima.rpc.server.SimpleRpcServerImpl;
import java.net.Socket;

public class SimpleRpcServerTest {

  public static void main(String[] args) throws Exception {
    RpcThreadFactory factory = new RpcThreadFactory();
    factory.newThread(new Runnable() {
      @Override
      public void run() {
        try {
          String group = "simple";
          String version = "1.0.0";
          AbstractRpcServer<Socket> rpcServer = new SimpleRpcServerImpl();
          RpcRegiester rpcRegiester = new RpcRegiester();
          RpcInterfaceRegister rpcInterfaceRegister = new RpcInterfaceRegister();
          rpcInterfaceRegister.setGroup(group);
          rpcInterfaceRegister.setVersion(version);
          rpcInterfaceRegister.setIntefaceName(EchoService.class.getName());
          rpcInterfaceRegister.setImplementClassName(EchoServiceImpl.class.getName());
          rpcRegiester.interfaceRegiest(rpcInterfaceRegister);
          rpcServer.register(9, rpcRegiester);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    }).start();
    Thread.sleep(10000000);
  }
}
