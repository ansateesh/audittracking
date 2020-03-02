package com.aholdusa.am.audittracking.entity;

import com.google.gson.annotations.Expose;

/**
 * Entity for store. Extends AbstractEntity which provides common attributes like id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Store extends AbstractEntity {
  private static final long serialVersionUID = 1L;
  @Expose
  private Integer number;
  private String network;
  private Integer startIp;
  private Integer endIp;
  private String ispIp;
  private String ispPort;
  private String protocol;
  private Double defaultDrawer;
  private String nextManagerUrl;
  
  public String getNextManagerUrl() {
	return nextManagerUrl;
}

public void setNextManagerUrl(String nextManagerUrl) {
	this.nextManagerUrl = nextManagerUrl;
}

public Double getDefaultDrawer() {
	return defaultDrawer;
}

public void setDefaultDrawer(Double defaultDrawer) {
	this.defaultDrawer = defaultDrawer;
}

private String division;
  
  public Store() {
    super();
  }
  
  public Integer getNumber() {
    return number;
  }
  
  public void setNumber(Integer number) {
    this.number = number;
  }
  
  public String getNetwork() {
    return network;
  }
  
  public void setNetwork(String network) {
    this.network = network;
  }
  
  public Integer getStartIp() {
    return startIp;
  }
  
  public void setStartIp(Integer startIp) {
    this.startIp = startIp;
  }
  
  public Integer getEndIp() {
    return endIp;
  }
  
  public void setEndIp(Integer endIp) {
    this.endIp = endIp;
  }
  
  public String getIspIp() {
    return ispIp;
  }
  
  public void setIspIp(String ispIp) {
    this.ispIp = ispIp;
  }
  
  public String getIspPort() {
    return ispPort;
  }
  
  public void setIspPort(String ispPort) {
    this.ispPort = ispPort;
  }
  
  public String getProtocol() {
    return protocol;
  }
  
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }
  
  public String getDivision() {
    return division;
  }
  
  public void setDivision(String division) {
    this.division = division;
  }
}