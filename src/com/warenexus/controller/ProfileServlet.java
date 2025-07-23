package com.warenexus.controller;

import com.warenexus.dao.CustomerDAO;
import com.warenexus.model.Account;
import com.warenexus.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/view-profile")
public class ProfileServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Account user = (session != null) ? (Account) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Customer customer = customerDAO.getCustomerByAccountId(user.getAccountId());
        req.setAttribute("customer", customer);
        req.getRequestDispatcher("profile.jsp").forward(req, resp);
    }
}
