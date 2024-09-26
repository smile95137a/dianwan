package com.one.onekuji.service;

import com.one.onekuji.model.PrizeNumber;
import com.one.onekuji.repository.PrizeNumberMapper;
import com.one.onekuji.repository.ProductDetailRepository;
import com.one.onekuji.repository.ProductRepository;
import com.one.onekuji.request.DetailReq;
import com.one.onekuji.response.DetailRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PrizeNumberMapper prizeNumberMapper;

    public List<DetailRes> getAllProductDetails() {
        return productDetailMapper.findAll();
    }

    public List<DetailRes> addProductDetails(List<DetailReq> detailReqs) {
        List<DetailRes> detailResList = new ArrayList<>();
        List<PrizeNumber> allPrizeNumbers = new ArrayList<>();
        int totalQuantity = 0;

        // 計算總數量
        for (DetailReq detailReq : detailReqs) {
            totalQuantity += detailReq.getQuantity();
        }

        // 更新產品總數量
        productRepository.updateTotalQua(totalQuantity, detailReqs.get(0).getProductId());

        // 創建並打亂獎品編號
        List<Integer> shuffledNumbers = new ArrayList<>();
        for (int i = 1; i <= totalQuantity; i++) {
            shuffledNumbers.add(i);
        }
        Collections.shuffle(shuffledNumbers);

        int currentIndex = 0;

        for (DetailReq detailReq : detailReqs) {
            // 轉義 HTML 字符
            detailReq.setDescription(escapeTextForHtml(detailReq.getDescription()));
            detailReq.setSpecification(escapeTextForHtml(detailReq.getSpecification()));
            detailReq.setStockQuantity(detailReq.getQuantity());
            // 計算尺寸
            BigDecimal size = detailReq.getHeight()
                    .multiply(detailReq.getWidth())
                    .multiply(detailReq.getLength());
            detailReq.setSize(size.toString());

            // 插入產品細節
            productDetailMapper.insert(detailReq);
            Long productDetailId = Long.valueOf(detailReq.getProductDetailId());

            // 為每個數量創建獎品編號
            List<PrizeNumber> detailPrizeNumbers = new ArrayList<>();
            for (int i = 0; i < detailReq.getQuantity(); i++) {
                PrizeNumber prizeNumber = new PrizeNumber();
                prizeNumber.setProductId(detailReq.getProductId());
                prizeNumber.setProductDetailId(Math.toIntExact(productDetailId));
                prizeNumber.setNumber(String.valueOf(shuffledNumbers.get(currentIndex)));
                prizeNumber.setIsDrawn(false);
                prizeNumber.setLevel(detailReq.getGrade());
                detailPrizeNumbers.add(prizeNumber);
                currentIndex++;
            }

            // 打亂當前產品細節的獎品編號
            Collections.shuffle(detailPrizeNumbers);
            allPrizeNumbers.addAll(detailPrizeNumbers);

            // 獲取並添加詳細回應
            DetailRes detailRes = productDetailMapper.findById(productDetailId);
            detailResList.add(detailRes);
        }

        // 批量插入所有獎品編號
        if (!allPrizeNumbers.isEmpty()) {
            prizeNumberMapper.insertBatch(allPrizeNumbers);
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
                escapeTextForHtml(detailReq.getSpecification()),
                detailReq.getProbability()
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
