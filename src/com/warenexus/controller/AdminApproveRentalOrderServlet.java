package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/approve-rental")
public class AdminApproveRentalOrderServlet extends HttpServlet {
    private final RentalOrderDAO rentalDAO = new RentalOrderDAO();
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập và quyền admin
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }

            Account admin = (Account) session.getAttribute("user");
            if (admin.getRoleId() != 1) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized.");
                return;
            }

            // Lấy RentalOrderID từ request
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));

            // Duyệt đơn thuê
            rentalDAO.approveRentalOrder(rentalOrderId, admin.getAccountId());
            System.out.println("Rental order " + rentalOrderId + " approved by admin " + admin.getAccountId());

            // Lấy warehouseID liên kết
            int warehouseId = rentalDAO.getWarehouseIdByRentalOrderId(rentalOrderId);
            System.out.println("WarehouseID found: " + warehouseId);

            if (warehouseId > 0) {
                boolean updated = warehouseDAO.updateStatus(warehouseId, "Rented");
                System.out.println("Warehouse status updated: " + updated);
                if (!updated) {
                    System.err.println("Failed to update warehouse status to 'Rented' for warehouseID: " + warehouseId);
                }
            } else {
                System.err.println("WarehouseID not found for rentalOrderId: " + rentalOrderId);
            }

            resp.sendRedirect(req.getContextPath() + "/admin-approve-list.jsp?success=1");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid rentalOrderId.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error approving rental order", e);
        }
    }
}
