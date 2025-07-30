package com.warenexus.controller;

import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Warehouse;

import com.warenexus.model.WarehouseImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin-warehouse")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,   // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
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
        if ("create".equals(action)) {
            try (Connection conn = dao.getCon()) {
                conn.setAutoCommit(false); // Bắt đầu transaction
                try {
                    Warehouse w = extractFromRequest(req);
                    int warehouseId = dao.insert(w, conn); // Truyền connection vào

                    Part imagePart = req.getPart("warehouseImage");
                    if (imagePart != null && imagePart.getSize() > 0) {
                        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                        InputStream inputStream = imagePart.getInputStream();
                        byte[] imageBytes = inputStream.readAllBytes();

                        WarehouseImage image = new WarehouseImage();
                        image.setWarehouseID(String.valueOf(warehouseId));
                        image.setImageFileName(fileName);
                        image.setImageData(imageBytes);

                        dao.insertWarehouseImage(image, conn); // Truyền connection vào
                    }
                    conn.commit(); // Thành công, commit
                } catch (Exception e) {
                    conn.rollback(); // Lỗi, rollback
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("update".equals(action)) {
            Warehouse w = extractFromRequest(req);
            w.setId(Integer.parseInt(idParam));
            dao.update(w);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(idParam);
            dao.deleteWarehouseWithImages(id);
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
