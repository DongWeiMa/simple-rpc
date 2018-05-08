package com.dongweima.rpc.io;

/**
 * @author dongweima
 */
public interface RpcIO {

  /**
   * 设置地址.
   * @param host 域名或ip
   * @param port 端口
   */
  void setAddress(String host,int port);

  /**
   * 读取数据.
   * @return 字节数组.
   */
  byte[] read();

  /**
   * 发送请求.
   * @param data 请求内容
   */
  void write(byte[] data);
}
