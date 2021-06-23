package com.revature.jack.ObjectMapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

		try {
			Connection conn = ds.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// NOT FULLY IMPLEMENTED YET
	public static List<Object> returnObjectsWhereColumnIs(Object obj, String ColumnName, String Value) {
		tables = ObjectMapper.getTables();
		table = tables.get(obj.getClass());
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("SELECT * FROM " + table.getTableName() + " WHERE UPPER(" + ColumnName + ") = UPPER('" + Value +"');");
		try {
			Connection conn = ds.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			List<Object> args = new ArrayList<>();
			while(rs.next()) {
				for(int i = 1; i<=columnCount; i++) {
					args.add(rs.getObject(i));
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
