package com.warenexus.controller;

import com.warenexus.dao.UserDAO;
import com.warenexus.model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles login (POST) and logout (GET).
 */
public class AuthController extends HttpServlet {

    private UserDAO userDao;

    @Override
    public void init() {
        userDao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
               .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Account u = userDao.findByEmail(email);

            boolean ok = (u != null && password.equals(u.getPassword()));

            if (ok) {
                req.getSession().setAttribute("user", u);

                String role = userDao.getUserRole(u.getAccountId());

                String dest;
                switch (role) {
                    case "Admin":
                        dest = "/admin/dashboard.jsp";
                        break;
                    case "Staff":
                        dest = "/employee.jsp";
                        break;
                    default:
                        dest = "/main.jsp";
                        break;
                }

                resp.sendRedirect(req.getContextPath() + dest);
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error.");
        }

        resp.sendRedirect(req.getContextPath() + "/login?error=Invalid%20credentials");
    }
}
