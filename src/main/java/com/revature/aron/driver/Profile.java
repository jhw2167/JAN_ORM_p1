package com.revature.aron.driver;

import com.revature.aron.annotations.Column;
import com.revature.aron.annotations.Table;

@Table(tableName = "profile")
public class Profile {

	@Column(columnName = "name")
	private String name;
	@Column(columnName = "age")
	private int age;

	public Profile(String name, int age) {
		this.name = name;
		this.age = age;

	}
}
