package com.warenexus.util;

import org.json.JSONObject;

public class PayOSUtil {
    public static JSONObject createPaymentRequest(double amount, String description, String returnUrl) {
        JSONObject data = new JSONObject();
        data.put("qrCode", "https://dummyimage.com/300x300/000/fff&text=MOCK+QR");
        data.put("paymentLink", "https://example.com/mock-payment");

        JSONObject result = new JSONObject();
        result.put("orderCode", System.currentTimeMillis());
        result.put("data", data);
        return result;
    }
}
