package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.request.CodeRequest;
import com.one.frontend.util.ResponseUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        params.add("Internetsite", "http://localhost:5173/mall-checkout"); // 接收狀態的網址
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


    @PostMapping("/express")
    public ResponseEntity<ApiResponse<?>> logisticsCallback(@RequestBody CodeRequest codeRequest) {
        String url = "https://logistics.gomypay.asia/Logisticstm.aspx";

        // 提取 code 值
        String code = codeRequest.getCode();
        System.out.println("711".equals(code));

        // 設定 POST 的參數
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if("1".equals(codeRequest.getX())){ //1是mall 2是賞品盒S
            params.add("Url", "https://onemorelottery.tw/mall-checkout");
        }else if ("2".equals(codeRequest.getX())){
            params.add("Url", "https://onemorelottery.tw/member-center/prize-checkout");
        }

        params.add("Opmode", "711".equals(code) ? "3" : ("family".equals(code) ? "1" : "0"));

        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            // 發送 POST 請求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            // 處理回應
            String responseBody = response.getBody();
            System.out.println("Response: " + responseBody);

            // 使用正則表達式提取 href 的 URL
            String extractedUrl = extractHrefUrl(responseBody);
            System.out.println("提取的 URL: " + extractedUrl);

            // 根據業務邏輯處理
            return ResponseEntity.ok(ResponseUtils.success(200, "查詢成功", extractedUrl));
        } catch (Exception e) {
            // 錯誤處理
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.error(500, "物流回調失敗"));
        }
    }

    // 提取 href URL 的方法
    private String extractHrefUrl(String html) {
        // 定義正則表達式來匹配 href URL
        String regex = "href=\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            String extractedUrl = matcher.group(1);
            // 替換掉 &amp; 為 & 符號
            extractedUrl = extractedUrl.replace("&amp;", "&");
            return extractedUrl;
        } else {
            return "未找到 href URL";
        }
    }



}
