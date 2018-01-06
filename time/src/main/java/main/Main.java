package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan({"com.time"})
@SpringBootApplication
public class Main {


    public static void main(String[] args) throws IOException {

        SpringApplication.run(Main.class, args);
        System.out.println("Chat microservice started !");
    }
}
