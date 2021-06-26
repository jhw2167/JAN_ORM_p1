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
	
	
	
}
