package com.warenexus.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailSender {

    public static void sendOTPEmail(String recipientEmail, String otp) {
        Properties config = new Properties();

        // Load file config.properties
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find config.properties file.");
            }
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email configuration.", e);
        }

        // Lấy thông tin từ file cấu hình
        String senderEmail = config.getProperty("email.username");
        String senderPassword = config.getProperty("email.password");
        String host = config.getProperty("smtp.host");
        String port = config.getProperty("smtp.port");

        // Cấu hình SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        // Tạo phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP for WareNexus Contract");

            // Nội dung HTML
            String emailContent = "<h3>Dear user,</h3>" +
                    "<p>Your OTP is: <strong>" + otp + "</strong></p>" +
                    "<p>This OTP is valid for 10 minutes.</p>" +
                    "<br><p>Best regards,<br>WareNexus Team</p>";

            message.setContent(emailContent, "text/html; charset=utf-8");

            // Gửi email
            Transport.send(message);
            System.out.println("OTP Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}
