package com.warenexus.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    // Thông tin cấu hình từ file config.properties
    private static final String senderEmail = ConfigUtil.getProperty("email.username");
    private static final String senderPassword = ConfigUtil.getProperty("email.password");
    private static final String host = ConfigUtil.getProperty("smtp.host");
    private static final String port = ConfigUtil.getProperty("smtp.port");

    /**
     * Gửi OTP cho khách hàng
     */
    public static void sendOTPEmail(String recipientEmail, String otp) {
        String subject = "Your OTP for WareNexus Contract";
        String content = "<h3>Dear user,</h3>"
                + "<p>Your OTP is: <strong>" + otp + "</strong></p>"
                + "<p>This OTP is valid for 10 minutes.</p>"
                + "<br><p>Best regards,<br>WareNexus Team</p>";

        sendEmail(recipientEmail, subject, content);
    }

    /**
     * Gửi email nhắc nhở khách thanh toán tiền tổng
     */
    public static void sendReminderEmail(String recipientEmail) {
        String subject = "Nhắc nhở thanh toán tổng tiền thuê kho - WareNexus";
        String content = "<p>Kính chào quý khách,</p>"
                + "<p>Đơn thuê kho của quý khách chỉ còn <strong>1 ngày</strong> để hoàn tất việc thanh toán tổng tiền thuê.</p>"
                + "<p>Vui lòng đăng nhập vào hệ thống WareNexus để thanh toán đúng hạn, tránh việc hủy đơn thuê.</p>"
                + "<br><p>Trân trọng,<br><strong>WareNexus Team</strong></p>";

        sendEmail(recipientEmail, subject, content);
    }

    /**
     * Gửi email thông báo hủy đơn thuê
     */
    public static void sendCancelEmail(String recipientEmail) {
        String subject = "Hủy đơn thuê do trễ hạn thanh toán - WareNexus";
        String content = "<p>Kính chào quý khách,</p>"
                + "<p>Đơn thuê kho của quý khách đã bị <strong>hủy</strong> do không hoàn tất thanh toán đúng hạn.</p>"
                + "<p>Nếu có thắc mắc, vui lòng liên hệ quản trị viên để biết thêm chi tiết.</p>"
                + "<br><p>Trân trọng,<br><strong>WareNexus Team</strong></p>";

        sendEmail(recipientEmail, subject, content);
    }

    /**
     * Hàm gửi email HTML dùng chung
     */
    private static void sendEmail(String recipientEmail, String subject, String htmlContent) {
        // Cấu hình SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Tạo phiên làm việc email
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "WareNexus"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("✅ Email sent to " + recipientEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + recipientEmail + ": " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
