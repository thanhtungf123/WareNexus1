package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.RentalOrderFullInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-rental-manage")
public class AdminRentalManageServlet extends HttpServlet {
    private final RentalOrderDAO rentalDAO = new RentalOrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<RentalOrderFullInfo> orders = rentalDAO.getAllApprovedRentalOrdersFullInfo();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/admin-rental-manage.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load approved rentals.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
