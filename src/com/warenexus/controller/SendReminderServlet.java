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

@WebServlet("/send-reminder")
public class SendReminderServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));

            RentalOrder rental = rentalOrderDAO.getRentalOrderById(rentalOrderId);
            if (rental == null) {
                req.getSession().setAttribute("error", "Không tìm thấy đơn thuê.");
                resp.sendRedirect("admin-rental-manage");
                return;
            }

            Customer customer = customerDAO.getByAccountId(rental.getAccountID());
            if (customer == null) {
                req.getSession().setAttribute("error", "Không tìm thấy thông tin khách hàng.");
                resp.sendRedirect("admin-rental-manage");
                return;
            }

            // Gửi email
            EmailSender.sendReminderEmail(customer.getEmail());

            // Đánh dấu đã gửi nhắc nhở
            rentalOrderDAO.markNotificationAsSent(rentalOrderId);

            req.getSession().setAttribute("message", "Đã gửi email nhắc nhở thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Có lỗi xảy ra khi gửi email.");
        }
        resp.sendRedirect("admin-rental-manage");
    }
}
