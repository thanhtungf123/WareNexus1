package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.RentalOrderFullInfo;
import com.warenexus.util.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/send-reminder")
public class SendReminderServlet extends HttpServlet {
    private final RentalOrderDAO rentalDAO = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int rentalOrderId = Integer.parseInt(req.getParameter("id"));

            RentalOrderFullInfo info = rentalDAO.getRentalOrderFullInfoById(rentalOrderId);
            if (info != null && !info.isFinalPaid()) {
                EmailSender.sendReminderEmail(
                        info.getCustomerEmail(),
                        info.getCustomerName(),
                        info.getWarehouseName(),
                        info.getDaysUntilFinalPaymentDue()
                );
                req.getSession().setAttribute("success", "Đã gửi email nhắc nhở tới khách hàng.");
            } else {
                req.getSession().setAttribute("error", "Không tìm thấy đơn thuê hoặc đơn đã thanh toán.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Lỗi khi gửi email nhắc nhở.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin-rental-manage");
    }
}
