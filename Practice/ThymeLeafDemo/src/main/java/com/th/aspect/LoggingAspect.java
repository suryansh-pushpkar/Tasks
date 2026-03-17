package com.th.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.th.controller.*.*(..))")
    public void logBeforeMethodExecution() {
        System.out.println("A method in the controller package is about to be executed.");
    }
}