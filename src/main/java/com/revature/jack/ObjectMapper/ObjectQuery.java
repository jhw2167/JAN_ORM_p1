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

	public static List<Object> returnObjectsWhereColumnIs(String tableName, String ColumnName, String Value) {
		tables = ObjectMapper.getTables();
		List<Object> objListtoReturn = new ArrayList<>();
		Class<?> calledClass = tables.entrySet().stream()
				.filter((entry -> entry.getValue().getTableName().equals(tableName))).map(Map.Entry::getKey).findFirst()
				.get();
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder(
				"SELECT * FROM " + tableName + " WHERE " + ColumnName + " = '" + Value + "';");
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
	
	public static List<Object> returnObjectsWhereColumnIsGreaterThan(String tableName, String ColumnName, String Value) {
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
