package com.one.frontend.service;

import com.one.frontend.repository.ProductRecommendationMappingMapper;
import com.one.frontend.response.RecommendRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRecommendationMappingService {

    @Autowired
    private ProductRecommendationMappingMapper mapper;

    public List<RecommendRes> getAllMappings() {
        return mapper.getAllMappings();
    }

    public List<RecommendRes> getMappingById(Long id) {
        return mapper.getMappingById(id);
    }
}
