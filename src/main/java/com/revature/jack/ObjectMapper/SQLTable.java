package com.revature.jack.ObjectMapper;


//IMPORTS
import com.revature.jack.utils.Pair;
import java.util.List;

/*
 * 	SQLTable object holds the table name and list of all STRING
 * 	column names of a table and their datatypes * 
 */

public class SQLTable 
{

	
	//Declare vars
	private String tableName;
	private List<Pair<String,String>> columns;
	
	
	
	/* Define Methods */
	public SQLTable(String tableName) {
		super();
		this.tableName = tableName;
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


	public List<Pair<String, String>> getColumns() {
		return columns;
	}
	//END GET COLUMNS


	public void setColumns(List<Pair<String, String>> columns) {
		this.columns = columns;
	}
	//END SET COLUMNS
	
	
	/* Other Methods */
	public void addCol(Pair<String, String> p) {
		columns.add(p);
	}
	
	
	public boolean removeCol(Pair<String, String> p) {
		return columns.removeIf((val) -> val.v1.equals(p.v1));
	}
	
}
//END CLASS 