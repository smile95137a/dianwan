package com.one.frontend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.one.frontend.model.StoreProduct;
import com.one.frontend.repository.StoreProductRepository;
import com.one.frontend.response.StoreProductRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreProductService {

    private final StoreProductRepository storeProductRepository;

    public List<StoreProductRes> getAllStoreProducts(int page, int size) {
        int offset = page * size;
        return storeProductRepository.findAll(offset, size);
    }

    public StoreProductRes getStoreProductByProductCode(String productCode) {
    	var res = storeProductRepository.findByProductCodeWithFavorites(productCode);
    	return res;
    }

    public boolean updateProductPopularity(String productCode) {
        try {
            storeProductRepository.incrementPopularityByProductCode(productCode);
            return true; 
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }

}
