package com.dongweima.rpc.common;

/**
 * @author dongweima
 */
public class RpcDTO {
  private String interfaceName;
  private String methodName;
  private Class<?>[] parameterTypes;
  private Object[] params;

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  public void setParameterTypes(Class<?>[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
  }
}
