package com.warenexus.controller;

import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.RentalOrderFullInfo;
import com.warenexus.util.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/cancel-rental")
public class CancelRentalServlet extends HttpServlet {
    private final RentalOrderDAO rentalDAO = new RentalOrderDAO();
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int rentalOrderId = Integer.parseInt(req.getParameter("rentalOrderId"));
            RentalOrderFullInfo order = rentalDAO.getRentalOrderFullInfoById(rentalOrderId);

            if (order == null) {
                req.getSession().setAttribute("error", "Không tìm thấy đơn thuê.");
                resp.sendRedirect("admin-rental-manage");
                return;
            }

            // Đơn đã thanh toán toàn bộ -> Không hủy
            if (order.isFinalPaid()) {
                req.getSession().setAttribute("error",
                        "Đơn thuê đã thanh toán đủ. Còn " + order.getDaysUntilEndDate() + " ngày nữa mới hết hạn. Không thể hủy.");
                resp.sendRedirect("admin-rental-manage");
                return;
            }

            // Chưa đến hạn thanh toán (tức là vẫn trong 14 ngày từ ngày bắt đầu)
            if (order.getDaysUntilFinalPaymentDue() > 0) {
                req.getSession().setAttribute("error",
                        "Còn " + order.getDaysUntilFinalPaymentDue() + " ngày nữa mới đến hạn thanh toán. Không thể hủy đơn.");
                resp.sendRedirect("admin-rental-manage");
                return;
            }

            // Đã quá hạn thanh toán -> tiến hành hủy
            boolean deleted = rentalDAO.deleteRentalOrderById(rentalOrderId);
            boolean updated = warehouseDAO.markWarehouseAsAvailable(order.getWarehouseID());

            if (deleted && updated) {
                EmailSender.sendCancelEmail(order.getCustomerEmail());
                req.getSession().setAttribute("success", "✅ Đã hủy đơn thuê và gửi email thông báo cho khách hàng.");
            } else {
                req.getSession().setAttribute("error", "❌ Có lỗi xảy ra khi hủy đơn thuê.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "❌ Lỗi hệ thống khi xử lý yêu cầu hủy.");
        }

        resp.sendRedirect("admin-rental-manage");
    }
}
