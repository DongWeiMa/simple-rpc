package com.dongweima.rpc.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dongweima
 */
public class AddressUtil {

  private static final Logger logger = Logger.getLogger(AddressUtil.class.getName());
  private static final String LOCALHOST = "127.0.0.1";

  public static String getIntranetIp() {
    try {
      Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
      while (allNetInterfaces.hasMoreElements()) {
        NetworkInterface netInterface = allNetInterfaces.nextElement();
        if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
          continue;
        }
        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress ip = addresses.nextElement();
          if (ip != null) {
            if (ip instanceof Inet4Address) {
              if (ip.getHostAddress().startsWith("192") || ip.getHostAddress().startsWith("10")
                  || ip.getHostAddress().startsWith("172") || ip.getHostAddress()
                  .startsWith("169")) {
                return ip.getHostAddress();
              }
            }
          }
        }
      }
    } catch (SocketException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
    return LOCALHOST;
  }

  private static String getLocalMacAddress(InetAddress ia) throws SocketException {
    byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mac.length; i++) {
      if (i != 0) {
        sb.append("-");
      }
      //字节转换为整数
      int temp = mac[i] & 0xff;
      String str = Integer.toHexString(temp);
      if (str.length() == 1) {
        sb.append("0").append(str);
      } else {
        sb.append(str);
      }
    }
    return sb.toString();
  }
}
