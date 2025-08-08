package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.WarehouseDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/admin-reject-rental")
public class RejectRentalServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int rentalOrderId = Integer.parseInt(request.getParameter("rentalOrderId"));

        boolean success = rentalOrderDAO.updateRentalStatus(rentalOrderId, "Rejected");
        // Lấy warehouseID liên kết
        int warehouseId = rentalOrderDAO.getWarehouseIdByRentalOrderId(rentalOrderId);
        boolean updated = warehouseDAO.updateStatus(warehouseId, "Available");
        if (success && updated) {
            response.sendRedirect("admin-approve-list.jsp?status=success");
        } else {
            response.sendRedirect("admin-approve-list.jsp?status=error");
        }
    }
}
