package com.one.frontend.service;

import com.one.frontend.model.ProductDetail;
import com.one.frontend.repository.ProductDetailRepository;
import com.one.frontend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProductDetail> getAllProductDetail() {
        return productDetailRepository.getAllProductDetail();
    }

    public List<ProductDetail> getProductDetailById(Integer productDetailId) {
        return productDetailRepository.getProductDetailByProductId(Long.valueOf(productDetailId));
    }


    public List<ProductDetail> getProductDetailByProductId(Long productId) {
        return productDetailRepository.getProductDetailByProductId(productId);
    }
}
