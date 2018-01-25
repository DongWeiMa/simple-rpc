package com.dongweima.rpc.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author dongweima
 */
public class ByteSocketReadUtil {

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

  public static void main(String[] args) {
    byte a = -1;
    int b = -1;
    System.out.println(a);
    System.out.println(b);
    System.out.println((byte) b);
    System.out.println((int) a);
  }

}
