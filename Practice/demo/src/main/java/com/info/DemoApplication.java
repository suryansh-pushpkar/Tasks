package com.info;


import javax.swing.Spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.info.service.Service;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DemoApplication {
	

  

	public static void main(String[] args) {
	ApplicationContext context	=SpringApplication.run(DemoApplication.class, args);
		Service service = context.getBean(Service.class);
		service.hello();

	
	}

}
