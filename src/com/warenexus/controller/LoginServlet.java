package com.warenexus.controller;

import com.warenexus.dao.AccountDAO;
import com.warenexus.model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            AccountDAO dao = new AccountDAO();
            Account user = dao.login(email, password); // đã sửa DAO ở bước trước

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("acc", user);
                session.setAttribute("accountId", user.getAccountId());

                // Chuyển hướng mặc định (bạn có thể dùng role sau khi query thêm bảng AccountRole nếu cần)
                response.sendRedirect("userhome.jsp");
            } else {
                request.setAttribute("error", "Sai email hoặc mật khẩu hoặc tài khoản chưa kích hoạt.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
