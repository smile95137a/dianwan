package com.one.onekuji;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// BackendApplication.java
@SpringBootApplication
@MapperScan(basePackages = {"com.one.onekuji.repository"})
public class BackendApplication{
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
