package com.dongweima.rpc.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字节流读取工具
 * @author dongweima
 */
public class ByteSocketReadUtil {

  private static final Logger logger = LoggerFactory.getLogger(ByteSocketReadUtil.class);
  private ByteSocketReadUtil() {

  }

  public static byte[] read(InputStream in) throws Exception {
    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len = -1;
    while ((len = in.read(buffer)) != -1) {
      outSteam.write(buffer, 0, len);
      if (len < 1024) {
        break;
      }
    }
    return  outSteam.toByteArray();
  }

}
