package com.revature.jack.App;

//IMPORTS
import com.revature.jack.Annotations.*;
import java.util.Date;

/*
 * 		Example class car that we will map into SQL using
 * 		reflection in java and our ORM
 */

@Table(name = "Cars")
public class Car {

	@PrimaryKey
	@Column(name = "model")
	private String model;

	@NotNull
	@Unique
	@Column(name = "year")
	private int year;

	@Column(name = "valves")
	private Integer valves;

	@CheckColumn(checkStmt = "miles>=0")
	@Column(name = "miles")
	private float miles;

	@Column(name = "purchaseDate")
	private Date purchaseDate;

	// we dont want this in our db
	private int qID;

	/* Declare Methods */
	
	public Car(String model, int year, Integer valves, float miles, Date purchaseDate) {
		super();
		this.model = model;
		this.year = year;
		this.valves = valves;
		this.miles = miles;
		this.purchaseDate = purchaseDate;
	}
	
	Car() {
		super();
	}
	// END CONSTRUCTOR

}
