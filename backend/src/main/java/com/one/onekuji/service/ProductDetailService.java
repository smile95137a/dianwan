package com.one.onekuji.service;

import com.one.onekuji.model.PrizeNumber;
import com.one.onekuji.model.ProductDetail;
import com.one.onekuji.repository.PrizeNumberMapper;
import com.one.onekuji.repository.ProductDetailRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.ProductDetailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrizeNumberMapper prizeNumberMapper;

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
            productDetail.setQuantity(product.getQuantity());
            productDetail.setSize(product.getSize());
            productDetail.setGrade(product.getGrade());
            productDetail.setImage(product.getImage());
            productDetail.setCreateDate(LocalDateTime.now());
            productDetailRepository.createProductDetail(productDetail);

            // 生成奖品编号
            generatePrizeNumbersForProductDetail(productDetail);

            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    private void generatePrizeNumbersForProductDetail(ProductDetail productDetail) {
        Integer currentMaxNumber = prizeNumberMapper.getMaxPrizeNumberByProductId(productDetail.getProductId());
        int startNumber = (currentMaxNumber != null ? currentMaxNumber : 0) + 1;

        int quantity = productDetail.getQuantity();
        IntStream.range(0, quantity).forEach(i -> {
            PrizeNumber prizeNumber = new PrizeNumber();
            prizeNumber.setProductId(productDetail.getProductId()); // 设置 productId
            prizeNumber.setProductDetailId(Long.valueOf(productDetail.getProductDetailId()));
            prizeNumber.setNumber(startNumber + i); // 从当前产品最大编号的下一个开始
            prizeNumber.setIsDrawn(false);
            prizeNumberMapper.insertPrizeNumber(prizeNumber);
        });
    }

    public String updateProductDetail(Integer productDetailId, ProductDetailReq product) {
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
