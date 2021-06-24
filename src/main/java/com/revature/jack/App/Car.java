package com.revature.jack.App;

//IMPORTS
import com.revature.jack.Annotations.*;
import java.util.Date;

/*
 * 		Example class car that we will map into SQL using
 * 		reflection in java and our ORM
 */

@Table(name = "cars")
public class Car {

	@Column
	@PrimaryKey
	private int id;
	
	@Column
	@ForeignKey(refColumn = "brandName", refClass = Brand.class)
	private String model;

	@NotNull
	@Unique
	@Column
	private int year;

	@Column
	private Integer valves;

	@CheckColumn(checkStmt = "miles>=0")
	@Column
	private float miles;

	@Column
	private Date purchaseDate;

	// we dont want this in our db
	private int qID;

	/* Declare Methods */
	
	public Car(int id, String model, int year, Integer valves, float miles, Date purchaseDate) {
		super();
		this.id = id;
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
