package com.warenexus.controller;

import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Warehouse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin-warehouse")
public class AdminWarehouseServlet extends HttpServlet {
    private final WarehouseDAO dao = new WarehouseDAO();

     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Warehouse> list = dao.getAll();
            if (list != null) {
                req.setAttribute("warehouses", list);
            } else {
                req.setAttribute("warehouses", new ArrayList<Warehouse>());
            }
            req.getRequestDispatcher("admin-warehouse-view.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load warehouse list.");
            req.getRequestDispatcher("admin-warehouse-view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("warehouseId");

        try {
            if ("create".equals(action)) {
                Warehouse w = extractFromRequest(req);
                dao.insert(w);
            } else if ("update".equals(action)) {
                Warehouse w = extractFromRequest(req);
                w.setId(Integer.parseInt(idParam));
                dao.update(w);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(idParam);
                dao.delete(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect("admin-warehouse");
    }

    private Warehouse extractFromRequest(HttpServletRequest req) {
        Warehouse w = new Warehouse();
        w.setWarehouseTypeId(Integer.parseInt(req.getParameter("typeId")));
        w.setName(req.getParameter("name"));
        w.setAddress(req.getParameter("address"));
        w.setWard(req.getParameter("ward"));
        w.setDistrict(req.getParameter("district"));
        w.setSize(Double.parseDouble(req.getParameter("size")));
        w.setPricePerUnit(Double.parseDouble(req.getParameter("price")));
        w.setStatus(req.getParameter("status"));
        w.setDescription(req.getParameter("description"));
        w.setLatitude(req.getParameter("latitude") != null && !req.getParameter("latitude").isBlank()
                      ? Double.parseDouble(req.getParameter("latitude")) : null);
        w.setLongitude(req.getParameter("longitude") != null && !req.getParameter("longitude").isBlank()
                      ? Double.parseDouble(req.getParameter("longitude")) : null);
        return w;
    }
}
