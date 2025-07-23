package com.warenexus.controller;

import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Payment;
import com.warenexus.util.PayOSUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONObject;
import vn.payos.PayOS;

import java.io.IOException;
import java.util.Date;

@WebServlet("/payos-payment")
public class PayOSPaymentServlet extends HttpServlet {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private static final String CLIENT_ID = "07a0181f-43af-43a1-b7f2-03fca43564f3";  // Thay bằng Client ID của bạn
    private static final String API_KEY = "1ae5fc7b-cbd2-4372-86e4-0d8e3e225aee";  // Thay bằng API Key của bạn
    private static final String CHECKSUM_KEY = "9eae37593e8d10a6eac7c93543c5c07c8671abbb0279f4b27e37f972c174e0e3";  // Thay bằng Checksum Key của bạn
    // Khởi tạo đối tượng PayOS
    private static PayOS payOS;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 1. Lấy tham số từ form
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            double deposit = Double.parseDouble(req.getParameter("deposit"));
            double totalPrice = Double.parseDouble(req.getParameter("totalPrice"));
            // Phương thức khởi tạo PayOS

            if (payOS == null) {
                payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
            }
            // 2. Cập nhật Deposit và TotalPrice vào DB
            rentalOrderDAO.updatePriceInfo(rentalOrderId, deposit, totalPrice);

            // 3. Tạo request tới PayOS (giả lập hoặc thật)
            String description = "Deposit payment for rental order #" + rentalOrderId;
            String returnUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                    + req.getContextPath() + "/payos-return?rentalOrderId=" + rentalOrderId + "&status=PAID";

            JSONObject response = PayOSUtil.createPaymentRequest(deposit, description, returnUrl);
            JSONObject data = response.getJSONObject("data");
            long orderCode = response.getLong("orderCode");

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
            req.setAttribute("qrUrl", data.getString("qrCode"));
            req.setAttribute("paymentLink", data.getString("paymentLink"));
            req.setAttribute("rentalOrderId", rentalOrderId);
            req.getRequestDispatcher("paymentQR.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Payment initialization failed: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}
