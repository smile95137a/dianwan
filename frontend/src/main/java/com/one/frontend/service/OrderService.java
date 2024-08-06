package com.one.frontend.service;

import com.one.frontend.ecpay.payment.integration.AllInOne;
import com.one.frontend.ecpay.payment.integration.domain.AioCheckOutALL;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

	public String ecpayCheckout(Integer userId) {

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate("2017/01/01 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		obj.setReturnURL("https://2217-111-248-73-184.ngrok-free.app/returnUrl");
		obj.setNeedExtraPaidInfo("N");
		obj.setCustomField1(String.valueOf(userId));
		String form = all.aioCheckOut(obj, null);
		System.out.println(form);
		return form;
	}
}