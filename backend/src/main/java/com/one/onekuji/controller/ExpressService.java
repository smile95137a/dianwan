package com.one.onekuji.controller;

import com.one.onekuji.request.Address;
import com.one.onekuji.request.CallHome;
import com.one.onekuji.request.HomeReq;
import com.one.onekuji.request.LogisticsRequest;
import com.one.onekuji.util.Md5;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ExpressService {
    public String convenience(LogisticsRequest logisticsRequest) {
        String url = "https://logistics.gomypay.asia/LogisticsAPI.aspx";
        String md5 = "5E11B0983580ABDE" + logisticsRequest.getVendorOrder();
        String s = Md5.MD5(md5.toLowerCase());
        s = s.toUpperCase();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("Vendororder", logisticsRequest.getVendorOrder()); // 客戶訂單編號
        params.add("mode", "C2C"); // 物流方式
        params.add("EshopId", "0038"); // 客戶代號
        params.add("StoreId", logisticsRequest.getStoreId()); // 門市代號
        params.add("Amount", logisticsRequest.getAmount()); // 交易金額
        params.add("ServiceType", "3"); // 服務型態代碼 //1- 取貨付款3- 取貨不付款退貨通5- 退貨付款4- 退貨不付款//通路代號 1:全家 2:萊爾富3: 統一超商 4.OK 超商
        params.add("OrderAmount", logisticsRequest.getOrderAmount()); // 商品價值
        params.add("SenderName", logisticsRequest.getSenderName()); // 寄件人姓名
        params.add("SendMobilePhone", logisticsRequest.getSendMobilePhone()); // 寄件人手機電話
        params.add("ReceiverName", logisticsRequest.getReceiverName()); // 取貨人姓名
        params.add("ReceiverMobilePhone", logisticsRequest.getReceiverMobilePhone()); // 取貨人手機電話
        params.add("OPMode", logisticsRequest.getOpMode()); // 通路代號
        params.add("Internetsite", "https://api.onemorelottery.tw:8081/logistics/callback"); // 接收狀態的網址
        params.add("ShipDate", logisticsRequest.getShipDate()); // 出貨日期
        params.add("CHKMAC", s); // 檢查碼

        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 設定為表單格式

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 發送 POST 請求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    public String home(HomeReq homeReq) {

//        String url = "https://logistics.gomypay.asia/Api/Delivery/PrintOBT.aspx"; //正式
        String url = "http://testlogistics.gomytw.com/Api/Delivery/PrintOBT.aspx"; //測試
        String md5 = "5E11B0983580ABDE" + homeReq.getVendorOrder();
        String s = Md5.MD5(md5.toLowerCase());
        s = s.toUpperCase();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("VendorOrder", homeReq.getVendorOrder()); // 客戶訂單編號
        params.add("EshopId", "0038"); // 客戶代號
        params.add("Thermosphere", "0001"); // 溫層(代碼)
        params.add("Spec", String.valueOf(homeReq.getSpec())); // 規格(代碼)
        params.add("ServiceType", "3"); // 服務型態代碼
        params.add("InternetSite", ""); // 接收狀態的網址
        params.add("Amount", String.valueOf(homeReq.getAmount())); // 交易金額
        params.add("RecipientName", homeReq.getRecipientName()); // 取貨人姓名
        params.add("RecipientMobile", homeReq.getRecipientMobile()); // 取貨人手機電話
        params.add("RecipientAddress", homeReq.getRecipientAddress()); // 取貨人地址
        params.add("SenderName", homeReq.getSenderName()); // 寄件人姓名
        params.add("SenderMobile", homeReq.getSenderMobile()); // 寄件人手機電話
        params.add("SenderZipCode", homeReq.getSenderZipCode()); // 寄件人郵碼
        params.add("SenderAddress", homeReq.getSenderAddress()); // 寄件人地址
        params.add("ShipmentDate", homeReq.getShipmentDate()); // 出貨日期
        params.add("DeliveryDate", homeReq.getDeliveryDate()); // 希望配達日期
        params.add("ProductTypeId","0015"); // 商品類別(代碼)
        params.add("ProductName", "景品"); // 商品名稱
        params.add("CHKMAC", s); // 檢查碼

        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 設定為表單格式

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 發送 POST 請求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();

    }

    public String getAddress(Address address) {
//        String url = "https://logistics.gomypay.asia/Api/Delivery/ParsingAddress.aspx"; //正式
        String url = "http://testlogistics.gomytw.com/Api/Delivery/ParsingAddress.aspx"; //測試

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("EshopId", "0038"); // 客戶代號
        params.add("Addresses", address.getAddress()); // 地址
        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 設定為表單格式

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 發送 POST 請求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();

    }

    public String callHome(CallHome callHome) {
        //String url = "https://logistics.gomypay.asia/Api/Delivery/Call.aspx"; //正式
        String url = "http://testlogistics.gomytw.com/Api/Delivery/Call.aspx"; //測試
        String md5 = "5E11B0983580ABDE" + callHome.getVendorOrder();
        String s = Md5.MD5(md5.toLowerCase());
        s = s.toUpperCase();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("EshopId", "0038"); // 客戶代號
        params.add("ContactName", callHome.getContactName()); // 聯絡人姓名
        params.add("ContactMobile", callHome.getContactMobile()); // 聯絡人手機
        params.add("ContactAddress", callHome.getContactAddress()); // 聯絡人地址
        params.add("NormalQuantity", String.valueOf(callHome.getNormalQuantity())); // 常溫出貨件數
        params.add("ColdQuantity", ""); // 冷藏出貨件數
        params.add("FreezeQuantity", ""); // 冷凍出貨件數
        params.add("IsContact", callHome.getIsContact()); // 是否需要電聯
        params.add("IsTrolley", callHome.getIsTrolley()); // 是否需要推車
        params.add("CHKMAC", s); // 檢查碼

        // 設定 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 設定為表單格式

        // 封裝請求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 發送 POST 請求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }
}
