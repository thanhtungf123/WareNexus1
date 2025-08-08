package com.warenexus.controller;

import com.warenexus.dao.CustomerDAO;
import com.warenexus.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int accountId = Integer.parseInt(req.getParameter("accountId"));
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");

            Customer updated = new Customer();
            updated.setAccountId(accountId);
            updated.setFullName(fullName);
            updated.setPhone(phone);
            updated.setEmail(email);
            customerDAO.update(updated);

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect("view-profile");
    }
}
