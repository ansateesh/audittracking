package com.aholdusa.am.audittracking.entity;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

import com.google.gson.annotations.Expose;

/**
 * AbstractEntity.java This is an abstract base class for the Entity class to extend from.
 * Common functionality used by descendants is implemented here.
 * 
 * @author jmalkan
 */
public abstract class AbstractEntity implements Entity {
  private static final long serialVersionUID = 1L;
  
  @Id
  private Long id;
  private Long version;
  private String  createdBy;
  private Timestamp createDate;
  private String lastModifiedBy;
  private Timestamp lastModifiedDate;
  private Boolean isDeleted = false; 
  private Integer recordStatus; 
  private String lockedBy; 
  
  /**
   * Creates a new instance of com.aholdusa.core.base.entity.AbstractEntity.java and Performs Initialization.
   */
  public AbstractEntity() {
    super();
  }
  
  /**
   * Creates a new instance of com.aholdusa.core.base.entity.AbstractEntity.java and Performs Initialization.
   * 
   * @param id The ID for this domain object.
   * 
   */
  public AbstractEntity(final Long id) {
    super();
    this.id = id;
  }
  
  
  public Long getId() {
    return id;
  }
  
  /**
   * Setter of the property <tt>id</tt>
   * 
   * @param id the id to set
   */
  public void setId(final Long id) {
    this.id = id;
  }
  
  
  public void setVersion(final Long version) {
    this.version = version;
  }

  
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
  
  
  public String getCreatedBy() {
    return createdBy;
  }
  
  
  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }
  
  
  public Timestamp getCreateDate() {
    return createDate;
  }
  
  
  public void setCreateDate(final Timestamp createDate) {
    this.createDate = createDate;
  }
  
  
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }
  
  
  public void setLastModifiedBy(final String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  
  public Timestamp getLastModifiedDate() {
    return lastModifiedDate;
  }
  
  
  public void setLastModifiedDate(final Timestamp lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
  
  
  public void setIsDeleted(final boolean isDeleted){
	  this.isDeleted = isDeleted;  
  }
  
  
  public boolean getIsDeleted(){
	  return (this.isDeleted == null)? false:this.isDeleted;
  }
  
  public Long getVersion() {
    return version;
  }
  
  public void setRecordStatus(Integer recordStatus) {
	  this.recordStatus = recordStatus; 
  }
  
  public Integer getRecordStatus() {
	  return this.recordStatus; 
  }
  
  public String getLockedBy() {
	  return this.lockedBy; 
  }
  
  public void setLockedBy(String lockedBy){
	  this.lockedBy = lockedBy;
  }
  
  public boolean equals(final Object entity) {
    if (this == entity)
      return true;
    
    if (entity instanceof AbstractEntity) {
      final Long id1 = ((AbstractEntity) entity).getId();
      final Long id2 = getId();
      
      return id1 == null && id2 == null || id1 != null && id1.equals(id2);
    }
    
    return false;
  }
  
//  public Object getObject() {
//		return this.getObject();
//	}
  
//  public String getWhereClause(){ 
//	  Field[] fields = this.getClass().getDeclaredFields();
//	  for (Field f : this.getClass().getDeclaredFields()) {
//		  if (Field.get(this) !=null) 			  
//		  }
//	  }
//		
//  }
  
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}