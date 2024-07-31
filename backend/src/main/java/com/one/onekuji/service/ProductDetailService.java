package com.one.onekuji.service;

import com.one.onekuji.model.ProductDetail;
import com.one.onekuji.repository.ProductDetailRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.ProductDetailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public ProductDetail getProductDetailById(Integer productDetailId) {
        return productDetailRepository.getProductDetailById(productDetailId);
    }

    public String createProductDetail(ProductDetailReq product) {
        try {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProductId(product.getProductId());
            productDetail.setProductName(product.getProductName());
            productDetail.setDescription(product.getDescription());
            productDetail.setSize(product.getSize());
            productDetail.setGrade(product.getGrade());
            productDetail.setImage(product.getImage());
            productDetail.setCreateDate(LocalDateTime.now());
            productDetailRepository.createProductDetail(productDetail);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    public String updateProductDetail(ProductDetailReq product) {
        try {
            ProductDetail productDetail = productDetailRepository.getProductDetailById(product.getProductDetailId());
            productDetail.setProductId(product.getProductId());
            productDetail.setProductName(product.getProductName());
            productDetail.setDescription(product.getDescription());
            productDetail.setSize(product.getSize());
            productDetail.setGrade(product.getGrade());
            productDetail.setImage(product.getImage());
            productDetail.setUpdateDate(LocalDateTime.now());
            productDetailRepository.updateProductDetail(productDetail);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    public String deleteProductDetail(Integer productDetailId) {
        try {
            productDetailRepository.deleteProductDetail(productDetailId);
            return "1";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }
}
