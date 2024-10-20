package com.one.frontend.service;

import com.google.gson.Gson;
import com.one.frontend.model.*;
import com.one.frontend.repository.*;
import com.one.frontend.response.PaymentResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderTempMapper orderTempMapper;

    @Autowired
    private OrderDetailTempMapper orderDetailTempMapper;

    @Autowired
    private UserRewardRepository userRewardRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String CUSTOMERID = "B82FD0DF7DE03FC702DEC35A2446E469";
    private final String STRCHECK = "d0q2mo1729enisehzolmhdwhkac38itb";

    public PaymentResponse creditCard(PaymentRequest paymentRequest) {

        String url = "https://n.gomypay.asia/ShuntClass.aspx";  //正式
//        String url = "https://n.gomypay.asia/TestShuntClass.aspx";  //測試

        PaymentRequest req = PaymentRequest.builder()
                .sendType("0")            // 傳送型態
                .payModeNo("2")           // 付款模式
                .customerId(CUSTOMERID) // 商店代號
                .orderNo(paymentRequest.getOrderNo())    // 交易單號
                .amount(paymentRequest.getAmount())           // 交易金額
                .transCode("00")          // 交易類別
                .buyerName(paymentRequest.getBuyerName())    // 消費者姓名
                .buyerTelm(paymentRequest.getBuyerTelm())  // 消費者手機
                .buyerMail(paymentRequest.getBuyerMail()) // 消費者Email
                .buyerMemo(paymentRequest.getBuyerMemo()) // 消費備註
                .cardNo(paymentRequest.getCardNo())  // 信用卡號
                .expireDate(paymentRequest.getExpireDate())       // 卡片有效日期
                .cvv(paymentRequest.getCvv())               // 卡片認證碼
                .transMode("1")           // 交易模式
                .installment("0")         // 期數
                .returnUrl(paymentRequest.getReturnUrl()) // 授權結果回傳網址
                .callbackUrl(paymentRequest.getCallbackUrl()) // 背景對帳網址
                .eReturn("1")             // 是否使用Json回傳
                .strCheck(STRCHECK) // 交易驗證密碼
                .build();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 构建请求体
            StringBuilder requestBody = new StringBuilder();
            requestBody.append("Send_Type=").append(req.getSendType())
                    .append("&Pay_Mode_No=").append(req.getPayModeNo())
                    .append("&CustomerId=").append(req.getCustomerId())
                    .append("&Order_No=")
                    .append("&Amount=").append(req.getAmount())
                    .append("&TransCode=").append(req.getTransCode())
                    .append("&Buyer_Name=").append(req.getBuyerName())
                    .append("&Buyer_Telm=").append(req.getBuyerTelm())
                    .append("&Buyer_Mail=").append(req.getBuyerMail())
                    .append("&Buyer_Memo=").append(req.getBuyerMemo())
                    .append("&CardNo=").append(req.getCardNo())
                    .append("&ExpireDate=").append(req.getExpireDate())
                    .append("&CVV=").append(req.getCvv())
                    .append("&TransMode=").append(req.getTransMode())
                    .append("&Installment=").append(req.getInstallment())
                    .append("&Return_url=")
                    .append("&Callback_Url=")
                    .append("&e_return=").append(req.getEReturn())
                    .append("&Str_Check=").append(req.getStrCheck());

            post.setEntity(new StringEntity(requestBody.toString()));

            System.out.println(requestBody);
            // 发送请求并接收响应
            HttpResponse response = httpClient.execute(post);
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

            // 处理响应体
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Response JSON: " + jsonResponse);

            Gson gson = new Gson();
            PaymentResponse paymentResponse = gson.fromJson(jsonResponse, PaymentResponse.class);
            System.out.println(paymentResponse);
            return paymentResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
return null;
    }


    public PaymentResponse webATM(PaymentRequest paymentRequest) {
//        String url = "https://n.gomypay.asia/ShuntClass.aspx";  //正式
        String url = "https://n.gomypay.asia/TestShuntClass.aspx";  //測試

        PaymentRequest req = PaymentRequest.builder()
                .sendType("4")            // 傳送型態
                .payModeNo("2")           // 付款模式
                .customerId(CUSTOMERID) // 商店代號
                .orderNo(paymentRequest.getOrderNo())    // 交易單號
                .amount(paymentRequest.getAmount())           // 交易金額
                .buyerName(paymentRequest.getBuyerName())    // 消費者姓名
                .buyerTelm(paymentRequest.getBuyerTelm())  // 消費者手機
                .buyerMail(paymentRequest.getBuyerMail()) // 消費者Email
                .buyerMemo(paymentRequest.getBuyerMemo()) // 消費備註
                .returnUrl(paymentRequest.getReturnUrl()) // 授權結果回傳網址
                .callbackUrl("https://api.onemorelottery.tw:8081/payment/paymentCallback") // 背景對帳網址
                .eReturn("1")             // 是否使用Json回傳
                .strCheck(STRCHECK) // 交易驗證密碼
                .build();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 构建请求体
            StringBuilder requestBody = new StringBuilder();
            requestBody.append("Send_Type=").append(req.getSendType())
                    .append("&Pay_Mode_No=").append(req.getPayModeNo())
                    .append("&CustomerId=").append(req.getCustomerId())
                    .append("&Order_No=")
                    .append("&Amount=").append(req.getAmount())
                    .append("&Buyer_Name=").append(req.getBuyerName())
                    .append("&Buyer_Telm=").append(req.getBuyerTelm())
                    .append("&Buyer_Mail=").append(req.getBuyerMail())
                    .append("&Buyer_Memo=").append(req.getBuyerMemo())
                    .append("&Return_url=")
                    .append("&Callback_Url=").append(req.getCallbackUrl())
                    .append("&e_return=").append(req.getEReturn())
                    .append("&Str_Check=").append(req.getStrCheck());

            post.setEntity(new StringEntity(requestBody.toString()));

            System.out.println(requestBody);
            // 发送请求并接收响应
            HttpResponse response = httpClient.execute(post);
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

            // 处理响应体
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Response JSON: " + jsonResponse);

            Gson gson = new Gson();
            PaymentResponse paymentResponse = gson.fromJson(jsonResponse, PaymentResponse.class);

            return paymentResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PaymentResponse webATM2(PaymentRequest paymentRequest) {
//        String url = "https://n.gomypay.asia/ShuntClass.aspx";  //正式
        String url = "https://n.gomypay.asia/TestShuntClass.aspx";  //測試

        PaymentRequest req = PaymentRequest.builder()
                .sendType("4")            // 傳送型態
                .payModeNo("2")           // 付款模式
                .customerId(CUSTOMERID) // 商店代號
                .orderNo(paymentRequest.getOrderNo())    // 交易單號
                .amount(paymentRequest.getAmount())           // 交易金額
                .buyerName(paymentRequest.getBuyerName())    // 消費者姓名
                .buyerTelm(paymentRequest.getBuyerTelm())  // 消費者手機
                .buyerMail(paymentRequest.getBuyerMail()) // 消費者Email
                .buyerMemo(paymentRequest.getBuyerMemo()) // 消費備註
                .returnUrl(paymentRequest.getReturnUrl()) // 授權結果回傳網址
                .callbackUrl("https://api.onemorelottery.tw:8081/payment/paymentCallback2") // 背景對帳網址
                .eReturn("1")             // 是否使用Json回傳
                .strCheck(STRCHECK) // 交易驗證密碼
                .build();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 构建请求体
            StringBuilder requestBody = new StringBuilder();
            requestBody.append("Send_Type=").append(req.getSendType())
                    .append("&Pay_Mode_No=").append(req.getPayModeNo())
                    .append("&CustomerId=").append(req.getCustomerId())
                    .append("&Order_No=")
                    .append("&Amount=").append(req.getAmount())
                    .append("&Buyer_Name=").append(req.getBuyerName())
                    .append("&Buyer_Telm=").append(req.getBuyerTelm())
                    .append("&Buyer_Mail=").append(req.getBuyerMail())
                    .append("&Buyer_Memo=").append(req.getBuyerMemo())
                    .append("&Return_url=")
                    .append("&Callback_Url=").append(req.getCallbackUrl())
                    .append("&e_return=").append(req.getEReturn())
                    .append("&Str_Check=").append(req.getStrCheck());

            post.setEntity(new StringEntity(requestBody.toString()));

            System.out.println(requestBody);
            // 发送请求并接收响应
            HttpResponse response = httpClient.execute(post);
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

            // 处理响应体
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Response JSON: " + jsonResponse);

            Gson gson = new Gson();
            PaymentResponse paymentResponse = gson.fromJson(jsonResponse, PaymentResponse.class);

            return paymentResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PaymentResponse topOp(PaymentRequest paymentRequest, String payMethod , Long userId) throws Exception {
        PaymentResponse response = null;
        if("1".equals(payMethod)){
            response = this.creditCard(paymentRequest);
        }else if("2".equals(payMethod)){
            response = this.webATM2(paymentRequest);
        }


        return response;
    }

    /**
     * 计算奖励金额
     */
    public int calculateReward(BigDecimal totalAmount) {
        if (totalAmount.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            return 10000;
        } else if (totalAmount.compareTo(BigDecimal.valueOf(50000)) >= 0) {
            return 4000;
        } else if (totalAmount.compareTo(BigDecimal.valueOf(30000)) >= 0) {
            return 2000;
        } else if (totalAmount.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            return 500;
        } else if (totalAmount.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            return 200;
        } else if (totalAmount.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return 30;
        }
        return 0; // 没有达标
    }

    /**
     * 获取用户当月的累积消费金额
     */
    public Award getTotalConsumeAmountForCurrentMonth(Long userId) {
        // 計算本月的起始與結束日期
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        // 獲取該用戶當月的消費總金額
        BigDecimal deposit = userTransactionRepository.getTotalAmountForUserAndMonth(userId, "CONSUME", startOfMonth, endOfMonth);

        // 初始化 Award 物件
        Award award = new Award();
        award.setCumulative(deposit);

        // 累計滿額條件和對應代幣數量
        int[] thresholds = {1000, 5000, 10000, 30000, 50000, 100000};
        int[] tokens = {30, 200, 500, 2000, 4000, 10000};

        // 創建 rewardStatusList 列表
        List<RewardStatus> rewardStatusList = new ArrayList<>();

        // 遍歷閾值和獎勵代幣
        for (int i = 0; i < thresholds.length; i++) {
            BigDecimal threshold = BigDecimal.valueOf(thresholds[i]);
            int tokenAmount = tokens[i];
            boolean achieved = deposit.compareTo(threshold) >= 0;

            // 無論是否達標，都要返回 RewardStatus
            rewardStatusList.add(new RewardStatus(threshold, tokenAmount, achieved));
        }

        // 設置結果到 Award 物件
        award.setRewardStatusList(rewardStatusList);

        // 檢查是否有達標但未領取的獎勵
        for (RewardStatus status : rewardStatusList) {
            if (status.isAchieved()) {
                // 檢查該門檻是否已領取過
                boolean hasReceivedThisReward = userRewardRepository.hasReceivedRewardForThreshold(
                        userId,
                        startOfMonth,
                        endOfMonth,
                        status.getThreshold()
                );

                if (!hasReceivedThisReward) {
                    // 更新用戶銀幣餘額
                    userRepository.updateSliverCoin(userId, BigDecimal.valueOf(status.getSliver()));

                    // 記錄獎勵發放
                    UserReward userReward = new UserReward();
                    userReward.setUserId(userId);
                    userReward.setRewardAmount(BigDecimal.valueOf(status.getSliver()));
                    userReward.setRewardDate(LocalDate.now());
                    userReward.setThresholdAmount(status.getThreshold());
                    userReward.setCreatedAt(LocalDate.now());
                    userRewardRepository.save(userReward);
                }
            }
        }

        return award;
    }


    // Method to create default RewardStatus list when no rewards have been given
    private List<RewardStatus> createDefaultRewardStatusList() {
        int[] thresholds = {1000, 5000, 10000, 30000, 50000, 100000};
        int[] tokens = {30, 200, 500, 2000, 4000, 10000};
        List<RewardStatus> defaultRewardStatusList = new ArrayList<>();

        for (int i = 0; i < thresholds.length; i++) {
            BigDecimal threshold = BigDecimal.valueOf(thresholds[i]);
            int tokenAmount = tokens[i];
            // 添加默認未達標的 RewardStatus
            defaultRewardStatusList.add(new RewardStatus(threshold, tokenAmount, false));
        }

        return defaultRewardStatusList;
    }







    /**
     * 记录储值交易
     */
    public void recordDeposit(Long userId, BigDecimal amount) {
        userRepository.updateBalance(userId , Integer.parseInt(String.valueOf(amount)));
        userTransactionRepository.insertTransaction(userId, "DEPOSIT", amount);
    }

    /**
     * 记录消费交易
     */
    public void recordConsume(Long userId, BigDecimal amount) {
        userTransactionRepository.insertTransaction(userId, "CONSUME", amount);
    }


    private final OrderRepository orderMapper;
    private final OrderDetailRepository orderDetailMapper;

    @Autowired
    public PaymentService(OrderTempMapper orderTempMapper, OrderDetailTempMapper orderDetailTempMapper,
                        OrderRepository orderMapper, OrderDetailRepository orderDetailMapper) {
        this.orderTempMapper = orderTempMapper;
        this.orderDetailTempMapper = orderDetailTempMapper;
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
    }

    @Transactional
    public void transferOrderFromTemp(String orderId) {
        // 1. 获取临时订单
        OrderTemp orderTemp = orderTempMapper.getOrderTempById(Integer.valueOf(orderId));
        if (orderTemp == null) {
            throw new IllegalArgumentException("OrderTemp not found for orderId: " + orderId);
        }

        // 2. 获取对应的临时订单明细
        List<OrderDetailTemp> orderDetailTemps = orderDetailTempMapper.getOrderDetailsByOrderId(Long.valueOf(orderTemp.getId()));
        if (orderDetailTemps == null || orderDetailTemps.isEmpty()) {
            throw new IllegalArgumentException("OrderDetailTemp not found for orderId: " + orderId);
        }

        // 3. 转换并保存到正式的 Order 表
        Order order = convertToOrder(orderTemp);
        orderMapper.insertOrder(order);

        // 4. 转换并保存到正式的 OrderDetail 表
        List<OrderDetail> orderDetails = convertToOrderDetails(Long.valueOf(order.getId()), orderDetailTemps);
        orderDetailMapper.savePrizeOrderDetailBatch(orderDetails);

        // 5. 清理临时表数据
        orderTempMapper.deleteOrderTemp(orderTemp.getId());
        orderDetailTempMapper.deleteOrderDetail(Long.valueOf(orderTemp.getOrderNumber()));
    }

    private Order convertToOrder(OrderTemp orderTemp) {
        return Order.builder()
                .orderNumber(orderTemp.getOrderNumber())
                .userId(orderTemp.getUserId())
                .totalAmount(orderTemp.getTotalAmount())
                .shippingCost(orderTemp.getShippingCost())
                .isFreeShipping(orderTemp.getIsFreeShipping())
                .bonusPointsEarned(orderTemp.getBonusPointsEarned())
                .bonusPointsUsed(orderTemp.getBonusPointsUsed())
                .createdAt(orderTemp.getCreatedAt())
                .updatedAt(orderTemp.getUpdatedAt())
                .paidAt(orderTemp.getPaidAt())
                .resultStatus(orderTemp.getResultStatus())
                .paymentMethod(orderTemp.getPaymentMethod())
                .shippingMethod(orderTemp.getShippingMethod())
                .shippingName(orderTemp.getShippingName())
                .shippingZipCode(orderTemp.getShippingZipCode())
                .shippingCity(orderTemp.getShippingCity())
                .shippingArea(orderTemp.getShippingArea())
                .shippingAddress(orderTemp.getShippingAddress())
                .billingZipCode(orderTemp.getBillingZipCode())
                .billingName(orderTemp.getBillingName())
                .billingCity(orderTemp.getBillingCity())
                .billingArea(orderTemp.getBillingArea())
                .billingAddress(orderTemp.getBillingAddress())
                .invoice(orderTemp.getInvoice())
                .trackingNumber(orderTemp.getTrackingNumber())
                .shippingPhone(orderTemp.getShippingPhone())
                .shopId(orderTemp.getShopId())
                .OPMode(orderTemp.getOPMode())
                .build();
    }

    private List<OrderDetail> convertToOrderDetails(Long orderId, List<OrderDetailTemp> orderDetailTemps) {
        return orderDetailTemps.stream().map(detailTemp -> OrderDetail.builder()
                .orderId(orderId)
                .productDetailId(detailTemp.getProductDetailId())
                .storeProductId(detailTemp.getStoreProductId())
                .quantity(detailTemp.getQuantity())
                .totalPrice(detailTemp.getTotalPrice())
                .bonusPointsEarned(detailTemp.getBonusPointsEarned())
                .build()
        ).collect(Collectors.toList());
    }
}
