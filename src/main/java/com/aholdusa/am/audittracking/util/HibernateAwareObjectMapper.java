package com.aholdusa.am.audittracking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * Object Mapper used to register hibernate 4 module. This is to support the Hibernate module for Jackson which helps in resolving cycles.
 * 
 * @author nusxr42
 *
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
  private static final long serialVersionUID = 1L;
  
  public HibernateAwareObjectMapper() {
    registerModule(new Hibernate4Module());
  }
}
