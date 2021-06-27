package com.revature.app;

import com.revature.annotations.*;
import com.revature.app.ProducerCountry;

@Table(name="brand")
public class Brand {

	
	/* Variables that define brand */
	@Column
	@PrimaryKey
	String brandName;
	
	@Column
	@NotNull
	int yearFounded;
	
	@Column
	@NotNull
	@ForeignKey(refColumn = "country", refClass=ProducerCountry.class)
	String country;
	
	/* Methods for brand object
	 * 
	 */

	public Brand(String brandName, int yearFounded, String country) {
		super();
		this.brandName = brandName;
		this.yearFounded = yearFounded;
		this.country = country;
	}
	
	


	public String getBrandName() {
		return brandName;
	}


	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public int getYearFounded() {
		return yearFounded;
	}


	public void setYearFounded(int yearFounded) {
		this.yearFounded = yearFounded;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}




	/* Print out Brand information */
	@Override
	public String toString() {
		return "Brand [brandName=" + brandName + 
				", yearFounded=" + yearFounded + 
				", country=" + country + "]";
	}
	//End toString
}
