package com.one.frontend.service;

import com.one.frontend.repository.ProductDetailRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.response.ProductDetailRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProductDetailRes> getAllProductDetail() {
        return productDetailRepository.getAllProductDetail();
    }

    public List<ProductDetailRes> getProductDetailByProductId(Long productId) {
        return productDetailRepository.getProductDetailByProductId(productId);
    }
}
