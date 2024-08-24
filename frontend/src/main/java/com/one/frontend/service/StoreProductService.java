package com.one.frontend.service;

import com.one.frontend.response.StoreProductRes;
import com.one.frontend.repository.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreProductService {

    @Autowired
    private StoreProductRepository storeProductRepository;

    public List<StoreProductRes> getAllStoreProducts() {
        return storeProductRepository.findAll();
    }

    public StoreProductRes getStoreProductById(Long id) {
        Optional<StoreProductRes> product = Optional.ofNullable(storeProductRepository.findById(id));
        return product.orElse(null);
    }
}
