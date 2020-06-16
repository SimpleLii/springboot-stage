package com.simplelii.app.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = "com.simplelii.app")
@EnableAspectJAutoProxy
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
