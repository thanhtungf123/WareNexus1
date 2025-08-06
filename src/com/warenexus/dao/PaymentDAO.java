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
    
    public boolean hasFinalPayment(int rentalOrderId) throws Exception {
    String sql = """
        SELECT COUNT(*) 
        FROM Payment p
        JOIN PaymentCategory pc ON p.PaymentCategoryID = pc.PaymentCategoryID
        WHERE p.RentalOrderID = ? AND pc.CategoryName = 'FinalPayment' AND p.Status = 'Completed'
    """;

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, rentalOrderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    }
    return false;
}
    
    public List<Payment> getPendingPaymentsByRentalOrderAndCategory(int rentalOrderId, int categoryId) throws Exception {
    List<Payment> list = new ArrayList<>();
    String sql = "SELECT * FROM Payment WHERE RentalOrderID = ? AND PaymentCategoryID = ? AND Status = 'Pending'";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, rentalOrderId);
        ps.setInt(2, categoryId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getInt("PaymentID"));
                payment.setRentalOrderID(rs.getInt("RentalOrderID"));
                payment.setAmount(rs.getDouble("Amount"));
                payment.setTransactionID(rs.getString("TransactionID"));
                payment.setStatus(rs.getString("Status"));
                payment.setPaymentDate(rs.getTimestamp("PaymentDate"));
                payment.setPaymentCategoryID(rs.getInt("PaymentCategoryID"));
                list.add(payment);
            }
        }
    }
    return list;
}

    public void updateStatus(int paymentId, String status) throws Exception {
        String sql = "UPDATE Payment SET Status = ? WHERE PaymentID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        }
    }
    
    public boolean isFinalPaymentCompleted(int rentalOrderId) throws Exception {
    String sql = "SELECT COUNT(*) FROM Payment p " +
                 "JOIN PaymentCategory c ON p.PaymentCategoryID = c.PaymentCategoryID " +
                 "WHERE p.RentalOrderID = ? AND c.CategoryName = 'FinalPayment' AND p.Status = 'Completed'";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, rentalOrderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}
    
    public long getDaysUntilEndDate(int rentalOrderId) throws Exception {
    String sql = "SELECT DATEDIFF(DAY, GETDATE(), EndDate) AS DaysLeft FROM RentalOrder WHERE RentalOrderID = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, rentalOrderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getLong("DaysLeft");
        }
    }
    return -1;
}    
    
}
