package com.revature.objectmapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.revature.exceptions.InvalidSavePointException;

public class DatabaseTransaction {
	/**
	 * Method to beginTransaction
	 */
	public static void beginTransaction() throws SQLException{
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("BEGIN;"); 
		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Method To Commit Transaction
	 */
	public static void commitTransaction() throws SQLException{
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("COMMIT;");
		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Method to set a Save Point in A Transaction
	 * @param SavePointName
	 * @throws InvalidSavePointException 
	 */
	public static void setSavepoint(String SavePointName) throws InvalidSavePointException {
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		if(SavePointName.contains(" ")) {
			throw new InvalidSavePointException("Invalid Savepoint Name, cannot contain spaces");
		}
		StringBuilder query = new StringBuilder("SAVEPOINT " + SavePointName + ";");
		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Method used to roll back to a save point in a transaction
	 * @param SavePointName
	 */
	public static void rollbackToSavepoint(String SavePointName) throws SQLException{
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("ROLLBACK TO SAVEPOINT " + SavePointName + ";");
		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to roll back to beginning of transaction
	 * @param SavePointName
	 */
	public static void rollbackTransaction() throws SQLException{
		PreparedStatement pstmt;
		DataSource ds = ObjectMapper.getDs();
		StringBuilder query = new StringBuilder("ROLLBACK;");
		try (Connection conn = ds.getConnection();) {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
