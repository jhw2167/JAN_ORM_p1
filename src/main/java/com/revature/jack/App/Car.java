package com.revature.jack.App;


//IMPORTS
import com.revature.jack.Annotations.*;
import java.util.Date;

/*
 * 		Example class car that we will map into SQL using
 * 		reflection in java and our ORM
 */

@Table(name="CarTest")
public class Car {
	
	@Column(name="model")
	private String model;
	
	@Column(name="year")
	private int year;
	
	@Column(name="valves")
	private Integer valves;
	
	@Column(name="miles")
	private float miles;
	
	@Column(name="purchaseDate")
	private Date purchaseDate;
	
	//we dont want this in our db
	private int qID;
	
	
	/* Declare Methods */
	
	Car() {
		super();
	}
	//END CONSTRUCTOR

}
