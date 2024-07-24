package com.one.frontend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.one.repository"})
public class frontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(frontEndApplication.class, args);
    }
}