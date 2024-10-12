package com.one.onekuji.service;

import com.one.onekuji.eenum.ProductStatus;
import com.one.onekuji.model.PrizeNumber;
import com.one.onekuji.model.Product;
import com.one.onekuji.model.ProductDetail;
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

        // 计算总数量，排除 grade 为 "LAST" 的项
        for (DetailReq detailReq : detailReqs) {
            if (shouldIncludeInPrizeNumbers(detailReq)) {
                totalQuantity += detailReq.getQuantity();
            }
        }

        // 更新产品总数量
        productRepository.updateTotalQua(totalQuantity, detailReqs.get(0).getProductId());

        // 创建并打乱奖品编号
        List<Integer> shuffledNumbers = new ArrayList<>();
        for (int i = 1; i <= totalQuantity; i++) {
            shuffledNumbers.add(i);
        }
        Collections.shuffle(shuffledNumbers);

        int currentIndex = 0;

        for (DetailReq detailReq : detailReqs) {
            // 转义 HTML 字符
            detailReq.setDescription(escapeTextForHtml(detailReq.getDescription()));
            detailReq.setSpecification(escapeTextForHtml(detailReq.getSpecification()));
            detailReq.setStockQuantity(detailReq.getQuantity());

            // 计算尺寸
            BigDecimal size = detailReq.getHeight()
                    .multiply(detailReq.getWidth())
                    .multiply(detailReq.getLength());
            detailReq.setSize(size.toString());

            // 插入产品细节
            productDetailMapper.insert(detailReq);
            Long productDetailId = Long.valueOf(detailReq.getProductDetailId());

            // 为每个数量创建奖品编号，排除 grade 为 "SP" 的项
            if (shouldIncludeInPrizeNumbers(detailReq)) {
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

                // 打乱当前产品细节的奖品编号
                Collections.shuffle(detailPrizeNumbers);
                allPrizeNumbers.addAll(detailPrizeNumbers);
            }

            // 获取并添加详细响应
            DetailRes detailRes = productDetailMapper.findById(productDetailId);
            detailResList.add(detailRes);
        }

        // 批量插入所有奖品编号
        if (!allPrizeNumbers.isEmpty()) {
            prizeNumberMapper.insertBatch(allPrizeNumbers);
        }

        return detailResList;
    }

    // 判断是否应将该项包括在奖品编号中
    private boolean shouldIncludeInPrizeNumbers(DetailReq detailReq) {
        return !"LAST".equals(detailReq.getGrade());
    }

    private boolean shouldIncludeInPrizeNumbers(ProductDetail detailReq) {
        return !"LAST".equals(detailReq.getGrade());
    }




    public DetailRes updateProductDetail(Long id, DetailReq productDetailReq) throws Exception {

        // 1. 先检查是否有已经被抽中的奖品编号（isDrawn 为 true）
        List<PrizeNumber> aTrue = prizeNumberMapper.isTrue(productDetailReq.getProductId());
        boolean hasDrawnPrize = aTrue.stream().anyMatch(x -> x.getIsDrawn());

        // 如果存在已被抽中的奖品，不允许更新库存
        if (hasDrawnPrize) {
            throw new Exception("已有抽過獎，不允许更新庫存");
        }


        // 1. 先更新商品细节的数量
        productDetailReq.setProductDetailId(Math.toIntExact(id));
        Product productById = productRepository.getProductById(Long.valueOf(productDetailReq.getProductId()));

        // 检查商品状态，若已上架则不能更新库存
        if (productById.getStatus().equals(ProductStatus.AVAILABLE)) {
            throw new Exception("此商品已上架，不得更新库存");
        } else {
            productDetailReq.setStockQuantity(productDetailReq.getQuantity());
        }


        // Escape text for HTML in description and specification
        productDetailReq.setDescription(escapeTextForHtml(productDetailReq.getDescription()));
        productDetailReq.setSpecification(escapeTextForHtml(productDetailReq.getSpecification()));

        // Calculate size
        BigDecimal size = productDetailReq.getHeight()
                .multiply(productDetailReq.getWidth())
                .multiply(productDetailReq.getLength());
        productDetailReq.setSize(size.toString());

        // 更新商品细节
        productDetailMapper.update(productDetailReq);

        // 2. 更新产品库存
        List<ProductDetail> productDetailByProductId = productDetailMapper.getProductDetailByProductId(Long.valueOf(productDetailReq.getProductId()));
        int totalQuantity = 0;

        // 计算产品总数量，排除 grade 为 "LAST" 的项
        for (ProductDetail detailReq : productDetailByProductId) {
            if (shouldIncludeInPrizeNumbers(detailReq)) {
                totalQuantity += detailReq.getQuantity();
            }
        }

        // 更新产品总数量
        productRepository.updateTotalQua(totalQuantity, productDetailReq.getProductId());

        // 3. 删除当前产品下所有的奖品编号
        prizeNumberMapper.deleteProductById(Long.valueOf(productDetailReq.getProductId()));

        // 4. 重新生成并排序奖品编号
        List<PrizeNumber> allPrizeNumbers = new ArrayList<>();
        int currentIndex = 0;

        // 创建并打乱奖品编号
        List<Integer> shuffledNumbers = new ArrayList<>();
        for (int i = 1; i <= totalQuantity; i++) {
            shuffledNumbers.add(i);
        }
        Collections.shuffle(shuffledNumbers);

        // 为每个产品细节重新生成奖品编号
        for (ProductDetail detailReq : productDetailByProductId) {
            if (shouldIncludeInPrizeNumbers(detailReq)) {
                List<PrizeNumber> detailPrizeNumbers = new ArrayList<>();
                for (int i = 0; i < detailReq.getQuantity(); i++) {
                    PrizeNumber prizeNumber = new PrizeNumber();
                    prizeNumber.setProductId(detailReq.getProductId());
                    prizeNumber.setProductDetailId(Math.toIntExact(detailReq.getProductDetailId()));
                    prizeNumber.setNumber(String.valueOf(shuffledNumbers.get(currentIndex)));
                    prizeNumber.setIsDrawn(false);
                    prizeNumber.setLevel(detailReq.getGrade());
                    detailPrizeNumbers.add(prizeNumber);
                    currentIndex++;
                }

                // 打乱当前产品细节的奖品编号
                Collections.shuffle(detailPrizeNumbers);
                allPrizeNumbers.addAll(detailPrizeNumbers);
            }
        }

        // 批量插入新的奖品编号
        if (!allPrizeNumbers.isEmpty()) {
            prizeNumberMapper.insertBatch(allPrizeNumbers);
        }

        // 返回更新后的商品详情
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
        // 先保留換行符的處理
        String escapedText = text.replace("\n", "<br/>").replace("\r", "");

        // 再進行其他字符轉義
        return escapedText.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                // 恢復 <br/> 原來的形式，避免被轉義
                .replace("&lt;br/&gt;", "<br/>");
    }

}
