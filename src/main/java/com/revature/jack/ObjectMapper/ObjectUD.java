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

import com.revature.jack.Annotations.PrimaryKey;
import com.revature.jack.App.Car;

public class ObjectUD {
	
	static Map<Class<?>, SQLTable> tables;
	static SQLTable table;

	public static void removeObjectFromTable(Car car)
			throws IllegalArgumentException, IllegalAccessException, SecurityException {
		tables = ObjectMapper.getTables();
		table = tables.get(car.getClass());
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		String query = "DELETE FROM " + table.getTableName() + "WHERE id=" + car.getId();
		

		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void dropTable(SQLTable table) {
		
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		String query = "DROP TABLE IF EXISTS " + table.getTableName();
		

		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void truncateTable(SQLTable table) {
		
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		String query = "TRUNCATE TABLE IF EXISTS " + table.getTableName();
		

		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void updateObjectToTable(Object obj, Object newObj)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		tables = ObjectMapper.getTables();
		table = tables.get(obj.getClass());
		PreparedStatement pstmt;
		int pKey = 0;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("UPDATE " + table.getTableName() + " SET ");
		Collection<SQLColumn> cols = table.getColumns().values();
		List<Object> value = new ArrayList<>();
		for (SQLColumn col : cols) {
			query.append(col.getName() + " = ");
			Field field = newObj.getClass().getDeclaredField(col.getName());
			field.setAccessible(true);
			value.add(field.get(newObj));
			query.append(field.toString() + ", ");
		}
		query.deleteCharAt(query.length() - 1);
		for (Field f : newObj.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(PrimaryKey.class)) {
				pKey=f.getInt(newObj);
			}
		}
		query.append(" WHERE id= " + pKey);
		query.append(";");

		//System.out.println(query.toString());

		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
}
