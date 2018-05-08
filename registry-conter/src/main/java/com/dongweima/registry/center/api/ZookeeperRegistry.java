package com.dongweima.registry.center.api;

import com.dongweima.registry.center.api.bean.Entity;
import com.dongweima.registry.center.zk.ChildListener;
import com.dongweima.registry.center.zk.CuratorZkClient;
import com.dongweima.registry.center.zk.ZkClient;
import java.util.HashSet;
import java.util.Set;

/**
 * zk 为 /registry/provider/service/ip:port
 *
 * 客户端和服务端都是在这里注册
 *
 * @author madongwei
 */
public class ZookeeperRegistry implements Registry {

  public static final int PROVIDER = 0;
  public static final int CONSUMER = 1;
  private static final String ROOT = "/registry";
  private ZkClient client;

  public ZookeeperRegistry(String url) {
    this.client = new CuratorZkClient(url);

  }
  
  @Override
  public void register(Entity entity) throws Exception {
    client.create(getUrl(entity), true);
  }

  @Override
  public void unregister(Entity entity) throws Exception {
    client.delete(getUrl(entity));
  }

  @Override
  public void subscribe(Entity entity, ChildListener listener) throws Exception {
    client.watchChild(getProviderUrl(entity), listener);
  }

  @Override
  public void unsubscribe(Entity entity, ChildListener listener)
      throws Exception {
    // TODO Auto-generated method stub

  }

  public Set<String> getInitRouters(Entity entity) throws Exception {
    return new HashSet<String>(client.getChild(getProviderUrl(entity)));
  }

  private String getUrl(Entity entity) {
    if (ZookeeperRegistry.PROVIDER == entity.getType()) {
      return getProviderNodeUrl(entity);
    } else {
      return getConsumerNodeUrl(entity);
    }
  }

  private String getProviderUrl(Entity entity) {
    return ROOT + "/" + "provider/" + entity.getService();
  }

  private String getProviderNodeUrl(Entity entity) {
    return getProviderUrl(entity) + "/" + entity.getNode();
  }

  private String getConsumerUrl(Entity entity) {
    return ROOT + "/" + "consumer/" + entity.getService() + "/" + entity.getNode();
  }

  private String getConsumerNodeUrl(Entity entity) {
    return getConsumerUrl(entity) + "/" + entity.getNode();
  }


}
