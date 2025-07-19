package com.warenexus.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import com.warenexus.dao.AccountDAO;
import com.warenexus.model.Account;

public class SignupServlet extends HttpServlet {

  private final AccountDAO userDao = new AccountDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/signup.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String email = req.getParameter("email");
    String pass  = req.getParameter("password");
    String confirm = req.getParameter("confirm");

    if (!pass.equals(confirm)) {
      req.setAttribute("error", "Passwords do not match");
      doGet(req, resp);
      return;
    }

    try {
      if (userDao.existsByEmail(email)) {
        req.setAttribute("error", "Email already registered");
        doGet(req, resp);
        return;
      }

      Account u = new Account();
      u.setEmail(email);
      u.setPassword(pass); // nếu cần mã hoá thì tự xử lý trước
      u.setIsActive(true);

      userDao.insert(u);

      resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=1");

    } catch (Exception e) {
      e.printStackTrace();
      req.setAttribute("error", "Internal error, please try again");
      doGet(req, resp);
    }
  }
}
