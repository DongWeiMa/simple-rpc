package com.dongweima.rpc.client;

import com.dongweima.rpc.common.RpcBaseParams;
import com.dongweima.rpc.common.dto.RpcDTO;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketAddress;

/**
 * @author dongweima create on 2018/1/26 1:09.
 */
public abstract class AbstractRpcClient {

  public abstract <T> T register(Class<T> interfaceClass, String group, String version);

  public abstract Object communicateWithRpcServer(RpcDTO rpcDTO);
  //todo定义获取地址的接口.

  /**
   * 获取rpcSever服务器的地址
   * @param params rpc基本参数.
   * @return  地址
   */
  public abstract SocketAddress getServerAddress(RpcBaseParams params);

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
