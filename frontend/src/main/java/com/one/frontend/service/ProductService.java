package com.one.frontend.service;

import com.one.model.Product;
import com.one.repository.ProductRepository;
import com.one.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    public List<Product> getAllProduct() {
        return productRepository.getAllProduct();
    }

    public Product getProductById(Integer productId) {
        return productRepository.getProductById(productId);
    }


}
