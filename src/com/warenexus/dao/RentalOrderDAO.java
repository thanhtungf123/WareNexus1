package com.warenexus.dao;

import com.warenexus.model.RentalOrder;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalOrderDAO {

    public int insert(RentalOrder ro) throws Exception {
        String sql = "INSERT INTO RentalOrder (AccountID, WarehouseID, StartDate, EndDate, Status, CreatedAt, IsDepositPaid, IsDepositRefunded) " +
                     "VALUES (?, ?, ?, ?, ?, GETDATE(), 0, 0)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ro.getAccountID());
            ps.setInt(2, ro.getWarehouseID());
            ps.setDate(3, new java.sql.Date(ro.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(ro.getEndDate().getTime()));
            ps.setString(5, ro.getStatus());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            else throw new SQLException("Không lấy được RentalOrderID");
        }
    }

    public void updateStatus(int rentalOrderId, String status) throws Exception {
        String sql = "UPDATE RentalOrder SET Status = ? WHERE RentalOrderID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, rentalOrderId);
            ps.executeUpdate();
        }
    }
    
    public List<RentalOrder> getPendingApprovalOrders() throws Exception {
    String sql = "SELECT * FROM RentalOrder WHERE IsDepositPaid = 1 AND Status = 'Pending'";
    List<RentalOrder> list = new ArrayList<>();
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            RentalOrder ro = new RentalOrder();
            ro.setRentalOrderID(rs.getInt("RentalOrderID"));
            ro.setAccountID(rs.getInt("AccountID"));
            ro.setWarehouseID(rs.getInt("WarehouseID"));
            ro.setStartDate(rs.getDate("StartDate"));
            ro.setEndDate(rs.getDate("EndDate"));
            ro.setDeposit(rs.getDouble("Deposit"));
            ro.setStatus(rs.getString("Status"));
            list.add(ro);
        }
    }
    return list;
}

    public void approveRentalOrder(int rentalOrderId, int staffAccountId) throws Exception {
        String sql = "UPDATE RentalOrder SET Status = 'Approved', StaffAccountID = ?, UpdatedAt = GETDATE() WHERE RentalOrderID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, staffAccountId);
            ps.setInt(2, rentalOrderId);
            ps.executeUpdate();
        }
    }
    public void markDepositPaid(int rentalOrderId) throws Exception {
        String sql = "UPDATE RentalOrder SET IsDepositPaid = 1, DepositPaidAt = GETDATE() WHERE RentalOrderID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, rentalOrderId);
            ps.executeUpdate();
        }
    }
    
    public void updatePriceInfo(int rentalOrderId, double deposit, double totalPrice) throws Exception {
    String sql = "UPDATE RentalOrder SET Deposit = ?, TotalPrice = ? WHERE RentalOrderID = ?";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setDouble(1, deposit);
        ps.setDouble(2, totalPrice);
        ps.setInt(3, rentalOrderId);
        ps.executeUpdate();
    }
}

}
