package com.dongweima.registry.center.api.bean;

/**
 * @author madongwei
 */
public class Entity {

  private String service;
  private String node;
  private int type;

  public Entity() {
  }

  public Entity(String interfaceName, String group, String version) {
    this.service = interfaceName + ":" + group + ":" + version;
  }

  public Entity(String interfaceName, String group, String version, String host, String port) {
    this.service = interfaceName + ":" + group + ":" + version;
    this.node = host + ":" + port;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getNode() {
    return node;
  }

  public void setNode(String node) {
    this.node = node;
  }
}
