package com.dongweima.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dongweima create on 2018/1/26 1:23.
 */
public class RpcRegiester {

  private Map<String, RpcInterfaceRegister> map = new ConcurrentHashMap<>(16);

  public void interfaceRegiest(RpcInterfaceRegister rip) {
    if (rip == null || rip.getIntefaceName() == null) {
      return;
    }
    String key = getKey(rip.getIntefaceName(), rip.getGroup(), rip.getVersion());
    map.put(key, rip);
  }

  public RpcInterfaceRegister getRegiesterMessage(String interfaceName, String group,
      String version) {
    String key = getKey(interfaceName, group, version);
    return map.get(key);
  }

  private String getKey(String interfaceName, String group, String version) {
    return interfaceName + group + version;
  }

}
