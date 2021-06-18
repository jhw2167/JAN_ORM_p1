package com.revature.jack.ObjectMapper;


import java.io.PrintStream;

//IMPORTS

//Reflection Imports
import java.lang.reflect.Field;

//Java SQL imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
//Java Lib Imports
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Project Imports
import com.revature.jack.Annotations.*;
import com.revature.jack.utils.Pair;
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
	private static ConnectionPool cp = new ConnectionPool();
	private static DataSource ds;
	static {
		ds = cp.setUpPool();
	}
	
	
	//Logger
	private static final Logger log = LoggerFactory.getLogger(ObjectMapper.class);

	
	/* Methods */
	
	
	//Constructor
	private ObjectMapper() {
		super();
		//not instatiated
	}
	
	/* MANAGING OUR MODEL */
	
	/* ObjectMapper ADD method */
	public static void addToModel(Class<?> c) throws Exception {
		tables.put(c, toTable(c));
	}
	//END ADD METHOD
	
	
	/* ObjectMapper Remove From Model method 
	 * 	- Should be used if you add a class to the model on accident
	 * 
	 * */
	public static boolean removeFromModel(Class<?> c) {
			return tables.remove(c, tables.get(c));
	}
	//END REMOVE FROM MODEL
	
	
	/* ObjectMapper Remove From Model method 
	 * 		- Note, function ALSO removes db from model
	 * */
	public static boolean removeFromDB(Class<?> c, boolean cascadeDelete) throws SQLException {
		if(dropTable(tables.get(c), cascadeDelete)) {
			return tables.remove(c, tables.get(c));
		}
		return false;
	}
	//END REMOVE FROM DB AND model method
	
	
	/* MANAGING OUR DATABASE */
	
	/*
	 * 	Completely Reconstructs the DB from the tables added into the model
	 * 	This operation WILL drop all data and tables in the EXISTING DB, so do not
	 *  use it unless you want to start fresh
	 */
	public static void buildDBFromModel() throws SQLException {
		Collection<SQLTable> buildables = tables.values();
		for (SQLTable t : buildables) {
			createTable(t);
		}
	}
	//END BUILDMODEL METHOD
	
	
	/*
	 *	Unlike buildDBFrom model, this method assumes the DB has already been constructed
	 *  and we dont want to reconstruct existing tables	
	 *  
	 */
	public static void updateDB() throws SQLException {
		Collection<SQLTable> buildables = tables.values();
		List<String> existingTables = getDBTables();
		for (SQLTable t : buildables) {
			if(!existingTables.contains(t.getTableName())) {
				createTable(t);
			}
		}
	}
	//END UPDATE DB METHOD
	
	/* to SQL obj */
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
		SQLTable table = new SQLTable(c.getName());
		
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
	
	
	/* Drop table query */
	public static boolean dropTable(SQLTable table, boolean cascade) throws SQLException 
	{
		String query = "DROP TABLE IF EXISTS " + table.getTableName();
		
		if(cascade) {
			query += " CASCADE";
		}
		query += ";";
		
		//Set and executre our prepared statement
		Connection conn = ds.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(query);
		return pstmt.execute();
	}
	
	
	/*
	 * Create a table in SQL from our SQL Table object
	 * 		- Drop table if exists
	 * 		- Add header information
	 * 		- Iterate over tables map of table to add all values
	 * 		- terminate query with );
	 * 		- set in prepared stmt and execute
	 */
	private static boolean createTable(SQLTable table) throws SQLException 
	{	
		//Drop table if exists
		dropTable(table, true);
		
		//add header with StringBuilder
		StringBuilder query = new StringBuilder("CREATE TABLE " + table.getTableName() + " ("); 
		
		//iterate over map in our SQLTable
		Collection<SQLColumn> cols = table.getColumns().values();
		
		for(SQLColumn col : cols) 
		{
			//Init value for our new column
			StringBuilder val = new StringBuilder(col.getName() + " ");
			
			//Add column type
			val.append(col.getSqlType() + " ");
			
			//Test for pk
			if(col.isPk()) {
				val.append("PRIMARY KEY ");
			}
			
			//Test for UNIQUE
			if(col.isUnique()) {
				val.append("UNIQUE ");
			}
			
			//Test for NOT NULL
			if(col.isNotNull()) {
				val.append("NOT NULL ");
			}
			
			//Test for CHECK
			if(col.getCheckStmt() != null) {
				val.append("CHECK (" + col.getCheckStmt() + ") ");
			}
			
			//Test for fk
			if(col.getFk() != null) {
				Pair<Class<?>, Field> pair = col.getFk();
				String tableName = pair.v1.getName();
				String colName = pair.v2.getName();
				
				val.append("REFERENCES " + tableName + " (" + colName + " ) ");
			}
			//INTERNALLY WE NEED TO ENSURE TABLES ARE CREATED IN PROPER ORDER
			//I think a multiMap is usable here
			
			//Test for Default
			if(col.getDefaultValue() != null) {
				val.append("DEFAULT " + col.getDefaultValue());
			}
			
			//Finish up
			val.append(",\n");
			query.append(val);
			System.out.println("Our value: " + val);			
			System.out.println("Our query: " + query + "\n");
			
		}
		//End value building FOR
		
		//remove comma
		query.delete(query.length() - 2, query.length());
		query.append(");");
		
		//Execute query
		Connection conn = ds.getConnection();
		System.out.println("Final query:\n" + query.toString());
		PreparedStatement pstmt = conn.prepareStatement(query.toString());
		return pstmt.execute();
	}
	//END CREATE TABLE METHOD
	
	
	public static List<String> getDBTables()
	{
		List<String> tableList = new ArrayList<String>();
		try (Connection conn = ds.getConnection()) 
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("show tables");
			
			//Compile results
			while(rs.next()) {
				tableList.add(rs.getNString(1));
			}
			
		} catch (SQLException e) {
			log.error("Could not connect to the database in ObjectMapper::createTable");
			return null;
		}
		
		return tableList;
	}
	
}
//END OBJECT MAPPER CLASS
