package com.revature.jack.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.revature.jack.ObjectMapper.SQLColumn;
import com.revature.jack.ObjectMapper.SQLTable;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
	public static final SQLTable refTable = null;
	public static final SQLColumn refColumn = null;
	
}
