package com.dongweima.registry.center.zk;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * @author dongweima
 */
@DisconfFile(filename = "zk.properties", app = "zk", version = "1.0.0")
public class ZkConfig {

  private static String url;

  @DisconfFileItem(name = "url")
  public static String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    ZkConfig.url = url;
  }
}
