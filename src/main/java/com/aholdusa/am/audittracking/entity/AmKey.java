package com.aholdusa.am.audittracking.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface AmKey {
//	public abstract boolean serialize();

//	public abstract boolean deserialize();
}
