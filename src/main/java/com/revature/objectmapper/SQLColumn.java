package com.revature.objectmapper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.revature.annotations.*;
import com.revature.exceptions.*;
import com.revature.utils.Pair;

/*
 * 		Represents a column in SQL, in Java - contains values:
 * 		-	column name
 * 		- 	datatype
 * 		- SQL constraints
 * 
 */

public class SQLColumn {

	/* DECLARE VARIABLES */

	private final String name;
	private final Class javaType;
	private final String sqlType;

	private final boolean pk;
	private final Pair<Class<?>, Field> fk;
	private final boolean unique;
	private final boolean notNull;
	private final String defaultValue;
	private final String checkStmt;

	// Type Mapper
	public static final Map<Class<?>, String> typeMap = new HashMap<>();
	static {
		loadSQLJavaValuePairs();
	}

	/* METHODS */

	// Constructors

	// Base constructor - no default constructor
	public SQLColumn(String name, Class javaType, String sqlType) {
		super();
		this.name = name;
		this.javaType = javaType;
		this.sqlType = sqlType;
		this.pk = false;
		this.fk = null;
		this.unique = false;
		this.notNull = false;
		this.defaultValue = null;
		this.checkStmt = null;
	}
	// END BASE CONSTRUCTOR

	// Create column with constraints
	public SQLColumn(String name, Class javaType, String sqlType, boolean pk, Pair<Class<?>, Field> fk, boolean unique,
			boolean notNull, String defaultValue, String checkStmt) {
		super();
		this.name = name;
		this.javaType = javaType;
		this.sqlType = sqlType;
		this.pk = pk;
		this.fk = fk;
		this.unique = unique;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
		this.checkStmt = (checkStmt.equals("")) ? null : checkStmt;
	}
	// END FULL CONSTRUCTOR

	// Create Column FROM FIELD TYPE
	public SQLColumn(Field field) throws NoSuchFieldException, InvalidColumnNameException {
		super();

		// Perform checks on data to make sure all is valids
		if (!isValidColumnName(field.getName())) {
			throw new InvalidColumnNameException(field.getName());
		}

		// Ensure java object is of simple type
		if (!typeMap.containsKey(field.getType())) {
			String err = "Can't deduce SQL type for column: '" + field.getName();
			err += "' of java type: '" + field.getType().toGenericString();
			err += "'. complex objects will have to be their own tables, see documentation";
			throw new NoSuchFieldException(err);
		}

		// Establish vars
		this.name = field.getName();
		this.javaType = field.getType();
		this.sqlType = typeMap.get(field.getType());

		this.pk = field.isAnnotationPresent(PrimaryKey.class);
		this.unique = field.isAnnotationPresent(Unique.class) && !pk;
		this.notNull = field.isAnnotationPresent(NotNull.class) && !pk;

		this.defaultValue = (field.isAnnotationPresent(DefaultValue.class))
				? field.getAnnotation(DefaultValue.class).defaultValue()
				: null;
		this.checkStmt = (field.isAnnotationPresent(CheckColumn.class))
				? field.getAnnotation(CheckColumn.class).checkStmt()
				: null;

		// Establishing foreign key takes a little bit of work
		if (field.isAnnotationPresent(ForeignKey.class)) 
		{
			System.out.println("about to add foreign key for column: " + name);
			ForeignKey fkObject = field.getAnnotation(ForeignKey.class);
			Class<?> fkClass = fkObject.refClass();
			Field fkColName = null;
			
			try {
				fkColName = fkClass.getDeclaredField(fkObject.refColumn());	
			} catch (NoSuchFieldException e) {
				String err = "NoSuchFieldException caught attempting to add field '" 
						+ e.getMessage() + "' for table '" + fkClass.getName() + "'";
				System.out.println(err);
				throw e;
			}
			
			this.fk = new Pair<Class<?>, Field>(fkClass, fkColName);
		} else {
			this.fk = null;
		}
	}

	// GETTERS

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

	public Pair<Class<?>, Field> getFk() {
		return fk;
	}

	public boolean isUnique() {
		return unique;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getCheckStmt() {
		return checkStmt;
	}

	/* END GETTERS */

	// SETTERS

	/* END SETTERS */

	// Other Methods
	public boolean isValidColumnName(String s) {
		// Check againt
		return true;
	}

	/*
	 * Returns true if this object you intend to load into this column is of the
	 * proper class
	 */
	public boolean isObjectOfProperType(Object o1) {
		return javaType.equals(o1.getClass());
	}

	private static void loadSQLJavaValuePairs() {
		
		//Numeric(Precision, Scale)
		//Precision is number of digits,
		//scale is number to the right of decimal

		// init our typemap
		typeMap.put(int.class, "INTEGER");
		typeMap.put(Integer.class, "INTEGER");
		typeMap.put(long.class, "INTEGER");
		typeMap.put(Long.class, "INTEGER");
		
		typeMap.put(short.class, "INTEGER");
		typeMap.put(Short.class, "INTEGER");
		
		typeMap.put(float.class, "NUMERIC");
		typeMap.put(Float.class, "NUMERIC");
		typeMap.put(double.class, "NUMERIC");
		typeMap.put(Double.class, "NUMERIC");
		
		typeMap.put(boolean.class, "BIT");
		typeMap.put(Boolean.class, "BIT");
		
		typeMap.put(char.class, "CHAR(1)");
		typeMap.put(Character.class, "CHAR(1)");
		typeMap.put(String.class, "TEXT");
	}

}
//END CLASS