package com.one.frontend.service;

import com.one.eenum.OrderStatus;
import com.one.frontend.dto.DrawRequest;
import com.one.model.*;
import com.one.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
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

    public void handleDraw(Integer userId, List<DrawRequest> drawRequests) throws Exception {
        // 計算總金額
        BigDecimal totalAmount = drawRequests.stream()
                .map(DrawRequest::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("抽獎金額");
        System.out.println(totalAmount);
        // 扣除會員內儲值金
        Integer balanceInt = userRepository.getBalance(userId);
        BigDecimal balance = new BigDecimal(balanceInt);
        if(balance.compareTo(totalAmount) >= 0){
            BigDecimal newBalance = balance.subtract(totalAmount);
            userRepository.deductUserBalance(userId , newBalance);
        }else{
            throw new Exception("餘額不足，請加值");
        }
        /*
        開始抽獎
         */
        // step.1 獲得所有產品的數量
        List<PrizeDetail> prizeDetails = prizeDetailRepository.getAllPrizeDetails();
        int totalQuantity = prizeDetails.stream().mapToInt(PrizeDetail::getQuantity).sum();
        if (totalQuantity == 0) {
            throw new Exception("所有獎品已被抽完");
        }
        // step.2 產品數量透過隨機數字抽獎
        Random random = new Random();
        int cumulativeQuantity = 0;
        int randomNumber = random.nextInt(totalQuantity);
        PrizeDetail selectedPrizeDetail = null;
        for (PrizeDetail prizeDetail : prizeDetails) {
            cumulativeQuantity += prizeDetail.getQuantity();
            if (randomNumber < cumulativeQuantity) {
                selectedPrizeDetail = prizeDetail;
                break;
            }
        }
        if (selectedPrizeDetail == null) {
            throw new Exception("抽獎失敗");
        }
        // step.3 更新抽獎數量
        selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
        prizeDetailRepository.updatePrizeDetailQuantity(selectedPrizeDetail);
        Prize prize = prizeRepository.getPrizeById(Math.toIntExact(selectedPrizeDetail.getPrizeId()));
        prize.setRemainingQuantity(prize.getRemainingQuantity() - 1);
        prizeRepository.updatePrizeRemainingQuantity(prize);

        // step.4 紀錄抽獎結果
        List<DrawResult> drawResults = drawRequests.stream().map(request -> {
            DrawResult drawResult = new DrawResult();
            drawResult.setUserId(Long.valueOf(userId));
            drawResult.setBlindBoxId(request.getBlindBoxId());
            drawResult.setPrizeDetailId(request.getPrizeDetailId());
            drawResult.setGachaId(request.getGachaId());
            drawResult.setDrawTime(LocalDateTime.now());
            drawResult.setAmount(request.getAmount());
            drawResult.setTotalDrawCount(request.getTotalDrawCount());
            drawResult.setRemainingDrawCount(request.getRemainingDrawCount());
            drawResult.setCreateDate(LocalDateTime.now());
            return drawResult;
        }).toList();
        // 批量插入抽奖结果
        drawRepository.insertBatch(drawResults);

        // step.5 記錄到order
        Order order = new Order();
        order.getUser().setId(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING_SHIPMENT.getDescription()); // 状态设置为 "PREPARING_SHIPMENT"
        order.setTotalAmount(drawResults.stream()
                .map(DrawResult::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)); // 總金額
        // 訂單
        orderRepository.insertOrder(order);
        // 订单詳情
        drawResults.forEach(drawResult -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setPrizeId(drawResult.getPrizeId()); // 假设 prizeId 和 prizeDetailId 是相同的
            orderDetail.setPrizeDetailId(drawResult.getPrizeDetailId());
            orderDetail.setQuantity(1); // 每个抽奖结果对应一个奖品
            orderDetail.setUnitPrice(drawResult.getAmount());
            orderDetail.setTotalPrice(drawResult.getAmount());
            orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT.getDescription()); // 初始状态为待处理
            orderDetailRepository.insertOrderDetail(orderDetail);
        });


        System.out.println("抽獎結果");
        drawResults.forEach(System.out::println);

    }

    public static void main(String[] args) throws Exception {
        DrawResultService service = new DrawResultService();
        List<DrawRequest> drawRequests = new ArrayList<>();

        // 假设总抽奖次数为6次，每次抽奖次数为1次，总共可以抽6次
        int totalDrawCount = 6;

        DrawRequest drawRequest1 = new DrawRequest();
        drawRequest1.setPrizeDetailId(1L);
        drawRequest1.setAmount(new BigDecimal("10.00"));
        drawRequest1.setTotalDrawCount(totalDrawCount); // 设置总抽奖次数
        drawRequest1.setRemainingDrawCount(totalDrawCount - 1); // 设置剩余抽奖次数
        drawRequests.add(drawRequest1);

        DrawRequest drawRequest2 = new DrawRequest();
        drawRequest2.setPrizeDetailId(2L);
        drawRequest2.setAmount(new BigDecimal("20.00"));
        drawRequest2.setTotalDrawCount(totalDrawCount); // 设置总抽奖次数
        drawRequest2.setRemainingDrawCount(totalDrawCount - 2); // 设置剩余抽奖次数
        drawRequests.add(drawRequest2);

        DrawRequest drawRequest3 = new DrawRequest();
        drawRequest3.setPrizeDetailId(3L);
        drawRequest3.setAmount(new BigDecimal("30.00"));
        drawRequest3.setTotalDrawCount(totalDrawCount); // 设置总抽奖次数
        drawRequest3.setRemainingDrawCount(totalDrawCount - 3); // 设置剩余抽奖次数
        drawRequests.add(drawRequest3);

        service.handleDraw(1 , drawRequests);
    }
}
