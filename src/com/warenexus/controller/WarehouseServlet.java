package com.warenexus.controller;

import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Warehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class WarehouseServlet extends HttpServlet {

    private WarehouseDAO dao;
    @Override public void init() { dao = new WarehouseDAO(); }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /* ---------------- detail page branch ---------------- */
        String id = req.getParameter("id");
        if (id != null) {
            try {
                int warehouseId = Integer.parseInt(id);
                Warehouse warehouse = dao.getById(warehouseId);

                if (warehouse != null) {
                    HttpSession session = req.getSession(false);
                    String role = (session != null)
                            ? String.valueOf((Integer) session.getAttribute("role"))
                            : null;
                    if (("1".equals(role) || "2".equals(role))) {
                        resp.sendRedirect(req.getContextPath() + "/admin-warehouse-detail?id=" + warehouseId);
                        return;
                    }
                    req.setAttribute("warehouse", warehouse);
                    req.getRequestDispatcher("warehouseDetail.jsp").forward(req, resp);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        /* ---------------- search (shared for HTML & JSON) ---------------- */
        List<Warehouse> list = dao.search(
            req.getParameter("keyword"),
            parseInt(req.getParameter("typeId")),
            empty(req.getParameter("status")),
            parseD(req.getParameter("sizeMin")),
            parseD(req.getParameter("sizeMax")),
            parseD(req.getParameter("priceMin")),
            parseD(req.getParameter("priceMax")),
            empty(req.getParameter("ward")),
            empty(req.getParameter("district"))
        );

        /* ---------------- JSON branch for map ---------------- */
        boolean wantsJson = "1".equals(req.getParameter("json")) ||
                            "application/json".equals(req.getHeader("Accept"));
        if (wantsJson) {
            JSONArray arr = new JSONArray();
            for (Warehouse w : list) {
                if (w.getLatitude() == null || w.getLongitude() == null) continue;
                arr.put(new JSONObject()
                    .put("id", w.getId())
                    .put("name", w.getName())
                    .put("address", w.getAddress())
                    .put("ward", w.getWard())
                    .put("district", w.getDistrict())
                    .put("latitude", w.getLatitude())
                    .put("longitude", w.getLongitude()));
            }
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(arr.toString());            // servlet JSON pattern :contentReference[oaicite:7]{index=7}
            return;
        }

        /* ---------------- regular HTML page ---------------- */
        req.setAttribute("warehouses", list);
        req.getRequestDispatcher("warehouseSearch.jsp").forward(req, resp);
    }

    /* helpers */
    private Integer parseInt(String s){ try{ return (s==null||s.isBlank())?null:Integer.parseInt(s);}catch(NumberFormatException e){return null;}}
    private Double  parseD  (String s){ try{ return (s==null||s.isBlank())?null:Double.parseDouble(s);}catch(NumberFormatException e){return null;}}
    private String  empty   (String s){ return (s==null||s.isBlank())?null:s; }
}
