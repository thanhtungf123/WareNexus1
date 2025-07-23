package com.warenexus.util;

import org.json.JSONObject;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Date;

public class PayOSUtil {

    private static final String CLIENT_ID = "07a0181f-43af-43a1-b7f2-03fca43564f3";  // Thay bằng Client ID của bạn
    private static final String API_KEY = "1ae5fc7b-cbd2-4372-86e4-0d8e3e225aee";  // Thay bằng API Key của bạn
    private static final String CHECKSUM_KEY = "9eae37593e8d10a6eac7c93543c5c07c8671abbb0279f4b27e37f972c174e0e3";

    private static final PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

    public static JSONObject createPaymentRequest(double amount, String description, String returnUrl) {
        try {
            long orderCode = generateOrderCode();

            ItemData item = ItemData.builder()
                    .name("Đặt cọc thuê dịch vụ")
                    .quantity(1)
                    .price((int) amount) // PayOS yêu cầu đơn vị là VND, kiểu int
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount((int) amount)
                    .description("MB0123")
                    .returnUrl(returnUrl)
                    .cancelUrl(returnUrl.replace("status=PAID", "status=CANCELED")) // fallback
                    .item(item)
                    .build();

            CheckoutResponseData checkout = payOS.createPaymentLink(paymentData);

            JSONObject result = new JSONObject();
            result.put("qrCode", checkout.getQrCode());
            result.put("paymentLink", checkout.getCheckoutUrl());
            result.put("orderCode", orderCode);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo đơn thanh toán: " + e.getMessage(), e);
        }
    }

    private static long generateOrderCode() {
        String currentTimeString = String.valueOf(new Date().getTime());
        return Long.parseLong(currentTimeString.substring(currentTimeString.length() - 10));
    }
}