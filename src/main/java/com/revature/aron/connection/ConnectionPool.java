package com.revature.aron.connection;

import javax.sql.DataSource;

//Connnection Pool Imports
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

//Java Util imports
import java.util.ArrayList;
import java.util.List;
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
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";

	static final String JDBC_USER = "postgres";
	static final String JDBC_PASS = "postgres";

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
}
