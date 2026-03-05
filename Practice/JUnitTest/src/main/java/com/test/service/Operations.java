package com.test.service;

public class Operations {
	public static int Add(int a, int b) {
		return a+b;
	}

	
	public int Multiply(int a, int b) {
		return a*b;
	}
	
	
	public int addAny(int... n) {
		int sum = 0;
		for(int nums : n) {
			sum += nums;
		}
		
		return sum;
	}
}
