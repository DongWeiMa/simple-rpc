package com.dongweima.rpc.simple.demo.client;

import com.dongweima.demo.simple.rpc.EchoService;
import com.dongweima.demo.simple.rpc.impl.EchoServiceImpl;
import com.dongweima.rpc.client.SimpleRpcClient;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;

public class RpcClientTest {

  @SuppressWarnings("all")
  public static void main(String[] args){
    new Thread(new Runnable() {
      @Override
      public void run() {
        SimpleRpcClient rpcClient = new SimpleRpcClient();
        String group = "simple";
        String version = "1.0.0";
        EchoService service = rpcClient
            .register(EchoService.class,group,version);
        System.out.println(service.echo("localhost"));
      }
    }).start();
    try{
      Thread.sleep(1000000);
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
