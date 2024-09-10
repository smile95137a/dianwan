package com.one.onekuji.service;

import com.one.onekuji.model.StoreCategory;
import com.one.onekuji.model.StoreProduct;
import com.one.onekuji.repository.StoreProductMapper;
import com.one.onekuji.request.StoreProductReq;
import com.one.onekuji.response.StoreProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreProductService {

    @Autowired
    private StoreProductMapper storeProductMapper;

    @Autowired
    private CategoryService categoryService;

    public List<StoreProductRes> getAllStoreProduct() {
        List<StoreProduct> storeProductList = storeProductMapper.findAll();
        return storeProductList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public StoreProductRes addStoreProduct(StoreProductReq storeProductReq) throws Exception {

        StoreCategory category = categoryService.getCategoryById(Long.valueOf(storeProductReq.getCategoryId()));
        if(category == null){
            throw new Exception("查無此類別，不能新增");
        }
        StoreProduct storeProduct = convertToEntity(storeProductReq);
        storeProduct.setCreatedAt(LocalDateTime.now());
        storeProduct.setUpdatedAt(LocalDateTime.now());
        storeProductMapper.insert(storeProduct);
        return convertToResponse(storeProduct);
    }

    public StoreProductRes updateStoreProduct(Long id, StoreProductReq storeProductReq) throws Exception {
        StoreProduct storeProduct = storeProductMapper.findById(id);
        if (storeProduct == null) {
            return null;
        }

        StoreCategory category = categoryService.getCategoryById(Long.valueOf(storeProductReq.getCategoryId()));
        if(category == null){
            throw new Exception("查無此類別，不能新增");
        }

        String description = formatTextToHtml(storeProductReq.getDescription());
        String specification = formatTextToHtml(storeProductReq.getSpecification());

        BigDecimal height = storeProductReq.getHeight() != null ? storeProductReq.getHeight() : BigDecimal.ZERO;
        BigDecimal width = storeProductReq.getWidth() != null ? storeProductReq.getWidth() : BigDecimal.ZERO;
        BigDecimal length = storeProductReq.getLength() != null ? storeProductReq.getLength() : BigDecimal.ZERO;

        // Perform the multiplication using BigDecimal methods
        BigDecimal size = height.multiply(width).multiply(length);
        // Update fields from request
        storeProduct.setProductName(storeProductReq.getProductName());
        storeProduct.setDescription(description);
        storeProduct.setHeight(storeProductReq.getHeight());
        storeProduct.setLength(storeProductReq.getLength());
        storeProduct.setWidth(storeProductReq.getWidth());
        storeProduct.setPrice(storeProductReq.getPrice());
        storeProduct.setStockQuantity(storeProductReq.getStockQuantity());
        storeProduct.setImageUrls(storeProductReq.getImageUrl());
        storeProduct.setCategoryId(storeProductReq.getCategoryId());
        storeProduct.setStatus(String.valueOf(storeProductReq.getStatus()));
        storeProduct.setSpecialPrice(storeProductReq.getSpecialPrice());
        storeProduct.setShippingMethod(storeProductReq.getShippingMethod());
        storeProduct.setShippingPrice(storeProductReq.getShippingPrice());
        storeProduct.setSize(size);
        storeProduct.setSpecification(specification);
        storeProduct.setUpdatedAt(LocalDateTime.now());

        // Update the store product
        storeProductMapper.update(storeProduct);

        // Convert to response
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
                storeProduct.getStoreProductId(),
                storeProduct.getProductName(),
                storeProduct.getDescription(),
                storeProduct.getPrice(),
                storeProduct.getStockQuantity(),
                storeProduct.getImageUrls(),
                storeProduct.getCategoryId(),
                storeProduct.getStatus(),
                storeProduct.getSpecialPrice(),
                storeProduct.getShippingMethod(),
                storeProduct.getSize(),
                storeProduct.getShippingPrice(),
                storeProduct.getLength(),
                storeProduct.getWidth(),
                storeProduct.getHeight(),
                storeProduct.getSpecification()
        );
    }

    private StoreProduct convertToEntity(StoreProductReq storeProductReq) {
        BigDecimal height = storeProductReq.getHeight() != null ? storeProductReq.getHeight() : BigDecimal.ZERO;
        BigDecimal width = storeProductReq.getWidth() != null ? storeProductReq.getWidth() : BigDecimal.ZERO;
        BigDecimal length = storeProductReq.getLength() != null ? storeProductReq.getLength() : BigDecimal.ZERO;
        String description = formatTextToHtml(storeProductReq.getDescription());
        String specification = formatTextToHtml(storeProductReq.getSpecification());
        BigDecimal size = height.multiply(width).multiply(length);
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setProductName(storeProductReq.getProductName());
        storeProduct.setDescription(description);
        storeProduct.setPrice(storeProductReq.getPrice());
        storeProduct.setStockQuantity(storeProductReq.getStockQuantity());
        storeProduct.setImageUrls(storeProductReq.getImageUrl());
        storeProduct.setCategoryId(storeProductReq.getCategoryId());
        storeProduct.setStatus(String.valueOf(storeProductReq.getStatus()));
        storeProduct.setSpecialPrice(storeProductReq.getSpecialPrice());
        storeProduct.setSize(size);
        storeProduct.setShippingMethod(storeProductReq.getShippingMethod());
        storeProduct.setShippingPrice(storeProductReq.getShippingPrice());
        storeProduct.setSpecification(specification);
        return storeProduct;
    }

    private String formatTextToHtml(String text) {
        if (text == null) return "";

        // Example formatting rules
        text = text.replaceAll("\\[p\\]", "<p>").replaceAll("\\[/p\\]", "</p>");
        text = text.replaceAll("\\[br\\]", "<br>");

        return text;
    }
}
