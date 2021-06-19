package com.revature.jack.Annotations;

//IMPORTS
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 		Table annotation is to be applied to a CLASS declaration
 * 		and indicates the the user would like to create this 
 * 		object as a class in SQL
 * 
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	public String name();
}