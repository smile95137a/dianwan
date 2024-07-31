package com.one.frontend.service;

import com.one.frontend.model.Product;
import com.one.frontend.repository.ProductRepository;
import com.one.frontend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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