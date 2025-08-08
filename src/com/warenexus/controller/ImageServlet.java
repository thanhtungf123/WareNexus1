package com.warenexus.controller;

import com.warenexus.dao.WarehouseDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    WarehouseDAO dao = new WarehouseDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int imageID = Integer.parseInt(request.getParameter("id"));
        byte[] imageData = dao.getImageDataFromDB(imageID); // lấy từ DB

        if (imageData != null) {
            response.setContentType("image/jpeg"); // hoặc image/png
            response.setContentLength(imageData.length);
            OutputStream out = response.getOutputStream();
            out.write(imageData);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

