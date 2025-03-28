package com.one.frontend.service;

import com.one.frontend.model.StoreCategory;
import com.one.frontend.repository.StoreCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreCategoryService {

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    public List<StoreCategory> getAllCategories() {
        return storeCategoryRepository.findAll();
    }

    public StoreCategory getCategoryById(Long categoryId) {
        return storeCategoryRepository.findById(categoryId);
    }
}
