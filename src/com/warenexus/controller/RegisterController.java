package com.warenexus.controller;

import com.warenexus.dao.UserDAO;
import com.warenexus.model.Account;
import com.warenexus.util.EmailSender;
import com.warenexus.util.OTPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private UserDAO dao;

    @Override
    public void init() {
        dao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/register.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");

        // Lưu thông tin tạm vào session
        session.setAttribute("tempFullName", fullName);
        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPhone", phone);
        session.setAttribute("tempPassword", password);

        // Tạo OTP
        String otp = OTPUtil.generateOTP(5);
        session.setAttribute("otp", otp);
        session.setAttribute("otpExpiry", System.currentTimeMillis() + 3 * 60 * 1000); // 3 phút

        try {
            EmailSender.sendOTPEmail(email, otp); // Gửi OTP qua email
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            req.setAttribute("password", password);
            req.setAttribute("message", "📧 Mã OTP đã được gửi đến email của bạn.");
            req.setAttribute("showOtpForm", true);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi gửi email OTP: " + e.getMessage());
        }

        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
}
