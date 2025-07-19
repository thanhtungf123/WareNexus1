package com.warenexus.controller;

import com.warenexus.dao.ReviewDAO;
import com.warenexus.model.Review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ReviewServlet extends HttpServlet {
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String wid = req.getParameter("warehouseID");
        try {
            int warehouseID = Integer.parseInt(wid);
            List<Review> reviews = reviewDAO.getReviewsByWarehouse(warehouseID);
            req.setAttribute("reviews", reviews);
            req.getRequestDispatcher("review.jsp").forward(req, resp);  // JSP bạn tự cấu hình
        } catch (Exception ex) {
            resp.sendError(400, "Invalid warehouseID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int rentalOrderID = Integer.parseInt(req.getParameter("rentalOrderID"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            Review review = new Review();
            review.setRentalOrderID(rentalOrderID);
            review.setRating(rating);
            review.setComment(comment);
            review.setReviewDate(new Date());

            reviewDAO.insert(review);
            resp.sendRedirect("success.jsp");  // hoặc reload lại trang chi tiết warehouse
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendError(500, "Failed to save review");
        }
    }
}
