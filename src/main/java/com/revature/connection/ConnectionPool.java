package com.revature.connection;

import javax.sql.DataSource;

//DB connection imports
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

//Connnection Pool Imports
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

//Java Util imports
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

//Logger Imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPool {
	// We use this class to supply database credentials and attain an object called
	// the Generic Object Pool
	// gPool is an object that holds all the connections to our database at one (in
	// a pool) drastically increasing performance whenever we perform a CRUM
	// operation on our DB.

	// JDBC Driver Name and Database URL
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static String JDBC_URL; 

	private static String JDBC_USER; 
	private static String JDBC_PASS;
	static { initConnectionProperties(); }

	// Typically supply this through a gitignored application.properties file OR
	// environment variables;
	private static GenericObjectPool gPool = null;
	
	//Logger
	private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);

	public DataSource setUpPool()  
	{
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e){
			log.error("Fatal Error, cannot find library class: " + JDBC_DRIVER);
			System.exit(1);
		}
		

		// Create an instance of GenericObjectPool that holds our POOl of Connection
		// Objects
		gPool = new GenericObjectPool();
		gPool.setMaxActive(5);

		// Create a ConnectionFactory Object which will be used by the pool to create
		// the connection object
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_URL, JDBC_USER, JDBC_PASS);

		// Create a PoolableConnectionFactory that will wrap around the Connection
		// Object created by the above connection Factory in order to add pooling
		// functionality
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);

		return new PoolingDataSource(gPool);

	}

	public GenericObjectPool getConnectionPool() {
		return gPool;
	}

	// This will be a method used to print the connection pool status

	public void printDbStatus() {
		System.out.println("Max: " + getConnectionPool().getMaxActive() + "; Active: "
				+ getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
	}
	//END PRINT DB STATUS
	
	/*
	 * Get connection properties from resources\properties folder
	 */
	private static void initConnectionProperties() {
		
		String filepath = "resources\\connection.properties";
		Properties prop = new Properties();
		
		//Load strings into properties
		try {
			
			prop.load(new FileReader(filepath));
			JDBC_URL = prop.getProperty("url");
			JDBC_USER = prop.getProperty("username");
			JDBC_PASS = prop.getProperty("password");
		 
		} catch (FileNotFoundException e) {
			System.out.println("could not find connection properties file for DB connection at: " + filepath);
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
			System.out.println("File I/O exception trying to load connection properties from: " + filepath);
			e.printStackTrace();
			System.exit(2);
		}
		
		
	}
	//END INIT PROJECT PROPERTIES
	
	
	
	
	
	
	
}
//END CLASS