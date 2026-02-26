package com.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.category.externalservices.CategoryClient;
@EnableFeignClients
@SpringBootApplication
public class CategoryMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoryMicroServiceApplication.class, args);
		System.out.println("Category Micro service Started");
	}
	
	

}
