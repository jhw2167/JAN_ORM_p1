package com.revature.aron.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class settest {
	public static void main(String[] args) {
		// 0 1 1 2 3 5 8 13 21 34 55 89 144
		// Ratio approaches golden number
		int x = 131;
		Double[] fib = new Double[x];
		fib[0] = 0.0;
		fib[1] = 1.0;
		for (int i = 2; i < x; i++) {
			fib[i] = fib[i-1] + fib[i-2];
		}
		System.out.println(fib[x-1]);
		
	}
}
