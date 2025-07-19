package com.warenexus.controller;

import com.warenexus.dao.SupportTicketDAO;
import com.warenexus.model.SupportTicket;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SupportTicketServlet extends HttpServlet {
    private final SupportTicketDAO dao = new SupportTicketDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roid = req.getParameter("rentalOrderID");
        try {
            int rentalOrderID = Integer.parseInt(roid);
            List<SupportTicket> tickets = dao.getByRentalOrderID(rentalOrderID);
            req.setAttribute("tickets", tickets);
            req.getRequestDispatcher("support.jsp").forward(req, resp);
        } catch (Exception ex) {
            resp.sendError(400, "Invalid request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int rentalOrderID = Integer.parseInt(req.getParameter("rentalOrderID"));
            String title = req.getParameter("title");
            String description = req.getParameter("description");

            SupportTicket ticket = new SupportTicket();
            ticket.setRentalOrderID(rentalOrderID);
            ticket.setIssueTitle(title);
            ticket.setIssueDescription(description);
            ticket.setStatus("Open");
            ticket.setCreatedAt(new Date());

            dao.insert(ticket);
            resp.sendRedirect("support.jsp");
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendError(500, "Error creating ticket");
        }
    }
}
