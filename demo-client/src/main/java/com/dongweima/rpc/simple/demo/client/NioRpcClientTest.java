package com.dongweima.rpc.simple.demo.client;

import com.dongweima.demo.simple.rpc.EchoService;
import com.dongweima.rpc.client.AbstractRpcClient;
import com.dongweima.rpc.client.NioRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongweima
 */
public class NioRpcClientTest {

  private static final Logger logger = LoggerFactory.getLogger(NioRpcClientTest.class);

  @SuppressWarnings("all")
  public static void main(String[] args) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        AbstractRpcClient rpcClient = new NioRpcClient();
        String group = "simple";
        String version = "1.0.0";
        EchoService service = rpcClient
            .register(EchoService.class, group, version);
        logger.info(service.echo("localhost"));
      }
    }).start();
    try {
      Thread.sleep(1000000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
