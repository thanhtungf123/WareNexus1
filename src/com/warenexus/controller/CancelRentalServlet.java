package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.CustomerDAO;
import com.warenexus.model.Customer;
import com.warenexus.model.RentalOrder;
import com.warenexus.util.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/cancel-rental")
public class CancelRentalServlet extends HttpServlet {

    private final RentalOrderDAO rentalDAO = new RentalOrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));

        try {
            RentalOrder order = rentalDAO.getRentalOrderById(rentalOrderId);
            if (order == null || !"Approved".equals(order.getStatus())) {
                req.getSession().setAttribute("error", "Không thể hủy đơn thuê không hợp lệ.");
                resp.sendRedirect("admin-rental-manage.jsp");
                return;
            }

            // Cập nhật trạng thái đơn thuê thành "Cancelled"
            rentalDAO.updateStatus(rentalOrderId, "Cancelled");

            // Gửi email thông báo hủy
            Customer customer = customerDAO.getByAccountId(order.getAccountID());
            if (customer != null) {
                EmailSender.sendCancelEmail(customer.getEmail());
            }

            req.getSession().setAttribute("message", "Đã hủy đơn thuê thành công.");
            resp.sendRedirect("admin-rental-manage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Có lỗi xảy ra khi hủy đơn.");
            resp.sendRedirect("admin-rental-manage.jsp");
        }
    }
}
