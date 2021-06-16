package com.revature.aron.driver;

import java.sql.Connection;

import javax.sql.DataSource;

import com.revature.aron.connection.ConnectionPool;
import com.revature.aron.objectmapper.ObjectMapper;

public class Driver {
	public static void main(String[] args) {
		Profile obj = new Profile("Aron", 23);
		Profile obj1 = new Profile("John", 21);
		LoginCredentials log1 = new LoginCredentials("Aron","aronuser","aronPass1");
		LoginCredentials log2 = new LoginCredentials("John","johnuser","johnPass1");
		ConnectionPool conn = new ConnectionPool();
		DataSource dataSource;
		Connection connObj = null;
		try {
			dataSource = conn.setUpPool();
			connObj = dataSource.getConnection();
			ObjectMapper.dropClassTable(obj, connObj);
			ObjectMapper.createClassTable(obj, connObj);
			ObjectMapper.insertObjToClassTable(obj, connObj);
			ObjectMapper.insertObjToClassTable(obj1, connObj);
			ObjectMapper.dropClassTable(log1, connObj);
			ObjectMapper.createClassTable(log1, connObj);
			ObjectMapper.insertObjToClassTable(log1, connObj);
			ObjectMapper.insertObjToClassTable(log2, connObj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
