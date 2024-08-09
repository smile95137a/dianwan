package com.one.frontend.service;

import com.one.frontend.dto.DrawRequest;
import com.one.frontend.eenum.OrderStatus;
import com.one.frontend.eenum.PrizeCategory;
import com.one.frontend.model.*;
import com.one.frontend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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


    public DrawResult handleDraw(Integer userId, DrawRequest drawRequests) throws Exception {
        // 先確認商品是否還有貨
        List<ProductDetail> check = productDetailRepository.getProductDetailByProductId(drawRequests.getProductId());
        check.forEach(System.out::println);
        int total = check.stream().mapToInt(ProductDetail::getQuantity).sum();
        System.out.println(total);
        if (total == 0) {
            throw new Exception("所有獎品已被抽完");
        }

        // 計算總金額
        Product product = productRepository.getProductById(Math.toIntExact(drawRequests.getProductId()));
        BigDecimal amount = BigDecimal.valueOf(product.getPrice());

        // 扣除會員內儲值金
        PrizeCategory category = product.getPrizeCategory();
        Integer balanceInt = userRepository.getBalance(userId);
        BigDecimal balance = new BigDecimal(balanceInt);
        if (category == PrizeCategory.BONUS) {
            Integer bonusPointsInt = userRepository.getBonusPoints(userId);
            BigDecimal bonusPoints = new BigDecimal(bonusPointsInt);
            if (bonusPoints.compareTo(amount) >= 0) {
                BigDecimal newBonusPoints = bonusPoints.subtract(amount);
                userRepository.deductUserBonusPoints(userId, newBonusPoints);
            } else {
                throw new Exception("紅利不足，請加值");
            }
        } else {
            if (balance.compareTo(amount) >= 0) {
                System.out.println("123" + " " + amount);
                userRepository.deductUserBalance(userId, amount);
            } else {
                throw new Exception("餘額不足，請加值");
            }
        }

        List<DrawResult> drawResults = new ArrayList<>();

    /*
    開始抽獎
     */
        // step.1 獲得所有產品的數量
        List<ProductDetail> productDetails = productDetailRepository.getProductDetailByProductId(drawRequests.getProductId());
        int totalQuantity = productDetails.stream().mapToInt(ProductDetail::getQuantity).sum();

        // step.2 產品數量透過隨機數字抽獎
        Random random = new Random();
        int cumulativeQuantity = 0;
        int randomNumber = random.nextInt(totalQuantity);
        ProductDetail selectedPrizeDetail = null;
        for (ProductDetail productDetail : productDetails) {
            cumulativeQuantity += productDetail.getQuantity();
            if (randomNumber < cumulativeQuantity) {
                System.out.println(productDetail);
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
        Product product1 = productRepository.getProductById(Math.toIntExact(selectedPrizeDetail.getProductId()));
        product1.setStockQuantity(product1.getStockQuantity() - 1);
        product1.setSoldQuantity(product1.getSoldQuantity() + 1);
        productRepository.updateProductQuantity(product1);
        System.out.println("取得剩餘結果" + product1.getStockQuantity());

        // step.4 紀錄抽獎結果
        DrawResult drawResult = new DrawResult();
        drawResult.setUserId(Long.valueOf(userId));
        drawResult.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
        drawResult.setDrawTime(LocalDateTime.now());
        drawResult.setProductName(drawRequests.getProductName());
        drawResult.setAmount(drawRequests.getAmount());
        drawResult.setTotalDrawCount(drawRequests.getTotalDrawCount());
        drawResult.setRemainingDrawCount(drawRequests.getRemainingDrawCount());
        drawResult.setDrawCount(1);
        drawResult.setCreateDate(LocalDateTime.now());
        drawResults.add(drawResult);

        // 批量插入抽獎结果
        drawRepository.insertBatch(drawResults);

        // step.5 記錄到order
        // 訂單
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
        order.setTotalAmount(amount); // 总金额

        orderRepository.insertOrder(order);
        Long orderId = orderRepository.getOrderIdByOrderNumber(order.getOrderNumber());

        System.out.println(order);

        // 訂單明細
        BigDecimal finalTotalAmount = amount;
        drawResults.forEach(drawResultItem -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(drawRequests.getProductId());
            orderDetail.setProductDetailId(drawResultItem.getProductDetailId());
            orderDetail.setProductDetailName(String.valueOf(productDetailRepository.getProductDetailById(Math.toIntExact(drawResultItem.getProductDetailId())).getProductName()));
            orderDetail.setQuantity(1);
            orderDetail.setUnitPrice(amount);
            orderDetail.setTotalPrice(finalTotalAmount);
            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
            System.out.println(orderDetail);
            orderDetailRepository.insertOrderDetail(orderDetail);
        });

        // 增加兌換紅利的次數
        User user = userRepository.getUserById(userId);
        Long count = user.getDrawCount();
        if (count != 3L) {
            userRepository.addDrawCount();
        } else {
            userRepository.updateBonus(userId);
        }

        System.out.println("抽獎結果");
        drawResults.forEach(System.out::println);

        return drawResults.get(0);
    }


    // 获取所有奖项，随机打乱未被抽中的奖项
    // 获取所有奖品详情，显示哪些奖品已被抽走
    public List<PrizeNumber> getAllPrizes(Long productId) {
        // 获取产品的所有详细信息
        List<ProductDetail> productDetails = productDetailRepository.getAllProductDetailsByProductId(productId);

        // 存储所有的奖品编号
        List<PrizeNumber> allPrizeNumbers = new ArrayList<>();

        // 遍历每个产品详细信息，获取对应的奖品编号
        for (ProductDetail productDetail : productDetails) {
            // 获取所有的奖品编号
            List<PrizeNumber> prizeNumbers = prizeNumberMapper.getAllPrizeNumbersByProductDetailId(Long.valueOf(productDetail.getProductDetailId()));

            // 根据 is_drawn 值调整 grade
            for (PrizeNumber prize : prizeNumbers) {
                if (!prize.getIsDrawn()) {
                    prize.setGrade(null); // 如果未抽中，设置 grade 为 null
                }
            }

            allPrizeNumbers.addAll(prizeNumbers);
        }

        return allPrizeNumbers;
    }

    // 处理抽奖
    // 处理自选抽奖
    public DrawResult handleDraw2(Long userId, Long productId, Integer prizeNumber) throws Exception {
        try {
            // 获取未抽走的奖品编号
            List<PrizeNumber> availablePrizeNumbers = prizeNumberMapper.getAvailablePrizeNumbersByProductDetailId(productId);
            if (availablePrizeNumbers.isEmpty()) {
                throw new Exception("所有奖品已被抽完");
            }
            //
            BigDecimal totalAmount = BigDecimal.ZERO;
            Product product = productRepository.getProductById(Math.toIntExact(productId));
            BigDecimal amount = BigDecimal.valueOf(product.getPrice());


            // 从未抽走的奖品编号中选择一个
            PrizeNumber selectedPrizeNumber = availablePrizeNumbers.stream()
                    .filter(pn -> pn.getNumber().equals(prizeNumber))
                    .findFirst()
                    .orElseThrow(() -> new Exception("该奖品编号已被抽走或不存在"));

            // 标记奖品编号为已抽走
            prizeNumberMapper.markPrizeNumberAsDrawn(selectedPrizeNumber.getPrizeNumberId() , selectedPrizeNumber.getProductId() , selectedPrizeNumber.getProductDetailId());

            // 扣除會員內儲值金
            PrizeCategory category = product.getPrizeCategory();
            Integer balanceInt = userRepository.getBalance(Math.toIntExact(userId));
            BigDecimal balance = new BigDecimal(balanceInt);
            if (category == PrizeCategory.BONUS) {
                Integer bonusPointsInt = userRepository.getBonusPoints(Math.toIntExact(userId));
                BigDecimal bonusPoints = new BigDecimal(bonusPointsInt);
                if (bonusPoints.compareTo(totalAmount) >= 0) {
                    BigDecimal newBonusPoints = bonusPoints.subtract(totalAmount);
                    userRepository.deductUserBonusPoints(Math.toIntExact(userId), newBonusPoints);
                } else {
                    throw new Exception("紅利不足，請加值");
                }
            }else{
                if (balance.compareTo(totalAmount) >= 0) {
                    BigDecimal newBalance = balance.subtract(totalAmount);
                    userRepository.deductUserBalance(Math.toIntExact(userId), newBalance);
                } else {
                    throw new Exception("餘額不足，請加值");
                }
            }


            // 更新奖品数量
            ProductDetail selectedPrizeDetail = productDetailRepository.getProductDetailById(Math.toIntExact(selectedPrizeNumber.getProductDetailId()));
            selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
            productDetailRepository.updateProductDetailQuantity(selectedPrizeDetail);
            List<DrawResult> drawResults = new ArrayList<>();
            // 记录抽奖结果
            DrawResult drawResult = new DrawResult();
            drawResult.setUserId(Long.valueOf(userId));
            drawResult.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
            drawResult.setDrawTime(LocalDateTime.now());
            drawResult.setAmount(amount);
            drawResult.setTotalDrawCount(1);
            drawResult.setDrawCount(1);
            drawResult.setCreateDate(LocalDateTime.now());
            drawResult.setProductName(selectedPrizeDetail.getProductName());
            drawResult.setLevel(selectedPrizeDetail.getGrade());
            drawResults.add(drawResult);
            drawRepository.insertBatch(drawResults);
            // 记录订单和订单明细
            // 生成订单
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setCreatedAt(LocalDateTime.now());
            order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
            order.setTotalAmount(amount); // 总金额

            orderRepository.insertOrder(order);
            Long orderId = orderRepository.getOrderIdByOrderNumber(order.getOrderNumber());

            // 生成订单明细
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
            orderDetail.setProductDetailName(selectedPrizeDetail.getProductName());
            orderDetail.setQuantity(1);
            orderDetail.setUnitPrice(amount);
            orderDetail.setTotalPrice(amount);
            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());

            orderDetailRepository.insertOrderDetail(orderDetail);

            //增加兌換紅利的次數
            User user = userRepository.getUserById(Math.toIntExact(userId));
            Long count = user.getDrawCount();
            if(count != 3L){
                userRepository.addDrawCount();
            }else{
                userRepository.updateBonus(Math.toIntExact(userId));
            }

            return drawResult;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public DrawResult handleDrawRandom(Long userId, Long productId) throws Exception {
        // 获取未抽走的奖品编号
        List<ProductDetail> product = productDetailRepository.getProductDetailByProductId(productId);
        if (product.isEmpty()) {
            throw new Exception("獎品已被抽完");
        }

        Random random = new Random();

// 定義每種獎品的概率
        double grandPrizeProbability = 0.05; // 大獎的概率為5%
        double consolationPrizeProbability = 0.95; // 安慰獎的概率為95%

// 根據隨機概率決定獎品
        ProductDetail selectedPrizeDetail;
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
        Product product2 = productRepository.getProductById(Math.toIntExact(productId));
        BigDecimal amount = BigDecimal.valueOf(product2.getPrice());
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
        drawResult.setTotalDrawCount(1);
        drawResult.setDrawCount(1);
        drawResult.setCreateDate(LocalDateTime.now());
        drawRepository.insertDrawResult(drawResult);

        // Record order and order details
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());

        orderRepository.insertOrder(order);
        Long orderId = orderRepository.getOrderIdByOrderNumber(order.getOrderNumber());

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setProductId(productId);
        orderDetail.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
        orderDetail.setProductDetailName(selectedPrizeDetail.getProductName());
        orderDetail.setQuantity(1);
        orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());

        orderDetailRepository.insertOrderDetail(orderDetail);

        return drawResult;
    }

    // 獲取大獎詳細信息
    public ProductDetail getGrandPrizeDetail(Long productId) {
        return productDetailRepository.findFirstByProductIdAndPrizeType(productId, "GRAND");
    }

    // 獲取安慰獎詳細信息
    public ProductDetail getConsolationPrizeDetail(Long productId) {
        return productDetailRepository.findFirstByProductIdAndPrizeType(productId, "CONSOLATION");
    }
}
