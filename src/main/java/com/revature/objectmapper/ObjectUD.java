package com.revature.objectmapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ObjectUD {

	public static void udpateAllWhere (String tableName, String columnName, String oldValue, String newValue)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		PreparedStatement pstmt;
		
		DataSource ds = ObjectMapper.getDs();
		String query = "UPDATE " + tableName + "SET " + columnName + " = '" + newValue + "' WHERE " + columnName + " = '" + oldValue + "';";
		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
