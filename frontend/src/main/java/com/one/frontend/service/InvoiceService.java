package com.one.frontend.service;

import com.one.frontend.model.InvoicePictureRequest;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.ReceiptReq;
import com.one.frontend.response.ReceiptRes;
import com.one.frontend.response.UserRes;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class InvoiceService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<ReceiptRes> addB2CInvoice(ReceiptReq invoiceRequest) {
        String url = "https://www.giveme.com.tw/invoice.do?action=addB2C";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String date = String.valueOf(LocalDateTime.parse(LocalDateTime.now().toString()));
        String md5 = InvoiceService.md5(date+"eason"+"Jj47075614");

        ReceiptReq req = ReceiptReq.builder().timeStamp(date).uncode("47075614").idno("eason")
                .sign(md5).orderCode(invoiceRequest.getOrderCode()).datetime(date).email(invoiceRequest.getEmail()).state(invoiceRequest.getState()).donationCode(invoiceRequest.getDonationCode()).totalFee(invoiceRequest.getTotalFee()).content("再來一抽備註").items(invoiceRequest.getItems()).build();

        // 创建请求实体
        HttpEntity<ReceiptReq> requestEntity = new HttpEntity<>(req, headers);

        // 发送 POST 请求

        return restTemplate.postForEntity(url, requestEntity, ReceiptRes.class);
    }

    private static String md5(String input) {
        try {
            // 创建 MD5 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算哈希值
            byte[] messageDigest = md.digest(input.getBytes());

            // 将哈希值转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 返回大写形式的哈希值
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<byte[]> getInvoicePicture(String code , Long userId) throws MessagingException {
        String url = "https://www.giveme.com.tw/invoice.do?action=picture";
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String date = String.valueOf(LocalDateTime.parse(LocalDateTime.now().toString()));
        String md5 = InvoiceService.md5(date+"eason"+"Jj47075614");
        InvoicePictureRequest req = InvoicePictureRequest.builder().timeStamp(date).uncode("47075614").idno(md5).code(code).type("2").build();
        // 创建请求实体
        HttpEntity<InvoicePictureRequest> entity = new HttpEntity<>(req, headers);

        // 发送 POST 请求
        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, entity, byte[].class);
        UserRes user = userRepository.getUserById(userId);

        mailService.sendRecImg(user.getUsername(),response);

        return response;
    }
}
