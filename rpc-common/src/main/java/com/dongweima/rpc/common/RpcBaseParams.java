package com.dongweima.rpc.common;

/**
 * 不是按照方法级别注册,而是按照类级别注册.
 *
 * @author dongweima
 */
public class RpcBaseParams {

  private String interfaceName;
  private String group;
  private String version;

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

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

  @Override
  public String toString() {
    return "RpcBaseParams{" +
        "interfaceName='" + interfaceName + '\'' +
        ", group='" + group + '\'' +
        ", version='" + version + '\'' +
        '}';
  }
}
