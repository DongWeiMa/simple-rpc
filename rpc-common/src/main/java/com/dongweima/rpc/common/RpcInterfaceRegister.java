package com.dongweima.rpc.common;

/**
 * @author dongweima
 */
public class RpcInterfaceRegister {
  private String group;
  private String version;
  private String intefaceName;
  private String implementClassName;

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getIntefaceName() {
    return intefaceName;
  }

  public void setIntefaceName(String intefaceName) {
    this.intefaceName = intefaceName;
  }

  public String getImplementClassName() {
    return implementClassName;
  }

  public void setImplementClassName(String implementClassName) {
    this.implementClassName = implementClassName;
  }
}
