package com.category.excepton;

public class ResourceNotFoundException extends RuntimeException{
   public ResourceNotFoundException(String message) {
	   super(message);
   }
}