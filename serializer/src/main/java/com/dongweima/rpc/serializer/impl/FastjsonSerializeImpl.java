package com.dongweima.rpc.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dongweima.rpc.common.RpcDTO;
import com.dongweima.rpc.serializer.Serialize;
import com.dongweima.rpc.serializer.SerializeEnum;
import com.dongweima.rpc.serializer.SerializeFactory;
import java.io.ObjectInputStream;

/**
 * @author dongweima create on 2018/1/24 1:02.
 */
public class FastjsonSerializeImpl implements Serialize {

  @Override
  public <T> byte[] serialize(T obj) {
    JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    return JSON.toJSONBytes(obj, SerializerFeature.WriteDateUseDateFormat);
  }

  @Override
  public <T> T deserialize(byte[] data, Class<T> clazz) {
    return JSON.parseObject(new String(data),clazz);
  }

  public static void main(String[] args){
    RpcDTO rpcDTO = new RpcDTO();
    rpcDTO.setMethodName("sss");
    rpcDTO.setInterfaceName("dadsa");
    Object[] objs = {""};
    rpcDTO.setParams(objs);
    Class[] c = {String.class};
    rpcDTO.setParameterTypes(c);
    Serialize serialize = SerializeFactory.getSerialize(SerializeEnum.FASTJSON);
    byte[] bytes = serialize.serialize(rpcDTO);
    RpcDTO s = serialize.deserialize(bytes,RpcDTO.class);
    System.out.println(s);
  }
}
