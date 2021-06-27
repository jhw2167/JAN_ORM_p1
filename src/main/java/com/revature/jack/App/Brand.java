package com.revature.jack.App;

//Imports
import com.revature.jack.Annotations.*;

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


	/* Print out Brand information */
	@Override
	public String toString() {
		return "Brand [brandName=" + brandName + 
				", yearFounded=" + yearFounded + 
				", country=" + country + "]";
	}
	
}
