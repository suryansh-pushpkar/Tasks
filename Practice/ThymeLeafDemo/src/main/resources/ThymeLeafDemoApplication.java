package com.th;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ThymeLeafDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThymeLeafDemoApplication.class, args);
    }

}
