package com.one.frontend.service;

import com.one.frontend.dto.DrawRequest;
import com.one.frontend.dto.OrderDetailDto;
import com.one.frontend.dto.OrderDto;
import com.one.frontend.eenum.OrderStatus;
import com.one.frontend.eenum.PrizeCategory;
import com.one.frontend.model.*;
import com.one.frontend.repository.*;
import com.one.frontend.response.ProductDetailRes;
import com.one.frontend.response.ProductRes;
import com.one.frontend.response.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DrawResultService {

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private PrizeNumberMapper prizeNumberMapper;


    public List<DrawResult> handleDraw(Integer userId, List<DrawRequest> drawRequests) throws Exception {
        // 先確認商品是否還有貨
        DrawRequest drawRequest = drawRequests.get(0); // 假設只有一個 `DrawRequest`
        List<ProductDetailRes> productDetails = productDetailRepository.getProductDetailByProductId(drawRequest.getProductId());

        int totalQuantity = productDetails.stream().mapToInt(ProductDetailRes::getQuantity).sum();
        if (totalQuantity == 0) {
            throw new Exception("所有獎品已被抽完");
        }

        // 計算總金額
        ProductRes product = productRepository.getProductById(Math.toIntExact(drawRequest.getProductId()));
        BigDecimal amount = product.getPrice();

        // 扣除會員內儲值金
        PrizeCategory category = drawRequest.getPrizeCategory();
        BigDecimal balance = new BigDecimal(userRepository.getBalance(userId));

        if (category == PrizeCategory.BONUS) {
            BigDecimal bonusPoints = new BigDecimal(userRepository.getBonusPoints(userId));
            if (bonusPoints.compareTo(amount) >= 0) {
                BigDecimal newBonusPoints = bonusPoints.subtract(amount);
                userRepository.deductUserBonusPoints(userId, newBonusPoints);
            } else {
                throw new Exception("紅利不足，請加值");
            }
        } else {
            if (balance.compareTo(amount) >= 0) {
                userRepository.deductUserBalance(userId, amount);
            } else {
                throw new Exception("餘額不足，請加值");
            }
        }

        // step.2 產品數量透過隨機數字抽獎
        Random random = new Random();
        int cumulativeQuantity = 0;
        int randomNumber = random.nextInt(totalQuantity);
        ProductDetailRes selectedPrizeDetail = null;

        for (ProductDetailRes productDetail : productDetails) {
            cumulativeQuantity += productDetail.getQuantity();
            if (randomNumber < cumulativeQuantity) {
                selectedPrizeDetail = productDetail;
                break;
            }
        }

        if (selectedPrizeDetail == null) {
            throw new Exception("未抽獎，抽獎次數為0");
        }

        // step.3 更新抽獎數量
        selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
        productDetailRepository.updateProductDetailQuantity(selectedPrizeDetail);

        ProductRes updatedProduct = productRepository.getProductById(Math.toIntExact(selectedPrizeDetail.getProductId()));
        updatedProduct.setStockQuantity(updatedProduct.getStockQuantity() - 1);
        productRepository.updateProductQuantity(updatedProduct);

        // step.4 紀錄抽獎結果
        DrawResult drawResult = new DrawResult();
        drawResult.setUserId(Long.valueOf(userId));
        drawResult.setProductId(drawRequest.getProductId());
        drawResult.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
        drawResult.setDrawTime(LocalDateTime.now());
        drawResult.setAmount(amount);
        drawResult.setDrawCount(1);
        drawResult.setRemainingDrawCount(drawRequest.getRemainingDrawCount());
        drawResult.setCreateDate(LocalDateTime.now());

        List<DrawResult> drawResults = Collections.singletonList(drawResult);
        drawRepository.insertBatch(drawResults);

        // step.5 記錄到訂單
        OrderDto order = new OrderDto();
        order.setUserId(Math.toIntExact(Long.valueOf(userId)));
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(amount);

        orderRepository.insertOrder(order);
        Long orderId = orderRepository.getOrderIdByOrderNumber(order.getOrderNumber());

        OrderDetailDto orderDetail = new OrderDetailDto();
        orderDetail.setOrderId(Math.toIntExact(orderId));
        orderDetail.setProductId(Math.toIntExact(drawRequest.getProductId()));
        orderDetail.setProductDetailName(selectedPrizeDetail.getProductName());
        orderDetail.setQuantity(1);
        orderDetail.setUnitPrice(amount);

        orderDetailRepository.insertOrderDetail(orderDetail);
        UserRes user = userRepository.getUserById(userId);
        Long drawCount = user.getDrawCount();
        if (drawCount < 3L) {
            userRepository.addDrawCount(Long.valueOf(userId));
        } else {
            userRepository.updateBonus(Long.valueOf(userId));
        }

        return drawResults;
    }


    public List<PrizeNumber> getAllPrizes(Long productId) {
        List<ProductDetail> productDetails = productDetailRepository.getAllProductDetailsByProductId(productId);

        List<PrizeNumber> allPrizeNumbers = new ArrayList<>();

        for (ProductDetail productDetail : productDetails) {
            List<PrizeNumber> prizeNumbers = prizeNumberMapper.getAllPrizeNumbersByProductDetailId(Long.valueOf(productDetail.getProductDetailId()));

            for (PrizeNumber prize : prizeNumbers) {
                if (!prize.getIsDrawn()) {
                    prize.setLevel(null); // 如果未抽中，设置 grade 为 null
                }
            }

            allPrizeNumbers.addAll(prizeNumbers);
        }

        return allPrizeNumbers;
    }

    public DrawResult handleDraw2(Long userId, Long productId, List<Integer> prizeNumbers) throws Exception {
        try {
            // 確認商品是否存在
            List<PrizeNumber> availablePrizeNumbers = prizeNumberMapper.getAvailablePrizeNumbersByProductDetailId(productId);
            if (availablePrizeNumbers.isEmpty()) {
                throw new Exception("所有獎品已被抽完");
            }

            // 計算抽獎總金額
            BigDecimal totalAmount = BigDecimal.ZERO;
            ProductRes product = productRepository.getProductById(Math.toIntExact(productId));
            BigDecimal amount = product.getPrice();

            // 查找指定的奖品编号
            PrizeNumber selectedPrizeNumber = availablePrizeNumbers.stream()
                    .filter(pn -> prizeNumbers.contains(pn.getNumber()))
                    .findFirst()
                    .orElseThrow(() -> new Exception("該獎品已被抽走"));

            // 将奖品编号标记为已抽中
            prizeNumberMapper.markPrizeNumberAsDrawn(selectedPrizeNumber.getPrizeNumberId(),
                    selectedPrizeNumber.getProductId(), selectedPrizeNumber.getProductDetailId());

            // 扣除会员内储值金或红利
            PrizeCategory category = product.getPrizeCategory();
            BigDecimal balance = new BigDecimal(userRepository.getBalance(Math.toIntExact(userId)));
            if (category == PrizeCategory.BONUS) {
                BigDecimal bonusPoints = new BigDecimal(userRepository.getBonusPoints(Math.toIntExact(userId)));
                if (bonusPoints.compareTo(amount) >= 0) {
                    BigDecimal newBonusPoints = bonusPoints.subtract(amount);
                    userRepository.deductUserBonusPoints(Math.toIntExact(userId), newBonusPoints);
                } else {
                    throw new Exception("紅利不足，請加值");
                }
            } else {
                if (balance.compareTo(amount) >= 0) {
                    BigDecimal newBalance = balance.subtract(amount);
                    userRepository.deductUserBalance(Math.toIntExact(userId), newBalance);
                } else {
                    throw new Exception("餘額不足，請加值");
                }
            }

            // 更新奖品数量
            ProductDetailRes selectedPrizeDetail = productDetailRepository.getProductDetailById(Math.toIntExact(selectedPrizeNumber.getProductDetailId()));
            selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
            productDetailRepository.updateProductDetailQuantity(selectedPrizeDetail);

            // 记录抽奖结果
            DrawResult drawResult = new DrawResult();
            drawResult.setUserId(userId);
            drawResult.setProductId(productId);
            drawResult.setProductDetailId(selectedPrizeDetail.getProductDetailId().longValue());
            drawResult.setDrawTime(LocalDateTime.now());
            drawResult.setAmount(amount);
            drawResult.setDrawCount(1);
            drawResult.setRemainingDrawCount(0);  // 根据需求调整
            drawResult.setPrizeNumber(selectedPrizeNumber.getNumber());
            drawResult.setStatus("ACTIVE");
            drawResult.setCreateDate(LocalDateTime.now());
            drawResult.setUpdateDate(LocalDateTime.now());
            drawRepository.insertDrawResult(drawResult);

            // 生成订单
            OrderDto order = new OrderDto();
            order.setUserId(Math.toIntExact(userId));
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setCreatedAt(LocalDateTime.now());
            order.setResultStatus(OrderStatus.PREPARING_SHIPMENT);
            order.setTotalAmount(amount);
            orderRepository.insertOrder(order);

            // 获取订单ID
            Long orderId = orderRepository.getOrderIdByOrderNumber(order.getOrderNumber());

            // 生成订单明细
            OrderDetailDto orderDetail = new OrderDetailDto();
            orderDetail.setOrderId(Math.toIntExact(orderId));
            orderDetail.setProductId(productId.intValue());
            orderDetail.setProductDetailId(selectedPrizeDetail.getProductDetailId());
            orderDetail.setProductDetailName(selectedPrizeDetail.getProductName());
            orderDetail.setQuantity(1);
            orderDetail.setUnitPrice(amount);
            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT);
            orderDetailRepository.insertOrderDetail(orderDetail);

// 处理用户抽奖次数和红利
            UserRes user = userRepository.getUserById(userId.intValue());
            Long drawCount = user.getDrawCount();
            if (drawCount < 3L) {
                userRepository.addDrawCount(userId);
            } else {
                userRepository.updateBonus(userId);
            }

            return drawResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("抽獎過程中出現錯誤: " + e.getMessage());
        }
    }

    public DrawResult handleDrawRandom(Long userId, Long productId) throws Exception {
        // 获取未抽走的奖品编号
        List<ProductDetailRes> product = productDetailRepository.getProductDetailByProductId(productId);
        if (product.isEmpty()) {
            throw new Exception("獎品已被抽完");
        }

        Random random = new Random();

// 定義每種獎品的概率
        double grandPrizeProbability = 0.05; // 大獎的概率為5%
        double consolationPrizeProbability = 0.95; // 安慰獎的概率為95%

// 根據隨機概率決定獎品
        ProductDetailRes selectedPrizeDetail;
        if (random.nextDouble() < grandPrizeProbability) {
            // 使用戶贏得大獎
            selectedPrizeDetail = getGrandPrizeDetail(productId);
        } else {
            // 使用戶贏得安慰獎
            selectedPrizeDetail = getConsolationPrizeDetail(productId);
        }

// 檢查選中的獎品是否仍可用
        if (selectedPrizeDetail.getQuantity() <= 0) {
            throw new Exception("選中的獎品已經被抽完");
        }

        // 扣除會員內儲值金
        BigDecimal totalAmount = BigDecimal.ZERO;
        ProductRes product2 = productRepository.getProductById(Math.toIntExact(productId));
        BigDecimal amount = product2.getPrice();
        PrizeCategory category = product2.getPrizeCategory();
        Integer balanceInt = userRepository.getBalance(Math.toIntExact(userId));
        BigDecimal balance = new BigDecimal(balanceInt);
        if (category == PrizeCategory.BONUS) {
            Integer bonusPointsInt = userRepository.getBonusPoints(Math.toIntExact(userId));
            BigDecimal bonusPoints = new BigDecimal(bonusPointsInt);
            if (bonusPoints.compareTo(totalAmount) >= 0) {
                BigDecimal newBonusPoints = bonusPoints.subtract(totalAmount);
                userRepository.deductUserBonusPoints(Math.toIntExact(userId), newBonusPoints);
            } else {
                throw new Exception("紅利不足");
            }
        }else{
            if (balance.compareTo(totalAmount) >= 0) {
                BigDecimal newBalance = balance.subtract(totalAmount);
                userRepository.deductUserBalance(Math.toIntExact(userId), newBalance);
            } else {
                throw new Exception("餘額不足，請加值");
            }
        }


// 更新獎品數量
        selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
        productDetailRepository.updateProductDetailQuantity(selectedPrizeDetail);
        // Record draw result
        DrawResult drawResult = new DrawResult();
        drawResult.setUserId(userId);
        drawResult.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
        drawResult.setDrawTime(LocalDateTime.now());
        drawResult.setDrawCount(1);
        drawResult.setCreateDate(LocalDateTime.now());
        drawRepository.insertDrawResult(drawResult);

        // Record order and order details
        // 生成订单
        OrderDto order = new OrderDto();
        order.setUserId(Math.toIntExact(userId));
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        order.setResultStatus(OrderStatus.PREPARING_SHIPMENT);
        order.setTotalAmount(amount);
        orderRepository.insertOrder(order);

        // 获取订单ID
        Long orderId = orderRepository.getOrderIdByOrderNumber(order.getOrderNumber());

        // 生成订单明细
        OrderDetailDto orderDetail = new OrderDetailDto();
        orderDetail.setOrderId(Math.toIntExact(orderId));
        orderDetail.setProductId(productId.intValue());
        orderDetail.setProductDetailId(selectedPrizeDetail.getProductDetailId());
        orderDetail.setProductDetailName(selectedPrizeDetail.getProductName());
        orderDetail.setQuantity(1);
        orderDetail.setUnitPrice(amount);
        orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT);

        orderDetailRepository.insertOrderDetail(orderDetail);

        return drawResult;
    }

    // 獲取大獎詳細信息
    public ProductDetailRes getGrandPrizeDetail(Long productId) {
        return productDetailRepository.findFirstByProductIdAndPrizeType(productId, "GRAND");
    }

    // 獲取安慰獎詳細信息
    public ProductDetailRes getConsolationPrizeDetail(Long productId) {
        return productDetailRepository.findFirstByProductIdAndPrizeType(productId, "CONSOLATION");
    }
}
