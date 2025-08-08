package com.warenexus.controller;

import com.warenexus.dao.ContractDAO;
import com.warenexus.model.Contract;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;

/**
 * Streams the contract PDF inline in the browser.
 */
@WebServlet("/view-contract")
public class ViewContractServlet extends HttpServlet {

    private final ContractDAO dao = new ContractDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));

        Contract contract;
        try {
            contract = dao.getByRentalOrderId(orderId);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if (contract == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /* ---------- DEBUG: log the exact path and existence ---------- */
        String rawPath  = contract.getPdfPath();
        System.out.println("[VIEW] raw path = '" + rawPath + "'");
        Path   pdf      = Paths.get(rawPath == null ? "" : rawPath.trim());
        System.out.println("[VIEW] Files.exists = " + Files.exists(pdf));
        /* ---------------------------------------------------------------- */

        if (!Files.exists(pdf)) {
            /* file gone – tell user and offer e-mail option */
            resp.sendError(HttpServletResponse.SC_GONE,
                    "PDF no longer on server. Click 'Email me PDF' in history to resend.");
            return;
        }

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
                "inline; filename=\"contract-" + orderId + ".pdf\"");

        Files.copy(pdf, resp.getOutputStream());
    }
}
