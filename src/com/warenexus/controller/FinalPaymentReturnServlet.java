package com.warenexus.controller;

import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.PaymentCategoryDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Payment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/final-payment-return")
public class FinalPaymentReturnServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final PaymentCategoryDAO categoryDAO = new PaymentCategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rentalOrderIdRaw = req.getParameter("rentalOrderId");
        String status = req.getParameter("status");

        if (rentalOrderIdRaw == null || status == null || !status.equalsIgnoreCase("PAID")) {
            // Thanh toán thất bại hoặc bị hủy
            resp.sendRedirect("final-payment-return.jsp?status=FAILED");
            return;
        }

        try {
            int rentalOrderId = Integer.parseInt(rentalOrderIdRaw);
            int finalPaymentCategoryId = categoryDAO.getCategoryIdByName("FinalPayment");

            // Lấy tất cả payment đang chờ xác nhận cho đơn thuê này
            List<Payment> pendingPayments = paymentDAO.getPendingPaymentsByRentalOrderAndCategory(rentalOrderId, finalPaymentCategoryId);

            for (Payment p : pendingPayments) {
                paymentDAO.updateStatus(p.getPaymentID(), "Completed");
                
            }
            RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
            rentalOrderDAO.updateStatus(rentalOrderId, "Approved");

            resp.sendRedirect("final-payment-return.jsp?status=PAID&rentalOrderId=" + rentalOrderId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("final-payment-return.jsp?status=FAILED");
        }
    }
}
