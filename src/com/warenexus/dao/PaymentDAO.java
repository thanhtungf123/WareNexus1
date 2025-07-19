package com.warenexus.dao;

import com.warenexus.model.Payment;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public void insert(Payment payment) throws Exception {
            String sql = "INSERT INTO Payment (RentalOrderID, PaymentCategoryID, Amount, Status, TransactionID, PaymentDate) " +
                         "VALUES (?, ?, ?, ?, ?, GETDATE())";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, payment.getRentalOrderID());
                ps.setInt(2, payment.getPaymentCategoryID()); // fix ở đây
                ps.setDouble(3, payment.getAmount());
                ps.setString(4, payment.getStatus());
                ps.setString(5, payment.getTransactionID());

                ps.executeUpdate();
            }
    }

    public void updateStatusByRentalOrderId(int rentalOrderId, String status) throws Exception {
    String sql = "UPDATE Payment SET Status = ? WHERE RentalOrderID = ?";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, status);
        ps.setInt(2, rentalOrderId);
        ps.executeUpdate();
        }
    }
    // Các phương thức khác nếu có
}
