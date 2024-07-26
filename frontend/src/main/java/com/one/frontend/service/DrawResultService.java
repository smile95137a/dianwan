package com.one.frontend.service;

import com.one.eenum.OrderStatus;
import com.one.frontend.dto.DrawRequest;
import com.one.model.*;
import com.one.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DrawResultService {

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrizeDetailRepository prizeDetailRepository;

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private GachaRepository gachaRepository;

    @Autowired
    private BlindBoxRepository blindBoxRepository;

    public void handleDraw(Integer userId, List<DrawRequest> drawRequests , Long prizeId) throws Exception {
        // 先確認商品是否還有貨
        List<PrizeDetail> check = prizeDetailRepository.getAllPrizeDetails();
        check.forEach(System.out::println);
        int total = check.stream().mapToInt(PrizeDetail::getQuantity).sum();
        System.out.println(total);
        if (total == 0) {
            throw new Exception("所有獎品已被抽完");
        }

        // 計算總金額
        BigDecimal totalAmount = BigDecimal.ZERO;
        Prize prizz = prizeRepository.getPrizeById(Math.toIntExact(prizeId));
        BigDecimal amount = prizz.getPrice();
        System.out.println("amout" + amount);
        for (DrawRequest request : drawRequests) {
            totalAmount = totalAmount.add(amount);
        }
        System.out.println("抽獎金額");
        System.out.println(totalAmount);
        // 扣除會員內儲值金
        Integer balanceInt = userRepository.getBalance(userId);
        BigDecimal balance = new BigDecimal(balanceInt);
        if (balance.compareTo(totalAmount) >= 0) {
            BigDecimal newBalance = balance.subtract(totalAmount);
            userRepository.deductUserBalance(userId, newBalance);
        } else {
            throw new Exception("餘額不足，請加值");
        }

        List<DrawResult> drawResults = new ArrayList<>();
        for (DrawRequest request : drawRequests) {
        /*
        開始抽獎
         */
            // step.1 獲得所有產品的數量
            List<PrizeDetail> prizeDetails = prizeDetailRepository.getAllPrizeDetails();
            int totalQuantity = prizeDetails.stream().mapToInt(PrizeDetail::getQuantity).sum();

            // step.2 產品數量透過隨機數字抽獎
            Random random = new Random();
            int cumulativeQuantity = 0;
            int randomNumber = random.nextInt(totalQuantity);
            PrizeDetail selectedPrizeDetail = null;
            for (PrizeDetail prizeDetail : prizeDetails) {
                cumulativeQuantity += prizeDetail.getQuantity();
                if (randomNumber < cumulativeQuantity) {
                    System.out.println(prizeDetail);
                    selectedPrizeDetail = prizeDetail;
                    break;
                }
            }
            if (selectedPrizeDetail == null) {
                throw new Exception("未抽獎，抽獎次數為0");
            }
            // step.3 更新抽獎數量
            selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
            prizeDetailRepository.updatePrizeDetailQuantity(selectedPrizeDetail);
            Prize prize = prizeRepository.getPrizeById(Math.toIntExact(selectedPrizeDetail.getPrizeId()));
            prize.setRemainingQuantity(prize.getRemainingQuantity() - 1);
            prizeRepository.updatePrizeRemainingQuantity(prize);
            System.out.println("取得剩餘結果" + prize.getRemainingQuantity());
            // step.4 紀錄抽獎結果
            DrawResult drawResult = new DrawResult();
            drawResult.setUserId(Long.valueOf(userId));
            drawResult.setBlindBoxId(request.getBlindBoxId());
            drawResult.setPrizeDetailId(Long.valueOf(selectedPrizeDetail.getPrizeDetailId()));
            drawResult.setGachaId(request.getGachaId());
            drawResult.setDrawTime(LocalDateTime.now());
            drawResult.setAmount(request.getAmount());
            drawResult.setTotalDrawCount(request.getTotalDrawCount());
            drawResult.setRemainingDrawCount(request.getRemainingDrawCount());
            drawResult.setCreateDate(LocalDateTime.now());
            drawResults.add(drawResult);
        }
        // 批量插入抽獎结果
        drawRepository.insertBatch(drawResults);

        // step.5 記錄到order
        // 訂單
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
        order.setTotalAmount(totalAmount); // 總金額
        System.out.println(order);

        orderRepository.insertOrder(order);
        //訂單明細
        BigDecimal finalTotalAmount = totalAmount;
        drawResults.forEach(drawResult -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setPrizeId(drawResult.getPrizeId());
            orderDetail.setPrizeDetailId(drawResult.getPrizeDetailId());
            orderDetail.setQuantity(1);
            orderDetail.setUnitPrice(finalTotalAmount);
            orderDetail.setTotalPrice(finalTotalAmount);
            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
            System.out.println(orderDetail);
            orderDetailRepository.insertOrderDetail(orderDetail);
        });



        System.out.println("抽獎結果");
        drawResults.forEach(System.out::println);
    }

//    public void handleDrawGacha(Integer userId, List<DrawRequest> drawRequests, Long gachaId) {
//        // 先確認商品是否還有貨
//        List<Gacha> check = gachaRepository.getGachaById(gachaId);
//        System.out.println(check);
//        int total = check.stream().mapToInt(PrizeDetail::getQuantity).sum();
//        System.out.println(total);
//        if (total == 0) {
//            throw new Exception("所有獎品已被抽完");
//        }
//
//        // 計算總金額
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        Prize prizz = prizeRepository.getPrizeById(Math.toIntExact(prizeId));
//        BigDecimal amount = prizz.getPrice();
//        System.out.println("amout" + amount);
//        for (DrawRequest request : drawRequests) {
//            totalAmount = totalAmount.add(amount);
//        }
//        System.out.println("抽獎金額");
//        System.out.println(totalAmount);
//        // 扣除會員內儲值金
//        Integer balanceInt = userRepository.getBalance(userId);
//        BigDecimal balance = new BigDecimal(balanceInt);
//        if (balance.compareTo(totalAmount) >= 0) {
//            BigDecimal newBalance = balance.subtract(totalAmount);
//            userRepository.deductUserBalance(userId, newBalance);
//        } else {
//            throw new Exception("餘額不足，請加值");
//        }
//
//        List<DrawResult> drawResults = new ArrayList<>();
//        for (DrawRequest request : drawRequests) {
//        /*
//        開始抽獎
//         */
//            // step.1 獲得所有產品的數量
//            List<PrizeDetail> prizeDetails = prizeDetailRepository.getAllPrizeDetails();
//            int totalQuantity = prizeDetails.stream().mapToInt(PrizeDetail::getQuantity).sum();
//
//            // step.2 產品數量透過隨機數字抽獎
//            Random random = new Random();
//            int cumulativeQuantity = 0;
//            int randomNumber = random.nextInt(totalQuantity);
//            PrizeDetail selectedPrizeDetail = null;
//            for (PrizeDetail prizeDetail : prizeDetails) {
//                cumulativeQuantity += prizeDetail.getQuantity();
//                if (randomNumber < cumulativeQuantity) {
//                    System.out.println(prizeDetail);
//                    selectedPrizeDetail = prizeDetail;
//                    break;
//                }
//            }
//            if (selectedPrizeDetail == null) {
//                throw new Exception("未抽獎，抽獎次數為0");
//            }
//            // step.3 更新抽獎數量
//            selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
//            prizeDetailRepository.updatePrizeDetailQuantity(selectedPrizeDetail);
//            Prize prize = prizeRepository.getPrizeById(Math.toIntExact(selectedPrizeDetail.getPrizeId()));
//            prize.setRemainingQuantity(prize.getRemainingQuantity() - 1);
//            prizeRepository.updatePrizeRemainingQuantity(prize);
//            System.out.println("取得剩餘結果" + prize.getRemainingQuantity());
//            // step.4 紀錄抽獎結果
//            DrawResult drawResult = new DrawResult();
//            drawResult.setUserId(Long.valueOf(userId));
//            drawResult.setBlindBoxId(request.getBlindBoxId());
//            drawResult.setPrizeDetailId(Long.valueOf(selectedPrizeDetail.getPrizeDetailId()));
//            drawResult.setGachaId(request.getGachaId());
//            drawResult.setDrawTime(LocalDateTime.now());
//            drawResult.setAmount(request.getAmount());
//            drawResult.setTotalDrawCount(request.getTotalDrawCount());
//            drawResult.setRemainingDrawCount(request.getRemainingDrawCount());
//            drawResult.setCreateDate(LocalDateTime.now());
//            drawResults.add(drawResult);
//        }
//        // 批量插入抽獎结果
//        drawRepository.insertBatch(drawResults);
//
//        // step.5 記錄到order
//        // 訂單
//        Order order = new Order();
//        order.setUserId(Long.valueOf(userId));
//        order.setCreatedAt(LocalDateTime.now());
//        order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
//        order.setTotalAmount(totalAmount); // 總金額
//        System.out.println(order);
//
//        orderRepository.insertOrder(order);
//        //訂單明細
//        BigDecimal finalTotalAmount = totalAmount;
//        drawResults.forEach(drawResult -> {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrder(order);
//            orderDetail.setPrizeId(drawResult.getPrizeId());
//            orderDetail.setPrizeDetailId(drawResult.getPrizeDetailId());
//            orderDetail.setQuantity(1);
//            orderDetail.setUnitPrice(finalTotalAmount);
//            orderDetail.setTotalPrice(finalTotalAmount);
//            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
//            System.out.println(orderDetail);
//            orderDetailRepository.insertOrderDetail(orderDetail);
//        });
//
//
//
//        System.out.println("抽獎結果");
//        drawResults.forEach(System.out::println);
//    }
//
//    public void handleDrawBlindBox(Integer userId, List<DrawRequest> drawRequests, Long blindBoxId) {
//        // 先確認商品是否還有貨
//        List<PrizeDetail> check = prizeDetailRepository.getAllPrizeDetails();
//        check.forEach(System.out::println);
//        int total = check.stream().mapToInt(PrizeDetail::getQuantity).sum();
//        System.out.println(total);
//        if (total == 0) {
//            throw new Exception("所有獎品已被抽完");
//        }
//
//        // 計算總金額
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        Prize prizz = prizeRepository.getPrizeById(Math.toIntExact(prizeId));
//        BigDecimal amount = prizz.getPrice();
//        System.out.println("amout" + amount);
//        for (DrawRequest request : drawRequests) {
//            totalAmount = totalAmount.add(amount);
//        }
//        System.out.println("抽獎金額");
//        System.out.println(totalAmount);
//        // 扣除會員內儲值金
//        Integer balanceInt = userRepository.getBalance(userId);
//        BigDecimal balance = new BigDecimal(balanceInt);
//        if (balance.compareTo(totalAmount) >= 0) {
//            BigDecimal newBalance = balance.subtract(totalAmount);
//            userRepository.deductUserBalance(userId, newBalance);
//        } else {
//            throw new Exception("餘額不足，請加值");
//        }
//
//        List<DrawResult> drawResults = new ArrayList<>();
//        for (DrawRequest request : drawRequests) {
//        /*
//        開始抽獎
//         */
//            // step.1 獲得所有產品的數量
//            List<PrizeDetail> prizeDetails = prizeDetailRepository.getAllPrizeDetails();
//            int totalQuantity = prizeDetails.stream().mapToInt(PrizeDetail::getQuantity).sum();
//
//            // step.2 產品數量透過隨機數字抽獎
//            Random random = new Random();
//            int cumulativeQuantity = 0;
//            int randomNumber = random.nextInt(totalQuantity);
//            PrizeDetail selectedPrizeDetail = null;
//            for (PrizeDetail prizeDetail : prizeDetails) {
//                cumulativeQuantity += prizeDetail.getQuantity();
//                if (randomNumber < cumulativeQuantity) {
//                    System.out.println(prizeDetail);
//                    selectedPrizeDetail = prizeDetail;
//                    break;
//                }
//            }
//            if (selectedPrizeDetail == null) {
//                throw new Exception("未抽獎，抽獎次數為0");
//            }
//            // step.3 更新抽獎數量
//            selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
//            prizeDetailRepository.updatePrizeDetailQuantity(selectedPrizeDetail);
//            Prize prize = prizeRepository.getPrizeById(Math.toIntExact(selectedPrizeDetail.getPrizeId()));
//            prize.setRemainingQuantity(prize.getRemainingQuantity() - 1);
//            prizeRepository.updatePrizeRemainingQuantity(prize);
//            System.out.println("取得剩餘結果" + prize.getRemainingQuantity());
//            // step.4 紀錄抽獎結果
//            DrawResult drawResult = new DrawResult();
//            drawResult.setUserId(Long.valueOf(userId));
//            drawResult.setBlindBoxId(request.getBlindBoxId());
//            drawResult.setPrizeDetailId(Long.valueOf(selectedPrizeDetail.getPrizeDetailId()));
//            drawResult.setGachaId(request.getGachaId());
//            drawResult.setDrawTime(LocalDateTime.now());
//            drawResult.setAmount(request.getAmount());
//            drawResult.setTotalDrawCount(request.getTotalDrawCount());
//            drawResult.setRemainingDrawCount(request.getRemainingDrawCount());
//            drawResult.setCreateDate(LocalDateTime.now());
//            drawResults.add(drawResult);
//        }
//        // 批量插入抽獎结果
//        drawRepository.insertBatch(drawResults);
//
//        // step.5 記錄到order
//        // 訂單
//        Order order = new Order();
//        order.setUserId(Long.valueOf(userId));
//        order.setCreatedAt(LocalDateTime.now());
//        order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
//        order.setTotalAmount(totalAmount); // 總金額
//        System.out.println(order);
//
//        orderRepository.insertOrder(order);
//        //訂單明細
//        BigDecimal finalTotalAmount = totalAmount;
//        drawResults.forEach(drawResult -> {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrder(order);
//            orderDetail.setPrizeId(drawResult.getPrizeId());
//            orderDetail.setPrizeDetailId(drawResult.getPrizeDetailId());
//            orderDetail.setQuantity(1);
//            orderDetail.setUnitPrice(finalTotalAmount);
//            orderDetail.setTotalPrice(finalTotalAmount);
//            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription());
//            System.out.println(orderDetail);
//            orderDetailRepository.insertOrderDetail(orderDetail);
//        });
//
//
//
//        System.out.println("抽獎結果");
//        drawResults.forEach(System.out::println);
//    }


    public static void main(String[] args) throws Exception {
        DrawResultService service = new DrawResultService();
        List<DrawRequest> drawRequests = new ArrayList<>();


        int totalDrawCount = 6;

        DrawRequest drawRequest1 = new DrawRequest();
        drawRequest1.setPrizeDetailId(1L);
        drawRequest1.setAmount(new BigDecimal("10.00"));
        drawRequest1.setTotalDrawCount(totalDrawCount);
        drawRequest1.setRemainingDrawCount(totalDrawCount - 1);
        drawRequests.add(drawRequest1);

        DrawRequest drawRequest2 = new DrawRequest();
        drawRequest2.setPrizeDetailId(2L);
        drawRequest2.setAmount(new BigDecimal("20.00"));
        drawRequest2.setTotalDrawCount(totalDrawCount);
        drawRequest2.setRemainingDrawCount(totalDrawCount - 2);
        drawRequests.add(drawRequest2);

        DrawRequest drawRequest3 = new DrawRequest();
        drawRequest3.setPrizeDetailId(3L);
        drawRequest3.setAmount(new BigDecimal("30.00"));
        drawRequest3.setTotalDrawCount(totalDrawCount);
        drawRequest3.setRemainingDrawCount(totalDrawCount - 3);
        drawRequests.add(drawRequest3);

        service.handleDraw(1 , drawRequests , 1L);
    }
}
