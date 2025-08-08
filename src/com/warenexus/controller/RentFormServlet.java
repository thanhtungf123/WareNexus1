package com.warenexus.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/rent-form")
public class RentFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin kho từ tham số truyền vào (từ warehouseDetail.jsp)
            String warehouseId = request.getParameter("warehouseId");
            String warehouseName = request.getParameter("warehouseName");

            if (warehouseId == null || warehouseName == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing warehouse information.");
                return;
            }

            // Truyền thông tin sang JSP
            request.setAttribute("warehouseId", warehouseId);
            request.setAttribute("warehouseName", warehouseName);

            request.getRequestDispatcher("rentForm.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error loading rent form", e);
        }
    }
}
