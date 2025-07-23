package com.warenexus.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendOTPEmail(String recipientEmail, String otp) {
        String senderEmail = "tienloveu123@gmail.com"; // Địa chỉ email người gửi
        String senderPassword = "vgfn whay lrck iaaa"; // Mật khẩu email người gửi
        String host = "smtp.gmail.com"; // Sử dụng Gmail SMTP server

        // Cấu hình thuộc tính kết nối email
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo đối tượng Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP for WareNexus Contract");

            // Nội dung email
            String emailContent = "Dear user,\n\n" +
                    "Your OTP to you is: " + otp + "\n\n" +
                    "This OTP is valid for 10 minutes.\n\n" +
                    "Best regards,\n" +
                    "WareNexus Team";
            message.setText(emailContent);

            // Gửi email
            Transport.send(message);
            System.out.println("OTP Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}