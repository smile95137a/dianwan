package com.one.onekuji.service;

import com.one.onekuji.repository.ProductDetailRepository;
import com.one.onekuji.request.DetailReq;
import com.one.onekuji.response.DetailRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailMapper;

    public List<DetailRes> getAllProductDetails() {
        return productDetailMapper.findAll();
    }

    public DetailRes addProductDetail(DetailReq productDetailReq) {
        productDetailMapper.insert(productDetailReq);
        return productDetailMapper.findById(Long.valueOf(productDetailReq.getProductDetailId()));
    }

    public List<DetailRes> addProductDetails(List<DetailReq> detailReqs) {
        List<DetailRes> detailResList = new ArrayList<>();

        for (DetailReq detailReq : detailReqs) {
            // 假设 insert 方法没有返回值，但会插入到数据库
            productDetailMapper.insert(detailReq);

            // 获取插入后的 DetailRes 对象，可能需要从数据库中查询
            DetailRes detailRes = productDetailMapper.findById(Long.valueOf(detailReq.getProductDetailId()));

            detailResList.add(detailRes);
        }

        return detailResList;
    }


    public DetailRes updateProductDetail(Long id, DetailReq productDetailReq) {
        productDetailReq.setProductDetailId(Math.toIntExact(id));
        productDetailMapper.update(productDetailReq);
        return productDetailMapper.findById(id);
    }

    public boolean deleteProductDetail(Long id) {
        int deleted = productDetailMapper.delete(id);
        return deleted > 0;
    }

    private DetailRes convertToEntity(DetailReq detailReq) {
        return new DetailRes(
                detailReq.getProductDetailId(),
                detailReq.getProductId(),
                detailReq.getDescription(),
                detailReq.getNote(),
                detailReq.getSize(),
                detailReq.getQuantity(),
                detailReq.getStockQuantity(),
                detailReq.getProductName(),
                detailReq.getGrade(),
                detailReq.getPrice(),
                detailReq.getSliverPrice(),
                detailReq.getImageUrl()
        );
    }
}
