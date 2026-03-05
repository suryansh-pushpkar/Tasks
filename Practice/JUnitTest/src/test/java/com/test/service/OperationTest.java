package com.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class OperationTest {
	
	@BeforeAll
	public static void init() {
		System.out.println("Init method before any test case");
	}
	@Test
	public void AddTest(int a, int b) {
		int result  = Operations.Add(40, 40);
		
		int expected = 80;
		Assertions.assertEquals(expected, result);
	}


}
