package com.dongweima.rpc.server;

import com.dongweima.registry.center.api.ZookeeperRegistry;
import com.dongweima.rpc.common.RpcInterfaceRegister;
import com.dongweima.rpc.common.RpcRegiester;
import com.dongweima.rpc.common.dto.RpcDTO;
import com.dongweima.rpc.common.thread.ExecutorFactory;
import com.dongweima.rpc.serializer.Serialize;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @author dongweima
 */
public abstract class AbstractRpcServer<T> {

  private ZookeeperRegistry zookeeperRegistry;
  private Executor executor;
  private Serialize serialize;
  private RpcRegiester rpcRegiester;

  public AbstractRpcServer() {
    this(
        ExecutorFactory.getDefaultExecutor(),
        SerializeFactory.getSerialize(SerializeEnum.FASTJSON),
        new ZookeeperRegistry("localhost:2181"));
  }

  public AbstractRpcServer(Executor executor,
      Serialize serialize, ZookeeperRegistry zookeeperRegistry) {
    this.executor = executor;
    this.serialize = serialize;
    this.zookeeperRegistry = zookeeperRegistry;
  }

  /**
   * 一次性注册所有服务.
   * @param rpcRegiester  注册信息
   */
  public abstract void register(int threadNumber, RpcRegiester rpcRegiester);

  /**
   * 解析rpcClient的请求.
   * @param t rpcClient的请求方式 可能是bio,可能是nio,也可能是其他.
   */
  abstract void resolveRpcClientRequest(T t);

  Object dealInput(byte[] bs) throws Exception {
    RpcDTO dto = serialize.deserialize(bs, RpcDTO.class);
    RpcInterfaceRegister reg = rpcRegiester
        .getRegiesterMessage(dto.getInterfaceName(), dto.getGroup(), dto.getVersion());
    Class<?> service = Class.forName(reg.getImplementClassName());
    //这里有个问题 就是该rpc能够访问所有的服务端的class的方法
    //而真正的rpc仅能访问自己配置的接口
    Method method = service.getMethod(dto.getMethodName(), dto.getParameterTypes());
    return method.invoke(service.newInstance(), dto.getParams());
  }


  public RpcRegiester getRpcRegiester() {
    return rpcRegiester;
  }

  public void setRpcRegiester(RpcRegiester rpcRegiester) {
    this.rpcRegiester = rpcRegiester;
  }

  public Executor getExecutor() {
    return executor;
  }

  public void setExecutor(Executor executor) {
    this.executor = executor;
  }

  public Serialize getSerialize() {
    return serialize;
  }

  public void setSerialize(Serialize serialize) {
    this.serialize = serialize;
  }

  public ZookeeperRegistry getZookeeperRegistry() {
    return zookeeperRegistry;
  }

  public void setZookeeperRegistry(ZookeeperRegistry zookeeperRegistry) {
    this.zookeeperRegistry = zookeeperRegistry;
  }
}
