package com.warenexus.controller;

import com.warenexus.dao.UserDAO;
import com.warenexus.model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyRegisterOTPController extends HttpServlet {
    private UserDAO dao;

    @Override
    public void init() {
        dao = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String inputOtp = req.getParameter("otp");

        String sessionOtp = (String) session.getAttribute("otp");
        Long expiry = (Long) session.getAttribute("otpExpiry");

        // Kiểm tra mã OTP
        if (sessionOtp != null && inputOtp.equals(sessionOtp) && System.currentTimeMillis() <= expiry) {
            // Lấy thông tin đăng ký tạm
            String fullName = (String) session.getAttribute("tempFullName");
            String email    = (String) session.getAttribute("tempEmail");
            String phone    = (String) session.getAttribute("tempPhone");
            String password = (String) session.getAttribute("tempPassword");

            try {
                // Ghi vào database
                dao.insertCustomer(fullName, email, phone, password);

                // Truy xuất tài khoản để đăng nhập tự động
                Account account = dao.findByEmail(email);
                if (account != null) {
                    session.setAttribute("user", account);

                    // Dọn dẹp dữ liệu tạm
                    session.removeAttribute("tempFullName");
                    session.removeAttribute("tempEmail");
                    session.removeAttribute("tempPhone");
                    session.removeAttribute("tempPassword");
                    session.removeAttribute("otp");
                    session.removeAttribute("otpExpiry");
                    session.setAttribute("otpStatus", "verified");

                    // Chuyển hướng tới trang người dùng
                    resp.sendRedirect(req.getContextPath() + "/userhome.jsp");
                    return;
                } else {
                    req.setAttribute("error", "❌ Không thể lấy thông tin tài khoản sau khi tạo.");
                }
            } catch (Exception e) {
                req.setAttribute("error", "❌ Lỗi khi tạo tài khoản: " + e.getMessage());
            }
        } else {
            req.setAttribute("error", "❌ Mã OTP không đúng hoặc đã hết hạn.");
        }

        // Nếu có lỗi, quay lại trang đăng ký
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
}
