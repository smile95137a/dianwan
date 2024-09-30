package com.one.frontend.service;

import com.google.gson.Gson;
import com.one.frontend.model.PaymentRequest;
import com.one.frontend.response.PaymentResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class PaymentService {

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

}
