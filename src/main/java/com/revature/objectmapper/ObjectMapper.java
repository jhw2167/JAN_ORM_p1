package com.revature.objectmapper;


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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.sql.DataSource;

import com.revature.annotations.*;
import com.revature.connection.*;
import com.revature.errors.ErrorCheck;
import com.revature.exceptions.*;
import com.revature.utils.Pair;


/*
 *  	Simple java object mapper class to TAKE IN an object of type class
 *  	and return a mapping to an SQL table and columns
 * 
 */

public class ObjectMapper {

	//Declare variables
	
	//map to enforce only 1 table per class
	private static Map<Class<?>, SQLTable> tables = new HashMap<>();
	private static ConnectionPool cp = new ConnectionPool();
	private static DataSource ds;
	static {
		ds = cp.setUpPool();
	}
	

	/* Methods */
		
	//Constructor
	private ObjectMapper() {
		super();
		//not instatiated
	}
	
	//Getter
	
	public static Map<Class<?>, SQLTable> getTables() {
		return tables;
	}
	
	public static DataSource getDs() {
		return ds;
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
	public static void buildDBFromModel() throws ForeignKeyException, NotMappableException {
		//System.out.println("Entering BuildDB from model");
		Collection<SQLTable> buildables = orderTablesByFK();
		//System.out.println("Successfully ordered tables by Fk");
		String errTable = "";
		
			for (SQLTable t : buildables) {
				errTable = t.getTableName();
				createTable(t);
			}
	}
	//END BUILDMODEL METHOD
	
	/*
	 * We need to order the tables correctly so that Foreign keys 
	 */
	private static Collection<SQLTable> orderTablesByFK() throws ForeignKeyException 
	{
		Collection<SQLTable> buildables = tables.values();
		Queue<SQLTable> queue = new LinkedList<>();
		
		//Alternative, set, queue implementation
		Set<SQLTable> ordered = new LinkedHashSet<>();
		
		//Add all values to set and queue initially
		for (SQLTable t : buildables) {
			orderTablesHelper(t, queue, ordered);
		}
		
		int limit = (int) (Math.pow(tables.size(), 2) + 1);	//After n^2 iterations, there must be a circular dependency
															//in our relationships and we will exit

		while(!queue.isEmpty() && limit-- > 0) {
			SQLTable t = queue.poll();
			orderTablesHelper(t, queue, ordered);
		}
		
		//Throw exception if queue isnt empty - should be ForeignKey exception...
		if(!queue.isEmpty()) {
			throw new ForeignKeyException();
		}
		
		return ordered;
	}
	
	/*
	 * Helper method will determine if next table up should be added to the 
	 * set (can be added safely) or needs another table added before it
	 */
	
	private static void orderTablesHelper(SQLTable t, Queue<SQLTable> queue, Set<SQLTable> ordered) 
	{
		List<Class<?>> refs = t.getReferences();
		//System.out.println("\n\nConsidering table: " + t.getTableName() + " references: ");
		
		boolean canAdd = true;
		for (Class<?> c : refs) 
		{
			//System.out.println("\t- " + c.getSimpleName());
			//System.out.println("\t- Tables get: " + tables.get(c));
			//System.out.println("\t- Ordered Contains: " + ordered.contains(tables.get(c)));
			if(!ordered.contains(tables.get(c))) {
				canAdd = false;
				break;
			}
		}
		
		//System.out.println("Can add: " + canAdd);
		//We can add table if all REFERENCED tables have already been added
		if(canAdd) {
			ordered.add(t);
		} else {
			queue.add(t);
		}
		//System.out.println();
		//System.out.println();
	}
	
	
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
	public static SQLTable toTable(Class<?> c) throws NotMappableException 
	{
		/*
		 * Use *reflection* to get the annotations and names of all
		 * relevat values in a java class passed by the user 
		 */
		
		
		if(!c.isAnnotationPresent(Table.class)) {
			System.out.println("Class not mappable: please annotate " + 
		"classes with @Table and fields with @Column");
			throw new NotMappableException(c.getName());
		}
		//System.out.println("Moving to mapping columns: \n");
		
		//Create new SQL table
		SQLTable table = new SQLTable(c.getAnnotation(Table.class).name());
		ErrorCheck.keywordCheck(table.getTableName());
		
		//Get all fields
		Field[] fields = c.getDeclaredFields();
		
		//parse through and get variables
		try {
			for (Field f : fields) {
				if(f.isAnnotationPresent(Column.class) && ErrorCheck.keywordCheck(f.getName())==false) {
						table.addCol(new SQLColumn(f)); 
				}
			}
		}
		catch (NoSuchFieldException e) {
			String err = "NoSuchFieldException caught attempting to add field '" 
						+ e.getMessage() + "' for table '" + table.getTableName() + "'";
			System.out.println(err);
			e.printStackTrace();
			throw new NotMappableException(table.getTableName());
		} catch (InvalidColumnNameException e ) {
			String err = "InvalidColumnNameException caught attempting to add field '" 
					+ e.getMessage() + "' for table '" + table.getTableName() + "'";
			System.out.println(err);
			e.printStackTrace();
			throw new NotMappableException(table.getTableName());
		}
		//End For
		//System.out.println("About to return table with values: \n" + table.toString());
		
		return table;
	}
	//END ObjectMapper::TOTABLE
	
	
	/* Drop table query */
	public static boolean dropTable(SQLTable table, boolean cascade) throws NotMappableException 
	{
		String query = "DROP TABLE IF EXISTS " + table.getTableName();
		
		if(cascade) {
			query += " CASCADE";
		}
		query += ";";
		
		//Set and execute our prepared statement
		boolean dropSuccess = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.execute();
			
		} catch (SQLException e) {
			System.out.println("SQLException caught trying to drop table: " + table.getTableName());
			System.out.println("MESSAGE: " + e.getMessage());
		} 
		finally 
		{	
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				// Close the Connection Object
				if (conn!= null) {
					conn.close();
				}
			} catch(SQLException e) {
				System.out.println("Critical Error trying to close DB connections, exiting");
				System.exit(1);
			}
			
		}
		//END FINALLY
	
		return dropSuccess;
	}
	
	
	/*
	 * Create a table in SQL from our SQL Table object
	 * 		- Drop table if exists
	 * 		- Add header information
	 * 		- Iterate over tables map of table to add all values
	 * 		- terminate query with );
	 * 		- set in prepared stmt and execute
	 */
	static boolean createTable(SQLTable table) throws NotMappableException 
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
				String tableName = pair.v1.getAnnotation(Table.class).name();
				String colName = pair.v2.getName();
				
				val.append("REFERENCES " + tableName + " (" + colName + ") ");
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
			//System.out.println("Our value: " + val);			
			//System.out.println("Our query: " + query + "\n");
			
		}
		//End value building FOR
		
		//remove comma
		query.delete(query.length() - 2, query.length());
		query.append(");");
		
		//Set and execute our prepared statement
		boolean createSuccess = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			//System.out.println("\n\nFinal query:\n" + query.toString());
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			createSuccess = pstmt.execute();
			
		} catch (SQLException e) {
			System.out.println("SQLException caught trying to create table: " + table.getTableName());
		} 
		finally 
		{	
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				// Close the Connection Object
				if (conn!= null) {
					conn.close();
				}
			} catch(SQLException e) {
				System.out.println("Critical Error trying to close DB connections, exiting");
				System.exit(1);
			}
			
		}
		//END FINALLY
		
		return createSuccess;
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
			System.out.println("Could not connect to the database in ObjectMapper::createTable");
			return null;
		}
		
		return tableList;
	}
	
}
//END OBJECT MAPPER CLASS
