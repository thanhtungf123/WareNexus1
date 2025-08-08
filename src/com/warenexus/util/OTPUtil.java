package com.warenexus.util;

import java.util.Random;
public class OTPUtil {

    // Phương thức tạo OTP ngẫu nhiên với chiều dài tùy chỉnh
    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        Random rand = new Random();

        // Các ký tự có thể xuất hiện trong OTP
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // Tạo OTP với độ dài yêu cầu
        for (int i = 0; i < length; i++) {
            otp.append(characters.charAt(rand.nextInt(characters.length())));
        }

        return otp.toString();
    }
}
