package com.revature.aron.objectmapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.revature.aron.driver.Profile;

public class ObjectMapper {

	public static void createClassTable(Object obj, Connection conn) {
		PreparedStatement pstmt;
		
		final String addObjectSQL = "CREATE TABLE ? (";
		try {
			pstmt = conn.prepareStatement(addObjectSQL);
			pstmt.setString(1, obj.getClass().getSimpleName().toString());

			for (Field field : obj.getClass().getDeclaredFields()) {
				Class type = field.getType();
				String name = field.getName();
				String typeToAdd = null;

				if (type.getSimpleName().toString().equals("String")) {
					typeToAdd = "VARCHAR(50)";
				}

				if (type.getSimpleName().toString().equals("int")) {
					typeToAdd = "NUMERIC";
				}

				String placeHolder = pstmt.toString() + " ? ?,";
				pstmt = conn.prepareStatement(placeHolder);
				pstmt.setString(1, name.toString());
				pstmt.setString(2, typeToAdd);
			}
			final String finalQuery = (pstmt.toString().substring(0, pstmt.toString().length() - 1) + ");").replace("'",
					"");

			System.out.println(finalQuery);
			pstmt = conn.prepareStatement(finalQuery);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void dropClassTable(Object obj, Connection conn) {
		PreparedStatement pstmt;
		final String addObjectSQL = "DROP TABLE IF EXISTS ? ;";
		try {
			pstmt = conn.prepareStatement(addObjectSQL);
			pstmt.setString(1, obj.getClass().getSimpleName().toString());
			final String finalQuery = pstmt.toString().replace("'","");
			pstmt = conn.prepareStatement(finalQuery);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void insertObjToClassTable(Object obj, Connection conn) throws IllegalArgumentException, IllegalAccessException {
		PreparedStatement pstmt;
		final String addObjectSQL = "INSERT INTO ? (";
		try {
			pstmt = conn.prepareStatement(addObjectSQL);
			pstmt.setString(1, obj.getClass().getSimpleName().toString());

			for (Field field : obj.getClass().getDeclaredFields()) {
				String name = field.getName();
				String placeHolder = pstmt.toString() + " ? ,";
				pstmt = conn.prepareStatement(placeHolder);
				pstmt.setString(1, name.toString());
			}
			final String intQuery = (pstmt.toString().substring(0, pstmt.toString().length() - 1) + ") VALUES (").replace("'", "");
			pstmt = conn.prepareStatement(intQuery);
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(obj);
				String placeHolder = pstmt.toString() + " ? ,";
				pstmt = conn.prepareStatement(placeHolder);
				pstmt.setString(1, value.toString());
			}
			
			final String finalQuery = (pstmt.toString().substring(0, pstmt.toString().length() - 1) + ");");
			
			System.out.println(finalQuery);
			pstmt = conn.prepareStatement(finalQuery);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
