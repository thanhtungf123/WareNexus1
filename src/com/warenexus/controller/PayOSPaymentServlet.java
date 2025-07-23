package com.warenexus.controller;

import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Payment;
import com.warenexus.util.PayOSUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

@WebServlet("/payos-payment")
public class PayOSPaymentServlet extends HttpServlet {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 1. Lấy tham số từ form
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            double deposit = Double.parseDouble(req.getParameter("deposit"));
            double totalPrice = Double.parseDouble(req.getParameter("totalPrice"));

            // 2. Cập nhật Deposit và TotalPrice vào DB
            rentalOrderDAO.updatePriceInfo(rentalOrderId, deposit, totalPrice);

            // 3. Tạo request tới PayOS (giả lập hoặc thật)
            String description = "Deposit payment for rental order #" + rentalOrderId;
            String returnUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                    + req.getContextPath() + "/payos-return?rentalOrderId=" + rentalOrderId + "&status=PAID";

            JSONObject response = PayOSUtil.createPaymentRequest(totalPrice, description, returnUrl);
            long orderCode = response.getLong("orderCode");
            String qrCode = response.getString("qrCode");
            String paymentLink = response.getString("paymentLink");

            // 4. Lưu thông tin giao dịch vào bảng Payment
            Payment payment = new Payment();
            payment.setRentalOrderID(rentalOrderId);
            payment.setAmount(deposit);
            payment.setStatus("Pending"); // Chờ xác nhận từ PayOS
            payment.setTransactionID(String.valueOf(orderCode));
            payment.setPaymentCategoryID(1); // 1 = Deposit
            payment.setPaymentDate(new Date());

            paymentDAO.insert(payment);

            // 5. Gửi thông tin QR sang trang thanh toán
            req.setAttribute("qrUrl", qrCode);
            req.setAttribute("paymentLink", paymentLink);
            req.setAttribute("rentalOrderId", rentalOrderId);
            req.getRequestDispatcher("paymentQR.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Payment initialization failed: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}
