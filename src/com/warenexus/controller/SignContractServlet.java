package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/signContract")
public class SignContractServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            rentalOrderDAO.updateStatus(rentalOrderId, "Pending");

            // Xoá session tránh truy cập lại
            req.getSession().removeAttribute("paidRentalOrderId");

            req.setAttribute("message", "Ký hợp đồng thành công. Vui lòng chờ quản trị viên duyệt.");
            req.getRequestDispatcher("userhome.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Lỗi khi ký hợp đồng", e);
        }
    }
}
