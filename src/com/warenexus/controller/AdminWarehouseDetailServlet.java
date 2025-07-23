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
            // Kiểm tra tham số đầu vào
            String idParam = req.getParameter("id");
            if (idParam == null || !idParam.matches("\\d+")) {
                resp.sendRedirect("page-not-found.jsp");
                return;
            }

            int warehouseId = Integer.parseInt(idParam);
            Warehouse warehouse = warehouseDAO.getById(warehouseId);

            if (warehouse == null) {
                req.setAttribute("errorMessage", "Warehouse not found.");
                resp.sendRedirect("page-not-found.jsp");
                return;
            }

            req.setAttribute("warehouse", warehouse);

            // Nếu kho đang được thuê, lấy thông tin đơn thuê và khách hàng
            if ("Rented".equalsIgnoreCase(warehouse.getStatus())) {
                RentalOrder order = rentalOrderDAO.getLatestApprovedRentalByWarehouseId(warehouseId);
                if (order != null) {
                    Customer customer = customerDAO.getCustomerByAccountId(order.getAccountID());
                    req.setAttribute("rentalOrder", order);
                    req.setAttribute("customer", customer);
                }
            }

            // Forward đến trang JSP
            req.getRequestDispatcher("admin-warehouse-detail.jsp").forward(req, resp);

        } catch (Exception e) {
            log("Error in AdminWarehouseDetailServlet", e);
            req.setAttribute("errorMessage", "Unable to load warehouse details.");
            resp.sendRedirect("admin-warehouse-view.jsp");
        }
    }
}
