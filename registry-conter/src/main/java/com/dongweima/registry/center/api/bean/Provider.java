package com.dongweima.registry.center.api.bean;

/**
 * @author madongwei
 */
public class Provider {

  private Entity entity;

  public Provider(Entity entity) {
    this.entity = entity;
  }

  public Entity getEntity() {
    return entity;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

}
