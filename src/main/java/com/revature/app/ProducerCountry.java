package com.revature.app;

//IMPORTS
import com.revature.annotations.*;

/*
 * Gives us some data about the country the car was produced in
 */

@Table(name="producer_country")
public class ProducerCountry {

	/* Declare variables */
	
	@PrimaryKey
	@Column
	private String country;
	
	@Column
	@NotNull
	private int numManufactuers;
	
	@Column
	@NotNull
	private long totalAnnual;

	
	
	/* Declare methods */
	
	
	//END CONSTRUCTOR
	
	
	
	/* GETTERS AND SETTERS */
	
	public String getCountry() {
		return country;
	}

	public ProducerCountry(String country, int numManufactuers, int totalExported) {
		super();
		this.country = country;
		this.numManufactuers = numManufactuers;
		this.totalAnnual = totalExported;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getNumManufactuers() {
		return numManufactuers;
	}

	public void setNumManufactuers(int numManufactuers) {
		this.numManufactuers = numManufactuers;
	}

	public long getTotalAnnual() {
		return totalAnnual;
	}

	public void setTotalAnnual(int totalAnnual) {
		this.totalAnnual = totalAnnual;
	}

	@Override
	public String toString() {
		return "ProducerCountry [country=" + country + ", numManufactuers=" + numManufactuers + ", totalAnnual="
				+ totalAnnual + "]";
	}
	
	
	
}
//END CLASS