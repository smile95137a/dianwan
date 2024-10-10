package com.one.frontend.controller;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ExpressController {

    @GetMapping("/logistics/callback")
    public ResponseEntity<String> logisticsCallback(
            @RequestParam String storename,
            @RequestParam String storeid,
            @RequestParam(required = false) String storeaddress,
            @RequestParam(required = false) String storeph
    ) {
        // 處理回傳的店名和店號
        System.out.println("Store Name: " + storename);
        System.out.println("Store ID: " + storeid);
        System.out.println("Store Address: " + storeaddress);
        System.out.println("Store Phone: " + storeph);
        // 設定要發送的 URL
        String url = "https://logistics.gomypay.asia/LogisticsAPI.aspx";


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("Vendororder", "12345678901234567890123456789012"); // 客戶訂單編號
        params.add("mode", "B2C"); // 物流方式
        params.add("EshopId", "0038"); // 客戶代號
        params.add("StoreId", "276649"); // 門市代號
        params.add("Amount", "1"); // 交易金額
        params.add("ServiceType", "1"); // 服務型態代碼
        params.add("OrderAmount", "1000"); // 商品價值
        params.add("SenderName", "張三"); // 寄件人姓名
        params.add("SendMobilePhone", "0912345678"); // 寄件人手機電話
        params.add("ReceiverName", "李四"); // 取貨人姓名
        params.add("ReceiverMobilePhone", "0987654321"); // 取貨人手機電話
        params.add("OPMode", "1"); // 通路代號
        params.add("Internetsite", "https://api.onemorelottery.tw:8081/logistics/callback"); // 接收狀態的網址
        params.add("ShipDate", "2024-10-08"); // 出貨日期
        params.add("CHKMAC", "YourCheckMacHere"); // 檢查碼


        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 設定為表單格式

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 發送 POST 請求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);


        return ResponseEntity.ok("Received logistics info successfully");
    }


    @PostMapping("/test")
    public ResponseEntity<String> logisticsCallback() {
        String url = "https://logistics.gomypay.asia/Logisticstm.aspx";

        // 設定 POST 的參數
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("Url", "http://localhost:8081/logistics/callback");
        params.add("Url", "https://api.onemorelottery.tw:8081/logistics/callback");
        params.add("Opmode", "1");

        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 設定為表單格式

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 發送 POST 請求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // 處理回應
        System.out.println("Response: " + response);
        // 根據業務邏輯處理
        return ResponseEntity.ok("Received logistics info successfully");
    }

}
