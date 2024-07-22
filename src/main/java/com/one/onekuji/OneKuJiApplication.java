package com.one.onekuji;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.one.onekuji.repository")
public class OneKuJiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneKuJiApplication.class, args);
    }

}
