package com.dongweima.demo.simple.rpc.impl;

import com.dongweima.demo.simple.rpc.EchoService;

public class EchoServiceImpl implements EchoService {

  @Override
  public String  echo(String ping) {
   return ping!=null?ping+" i am ok":"i am ok";
  }
}
