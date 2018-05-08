package com.dongweima.demo.simple.rpc.impl;

import com.dongweima.demo.simple.rpc.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServiceImpl implements EchoService {

  private static Logger logger = LoggerFactory.getLogger(EchoServiceImpl.class);
  @Override
  public String  echo(String ping) {
    logger.info("{} get message : {}", Thread.currentThread().getName(), ping);
   return ping!=null?ping+" i am ok":"i am ok";
  }
}
