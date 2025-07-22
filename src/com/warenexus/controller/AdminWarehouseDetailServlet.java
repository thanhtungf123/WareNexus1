package com.warenexus.controller;

import com.warenexus.dao.*;
import com.warenexus.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin-warehouse-detail")
public class AdminWarehouseDetailServlet extends HttpServlet {
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int warehouseId = Integer.parseInt(req.getParameter("id"));
            Warehouse warehouse = warehouseDAO.getById(warehouseId);
            req.setAttribute("warehouse", warehouse);

            if (warehouse != null && "Rented".equalsIgnoreCase(warehouse.getStatus())) {
                RentalOrder order = rentalOrderDAO.getLatestApprovedRentalByWarehouseId(warehouseId); // ✅ dùng hàm mới
                if (order != null) {
                    Customer customer = customerDAO.getCustomerByAccountId(order.getAccountID());
                    req.setAttribute("rentalOrder", order);
                    req.setAttribute("customer", customer);
                }
            }

            req.getRequestDispatcher("admin-warehouse-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("admin-warehouse-view.jsp");
        }
    }
}

