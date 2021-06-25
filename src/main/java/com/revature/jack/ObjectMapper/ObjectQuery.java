package com.revature.jack.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

public class ObjectQuery {
	static Map<Class<?>, SQLTable> tables;
	static SQLTable table;

	public static void createTableFromClass(Class<?> c) {
		try {
			ObjectMapper.addToModel(c);
			tables = ObjectMapper.getTables();
			ObjectMapper.createTable(tables.get(c));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void addObjectToTable(Object obj)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		tables = ObjectMapper.getTables();
		table = tables.get(obj.getClass());
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("INSERT INTO " + table.getTableName() + " (");
		Collection<SQLColumn> cols = table.getColumns().values();
		List<Object> value = new ArrayList<>();
		for (SQLColumn col : cols) {
			query.append(col.getName() + ",");
			Field field = obj.getClass().getDeclaredField(col.getName());
			field.setAccessible(true);
			value.add(field.get(obj));
		}
		query.deleteCharAt(query.length() - 1);
		query.append(") VALUES (");
		value.forEach(a -> query.append("'" + a.toString() + "' " + ","));
		query.deleteCharAt(query.length() - 1);
		query.append(");");

		System.out.println(query.toString());

		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Returns the Objects that match Query of Database
	 * <p>Query: SELECT * FROM tableName WHERE ColumnName = Value;
	 * 
	 * @param tableName String of the TableName defined by the User using the {@code @Table} Annotation
	 * @param ColumnName String of the field with {@code @Column} Annotation in corresponding Class
	 * @param Value String of the value that is found in the corresponding column
	 * 
	 * @return {@code List<Object>} List of Objects that Match the Query
	 * 
	 */
	public static List<Object> returnObjectsWhereColumnIs(String tableName, String ColumnName, String Value) {
		// Gets all tables in our Object Mapper
		tables = ObjectMapper.getTables();
		// Initializes a List of objects to return
		List<Object> objListtoReturn = new ArrayList<>();
		// Finds the class from the table name given
		Class<?> calledClass = tables.entrySet().stream()
				.filter((entry -> entry.getValue().getTableName().equals(tableName))).map(Map.Entry::getKey).findFirst()
				.get();
		// Get the DataSource (Connection Pooling Object)
		DataSource ds = ObjectMapper.getDs();
		// Builds a Query
		StringBuilder query = new StringBuilder(
				"SELECT * FROM " + tableName + " WHERE " + ColumnName + " = '" + Value + "';");
		// Try with Resources (connection object) - closes connection automatically
		try (Connection conn = ds.getConnection()) {
			// Creates the Prepared Statement using Query
			PreparedStatement pstmt = conn.prepareStatement(query.toString());
			// Executes Query to get Result Set
			ResultSet rs = pstmt.executeQuery();
			// Get the ResultSet's MetaData for the Column Count
			ResultSetMetaData md = rs.getMetaData();
			// Create and Object Getter and Setter Object to Create the Objects
			ObjectGetterAndSetter ogas = new ObjectGetterAndSetter();
			// While loop that iterates over every object returned from the Query
			while (rs.next()) {
				// Creates a new Object
				Object objToReturn = new Object();
				// Try Catch block to create a new Instance of the Object, (uses the no args
				// constructor)
				try {
					// Gets the No Args Constructor
					Constructor<?> c = calledClass.getDeclaredConstructor();
					// Sets Accessibility of No Args Construct to True
					c.setAccessible(true);
					// Creates the Object to Return from the No Args Constructor
					objToReturn = calledClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				// For loop that iterates through each column of the result set
				for (int i = 1; i <= md.getColumnCount(); i++) {
					// If the Class Returned is BigDecimal, change it to float
					if (rs.getObject(i).getClass().equals(BigDecimal.class)) {
						BigDecimal value = (BigDecimal) rs.getObject(i);
						float f = value.floatValue();
						// Invokes the Setter by Passing in the Obj that will have field set, the Field
						// name, and the Field Value
						ogas.invokeSetter(objToReturn, md.getColumnName(i), f);
					} else {
						// Invokes the Setter by Passing in the Obj that will have field set, the Field
						// name, and the Field Value
						ogas.invokeSetter(objToReturn, md.getColumnName(i), rs.getObject(i));
					}
				}
				// Adds the new Object to the List
				objListtoReturn.add(objToReturn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Returns the List of Objects
		return objListtoReturn;

	}

	public static List<Object> returnObjectsWhereColumnIsLessThan(String tableName, String ColumnName, String Value) {
		tables = ObjectMapper.getTables();
		List<Object> objListtoReturn = new ArrayList<>();
		Class<?> calledClass = tables.entrySet().stream()
				.filter((entry -> entry.getValue().getTableName().equals(tableName))).map(Map.Entry::getKey).findFirst()
				.get();
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder(
				"SELECT * FROM " + tableName + " WHERE " + ColumnName + " < '" + Value + "';");
		try (Connection conn = ds.getConnection()) {
			pstmt = conn.prepareStatement(query.toString());
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			ObjectGetterAndSetter ogas = new ObjectGetterAndSetter();
			while (rs.next()) {
				Object objToReturn = null;
				try {
					Constructor<?> c = calledClass.getDeclaredConstructor();
					c.setAccessible(true);
					objToReturn = calledClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				for (int i = 1; i <= columnCount; i++) {
					if (rs.getObject(i).getClass().equals(BigDecimal.class)) {
						BigDecimal value = (BigDecimal) rs.getObject(i);
						float f = value.floatValue();
						ogas.invokeSetter(objToReturn, md.getColumnName(i), f);
					} else {
						ogas.invokeSetter(objToReturn, md.getColumnName(i), rs.getObject(i));
					}

				}
				objListtoReturn.add(objToReturn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return objListtoReturn;

	}

	public static List<Object> returnObjectsWhereColumnIsGreaterThan(String tableName, String ColumnName,
			String Value) {
		tables = ObjectMapper.getTables();
		List<Object> objListtoReturn = new ArrayList<>();
		Class<?> calledClass = tables.entrySet().stream()
				.filter((entry -> entry.getValue().getTableName().equals(tableName))).map(Map.Entry::getKey).findFirst()
				.get();
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder(
				"SELECT * FROM " + tableName + " WHERE " + ColumnName + " > '" + Value + "';");
		try (Connection conn = ds.getConnection()) {
			pstmt = conn.prepareStatement(query.toString());
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			ObjectGetterAndSetter ogas = new ObjectGetterAndSetter();
			while (rs.next()) {
				Object objToReturn = null;
				try {
					Constructor<?> c = calledClass.getDeclaredConstructor();
					c.setAccessible(true);
					objToReturn = calledClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				for (int i = 1; i <= columnCount; i++) {
					if (rs.getObject(i).getClass().equals(BigDecimal.class)) {
						BigDecimal value = (BigDecimal) rs.getObject(i);
						float f = value.floatValue();
						ogas.invokeSetter(objToReturn, md.getColumnName(i), f);
					} else {
						ogas.invokeSetter(objToReturn, md.getColumnName(i), rs.getObject(i));
					}

				}
				objListtoReturn.add(objToReturn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return objListtoReturn;

	}

}
