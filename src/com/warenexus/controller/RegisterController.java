package com.warenexus.controller;

import com.warenexus.dao.UserDAO;
import com.warenexus.model.Account;
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
        String fullName = req.getParameter("fullName");
        String email    = req.getParameter("email");
        String phone    = req.getParameter("phone");
        String password = req.getParameter("password");

        try {
            // Insert Account + Role + Customer
            dao.registerCustomer(fullName, email, phone, password);

            // Retrieve inserted Account
            Account account = dao.findByEmail(email);
            if (account != null) {
                req.getSession().setAttribute("user", account);
                resp.sendRedirect(req.getContextPath() + "/userhome.jsp");
            } else {
                req.setAttribute("error", "Registration failed");
                req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }
}
