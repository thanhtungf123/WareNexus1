package com.warenexus.controller;

import com.warenexus.dao.*;
import com.warenexus.model.*;
import com.warenexus.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;

/**
 * GET /download-contract-email?orderId=123
 *  • streams the PDF if it’s still on disk
 *  • otherwise regenerates a bare contract and e-mails it to the user
 */
@WebServlet("/download-contract-email")
public class DownloadContractServlet extends HttpServlet {

    private final ContractDAO    contractDAO    = new ContractDAO();
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final CustomerDAO    customerDAO    = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        HttpSession session = req.getSession(false);
        Account acc = (Account) session.getAttribute("acc");
        if (acc == null) { resp.sendRedirect("login.jsp"); return; }

        /* fetch contract row (may be null) */
        Contract ct;
        try { ct = contractDAO.getByRentalOrderId(orderId); }
        catch (Exception e) { throw new ServletException(e); }

        /* fallback pdf path */
        String pdfPath = (ct != null)
                ? ct.getPdfPath()
                : "C:/contracts/contract_" + orderId + ".pdf";

        Path pdf = Paths.get(pdfPath);

        /* --------------------------------------------------------- */
        /*  CASE 1: file exists ➜ stream attachment                  */
        /* --------------------------------------------------------- */
        if (Files.exists(pdf)) {
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition",
                    "attachment; filename=\"contract-" + orderId + ".pdf\"");
            Files.copy(pdf, resp.getOutputStream());
            return;
        }

        /* --------------------------------------------------------- */
        /*  CASE 2: file missing ➜ regenerate + email               */
        /* --------------------------------------------------------- */
        try {
            RentalOrder ro = rentalOrderDAO.getRentalOrderById(orderId);
            Customer    cu = customerDAO.getByAccountId(acc.getAccountId());

            /* Quick-and-dirty regenerate without signature */
            String fontPath = getServletContext().getRealPath("/fonts/arial.ttf");
            PDFGenerator.createPDFContract(orderId, null/*no sig*/,
                    acc.getEmail(), cu, ro, fontPath);

            /* Send via EmailSender */
            EmailSender.sendContractPDF(acc.getEmail(), pdf.toFile(), orderId);

            req.setAttribute("orderId", orderId);
            req.getRequestDispatcher("contract-resent.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Could not regenerate or email PDF", e);
        }
    }
}
