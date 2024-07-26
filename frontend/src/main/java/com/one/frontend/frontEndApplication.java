package com.one.frontend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.one.repository"})
@ComponentScan(basePackages = {"com.one"})
public class frontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(frontEndApplication.class, args);
    }
}