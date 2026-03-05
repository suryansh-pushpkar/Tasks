package com.reflection;
import com.reflection.*;
import java.lang.reflect.Method;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
		
		try {
			Class c=Class.forName("com.reflection.Apple");
		Apple apple = (Apple)c.newInstance();
			Apple apple2 = (Apple)c.newInstance();
			
			Method m = c.getDeclaredMethod("show", null);
			m.setAccessible(true);
			m.invoke(apple);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
