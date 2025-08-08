package com.warenexus.controller;

import com.warenexus.model.Account;
import com.warenexus.util.EmailSender;
import com.warenexus.util.OTPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/requestOTP")
public class RequestOTPServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy rentalOrderId từ session
            Integer rentalOrderId = Integer.parseInt(request.getParameter("rentalOrderId"));
            if (rentalOrderId == null) {
                response.sendRedirect("userhome.jsp");
                return;
            }

            // Lấy chữ ký từ form (nếu có)
            String signatureImage = request.getParameter("signatureImage");

            // Kiểm tra nếu chữ ký không có (dùng chữ ký mặc định)
            if (signatureImage == null || signatureImage.isEmpty()) {
                signatureImage = "";  // Chữ ký mặc định
            }

            // Lưu chữ ký vào session hoặc cơ sở dữ liệu nếu cần
            HttpSession session = request.getSession();
            session.setAttribute("signatureImage", signatureImage);  // Lưu chữ ký vào session

            // Tạo OTP mới
            String otp = OTPUtil.generateOTP(6);  // Tạo OTP dài 6 ký tự

            // Lưu OTP vào session
            session.setAttribute("otp", otp);
            session.setAttribute("otpExpiration", System.currentTimeMillis() + 10 * 60 * 1000); // Thời gian hết hạn là 10 phút

            // Lấy email người dùng từ session
            Account acc = (Account) session.getAttribute("acc");
            if (acc == null) {
                request.setAttribute("errorMessage", "Bạn chưa đăng nhập.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            // Gửi OTP qua email cho người dùng
            String userEmail = acc.getEmail();  // Lấy email người dùng từ session hoặc DB
            EmailSender.sendOTPEmail(userEmail, otp);  // Gửi email với OTP

            // Chuyển đến trang nhập OTP
            response.sendRedirect("enterOtp.jsp?rentalOrderId=" + rentalOrderId);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi gửi OTP. Vui lòng thử lại.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

