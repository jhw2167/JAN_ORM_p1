package com.revature.jack.ObjectMapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.revature.jack.utils.Pair;
import com.revature.jack.Annotations.*;

/*
 * 		Represents a column in SQL, in Java - contains values:
 * 		-	column name
 * 		- 	datatype
 * 		- SQL constraints
 * 
 */



public class SQLColumn
{

	/* DECLARE VARIABLES */
	
	private final String name;
	private final Class javaType;
	private final String sqlType;
	
	private final boolean pk;
	private final Pair<SQLTable, SQLColumn> fk;
	private final boolean unique;
	private final boolean notNull;
	private final String checkStmt;
	
	//Type Mapper
	public static final Map<Class<?>, String> typeMap = new HashMap<>();
	static {
		loadSQLJavaValuePairs();
	}
	
	/* METHODS */
	
	//Constructors
	
	//Base constructor - no default constructor
	public SQLColumn(String name, Class javaType, String sqlType) 
	{
		super();
		this.name = name;
		this.javaType = javaType;
		this.sqlType = sqlType;
		this.pk = false;
		this.fk = null;
		this.unique = false;
		this.notNull = false;
		this.checkStmt = null;
	}
	//END BASE CONSTRUCTOR
	
	
	//Create column with constraints
	public SQLColumn(String name, Class javaType, String sqlType, boolean pk, Pair<SQLTable, SQLColumn> fk, boolean unique, boolean notNull,
			String checkStmt) 
	{
		super();
		this.name = name;
		this.javaType = javaType;
		this.sqlType = sqlType;
		this.pk = pk;
		this.fk = fk;
		this.unique = unique;
		this.notNull = notNull;
		this.checkStmt = (checkStmt.equals("")) ? null : checkStmt;
	}
	//END FULL CONSTRUCTOR


	//Create Column FROM FIELD TYPE
	public SQLColumn(Field field) throws Exception 
	{
		super();
		
		//Perform checks on data to make sure all is valids
		if (!isValidColumnName(field.getName())) {
			throw new Exception();
		}
		
		//Ensure java object is of simple type
		if (!typeMap.containsKey(field.getClass())) {
			String err = "Can't deduce SQL type for column " + field.getName();
			err += " of java type " + field.getClass().toGenericString();
			err += ": complex objects will have to be their own tables";
			throw new Exception(err);
		}
		
		
		//Establish vars
		this.name = field.getName();
		this.javaType = field.getClass();
		this.sqlType = typeMap.get(field.getClass());
		
		this.pk = (field.isAnnotationPresent(PrimaryKey.class)) ? true : false;
		this.unique = unique;
		this.notNull = notNull;
		this.checkStmt = (checkStmt.equals("")) ? null : checkStmt;
		
		//Establishing foreign key takes a little bit of work
		if(field.isAnnotationPresent(ForeignKey.class)) {
			ForeignKey fkObject = field.getAnnotation(ForeignKey.class);
			this.fk = new Pair<>(fkObject.refTable, fkObject.refColumn);
		} else {
			this.fk = null;
		}
	}
	
	
	//GETTERS
	
	public String getName() {
		return name;
	}


	public Class getJavaType() {
		return javaType;
	}


	public String getSqlType() {
		return sqlType;
	}


	public boolean isPk() {
		return pk;
	}


	public Pair<SQLTable, SQLColumn> getFk() {
		return fk;
	}


	public boolean isUnique() {
		return unique;
	}


	public boolean isNotNull() {
		return notNull;
	}


	public String getCheckStmt() {
		return checkStmt;
	}
	
	
	/* END GETTERS */
	
	
	//SETTERS
	
	/* END SETTERS */
	
	
	
	//Other Methods
	public boolean isValidColumnName(String s) {
		//Check againt 
		return true;
	}
	
	
	public boolean isProperType(Object o1) {
		return javaType.equals(o1.getClass());
	}
	
	
	
	private static void loadSQLJavaValuePairs() {
		//create properties object
		
		//open valuePairs.xml
		
		//load properties
		
		//init our typemap
		typeMap.put(int.class, "INTEGER");
		typeMap.put(Integer.class, "INTEGER");
		typeMap.put(String.class, "TEXT");
		typeMap.put(float.class, "NUMERIC");
	}
	
	
	
	
	
}
//END CLASS