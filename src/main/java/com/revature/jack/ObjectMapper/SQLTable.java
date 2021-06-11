package com.revature.jack.ObjectMapper;


import java.util.HashMap;
//IMPORTS
import java.util.Map;

/*
 * 	SQLTable object holds the table name and list of all STRING
 * 	column names of a table and their datatypes * 
 */

public class SQLTable 
{

	
	//Declare vars
	private String tableName;
	
	//Maps column NAMES to column SQL TYPES
	private Map<String, String> columns;
	
	
	
	/* Define Methods */
	public SQLTable(String tableName) {
		super();
		this.tableName = tableName;
		columns = new HashMap<>();
	}
	//End Constructor


	/* Getters and Setters */

	public String getTableName() {
		return tableName;
	}
	//END GET TABLE NAME


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	//END SET TABLE NAME


	
	/* Other Methods */
	public void addCol(String colName, String colType) {
		columns.put(colName, colType);
	}
	//END METHOD ADDCOL
	
	public boolean removeCol(String colName) {
		return columns.remove(colName) != null;
	}
	//END METHOD REMOVECOL
	
	
	public boolean contains(String colName) {
		return columns.containsKey(colName);
	}
	//END METHOD CONTAINS
	
	public Map<String, String> getColumns() {
		return columns;
	}
	//END METHOD getCOLUMNS

	public void setColumns(Map<String, String> columns) {
		this.columns = columns;
	}
	//END METHOD getCOLUMNS


	@Override
	public String toString() {
		return "SQLTable [tableName=" + tableName + ", columns=" + columns + "]";
	}

	
	
	
}
//END CLASS 