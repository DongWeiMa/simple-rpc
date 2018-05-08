package com.dongweima.rpc.client;

import com.dongweima.rpc.common.RpcBaseParams;
import com.dongweima.rpc.common.dto.RpcDTO;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author dongweima
 */
public class NioRpcClient extends AbstractRpcClient {

  @Override
  public Object communicateWithRpcServer(RpcDTO rpcDTO) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(128);
    SocketChannel client = null;
    try {
      client = SocketChannel.open();
      if (client.isOpen()) {
        client.configureBlocking(true);
        client.setOption(StandardSocketOptions.SO_RCVBUF, 128);
        client.setOption(StandardSocketOptions.SO_SNDBUF, 128);
        client.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
        client.setOption(StandardSocketOptions.SO_LINGER, 5);
        client.connect(getServerAddress(rpcDTO));
        if (client.isConnected()) {
          byte[] serializeBytes = getSerialize().serialize(rpcDTO);
          ByteBuffer byteBuffer = ByteBuffer.allocateDirect(serializeBytes.length);
          byteBuffer.put(serializeBytes);
          byteBuffer.flip();
          client.write(byteBuffer);
          byteBuffer.clear();
          byte[] bytes = new byte[128];
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          int len;
          while ((len = client.read(buffer)) != -1) {
            buffer.flip();
            buffer.get(bytes);
            byteArrayOutputStream.write(bytes, 0, len);
            buffer.clear();
            if (len < 128) {
              break;
            }

          }
          byte[] b = byteArrayOutputStream.toByteArray();
          return getSerialize().deserialize(b, rpcDTO.getReturnType());
        }
      }
      throw new RuntimeException("client socket cannot open");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (client != null) {
        try {
          client.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  @Override
  public SocketAddress getServerAddress(RpcBaseParams params) {
    return new InetSocketAddress("127.0.0.1", 8080);
  }
}
