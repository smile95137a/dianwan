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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class InvoiceService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;
    // 如果你没有使用 Spring 的依赖注入，也可以手动初始化
    public InvoiceService() {
        this.restTemplate = new RestTemplate();
    }
    public static ReceiptReq generateFakeReceipt() {
        Random random = new Random();

        // 建立一個 ReceiptReq 物件
        ReceiptReq receipt = ReceiptReq.builder()
                .timeStamp(String.valueOf(System.currentTimeMillis()))
                .datetime("2024-09-27 12:34:56")
                .state(0)
                .totalFee(String.valueOf(random.nextInt(10000)))
                .items(generateFakeItems(3))  // 建立一個帶有3個假項目的Item列表
                .build();

        return receipt;
    }

    private static List<ReceiptReq.Item> generateFakeItems(int count) {
        List<ReceiptReq.Item> items = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= count; i++) {
            ReceiptReq.Item item = ReceiptReq.Item.builder()
                    .name("Item " + i)
                    .money(random.nextInt(1000))
                    .number(random.nextInt(10) + 1)
                    .build();

            items.add(item);
        }

        return items;
    }

    public static void main(String[] args) {
        ReceiptReq fakeReceipt = generateFakeReceipt();
        InvoiceService service = new InvoiceService();
        System.out.println(fakeReceipt);
        service.addB2CInvoice(fakeReceipt);

    }

    public ResponseEntity<ReceiptRes> addB2CInvoice(ReceiptReq invoiceRequest) {
        String url = "https://www.giveme.com.tw/invoice.do?action=addB2C";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String date = String.valueOf(LocalDateTime.parse(LocalDateTime.now().toString()));
//        String md5 = InvoiceService.md5(date+"eason"+"Jj47075614");
        String md5 = InvoiceService.md5(date+"Giveme09"+"6F89Gi");
        ReceiptReq req = ReceiptReq.builder().timeStamp(date).uncode("53418005").idno("Giveme09")
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
