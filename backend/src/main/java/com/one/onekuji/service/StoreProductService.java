package com.one.onekuji.service;

import com.one.onekuji.model.StoreProduct;
import com.one.onekuji.repository.StoreProductMapper;
import com.one.onekuji.request.StoreProductReq;
import com.one.onekuji.response.StoreProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreProductService {

    @Autowired
    private StoreProductMapper storeProductMapper;

    public List<StoreProductRes> getAllStoreProduct() {
        List<StoreProduct> storeProductList = storeProductMapper.findAll();
        return storeProductList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public StoreProductRes addStoreProduct(StoreProductReq storeProductReq) {
        StoreProduct storeProduct = convertToEntity(storeProductReq);
        storeProduct.setCreatedAt(LocalDateTime.now());
        storeProduct.setUpdatedAt(LocalDateTime.now());
        storeProductMapper.insert(storeProduct);
        return convertToResponse(storeProduct);
    }

    public StoreProductRes updateStoreProduct(Long id, StoreProductReq storeProductReq) {
        StoreProduct storeProduct = storeProductMapper.findById(id);
        if (storeProduct == null) {
            return null;
        }
        storeProduct.setProductName(storeProductReq.getProductName());
        storeProduct.setDescription(storeProductReq.getDescription());
        storeProduct.setPrice(storeProductReq.getPrice());
        storeProduct.setStockQuantity(storeProductReq.getStockQuantity());
        storeProduct.setImageUrl(storeProductReq.getImageUrl());
        storeProduct.setCategoryId(storeProductReq.getCategoryId());
        storeProduct.setStatus(String.valueOf(storeProductReq.getStatus()));
        storeProduct.setSpecialPrice(storeProductReq.getSpecialPrice());
        storeProduct.setShippingMethod(storeProductReq.getShippingMethod());
        storeProduct.setShippingPrice(storeProductReq.getShippingPrice());
        storeProduct.setSize(storeProductReq.getSize());
        storeProduct.setUpdatedAt(LocalDateTime.now());
        storeProductMapper.update(storeProduct);
        return convertToResponse(storeProduct);
    }

    public boolean deleteStoreProduct(Long id) {
        StoreProduct storeProduct = storeProductMapper.findById(id);
        if (storeProduct == null) {
            return false;
        }
        storeProductMapper.delete(id);
        return true;
    }

    private StoreProductRes convertToResponse(StoreProduct storeProduct) {
        return new StoreProductRes(
                storeProduct.getProductName(),
                storeProduct.getDescription(),
                storeProduct.getPrice(),
                storeProduct.getStockQuantity(),
                storeProduct.getImageUrl(),
                storeProduct.getCategoryId(),
                storeProduct.getStatus(),
                storeProduct.getSpecialPrice(),
                storeProduct.getShippingMethod(),
                storeProduct.getSize(),
                storeProduct.getShippingPrice()
        );
    }

    private StoreProduct convertToEntity(StoreProductReq storeProductReq) {
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setProductName(storeProductReq.getProductName());
        storeProduct.setDescription(storeProductReq.getDescription());
        storeProduct.setPrice(storeProductReq.getPrice());
        storeProduct.setStockQuantity(storeProductReq.getStockQuantity());
        storeProduct.setImageUrl(storeProductReq.getImageUrl());
        storeProduct.setCategoryId(storeProductReq.getCategoryId());
        storeProduct.setStatus(String.valueOf(storeProductReq.getStatus()));
        storeProduct.setSpecialPrice(storeProductReq.getSpecialPrice());
        storeProduct.setSize(storeProductReq.getSize());
        storeProduct.setShippingMethod(storeProductReq.getShippingMethod());
        storeProduct.setShippingPrice(storeProductReq.getShippingPrice());
        return storeProduct;
    }
}
