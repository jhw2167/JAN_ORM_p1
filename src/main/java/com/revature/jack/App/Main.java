package com.revature.jack.App;

import com.revature.jack.ObjectMapper.ObjectMapper;
import com.revature.jack.ObjectMapper.SQLTable;

public class Main {

	public static void main(String[] args) 
	{
		
		//Lets go ahead and see if we get the correct values
		//for our car class:
		
		SQLTable t1 = ObjectMapper.toTable(Car.class);
		System.out.println(t1);

	}

}
