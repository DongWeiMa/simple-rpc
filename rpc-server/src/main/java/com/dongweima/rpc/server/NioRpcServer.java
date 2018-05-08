package com.dongweima.rpc.server;

import com.dongweima.rpc.common.RpcRegiester;
import com.dongweima.rpc.common.thread.ExecutorFactory;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongweima
 */
public class NioRpcServer extends AbstractRpcServer<SocketChannel> {

  private static final Logger logger = LoggerFactory.getLogger(NioRpcServer.class);
  @Override
  public void register(int port, RpcRegiester rpcRegiester) {
    this.setRpcRegiester( rpcRegiester);
    this.setExecutor(ExecutorFactory.getDefaultExecutor());
    try {
      ServerSocketChannel server = ServerSocketChannel.open();
      if (server.isOpen()) {
        server.configureBlocking(true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 128);
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.bind(new InetSocketAddress("127.0.0.1", 8080));
        logger.debug("rpc server start");
        while (true) {
          resolveRpcClientRequest(server.accept());
        }
      } else {
        throw new RuntimeException("server socket not cannot open");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void resolveRpcClientRequest(final SocketChannel socketChannel) {
    getExecutor().execute(new Runnable() {
      @Override
      public void run() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(128);
        byte[] b = new byte[128];
        try {
          int len = 0;
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          while ((len = socketChannel.read(buffer)) != -1) {
            buffer.flip();
            buffer.get(b);
            out.write(b, 0, len);
            if (buffer.limit() < 128) {
              break;
            }
            buffer.clear();
          }
          byte[] bs = out.toByteArray();
          Object result = dealInput(bs);
          byte[] bytes = getSerialize().serialize(result);
          ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
          byteBuffer.put(bytes);
          socketChannel.write(byteBuffer);
        } catch (Exception e) {
          e.printStackTrace();
        }
        try{
          socketChannel.close();
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });

  }
}
