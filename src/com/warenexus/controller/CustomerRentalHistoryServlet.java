package com.warenexus.controller;

import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Account;
import com.warenexus.model.RentalOrder;
import com.warenexus.model.Warehouse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebServlet("/rental-history")
public class CustomerRentalHistoryServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy session và kiểm tra đăng nhập
            HttpSession session = req.getSession(false);
            Account acc = (Account) session.getAttribute("acc");

            if (acc == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            int accountId = acc.getAccountId();

            // Lấy danh sách đơn thuê đã được duyệt của người dùng
            List<RentalOrder> orders = rentalOrderDAO.getAllOrdersByAccount(accountId);

            // Các map hỗ trợ render JSP
            Map<Integer, Warehouse> warehouseMap = new HashMap<>();
            Map<Integer, Boolean> isDepositPaidMap = new HashMap<>();
            Map<Integer, Boolean> isFinalPaidMap = new HashMap<>();
            Map<Integer, Long> daysLeftMap = new HashMap<>();
            Map<Integer, Boolean> isDueSoonMap = new HashMap<>();

            LocalDate today = LocalDate.now();

            for (RentalOrder order : orders) {
                int rentalOrderId = order.getRentalOrderID();

                // Lấy kho liên quan
                Warehouse warehouse = warehouseDAO.getById(order.getWarehouseID());
                warehouseMap.put(rentalOrderId, warehouse);
                
                // Tiền cọc đã thanh toán chưa
                boolean isDepositPaid = order.isDepositPaid();  // field này phải có trong RentalOrder
                isDepositPaidMap.put(rentalOrderId, isDepositPaid);

                // Kiểm tra đã thanh toán tiền tổng chưa
                boolean isFinalPaid = paymentDAO.hasFinalPayment(rentalOrderId);
                isFinalPaidMap.put(rentalOrderId, isFinalPaid);

                // Tính số ngày còn lại đến ngày kết thúc
                LocalDate endDate = new java.sql.Date(order.getEndDate().getTime()).toLocalDate();
                long daysLeft = ChronoUnit.DAYS.between(today, endDate);
                daysLeftMap.put(rentalOrderId, daysLeft);

                // Đánh dấu nếu đơn đó đến hạn thanh toán (≤ 14 ngày và chưa thanh toán)
                boolean isDueSoon = !isFinalPaid && daysLeft <= 14 && daysLeft >= 0;
                isDueSoonMap.put(rentalOrderId, isDueSoon);
            }

            // Gửi dữ liệu đến JSP
            req.setAttribute("orders", orders);
            req.setAttribute("warehouseMap", warehouseMap);
            req.setAttribute("isDepositPaidMap", isDepositPaidMap);
            req.setAttribute("isFinalPaidMap", isFinalPaidMap);
            req.setAttribute("daysLeftMap", daysLeftMap);
            req.setAttribute("isDueSoonMap", isDueSoonMap);

            req.getRequestDispatcher("history.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Không thể lấy dữ liệu lịch sử thuê.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}
