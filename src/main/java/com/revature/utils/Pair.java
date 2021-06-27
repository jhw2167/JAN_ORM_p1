package com.revature.utils;


/*
 * 	Java has no native support for "paired" objects, so we do our
 *  best to give our own implementation here
 *  
 *   Pair will hold two public associated values 
 */

public class Pair<T, U> {
	
	//Declare vars
	public final T v1;
	public final U v2;
	
	
	
	//Assign vars:
	public Pair(T v1, U v2) 
	{
		super();
		this.v1 = v1;
		this.v2 = v2;
	}
	//END CONSTRUCTOR

	

}
//END CLASS