package com.dongweima.rpc.simple.demo.client;

import com.dongweima.demo.simple.rpc.EchoService;
import com.dongweima.demo.simple.rpc.impl.EchoServiceImpl;
import com.dongweima.rpc.client.RpcImporter;
import java.net.InetSocketAddress;
import javax.xml.ws.WebServiceException;

public class RpcClientTest {
  
  public static void main(String[] args)throws Exception{
    new Thread(new Runnable() {
      public void run() {
        RpcImporter<EchoService> rpcImporter = new RpcImporter<EchoService>();
        EchoService echoService = rpcImporter
            .importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8080));
        System.out.println(echoService.echo("localhost"));
      }
    }).start();
    Thread.sleep(1000000);
  }
}
