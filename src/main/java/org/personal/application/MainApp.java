package org.personal.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(MainApp.class,args);
    }
}