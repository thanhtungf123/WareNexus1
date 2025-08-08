package com.warenexus.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.warenexus.model.Account;

/**
 * Authentication filter: redirects anonymous users to /login.
 * Mappings are declared in web.xml, so no @WebFilter annotation here.
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest sReq, ServletResponse sRes, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest  req  = (HttpServletRequest) sReq;
        HttpServletResponse resp = (HttpServletResponse) sRes;

        Account user = (Account) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String path = req.getRequestURI();
        if (path.startsWith(req.getContextPath() + "/admin") && user.getRoleId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        chain.doFilter(req, resp);
    }
}
