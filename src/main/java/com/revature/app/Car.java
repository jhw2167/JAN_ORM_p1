package com.revature.app;

import com.revature.annotations.*;
import com.revature.nate.annotations.NoArgsContructor;
import com.revature.nate.annotations.Setter;
import java.util.Objects;


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


	// we dont want this in our db
	private int qID;

	/* Declare Methods */
	
	public Car(int id, String model, int year, Integer valves, float miles) {
		super();
		this.id = id;
		this.model = model;
		this.year = year;
		this.valves = valves;
		this.miles = miles;
	}
	public Car() {
		super();
	}
	// END CONSTRUCTOR
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Integer getValves() {
		return valves;
	}
	public void setValves(Integer valves) {
		this.valves = valves;
	}
	public float getMiles() {
		return miles;
	}
	public void setMiles(float miles) {
		this.miles = miles;
	}
	public int getqID() {
		return qID;
	}
	public void setqID(int qID) {
		this.qID = qID;
	}
	@Override
	public String toString() {
		return "Car [id=" + id + ", model=" + model + ", year=" + year + ", valves=" + valves + ", miles=" + miles
				+ ", qID=" + qID + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, miles, model, qID, valves, year);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return id == other.id && Float.floatToIntBits(miles) == Float.floatToIntBits(other.miles)
				&& Objects.equals(model, other.model) && qID == other.qID && Objects.equals(valves, other.valves)
				&& year == other.year;
	}

	
	/* GETTERS AND SETTERS */
	
	
	
	
	

}
