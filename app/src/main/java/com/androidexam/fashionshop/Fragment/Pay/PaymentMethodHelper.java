package com.androidexam.fashionshop.Fragment.Pay;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethodHelper {
    public static final Map<String, String> paymentMethodMap = new HashMap<>();

    static {
        paymentMethodMap.put("Thanh toán khi nhận hàng", "CASH");
        paymentMethodMap.put("Ví VNPay", "VNPAY");
        paymentMethodMap.put("CASH","Thanh toán khi nhận hàng");
        paymentMethodMap.put("VNPAY","Ví VNPay");


        paymentMethodMap.put("UNCONFIRMED","Chưa xác nhận");
        paymentMethodMap.put("CONFIRMED", "Đã xác nhận");
        paymentMethodMap.put("IN_TRANSIT","Đang giao");
        paymentMethodMap.put("PACKAGING","Đóng gói");
        paymentMethodMap.put("DELIVERED", "Giao thành công");
        paymentMethodMap.put("CANCELLED", "Đã hủy");
        paymentMethodMap.put("RETURN_EXCHANGE","Trả hàng/Đổi hàng");
        paymentMethodMap.put("REFUNDED","Trả tiền");
        paymentMethodMap.put("PREPARING_PAYMENT","Chuẩn bị thanh toán trực tuyến");


        // Thêm các ánh xạ khác nếu cần
    }
}
