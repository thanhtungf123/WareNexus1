package com.warenexus.controller;

import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Warehouse;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/warehouses")
public class WarehouseApiServlet extends HttpServlet {

    private final WarehouseDAO dao = new WarehouseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Note: Currently fetches all warehouses. You might want to pass
        // request parameters to the search method in the future.
        List<Warehouse> list =
                dao.search(null, null, null, null, null,
                           null, null, null, null);

        /* Build JSON array — skip rows lacking coords */
        JSONArray arr = new JSONArray();
        for (Warehouse w : list) {
            if (w.getLatitude() == null || w.getLongitude() == null) continue;

            JSONObject j = new JSONObject()
                    .put("id", w.getId())
                    .put("name", w.getName())
                    .put("address", w.getAddress())
                    .put("ward", w.getWard())
                    .put("district", w.getDistrict())
                    .put("latitude", w.getLatitude())
                    .put("longitude", w.getLongitude())
                    // --- ADDED LINES ---
                    .put("size", w.getSize())
                    .put("pricePerUnit", w.getPricePerUnit());
            
            arr.put(j);
        }

        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(arr.toString());
    }
}