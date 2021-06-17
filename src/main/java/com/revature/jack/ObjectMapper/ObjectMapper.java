package com.revature.jack.ObjectMapper;


import java.lang.reflect.Field;
import java.util.HashMap;
//IMPORTS
import java.util.Map;

import com.revature.jack.Annotations.*;


/*
 *  	Simple java object mapper class to TAKE IN an object of type class
 *  	and return a mapping to an SQL table and columns
 * 
 */

public class ObjectMapper {

	//Declare variables
	
	public static Map<Class<?>, SQLTable> tables;
	//map to enforce only 1 table per class
	
	/* Methods */
	
	
	//Constructor
	private ObjectMapper() {
		super();
		//not instatiated
	}
	
	//to SQL obj
	
	public static SQLTable toTable(Class<?> c) {
		/*
		 * Use *reflection* to get the annotations and names of all
		 * relevat values in a java class passed by the user 
		 */
		
		//WE NEED TO THROW SOME EXCEPTION ON ERRORS
		
		if(!c.isAnnotationPresent(Table.class)) {
			System.out.println("Class not mappable: please annotate " + 
		"classes with @Table and fields with @Column");
			
			//maybe throw "notMappable"
			return null;
		}
		
		//Create new SQL table
		SQLTable table = new SQLTable(c.getAnnotation(Table.class).name());
		
		//Get all annotations
		Field[] fields = c.getDeclaredFields();
		
		
		//parse through and get variables
		for (Field f : fields) {
			if(f.isAnnotationPresent(Column.class)) 
			{
				String colName = f.getAnnotation(Column.class).name();
				if (table.contains(colName)) {
					//throw NotMapable
					return null;
				} // else if (name is SQL keyword)...
				//else add to column
				table.addCol(colName, typeMap.get(f.getType()));
			}
		}
		//End For

		return table;
	}
	//END ObjectMapper::TOTABLE
	
	

}
