package com.dongweima.rpc.serializer;

import com.dongweima.rpc.serializer.impl.FastjsonSerializeImpl;

/**
 * 简单工厂模式.
 * @author dongweima create on 2018/1/24 0:45.
 */
public class SerializeFactory {

  private static Serialize fastjson = new FastjsonSerializeImpl();
  private SerializeFactory(){
    
  }
  
  public static Serialize getSerialize(String serializeName){
    switch (serializeName) {
      case SerializeEnum.FASTJSON:
        return fastjson;
      default:
        return fastjson;
    }
  }

}
