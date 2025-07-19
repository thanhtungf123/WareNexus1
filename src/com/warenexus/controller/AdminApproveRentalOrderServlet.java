package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/approve-rental")
public class AdminApproveRentalOrderServlet extends HttpServlet {
    private final RentalOrderDAO dao = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }

            Account staff = (Account) session.getAttribute("user");

            // Kiểm tra quyền Admin (RoleID = 1)
            if (staff.getRoleId() != 1) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to perform this action.");
                return;
            }

            // Lấy ID đơn thuê từ request
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));

            // Duyệt đơn thuê
            dao.approveRentalOrder(rentalOrderId, staff.getAccountId());

            // Chuyển hướng sau khi duyệt thành công
            resp.sendRedirect(req.getContextPath() + "/admin-approve-list.jsp?success=1");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid rentalOrderId.");
        } catch (Exception e) {
            throw new ServletException("Error approving rental order", e);
        }
    }
}
