package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/reject-rental")
public class RejectRentalServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int rentalOrderId = Integer.parseInt(request.getParameter("rentalOrderId"));

        boolean success = rentalOrderDAO.updateRentalStatus(rentalOrderId, "Rejected");
        if (success) {
            response.sendRedirect("admin-approve-list.jsp?status=success");
        } else {
            response.sendRedirect("admin-approve-list.jsp?status=error");
        }
    }
}
