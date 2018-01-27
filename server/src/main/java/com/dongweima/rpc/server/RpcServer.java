package com.dongweima.rpc.server;

import com.dongweima.rpc.common.RpcRegiester;

/**
 * @author dongweima
 */
public interface RpcServer<T> {

  /**
   * 一次性注册所有服务.
   * @param port 端口
   * @param rpcRegiester  注册信息
   */
  void register(int port, RpcRegiester rpcRegiester);


  void resolveRpcClientRequest(T t);
}
