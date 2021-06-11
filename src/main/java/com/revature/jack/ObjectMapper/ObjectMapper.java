package com.revature.jack.ObjectMapper;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
//IMPORTS
import java.util.List;
import com.revature.jack.utils.*;


/*
 *  	Simple java object mapper class to TAKE IN an object of type class
 *  	and return a mapping to an SQL table and columns
 * 
 */

public class ObjectMapper {

	//Declare variables
	public List<SQLTable> tables;
	
	/* Methods */
	
	
	//Constructor
	
	
	//to SQL obj
	
	public static SQLTable toTable(Class<?> c) {
		/*
		 * Use *reflection* to get the annotations and names of all
		 * relevat values in a java class passed by the user 
		 */
		
		
		//Get all annotations
		Field[] fields = c.getDeclaredFields();
		
		//parse through and get variables
		
		//create pair with variable fields and name
		
		//create new SQL Table
		
		
		
	}
	
}
