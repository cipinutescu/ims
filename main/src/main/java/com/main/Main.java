package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.auth","com.chat","com.time","com.arch"})
@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
        System.out.println("Application successfull microservice started !");
    }
}
