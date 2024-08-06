package com.one.onekuji;

import com.one.onekuji.repository.ProductRepository;
import com.one.onekuji.util.ImageUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// BackendApplication.java
@SpringBootApplication
@MapperScan(basePackages = {"com.one.onekuji.repository"})
public class BackendApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        String imagePath = "C:/Users/a0970/IdeaProjects/oneKuJi/backend/uploads/4.png";
        String base64 = ImageUtils.imageToBase64(imagePath);
        base64 = "data:image/png;base64,"+base64;
        productRepository.updateImageUrlForProducts(base64);
    }
}
