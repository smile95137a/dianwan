package com.one.onekuji.service;

import com.one.eenum.ProductStatus;
import com.one.model.Product;
import com.one.model.User;
import com.one.onekuji.request.ProductReq;
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

    public String createProduct(ProductReq productReq) {
        try {
            Product product = new Product();
            User user = userRepository.getUserById(Math.toIntExact(productReq.getUserId()));
            product.setProductName(productReq.getProductName());
            product.setDescription(productReq.getDescription());
            product.setPrice(productReq.getPrice());
            product.setStockQuantity(productReq.getStockQuantity());
            product.setSoldQuantity(productReq.getStockQuantity());
            product.setImageUrl(productReq.getImageUrl());
            product.setStartDate(productReq.getStartDate());
            product.setEndDate(productReq.getEndDate());
            product.setCreatedAt(LocalDateTime.now());
            product.setCreatedUser(user.getNickname());
            product.setProductType(productReq.getProductType());
            if(productReq.getPrizeCategory() != null){
                product.setPrizeCategory(productReq.getPrizeCategory());
            }
            product.setStatus(ProductStatus.NOT_AVAILABLE_YET);
            productRepository.createProduct(product);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    public String updateProduct(ProductReq productReq) {
        try {
            Product product = productRepository.getProductById(Math.toIntExact(productReq.getProductId()));
            User user = userRepository.getUserById(Math.toIntExact(productReq.getUserId()));

            product.setProductName(productReq.getProductName());
            product.setDescription(productReq.getDescription());
            product.setPrice(productReq.getPrice());
            product.setStockQuantity(productReq.getStockQuantity());
            product.setSoldQuantity(productReq.getStockQuantity());
            product.setImageUrl(productReq.getImageUrl());
            product.setStartDate(productReq.getStartDate());
            product.setEndDate(productReq.getEndDate());
            product.setUpdatedAt(LocalDateTime.now());
            product.setUpdateUser(user.getNickname());
            product.setProductType(productReq.getProductType());
            if(productReq.getPrizeCategory() != null){
                product.setPrizeCategory(productReq.getPrizeCategory());
            }
            product.setStatus(productReq.getStatus());

            productRepository.updateProduct(product);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    public String deleteProduct(Integer productId) {
        try {
            productRepository.deleteProduct(productId);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }
}
