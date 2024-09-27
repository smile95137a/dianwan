package com.one.frontend.controller;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.request.ReceiptReq;
import com.one.frontend.response.ReceiptRes;
import com.one.frontend.service.InvoiceService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/add")
    public void addInvoice(@RequestBody ReceiptReq invoiceRequest) throws MessagingException {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
        Long userId = userDetails.getId();
        ResponseEntity<ReceiptRes> res = invoiceService.addB2CInvoice(invoiceRequest);
        System.out.println(res.getBody());
//        ReceiptRes receiptRes = res.getBody();
//        invoiceService.getInvoicePicture(receiptRes.getCode() , userId);
    }
}
