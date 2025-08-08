package com.warenexus.controller;

import com.warenexus.dao.*;
import com.warenexus.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * /rental-history  —  shows customer’s rental list (history.jsp)
 * DEBUG statements are included; remove them when done.
 */
@WebServlet("/rental-history")
public class CustomerRentalHistoryServlet extends HttpServlet {

    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final PaymentDAO     paymentDAO     = new PaymentDAO();
    private final WarehouseDAO   warehouseDAO   = new WarehouseDAO();
    private final ContractDAO    contractDAO    = new ContractDAO();   // ⭐ NEW

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            /* ---------- auth ---------- */
            HttpSession session = req.getSession(false);
            Account acc = (Account) session.getAttribute("acc");   // keep “acc” (your code uses it)
            if (acc == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            int accountId = acc.getAccountId();

            /* ---------- rentals ---------- */
            List<RentalOrder> orders =
                    rentalOrderDAO.getAllOrdersByAccount(accountId);
            System.out.println("[DEBUG] Rental orders fetched = " + orders.size());

            /* ---------- helpers for JSP ---------- */
            Map<Integer, Warehouse> warehouseMap   = new HashMap<>();
            Map<Integer, Boolean>   isDepositPaidMap = new HashMap<>();
            Map<Integer, Boolean>   isFinalPaidMap   = new HashMap<>();
            Map<Integer, Long>      daysLeftMap      = new HashMap<>();
            Map<Integer, Boolean>   isDueSoonMap     = new HashMap<>();
            Map<Integer, Contract>  contractMap      = new HashMap<>();   // ⭐ NEW

            LocalDate today = LocalDate.now();

            for (RentalOrder order : orders) {
                int id = order.getRentalOrderID();

                /* warehouse */
                Warehouse w = warehouseDAO.getById(order.getWarehouseID());
                warehouseMap.put(id, w);

                /* deposit paid? */
                boolean depositPaid = order.isDepositPaid();
                isDepositPaidMap.put(id, depositPaid);

                /* final payment? */
                boolean finalPaid = paymentDAO.hasFinalPayment(id);
                isFinalPaidMap.put(id, finalPaid);

                /* days left */
                LocalDate end = new java.sql.Date(
                        order.getEndDate().getTime()).toLocalDate();
                long daysLeft = ChronoUnit.DAYS.between(today, end);
                daysLeftMap.put(id, daysLeft);

                boolean dueSoon = !finalPaid && daysLeft <= 14 && daysLeft >= 0;
                isDueSoonMap.put(id, dueSoon);

                /* contract (may be null) */
                Contract c = contractDAO.getByRentalOrderId(id);
                if (c != null) contractMap.put(id, c);
            }

            
            
            
            
            
            
            
            
            

            
            
            
            System.out.println("[DEBUG] contractMap size      = " + contractMap.size());
            System.out.println("[DEBUG] contractMap keys      = " + contractMap.keySet());

            /* ---------- push to JSP ---------- */
            req.setAttribute("orders",            orders);
            req.setAttribute("warehouseMap",      warehouseMap);
            req.setAttribute("isDepositPaidMap",  isDepositPaidMap);
            req.setAttribute("isFinalPaidMap",    isFinalPaidMap);
            req.setAttribute("daysLeftMap",       daysLeftMap);
            req.setAttribute("isDueSoonMap",      isDueSoonMap);
            req.setAttribute("contractMap",       contractMap);     // ⭐ NEW

            req.getRequestDispatcher("history.jsp").forward(req, resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error",
                    "Không thể lấy dữ liệu lịch sử thuê.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}
