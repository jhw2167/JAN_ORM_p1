package com.revature.jack.App;

import java.sql.SQLException;

import com.revature.jack.ObjectMapper.ObjectMapper;
import com.revature.jack.ObjectMapper.SQLTable;

public class Main {
	
	public static void main(String[] args) 
	{
		
		//Lets go ahead and see if we get the correct values
		//for our car class:
		
		//Add our car class to model
		try {
			ObjectMapper.addToModel(Car.class);
			System.out.println("Sucessfully added Car to model and converted to SQL Table");
		} catch (Exception e) {
			System.out.println("Exception caught adding model: " + e.getMessage());
			e.printStackTrace();
		}
		
		//Add table to SQL
		try {
			ObjectMapper.buildDBFromModel();
		} catch (SQLException e) {
			System.out.println("Exception caught building db: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
