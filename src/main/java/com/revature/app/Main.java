package com.revature.app;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.revature.exceptions.InvalidOperandException;
import com.revature.objectmapper.*;

public class Main {
	public static void main(String[] args) throws ParseException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		// Lets go ahead and see if we get the correct values
		// for our car class:
		// Add our car class to model
		try {
			ObjectMapper.addToModel(Car.class);
			ObjectMapper.addToModel(Brand.class);
			ObjectMapper.addToModel(ProducerCountry.class);
		} catch (Exception e) {
			System.out.println("Exception caught adding model: " + e.getMessage());
			e.printStackTrace();
		}
		// Add table to SQL
		try {
			ObjectMapper.buildDBFromModel();
		} catch (Exception e) {
			System.out.println("Exception caught building db: " + e.getMessage());
			e.printStackTrace();
		}
		
		
		//add entries to producer countries
		ProducerCountry germany = new ProducerCountry("Germany", 5, 4_700_000); 
		ProducerCountry usa = new ProducerCountry("USA", 18, 2_200_000);
		ProducerCountry japan = new ProducerCountry("Japan", 10, 8_330_000);
		
		ObjectQuery.addObjectToTable(germany);
		ObjectQuery.addObjectToTable(usa);
		ObjectQuery.addObjectToTable(japan);
		
		// Create table for brands
		Brand bmw = new Brand("BMW", 1916, "Germany");
		Brand ford = new Brand("Ford", 1903, "USA");
		Brand honda = new Brand("Honda", 1958, "Japan");
		Brand toyota = new Brand("Toyota", 1937, "Japan");
		
		ObjectQuery.addObjectToTable(bmw);
		ObjectQuery.addObjectToTable(ford);
		ObjectQuery.addObjectToTable(honda);
		ObjectQuery.addObjectToTable(toyota);
		
		// Create Table from Class
		Car newCar = new Car(4, "Ford", 2021, 4, 0.0f);
		Car newCar2 = new Car(5, "BMW", 2020, 5, 0.0f);
		Car oldCar = new Car(1, "Honda", 2001, 2, 101200.12f);
		Car oldCar2 = new Car(10, "Toyota", 2002, 2, 123123.12f);
		
		ObjectQuery.addObjectToTable(newCar);
		ObjectQuery.addObjectToTable(newCar2);
		ObjectQuery.addObjectToTable(oldCar);
		
		
		// Transaction
		try {
			DatabaseTransaction.beginTransaction();
			ObjectQuery.addObjectToTable(oldCar2);
			// QUERY for ALL CARS
			System.out.println("All Cars Before RollBack:");
			List<Object> allCarsQuery = ObjectQuery.returnAllObjectsFromTable("cars");
			allCarsQuery.forEach(a -> System.out.println(a));
			System.out.println();
			DatabaseTransaction.rollbackTransaction();
			DatabaseTransaction.commitTransaction();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// QUERY for ALL CARS
		System.out.println("All Cars After RollBack:");
		List<Object> allCarsQuery = ObjectQuery.returnAllObjectsFromTable("cars");
		allCarsQuery.forEach(a -> System.out.println(a));
		System.out.println();
		
		// QUERY for FORD
		System.out.println("Model is Ford:");
		List<Object> fordQuery = ObjectQuery.returnObjectsWhereColumnIs("cars", "model", "Ford");
		fordQuery.forEach(a -> System.out.println(a));
		System.out.println();
		
		// QUERY FOR YEAR LESS THAN 2021
		System.out.println("Year is less than 2021:");
		List<Object> yearQuery = ObjectQuery.returnObjectsWhereColumnIsLessThan("cars", "Year", "2021");
		yearQuery.forEach(a -> System.out.println(a));
		System.out.println();
		
		// QUERY FOR MILES LESS THAN 10
		System.out.println("Miles Less than 10:");
		List<Object> milesQuery = ObjectQuery.returnObjectsWhereColumnIsLessThan("cars", "miles", "10");
		milesQuery.forEach(a -> System.out.println(a));
		System.out.println();
		
		
		// QUERY FOR MILES GREATER THAN 10
		System.out.println("Miles Greater than 10:");
		List<Object> milesgreaterQuery = ObjectQuery.returnObjectsWhereColumnIsGreaterThan("cars", "miles", "10");
		milesgreaterQuery.forEach(a -> System.out.println(a));
		System.out.println();
		
		// QUERY FOR FORD 2021
		System.out.println("Model is Ford and Year is 2021:");
		List<Object> ford2021Query = ObjectQuery.returnObjectsWhere2ColumnsAre("cars", "model", "Ford", "year", "2021");
		ford2021Query.forEach(a -> System.out.println(a));
		System.out.println();
		
		
		// QUERY FOR FORD newer than 2000 with Miles Not 10
		System.out.println("Ford newer than 2000 with Miles not 10:");
		String[] colNames = { "model", "year", "miles" };
		String[] operands = { "=", ">", "<>" };
		String[] values = { "Ford", "2000", "10" };
		try {
			List<Object> complexQueryEx = ObjectQuery.returnObjectsWhereColumnsAre("cars", colNames, operands, values);
			complexQueryEx.forEach(a -> System.out.println(a));
		} catch (InvalidOperandException e) {
			e.printStackTrace();
		}
		System.out.println();
		// QUERY FOR COUNT OF OBJECTS WHERE Model = Ford
		System.out.println("Count of cars that are Ford:");
		System.out.println(ObjectQuery.returnCountOfObjectsWhereColumnIs("cars", "model", "Ford") + "\n");
		// QUERY FOR COUNT OF OBJECTS WHERE MILES < 1000
		System.out.println("Count of cars that have less than 1000 Miles:");
		System.out.println(ObjectQuery.returnCountOfObjectsWhereColumnIsLessThan("cars", "miles", "1000") + "\n");
		// QUERY FOR COUNT OF OBJECTS WHERE MILES > 1000
		System.out.println("Count of cars that have more than 1000 Miles:");
		System.out.println(ObjectQuery.returnCountOfObjectsWhereColumnIsGreaterThan("cars", "miles", "1000") + "\n");
		// QUERY The CACHE;
		System.out.println("Cache Objects");
		ObjectCache oCache = ObjectQuery.getCache();
		oCache.getAllObjectsInCache(Car.class).forEach(car -> System.out.println(car));
	}
	//END MAIN METHOD
}
//END MAIN CLASS