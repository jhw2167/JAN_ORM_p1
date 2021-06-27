package com.revature.annotations;


//IMPORTS
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
 * Column annotation for our ORM.
 * 
 * Indicates user wants this variable to be a column
 * in an SQL table
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
}
//END INTERFACE

	

