package com.warenexus.util;

import java.io.*;
import java.util.Base64;

public class SignatureUtils {

    public static String saveBase64Signature(String base64String, String outputPath) throws IOException {
        // Loại bỏ header "data:image/png;base64," nếu có
        if (base64String.contains("base64,")) {
            base64String = base64String.split("base64,")[1];
        }

        // Giải mã Base64 thành byte[]
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // Tạo tệp hình ảnh
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
            fileOutputStream.write(decodedBytes);
        }

        return outputPath; // Trả về đường dẫn tệp hình ảnh đã lưu
    }
}
