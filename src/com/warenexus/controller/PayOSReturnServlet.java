package com.warenexus.controller;

import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/payos-return")
public class PayOSReturnServlet extends HttpServlet {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            String status = req.getParameter("status");

            if ("PAID".equalsIgnoreCase(status)) {
                paymentDAO.updateStatusByRentalOrderId(rentalOrderId, "Completed"); // đúng enum status
                rentalOrderDAO.markDepositPaid(rentalOrderId);
                req.getSession().setAttribute("paidRentalOrderId", rentalOrderId); // cho signContract.jsp
            }

            req.setAttribute("rentalOrderId", rentalOrderId);
            req.setAttribute("status", status);
            req.getRequestDispatcher("payos_return.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Lỗi xử lý phản hồi từ PayOS", e);
        }
    }
}
