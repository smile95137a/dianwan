package com.one.frontend.service;

import com.google.gson.Gson;
import com.one.frontend.model.PaymentRequest;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.repository.UserTransactionRepository;
import com.one.frontend.response.PaymentResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PaymentService {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Autowired
    private UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String CUSTOMERID = "B82FD0DF7DE03FC702DEC35A2446E469";
    private final String STRCHECK = "d0q2mo1729enisehzolmhdwhkac38itb";

    public PaymentResponse creditCard(PaymentRequest paymentRequest) {

//        String url = "https://n.gomypay.asia/ShuntClass.aspx";  //正式
        String url = "https://n.gomypay.asia/TestShuntClass.aspx";  //測試

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
                .sendType("3")            // 傳送型態
                .payModeNo("2")           // 付款模式
                .customerId(CUSTOMERID) // 商店代號
                .orderNo(paymentRequest.getOrderNo())    // 交易單號
                .amount(paymentRequest.getAmount())           // 交易金額
                .buyerName(paymentRequest.getBuyerName())    // 消費者姓名
                .buyerTelm(paymentRequest.getBuyerTelm())  // 消費者手機
                .buyerMail(paymentRequest.getBuyerMail()) // 消費者Email
                .buyerMemo(paymentRequest.getBuyerMemo()) // 消費備註
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
            response = this.webATM(paymentRequest);
        }
        if("1".equals(response.getResult())){
            userRepository.updateBalance(userId , Integer.parseInt(response.getAmount()));
        }else{
            throw new Exception("儲值失敗，原因 :" + response.getRetMsg());
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
    public BigDecimal getTotalConsumeAmountForCurrentMonth(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return userTransactionRepository.getTotalAmountForUserAndMonth(userId, "DEPOSIT", startOfMonth, endOfMonth);
    }

    /**
     * 记录储值交易
     */
    public void recordDeposit(Long userId, BigDecimal amount) {
        userTransactionRepository.insertTransaction(userId, "DEPOSIT", amount);
    }

    /**
     * 记录消费交易
     */
    public void recordConsume(Long userId, BigDecimal amount) {
        userTransactionRepository.insertTransaction(userId, "CONSUME", amount);
    }
}
