package com.dongweima.registry.center.api.bean;

import com.dongweima.registry.center.api.ZookeeperRegistry;
import com.dongweima.registry.center.zk.ChildListener;
import java.util.Set;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongweima
 */
public class Consumer implements ChildListener {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
  private Set<String> providers;

  private Entity entity;

  public Consumer(String service, String node, Set<String> providers) {
    this.entity = new Entity();
    this.entity.setService(service);
    this.entity.setNode(node);
    this.entity.setType(ZookeeperRegistry.CONSUMER);
    //初始路由信息，后续路由会自动更新，通过zk订阅
    this.providers = providers;
  }

  @Override
  public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
      throws Exception {

    switch (event.getType()) {
      case CHILD_ADDED:
        providers.add(event.getData().getPath());
        break;
      case CHILD_UPDATED:
        providers.add(event.getData().getPath());
        break;
      case CHILD_REMOVED:
        providers.remove(event.getData().getPath());
        break;
      default:
        break;
    }
  }

  public void printProviders() {
    logger.debug("------------->Print providres by consumer:start<----------");
    for (String url : providers) {
      logger.debug(url);
    }
    logger.debug("------------->Print providres by consumer:end<----------");
  }

  public Set<String> getProviders() {
    return providers;
  }

  public Entity getEntity() {
    return entity;
  }

}
