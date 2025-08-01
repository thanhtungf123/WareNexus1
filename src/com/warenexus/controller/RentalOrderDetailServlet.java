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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@WebServlet("/rental-detail")
public class RentalOrderDetailServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            Account acc = (session != null) ? (Account) session.getAttribute("acc") : null;

            if (acc == null || acc.getRoleId() != 3) {
                resp.sendRedirect("login.jsp");
                return;
            }

            String param = req.getParameter("rentalOrderId");
            if (param == null || param.trim().isEmpty()) {
                throw new IllegalArgumentException("Missing rentalOrderId parameter");
            }

            int rentalOrderId = Integer.parseInt(param);
            RentalOrder rental = rentalOrderDAO.getRentalOrderById(rentalOrderId);

            if (rental == null) {
                throw new IllegalArgumentException("Rental order not found for ID: " + rentalOrderId);
            }

            if (rental.getAccountID() != acc.getAccountId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this rental order.");
                return;
            }

            Warehouse warehouse = warehouseDAO.getById(rental.getWarehouseID());
            if (warehouse == null) {
                throw new IllegalArgumentException("Warehouse not found for rental order.");
            }

            boolean isFinalPaid = paymentDAO.hasFinalPayment(rentalOrderId);
            long daysLeft = paymentDAO.getDaysUntilEndDate(rentalOrderId);

            // ➕ Tính ngày đến hạn thanh toán tổng (14 ngày từ ngày bắt đầu)
            Date start = rental.getStartDate();
            LocalDate startDate = LocalDate.of(
                start.getYear() + 1900,
                start.getMonth() + 1,
                start.getDate()
            );
            LocalDate today = LocalDate.now();
            LocalDate dueFinalPaymentDate = startDate.plusDays(14);
            long daysUntilFinalDue = ChronoUnit.DAYS.between(today, dueFinalPaymentDate);


            req.setAttribute("rental", rental);
            req.setAttribute("warehouse", warehouse);
            req.setAttribute("isFinalPaid", isFinalPaid);
            req.setAttribute("daysLeft", daysLeft);
            req.setAttribute("daysUntilFinalDue", daysUntilFinalDue);

            req.getRequestDispatcher("rental-detail.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred while retrieving rental detail: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}
