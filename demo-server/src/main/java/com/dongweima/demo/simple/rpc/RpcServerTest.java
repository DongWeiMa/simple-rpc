package com.dongweima.demo.simple.rpc;

import com.dongweima.rpc.server.RpcExporter;

public class RpcServerTest {
  public static void main(String[] args)throws Exception{
      new Thread(new Runnable() {
        public void run() {
          try{
            RpcExporter.exporter("127.0.0.1",8080);
          }catch (Exception e){
            e.printStackTrace();
          }

        }
      }).start();
      Thread.sleep(10000000);
  }
}
