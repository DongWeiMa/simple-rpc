package com.dongweima.rpc.common.dto;

import com.dongweima.rpc.common.RpcBaseParams;
import java.util.Arrays;

/**
 * @author dongweima
 */
public class RpcDTO  extends RpcBaseParams {

  private String methodName;
  private Class<?>[] parameterTypes;
  private Class<?> returnType;
  private Object[] params;

  public Class<?> getReturnType() {
    return returnType;
  }

  public void setReturnType(Class<?> returnType) {
    this.returnType = returnType;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
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

  @Override
  public String toString() {
    return "RpcDTO{" +
        "methodName='" + methodName + '\'' +
        ", parameterTypes=" + Arrays.toString(parameterTypes) +
        ", returnType=" + returnType +
        ", params=" + Arrays.toString(params) +
        '}';
  }
}
