package com.revature.jack.App;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.revature.aron.exceptions.InvalidOperandException;
import com.revature.jack.ObjectMapper.ObjectMapper;
import com.revature.jack.ObjectMapper.ObjectQuery;

public class Main {

	public static void main(String[] args) throws ParseException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {

		// Lets go ahead and see if we get the correct values
		// for our car class:

		//Add our car class to model
		try {
			ObjectMapper.addToModel(Car.class);
			ObjectMapper.addToModel(Brand.class);
		} catch (Exception e) {
			System.out.println("Exception caught adding model: " + e.getMessage());
			e.printStackTrace();
		}
		
		//Add table to SQL
		try {
			ObjectMapper.buildDBFromModel();
		} catch (SQLException e) {
			System.out.println("SQLException caught building db: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception caught building db: " + e.getMessage());
			e.printStackTrace();
		}

		// Create table for brands
		Brand bmw = new Brand("BMW", 1916, "Germany");
		Brand ford = new Brand("Ford", 1903, "USA");
		Brand honda = new Brand("Honda", 1958, "Japan");
		Brand toyota = new Brand("Toyota", 1937, "Japan");

		// Create Table from Class
		Car newCar = new Car(4, "Ford", 2021, 4, 0.0f);
		Car newCar2 = new Car(5, "BMW", 2020, 5, 0.0f);
		Car oldCar = new Car(1, "Honda", 2001, 2, 101200.12f);
		List<Car> cars = new ArrayList<>();

		ObjectQuery.addObjectToTable(newCar);
		ObjectQuery.addObjectToTable(newCar2);
		ObjectQuery.addObjectToTable(oldCar);
		// QUERY for FORD
		System.out.println("Model is Ford:");
		List<Object> fordQuery = ObjectQuery.returnObjectsWhereColumnIs("cars", "model", "Ford");
		fordQuery.forEach(a -> System.out.println(a));
		System.out.println();
		// QUERY FOR YEAR 2021
		System.out.println("Year is 2021:");
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
		//QUERY FOR FORD 2021
		System.out.println("Model is Ford and Year is 2021:");
		List<Object> ford2021Query = ObjectQuery.returnObjectsWhere2ColumnsAre("cars", "model", "Ford","year","2021");
		milesgreaterQuery.forEach(a -> System.out.println(a));
		System.out.println();
		//QUERY FOR FORD newer than 2000 with Miles Not 10
		System.out.println("Ford newer than 2000 with Miles not 10:");
		String[] colNames = {"model","year","miles"};
		String[] operands = {"=",">","<>"};
		String[] values = {"Ford","2000","10"};
		try {
			List<Object> complexQueryEx = ObjectQuery.returnObjectsWhereColumnsAre("cars", colNames, operands, values);
		} catch (InvalidOperandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		milesgreaterQuery.forEach(a -> System.out.println(a));
		System.out.println();
	}

}
