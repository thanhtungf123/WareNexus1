package com.warenexus.controller;

import com.warenexus.dao.CustomerDAO;
import com.warenexus.dao.PaymentDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Account;
import com.warenexus.model.Customer;
import com.warenexus.model.RentalOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/admin-rental-manage")
    public class AdminRentalManageServlet extends HttpServlet {
        private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
        private final CustomerDAO customerDAO = new CustomerDAO();
        private final PaymentDAO paymentDAO = new PaymentDAO();

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            List<RentalOrder> orders = new ArrayList<>();
            Map<Integer, Customer> customerMap = new HashMap<>();
            Map<Integer, Boolean> finalPaymentMap = new HashMap<>();

            try {
                HttpSession session = req.getSession(false);
                Account acc = (Account) session.getAttribute("user");
                if (acc == null || acc.getRoleId() != 1) {
                    resp.sendRedirect("login.jsp");
                    return;
                }

                // ✅ LẤY TOÀN BỘ ĐƠN ĐƯỢC DUYỆT
                orders = rentalOrderDAO.getAllApprovedOrders();

                for (RentalOrder ro : orders) {
                    Customer customer = customerDAO.getByAccountId(ro.getAccountID());
                    customerMap.put(ro.getRentalOrderID(), customer);

                    boolean hasFinalPayment = paymentDAO.hasFinalPayment(ro.getRentalOrderID());
                    finalPaymentMap.put(ro.getRentalOrderID(), hasFinalPayment);
                }

            } catch (Exception e) {
                req.setAttribute("error", "Không thể tải danh sách đơn thuê.");
                e.printStackTrace();
            }

            req.setAttribute("orders", orders);
            req.setAttribute("customers", customerMap);
            req.setAttribute("finalPayments", finalPaymentMap);
            req.getRequestDispatcher("admin/admin-rental-manage.jsp").forward(req, resp);
        }
    }
