package com.warenexus.controller;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.RentalOrder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/create-rental-order")
public class CreateRentalOrderServlet extends HttpServlet {
    private final RentalOrderDAO dao = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy từ session
            HttpSession session = req.getSession();
            com.warenexus.model.Account acc = (com.warenexus.model.Account) session.getAttribute("acc");
            String redirectURL = req.getParameter("currentURL");
            if (acc == null) {
                session.setAttribute("redirectAfterLogin", redirectURL);
                resp.sendRedirect("login.jsp");
                return;
            }

            int accountId = acc.getAccountId();

            // Lấy tham số
            String whIdParam = req.getParameter("warehouseId");
            String statusWarehouse = req.getParameter("status");
            String startParam = req.getParameter("startDate");
            String endParam = req.getParameter("endDate");

            if (StringUtils.isEmpty(whIdParam) || StringUtils.isEmpty(startParam) || StringUtils.isEmpty(endParam)) {
                throw new ServletException("Thiếu thông tin đơn thuê");
            }

            int warehouseId = Integer.parseInt(whIdParam);
            Date startDate = Date.valueOf(startParam);
            Date endDate = Date.valueOf(endParam);

            // Tạo đơn thuê mới
            RentalOrder ro = new RentalOrder();
            ro.setAccountID(accountId);
            ro.setWarehouseID(warehouseId);
            ro.setStartDate(startDate);
            ro.setEndDate(endDate);
            ro.setStatus("Pending");

            int rentalOrderId = dao.insert(ro);

            // Truyền cả rentalOrderId và warehouseId sang JSP
            req.setAttribute("rentalOrderId", rentalOrderId);
            req.setAttribute("warehouseId", warehouseId);
            req.getRequestDispatcher("rentForm.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Không thể tạo đơn thuê", e);
        }
    }
}

