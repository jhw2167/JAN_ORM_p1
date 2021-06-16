package com.revature.aron.driver;

import com.revature.aron.annotations.Column;
import com.revature.aron.annotations.Table;

@Table(tableName = "profile")
public class LoginCredentials {
	@Column(columnName = "name")
	private String name;
	@Column(columnName = "username")
	private String username;
	@Column(columnName = "password")
	private String password;
	public LoginCredentials(String name, String username, String password) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
	}
	
}
