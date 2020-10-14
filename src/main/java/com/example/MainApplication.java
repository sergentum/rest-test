package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public final static String APP_CONTEXT = "test";

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
