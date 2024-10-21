package com.one.frontend.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentResponse {
    @SerializedName("Send_Type")
    private String sendType; // 传送型态
    private String result;    // 结果
    @SerializedName("ret_msg")
    private String retMsg;    // 返回消息
    @SerializedName("OrderID")
    private String orderId;    // 订单 ID
    @SerializedName("e_Cur")
    private String currency;   // 货币
    @SerializedName("e_money")
    private String amount;     // 金额
    @SerializedName("e_date")
    private String date;       // 日期
    @SerializedName("e_time")
    private String time;       // 时间
    @SerializedName("e_orderno")
    private String orderNo;    // 订单号
    @SerializedName("e_no")
    private String number;     // 编号
    @SerializedName("e_outlay")
    private String outlay;     // 支出
    @SerializedName("str_check")
    private String checkString; // 检查字符串
    @SerializedName("bankname")
    private String bankName;   // 银行名称
    @SerializedName("avcode")
    private String avCode;     // AV 代码
    @SerializedName("Invoice_No")
    private String invoiceNo;   // 发票号
    @SerializedName("e_payaccount")
    private String ePayAccount;

    // Getters and Setters
    // (可以使用 Lombok 自动生成)
}
