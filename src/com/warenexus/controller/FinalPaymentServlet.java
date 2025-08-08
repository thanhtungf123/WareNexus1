package com.warenexus.controller;

import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.PaymentCategoryDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Account;
import com.warenexus.model.Payment;
import com.warenexus.model.RentalOrder;
import com.warenexus.util.PayOSUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

@WebServlet("/final-payment")
public class FinalPaymentServlet extends HttpServlet {

    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final PaymentCategoryDAO paymentCategoryDAO = new PaymentCategoryDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            Account acc = (Account) session.getAttribute("acc");
            if (acc == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            double amount = Double.parseDouble(req.getParameter("amount"));

            RentalOrder rentalOrder = rentalOrderDAO.getRentalOrderById(rentalOrderId);
            if (rentalOrder == null || rentalOrder.getAccountID() != acc.getAccountId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện hành động này.");
                return;
            }

            String description = "Thanh toán tiền tổng cho đơn thuê #" + rentalOrderId;
            String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
            String returnUrl = baseUrl + "/final-payment-return?rentalOrderId=" + rentalOrderId + "&status=PAID";

            JSONObject payosResponse = PayOSUtil.createPaymentRequest(amount, description, returnUrl);
            String qrUrl = payosResponse.getString("qrCode");
            String paymentLink = payosResponse.getString("paymentLink");
            String orderCode = payosResponse.get("orderCode").toString();

            // Lấy PaymentCategoryID cho 'FinalPayment'
            int finalPaymentCategoryId = paymentCategoryDAO.getCategoryIdByName("FinalPayment");

            Payment payment = new Payment();
            payment.setRentalOrderID(rentalOrderId);
            payment.setAmount(amount);
            payment.setStatus("Pending");
            payment.setTransactionID(orderCode);
            payment.setPaymentCategoryID(finalPaymentCategoryId);
            payment.setPaymentDate(new Date());

            paymentDAO.insert(payment);

            req.setAttribute("qrUrl", qrUrl);
            req.setAttribute("paymentLink", paymentLink);
            req.setAttribute("rentalOrderId", rentalOrderId);
            req.getRequestDispatcher("paymentQR.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi khi tạo thanh toán.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}
