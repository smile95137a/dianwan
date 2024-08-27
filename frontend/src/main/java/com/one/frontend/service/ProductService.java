package com.one.frontend.service;

import com.one.frontend.repository.ProductRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.response.ProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    public List<ProductRes> getAllProduct(int page, int size) {
        int offset = page * size;
        try {
            return productRepository.getAllProduct(offset, size);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ProductRes getProductById(Integer productId) {
        return productRepository.getProductById(productId);
    }


}
