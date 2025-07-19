package com.warenexus.controller;

import com.warenexus.dao.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/delete-customer")
public class DeleteCustomerServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int accountId = Integer.parseInt(req.getParameter("id"));
            customerDAO.delete(accountId);
        } catch (Exception e) {
            e.printStackTrace(); // Optionally log error
        }
        resp.sendRedirect("admin-customers.jsp");
    }
}
