package com.dongweima.rpc.client;

import com.dongweima.registry.center.api.ZookeeperRegistry;
import com.dongweima.rpc.common.ByteSocketReadUtil;
import com.dongweima.rpc.common.RpcBaseParams;
import com.dongweima.rpc.common.dto.RpcDTO;
import com.dongweima.rpc.serializer.Serialize;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongweima
 */
public class SimpleRpcClient extends AbstractRpcClient {

  private static final Logger logger = LoggerFactory.getLogger(SimpleRpcClient.class);
  private static Random random = new Random();

  public SimpleRpcClient() {
    super();
  }

  public SimpleRpcClient(Serialize serialize,
      ZookeeperRegistry zookeeperRegistry) {
    super(serialize, zookeeperRegistry);
  }

  @Override
  public SocketAddress getServerAddress(RpcBaseParams params) {
    List<String> routers = getRouters((RpcDTO) params);
    int len = routers.size();
    String chooseRouter = routers.get(random.nextInt(len));
    String[] hp = chooseRouter.split(":");
    logger.debug("client connect to " + chooseRouter);
    return new InetSocketAddress(hp[0], Integer.valueOf(hp[1]));
  }

  @Override
  public Object communicateWithRpcServer(RpcDTO rpcDTO) {
    Socket socket;
    OutputStream out = null;
    InputStream in = null;
    try {
      socket = new Socket();
      socket.connect(getServerAddress(rpcDTO));
      out = socket.getOutputStream();
      out.write(getSerialize().serialize(rpcDTO));
      out.flush();
      in = socket.getInputStream();
      return getSerialize().deserialize(ByteSocketReadUtil.read(in), rpcDTO.getReturnType());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
    return null;
  }
}
