package com.aholdusa.am.audittracking.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

/**
 * Entity.java
 * 
 * @author jmalkan
 */
public interface Entity extends Serializable {
  /** The Entity's Id column name. */
  public static final String ID_COLUMN_NAME = "id";
  
  /**
   * Getter of the property <tt>id</tt> of the domain.
   * 
   * @return the id of the current entity.
   */
  public Long getId();
  
  /**
   * Getter of the property <tt>createdBy</tt>
   * 
   * @return the createdBy
   */
  public String getCreatedBy();
  
  /**
   * Setter of the property <tt>createdBy</tt>
   * 
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(final String createdBy);
  
  /**
   * Getter of the property <tt>createDate</tt>
   * 
   * @return the createDate
   */
  public Timestamp getCreateDate();
  
  /**
   * Setter of the property <tt>createDate</tt>
   * 
   * @param createDate the createDate to set
   */
  public void setCreateDate(final Timestamp createDate);
  
  /**
   * Getter of the property <tt>lastModifiedBy</tt>
   * 
   * @return the lastModifiedBy
   */
  public String getLastModifiedBy();
  
  /**
   * Setter of the property <tt>lastModifiedBy</tt>
   * 
   * @param lastModifiedBy the lastModifiedBy to set
   */
  public void setLastModifiedBy(final String lastModifiedBy);
  
  /**
   * Getter of the property <tt>lastModifiedDate</tt>
   * 
   * @return the lastModifiedDate
   */
  public Timestamp getLastModifiedDate();
  
  /**
   * Setter of the property <tt>lastModifiedDate</tt>
   * 
   * @param lastModifiedDate the lastModifiedDate to set
   */
  public void setLastModifiedDate(final Timestamp lastModifiedDate);
  
  /**
   * Getter of the property <tt>version</tt>
   * 
   * @return the version
   */
  public Long getVersion();
  
  /**
   * Setter of the property <tt>version</tt>
   * 
   * @param version the version to set
   */
  public void setVersion(final Long version);

  public void setIsDeleted(boolean isDeleted);
  
  public boolean getIsDeleted(); 
// public <T> T getObjectFromJson (LinkedHashMap jsonMap);
  
}