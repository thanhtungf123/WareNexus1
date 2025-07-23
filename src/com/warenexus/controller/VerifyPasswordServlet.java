package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.UserDAO;
import com.warenexus.model.Account;
import com.warenexus.model.RentalOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/verifyPassword")
public class VerifyPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = ((Account) req.getSession().getAttribute("acc")).getEmail(); // Lấy email từ session
        String password = req.getParameter("password");
        int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));

        try {
            // Tìm tài khoản từ email
            Account account = userDAO.findByEmail(email);

            if (account != null && account.getPassword().equals(password)) { // Kiểm tra mật khẩu
                RentalOrder rentalOrder = rentalOrderDAO.getRentalOrderById(rentalOrderId);
                req.setAttribute("rentalOrder", rentalOrder); // Đưa RentalOrder vào request
                req.getSession().setAttribute("verifiedRentalOrderId", rentalOrderId);
                resp.sendRedirect("signContract.jsp?rentalOrderId=" + rentalOrderId);
            } else {
                req.setAttribute("errorMessage", "Incorrect password, please try again.");
                req.getRequestDispatcher("confirmPassword.jsp?rentalOrderId=" + rentalOrderId).forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException("Error occurred while verifying password.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
