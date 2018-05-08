package com.dongweima.rpc.client;

import com.dongweima.registry.center.api.ZookeeperRegistry;
import com.dongweima.registry.center.api.bean.Consumer;
import com.dongweima.registry.center.api.bean.Entity;
import com.dongweima.rpc.common.RpcBaseParams;
import com.dongweima.rpc.common.dto.RpcDTO;
import com.dongweima.rpc.serializer.Serialize;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongweima create on 2018/1/26 1:09.
 */
public abstract class AbstractRpcClient implements Router {

  private static final Logger logger = LoggerFactory.getLogger(AbstractRpcClient.class);
  private Serialize serialize;
  private ZookeeperRegistry zookeeperRegistry;
  /**
   * 客户端信息
   */
  private Map<String, Consumer> consumerMap = new ConcurrentHashMap<>();

  AbstractRpcClient() {
    this(SerializeFactory.getSerialize(SerializeEnum.FASTJSON),
        new ZookeeperRegistry("localhost:2181"));
  }

  AbstractRpcClient(Serialize serialize,
      ZookeeperRegistry zookeeperRegistry) {
    this.serialize = serialize;
    this.zookeeperRegistry = zookeeperRegistry;
  }


  /**
   * client 注册，包括生成service的代理类，路由的初始化
   * 其中路由的初始化包括将remote信息同步到本地，并订阅变化
   */
  @SuppressWarnings("unchecked")
  public <T> T register(Class<T> interfaceClass, String group, String version) {
    logger.debug("rpc client start");
    Entity entity = new Entity(interfaceClass.getName(), group, version);
    if (consumerMap.get(entity.getService()) == null) {
      try {
        Consumer consumer = new Consumer(entity.getService(), null,
            zookeeperRegistry.getInitRouters(entity));
        consumerMap.put(entity.getService(), consumer);
      } catch (Exception e) {
        logger.warn("client获取路由失败 {}", entity.toString(), e);
      }
    }
    return (T) Proxy.newProxyInstance(
        interfaceClass.getClassLoader(),
        new Class<?>[]{
            interfaceClass
        }, new InterfaceProxy(group, version, interfaceClass.getName()));
  }

  public abstract Object communicateWithRpcServer(RpcDTO rpcDTO);

  //定义获取地址的接口.
  @Override
  public List<String> getRouters(RpcBaseParams rpcDTO) {
    Entity entity = new Entity(rpcDTO.getInterfaceName(), rpcDTO.getGroup(), rpcDTO.getVersion());
    Consumer consumer = consumerMap.get(entity.getService());
    if (consumer != null) {
      return new ArrayList<>(consumer.getProviders());
    }
    throw new RuntimeException("can found route");
  }

  Serialize getSerialize() {
    return serialize;
  }

  void setSerialize(Serialize serialize) {
    this.serialize = serialize;
  }

  ZookeeperRegistry getZookeeperRegistry() {
    return zookeeperRegistry;
  }

  void setZookeeperRegistry(ZookeeperRegistry zookeeperRegistry) {
    this.zookeeperRegistry = zookeeperRegistry;
  }

  class InterfaceProxy implements InvocationHandler {

    private String group;
    private String version;
    private String interfaceName;

    InterfaceProxy(String group, String version, String interfaceName) {
      this.interfaceName = interfaceName;
      this.group = group;
      this.version = version;

    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
      RpcDTO rpcDTO = new RpcDTO();
      rpcDTO.setGroup(group);
      rpcDTO.setVersion(version);
      rpcDTO.setInterfaceName(interfaceName);
      rpcDTO.setMethodName(method.getName());
      rpcDTO.setParameterTypes(method.getParameterTypes());
      rpcDTO.setReturnType(method.getReturnType());
      rpcDTO.setParams(params);
      return communicateWithRpcServer(rpcDTO);
    }
  }
}
