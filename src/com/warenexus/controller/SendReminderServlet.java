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

@WebServlet("/admin/send-reminder")
public class SendReminderServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            RentalOrder rental = rentalOrderDAO.getRentalOrderById(rentalOrderId);

            if (rental != null) {
                Customer customer = customerDAO.getByAccountId(rental.getAccountID());
                if (customer != null) {
                    EmailSender.sendReminderEmail(customer.getEmail());
                    rentalOrderDAO.markNotificationAsSent(rentalOrderId);
                    req.getSession().setAttribute("message", "Đã gửi nhắc nhở thành công.");
                } else {
                    req.getSession().setAttribute("error", "Không tìm thấy thông tin khách hàng.");
                }
            } else {
                req.getSession().setAttribute("error", "Không tìm thấy đơn thuê.");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Có lỗi xảy ra khi gửi nhắc nhở.");
            e.printStackTrace();
        }
        resp.sendRedirect("admin-rental-manage.jsp");
    }
}
