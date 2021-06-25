package com.revature.jack.App;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.revature.jack.ObjectMapper.ObjectQuery;

public class Main {

	public static void main(String[] args) throws ParseException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {

		// Lets go ahead and see if we get the correct values
		// for our car class:

//		//Add our car class to model
//		try {
//			ObjectMapper.addToModel(Car.class);
//			System.out.println("Sucessfully added Car to model and converted to SQL Table");
//		} catch (Exception e) {
//			System.out.println("Exception caught adding model: " + e.getMessage());
//			e.printStackTrace();
//		}
//		
//		//Add table to SQL
//		try {
//			ObjectMapper.buildDBFromModel();
//		} catch (SQLException e) {
//			System.out.println("SQLException caught building db: " + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.out.println("Exception caught building db: " + e.getMessage());
//			e.printStackTrace();
//		}

		// Create table for brands

		// Create table for brands
		ObjectQuery.createTableFromClass(Brand.class);
		Brand bmw = new Brand("BMW", 1916, "Germany");
		Brand ford = new Brand("Ford", 1903, "USA");
		Brand honda = new Brand("Honda", 1958, "Japan");
		Brand toyota = new Brand("Toyota", 1937, "Japan");

		// Create Table from Class
		ObjectQuery.createTableFromClass(Car.class);
		Car newCar = new Car(4, "Ford", 2021, 4, 0.0f, new Date(System.currentTimeMillis()));
		Car newCar2 = new Car(5, "BMW", 2020, 5, 0.0f, new Date(System.currentTimeMillis()));
		Car oldCar = new Car(1, "Honda", 2001, 2, 101200.12f, new Date(1000000000000L));
		List<Car> cars = new ArrayList<>();

		ObjectQuery.addObjectToTable(newCar);
		ObjectQuery.addObjectToTable(newCar2);
		ObjectQuery.addObjectToTable(oldCar);

		try {
			List<?> fordQuery = ObjectQuery.returnObjectsWhereColumnIs("cars", "model", "Ford");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
