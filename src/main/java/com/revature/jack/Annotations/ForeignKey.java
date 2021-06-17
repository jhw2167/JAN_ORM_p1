package com.revature.jack.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import com.revature.jack.ObjectMapper.SQLColumn;
import com.revature.jack.ObjectMapper.SQLTable;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
	public static final Class<?> refTable = null;
	public static final Field refColumn = null;
}
