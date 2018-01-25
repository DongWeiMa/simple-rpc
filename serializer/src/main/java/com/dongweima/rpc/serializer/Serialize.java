package com.dongweima.rpc.serializer;

/**
 * @author dongweima create on 2018/1/23 23:56.
 */
public interface Serialize {
  /**
   * 序列化接口.
   */
  <T> byte[] serialize(T obj);
  
  /**
   * 如果要支持的更好,应该把协议也包含进去.根据协议选择具体的反序列化方案.
   * 反序列化接口.
   */
  <T> T deserialize(byte[] data,Class<T> clazz);
}
