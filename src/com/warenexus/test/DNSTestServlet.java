package com.warenexus.test;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

@WebServlet("/dns-test")
public class DNSTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        try (PrintWriter out = resp.getWriter()) {
            InetAddress address = InetAddress.getByName("api.payos.vn");
            out.println("Resolved IP: " + address.getHostAddress());
        } catch (Exception e) {
            // In lỗi chi tiết
            PrintWriter out = resp.getWriter();
            out.println("DNS Resolution Failed");
            e.printStackTrace(out); // In stacktrace vào response luôn
        }
    }
}
