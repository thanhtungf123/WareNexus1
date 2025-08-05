package com.warenexus.controller;

import com.warenexus.dao.CustomerDAO;
import com.warenexus.dao.RentalOrderDAO;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // ✅ GỌI đúng DAO mới
            List<RentalOrder> orders = rentalOrderDAO.getAllOngoingOrders();

            Map<Integer, Customer> customerMap = new HashMap<>();
            for (RentalOrder ro : orders) {
                Customer customer = customerDAO.getByAccountId(ro.getAccountID());
                customerMap.put(ro.getRentalOrderID(), customer);
            }

            req.setAttribute("orders", orders);
            req.setAttribute("customers", customerMap);
        } catch (Exception e) {
            req.setAttribute("error", "Không thể tải danh sách đơn thuê.");
            e.printStackTrace();
        }
        req.getRequestDispatcher("admin/admin-rental-manage.jsp").forward(req, resp);
    }
}
