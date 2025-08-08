package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Account;
import com.warenexus.model.RentalOrder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/userhome")
public class UserHomeServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<RentalOrder> unnotifiedOrders = rentalOrderDAO.getUnnotifiedApprovedOrders(user.getAccountId());
            session.setAttribute("unnotifiedOrders", unnotifiedOrders);

            request.getRequestDispatcher("userhome.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
