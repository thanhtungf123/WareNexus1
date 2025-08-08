package com.warenexus.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false); // get current session, don't create new
        if (session != null) {
            session.invalidate(); // destroy session
        }
        resp.sendRedirect("login.jsp"); // redirect to login
    }
}
