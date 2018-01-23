package com.dongweima.rpc.simple.demo.client;

import com.dongweima.demo.simple.rpc.EchoService;
import com.dongweima.demo.simple.rpc.impl.EchoServiceImpl;
import com.dongweima.rpc.client.RpcImporter;

public class RpcClientTest {

  @SuppressWarnings("all")
  public static void main(String[] args) throws Exception {
    new Thread(new Runnable() {
      @Override
      public void run() {
        RpcImporter<EchoService> rpcImporter = new RpcImporter<EchoService>();
        EchoService echoService = rpcImporter
            .getProxyService(EchoServiceImpl.class);
        System.out.println(echoService.echo("localhost"));
      }
    }).start();
    Thread.sleep(1000000);
  }
}
