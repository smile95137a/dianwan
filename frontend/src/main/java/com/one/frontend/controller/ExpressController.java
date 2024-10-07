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

    @PostMapping("/logistics/callback")
    public ResponseEntity<String> logisticsCallback(
            @RequestParam String storename,
            @RequestParam String storied
    ) {
        // 處理回傳的店名和店號
        System.out.println("Store Name: " + storename);
        System.out.println("Store ID: " + storied);
        System.out.println(123);
        // 根據業務邏輯處理
        return ResponseEntity.ok("Received logistics info successfully");
    }


    @GetMapping("/test")
    public ResponseEntity<String> logisticsCallback() {
        String url = "https://logistics.gomypay.asia/Logisticstm.aspx";

        // 設定 POST 的參數
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("Url", "http://localhost:8081/logistics/callback");
        params.add("Url", "https://api.onemorelottery.tw:8081/logistics/callback");
        params.add("Opmode", "3");

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
