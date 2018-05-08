package com.dongweima.rpc.simple.demo.client;

import com.dongweima.demo.simple.rpc.EchoService;
import com.dongweima.rpc.client.AbstractRpcClient;
import com.dongweima.rpc.client.SimpleRpcClient;

public class SimpleRpcClientTest {

  @SuppressWarnings("all")
  public static void main(String[] args) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        AbstractRpcClient rpcClient = new SimpleRpcClient();
        String group = "simple";
        String version = "1.0.0";
        EchoService service = rpcClient
            .register(EchoService.class, group, version);
        for (int i = 0; i < 100; i++) {
          System.out.println(service.echo("localhost"));
        }
      }
    }).start();
    try {
      Thread.sleep(5000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
