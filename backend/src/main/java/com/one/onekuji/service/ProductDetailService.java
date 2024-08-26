package com.one.onekuji.service;

import com.one.onekuji.repository.ProductDetailRepository;
import com.one.onekuji.request.DetailReq;
import com.one.onekuji.response.DetailRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailMapper;

    public List<DetailRes> getAllProductDetails() {
        return productDetailMapper.findAll();
    }

    public List<DetailRes> addProductDetails(List<DetailReq> detailReqs) {
        List<DetailRes> detailResList = new ArrayList<>();

        for (DetailReq detailReq : detailReqs) {
            // Escape text for HTML in description and specification
            detailReq.setDescription(escapeTextForHtml(detailReq.getDescription()));
            detailReq.setSpecification(escapeTextForHtml(detailReq.getSpecification()));

            // Calculate size
            BigDecimal size = detailReq.getHeight()
                    .multiply(detailReq.getWidth())
                    .multiply(detailReq.getLength());
            detailReq.setSize(size.toString());

            productDetailMapper.insert(detailReq);

            DetailRes detailRes = productDetailMapper.findById(Long.valueOf(detailReq.getProductDetailId()));

            detailResList.add(detailRes);
        }

        return detailResList;
    }

    public DetailRes updateProductDetail(Long id, DetailReq productDetailReq) {
        productDetailReq.setProductDetailId(Math.toIntExact(id));

        // Escape text for HTML in description and specification
        productDetailReq.setDescription(escapeTextForHtml(productDetailReq.getDescription()));
        productDetailReq.setSpecification(escapeTextForHtml(productDetailReq.getSpecification()));

        // Calculate size
        BigDecimal size = productDetailReq.getHeight()
                .multiply(productDetailReq.getWidth())
                .multiply(productDetailReq.getLength());
        productDetailReq.setSize(size.toString());

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
                escapeTextForHtml(detailReq.getDescription()), // Escape HTML in description
                detailReq.getNote(),
                detailReq.getSize(),
                detailReq.getQuantity(),
                detailReq.getStockQuantity(),
                detailReq.getProductName(),
                detailReq.getGrade(),
                detailReq.getPrice(),
                detailReq.getSliverPrice(),
                detailReq.getImageUrls(),
                detailReq.getLength(),
                detailReq.getWidth(),
                detailReq.getHeight(),
                escapeTextForHtml(detailReq.getSpecification()) // Escape HTML in specification
        );
    }

    private String escapeTextForHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("\n", "<br/>")
                .replace("\r", "");
    }
}
