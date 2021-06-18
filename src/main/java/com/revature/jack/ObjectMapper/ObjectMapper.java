package com.revature.jack.ObjectMapper;


//IMPORTS

//Reflection Imports
import java.lang.reflect.Field;

//Java SQL imports
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//Java Lib Imports
import java.util.HashMap;
import java.util.Map;

//Project Imports
import com.revature.jack.Annotations.*;
import com.revature.aron.connection.*;

/*
 *  	Simple java object mapper class to TAKE IN an object of type class
 *  	and return a mapping to an SQL table and columns
 * 
 */

public class ObjectMapper {

	//Declare variables
	
	//map to enforce only 1 table per class
	private static Map<Class<?>, SQLTable> tables;
	
	private static ConnectionPool conn = new ConnectionPool();
	
	static {
		conn.setUpPool();
	}
	
	/* Methods */
	
	
	//Constructor
	private ObjectMapper() {
		super();
		//not instatiated
	}
	
	//to SQL obj
	
	public static SQLTable toTable(Class<?> c) throws Exception {
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
		
		//Get all fields
		Field[] fields = c.getDeclaredFields();
		
		//parse through and get variables
		for (Field f : fields) {
			if(f.isAnnotationPresent(Column.class)) {
				table.addCol(new SQLColumn(f));
			}
		}
		//End For

		return table;
	}
	//END ObjectMapper::TOTABLE
	
	
	/*
	 * Drop table query
	 */
	public static boolean dropTable(SQLTable table, boolean cascade) throws SQLException 
	{
		String query = "DROP TABLE IF EXISTS " + table.getTableName();
		
		if(cascade) {
			query += " CASCADE";
		}
		query += ";";
		
		//Set and executre our prepared statement
		
		
	}
	

}
