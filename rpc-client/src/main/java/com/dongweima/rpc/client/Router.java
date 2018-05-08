package com.dongweima.rpc.client;

import com.dongweima.rpc.common.RpcBaseParams;
import java.net.SocketAddress;
import java.util.List;

/**
 * 路由接口，包含获取所有路由和获取单挑路由信息即路由寻址。
 *
 * @author dongweima
 */
public interface Router {

  /**
   * 获取本地缓存的所有路由信息
   *
   * @param params rpc基本参数.
   * @return 本地缓存的地址列表
   */
  List<String> getRouters(RpcBaseParams params);

  /**
   * 获取rpcSever服务器的地址.
   * 负载均衡策略也在其中实现。
   * 使用策略模式
   *
   * @param params rpc基本参数.
   * @return 地址
   */
  SocketAddress getServerAddress(RpcBaseParams params);
}
