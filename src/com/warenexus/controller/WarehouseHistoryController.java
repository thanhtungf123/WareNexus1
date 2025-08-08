package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.ContractDAO;
import com.warenexus.model.Account;
import com.warenexus.model.RentalOrder;
import com.warenexus.model.Contract;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

/**
 * Shows the list of warehouses a customer has rented,
 * now enriched with contract information.
 */
@WebServlet("/warehouse-history")
public class WarehouseHistoryController extends HttpServlet {

    private final RentalOrderDAO  rentalOrderDAO  = new RentalOrderDAO();
    private final ContractDAO     contractDAO     = new ContractDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Account user = (Account) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            /* ------------------- load rentals ------------------- */
            List<RentalOrder> history =
                    rentalOrderDAO.getRentalOrderByAccountID(user.getAccountId());

            /* ------------------- load contracts ----------------- */
            Map<Integer, Contract> contractMap = new HashMap<>();
            for (RentalOrder ro : history) {
                Contract c = contractDAO.getByRentalOrderId(ro.getRentalOrderID());
                if (c != null) contractMap.put(ro.getRentalOrderID(), c);
            }

            /* ------------------- forward to JSP ----------------- */
            request.setAttribute("rentalHistory", history);
            request.setAttribute("contractMap", contractMap);
            request.getRequestDispatcher("warehouse-history.jsp")
                   .forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
