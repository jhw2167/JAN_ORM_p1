package com.revature.errors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.revature.exceptions.ReservedWordException;

public class ErrorCheck {

	public static boolean keywordCheck(String string) {
		
		try {
		      File file = new File("keywords.txt");
		      Scanner scan = new Scanner(file);
		      while (scan.hasNextLine()) {
		        String data = scan.nextLine();
		        if(string.equalsIgnoreCase(data)) {
		        	throw new ReservedWordException();
		        }
		        
		      }
		      scan.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("keyword.txt file not found");
		      e.printStackTrace();
		    }
		
		return false;
		
	}
	public static void main(String[] args) {
		System.out.println(keywordCheck("INSERT"));
	}
	
}
