package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Account;
import com.warenexus.model.RentalOrder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/warehouse-history")
public class WarehouseHistoryController extends HttpServlet {
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
            //Lấy danh sách đơn thuê kho của người dùng
            List<RentalOrder> historyList = rentalOrderDAO.getRentalOrderByAccountID(user.getAccountId());

            // Gửi danh sách sang JSP để hiển thị
            request.setAttribute("rentalHistory", historyList);
            request.getRequestDispatcher("warehouse-history.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
