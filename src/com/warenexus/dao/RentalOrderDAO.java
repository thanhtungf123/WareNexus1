package com.warenexus.dao;

import com.warenexus.model.RentalOrder;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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
    public void updateDates(int rentalOrderId, Date startDate, Date endDate ) throws Exception {
        String sql = "UPDATE RentalOrder SET StartDate = ?, EndDate = ? WHERE RentalOrderID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ps.setInt(3, rentalOrderId);
            ps.executeUpdate();
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
    public List<RentalOrder> getPendingOrders() throws Exception {
        String sql = "SELECT * FROM RentalOrder WHERE Status = 'Pending'";
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

    public RentalOrder getRentalOrderById(int rentalOrderId) throws Exception {
        String sql = "SELECT * FROM RentalOrder WHERE RentalOrderID = ?";
        RentalOrder rentalOrder = null;
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, rentalOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rentalOrder = new RentalOrder();
                    rentalOrder.setRentalOrderID(rs.getInt("RentalOrderID"));
                    rentalOrder.setAccountID(rs.getInt("AccountID"));
                    rentalOrder.setWarehouseID(rs.getInt("WarehouseID"));
                    rentalOrder.setStartDate(rs.getDate("StartDate"));
                    rentalOrder.setEndDate(rs.getDate("EndDate"));
                    rentalOrder.setDeposit(rs.getDouble("Deposit"));
                    rentalOrder.setTotalPrice(rs.getDouble("TotalPrice"));
                    rentalOrder.setStatus(rs.getString("Status"));
                    rentalOrder.setCreatedAt(rs.getDate("CreatedAt"));
                    rentalOrder.setUpdatedAt(rs.getDate("UpdatedAt"));
                    rentalOrder.setDepositPaid(rs.getBoolean("IsDepositPaid"));
                    rentalOrder.setDepositPaidAt(rs.getDate("DepositPaidAt"));
                    rentalOrder.setDepositRefunded(rs.getBoolean("IsDepositRefunded"));
                    rentalOrder.setDepositRefundedAt(rs.getDate("DepositRefundedAt"));
                }
            }
        }
        return rentalOrder;
    }

    public List<RentalOrder> getRentalOrderByAccountID(int accountID) throws Exception {
        String sql = "SELECT * FROM RentalOrder WHERE AccountID = ?";
        List<RentalOrder>  rentalOrderList = new ArrayList<>();
        RentalOrder rentalOrder = null;
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rentalOrder = new RentalOrder();
                    rentalOrder.setRentalOrderID(rs.getInt("RentalOrderID"));
                    rentalOrder.setAccountID(rs.getInt("AccountID"));
                    rentalOrder.setWarehouseID(rs.getInt("WarehouseID"));
                    rentalOrder.setStartDate(rs.getDate("StartDate"));
                    rentalOrder.setEndDate(rs.getDate("EndDate"));
                    rentalOrder.setDeposit(rs.getDouble("Deposit"));
                    rentalOrder.setTotalPrice(rs.getDouble("TotalPrice"));
                    rentalOrder.setStatus(rs.getString("Status"));
                    rentalOrder.setCreatedAt(rs.getDate("CreatedAt"));
                    rentalOrder.setUpdatedAt(rs.getDate("UpdatedAt"));
                    rentalOrder.setDepositPaid(rs.getBoolean("IsDepositPaid"));
                    rentalOrder.setDepositPaidAt(rs.getDate("DepositPaidAt"));
                    rentalOrder.setDepositRefunded(rs.getBoolean("IsDepositRefunded"));
                    rentalOrder.setDepositRefundedAt(rs.getDate("DepositRefundedAt"));
                    rentalOrderList.add(rentalOrder);
                }
            }
        }
        return rentalOrderList;
    }


    public boolean updateRentalStatus(int rentalOrderId, String newStatus) {
    String sql = "UPDATE RentalOrder SET Status = ?, UpdatedAt = ? WHERE RentalOrderID = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, newStatus);
        stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        stmt.setInt(3, rentalOrderId);
        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    public RentalOrder getCurrentRentalByWarehouseID(int warehouseID) {
    String sql = """
        SELECT * FROM RentalOrder
        WHERE WarehouseID = ? 
          AND Status = 'Approved'
          AND StartDate <= GETDATE()
          AND EndDate >= GETDATE()
    """;

    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, warehouseID);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                RentalOrder ro = new RentalOrder();
                ro.setRentalOrderID(rs.getInt("RentalOrderID"));
                ro.setAccountID(rs.getInt("AccountID"));
                ro.setWarehouseID(rs.getInt("WarehouseID"));
                ro.setStartDate(rs.getDate("StartDate"));
                ro.setEndDate(rs.getDate("EndDate"));
                ro.setDeposit(rs.getDouble("Deposit"));
                ro.setTotalPrice(rs.getDouble("TotalPrice"));
                ro.setStatus(rs.getString("Status"));
                return ro;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    
    public RentalOrder getCurrentRentalByWarehouseId(int warehouseId) {
        String sql = """
            SELECT * FROM RentalOrder
            WHERE WarehouseID = ?
            AND Status = 'Approved'
            AND GETDATE() BETWEEN StartDate AND EndDate
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                RentalOrder rental = new RentalOrder();
                rental.setRentalOrderID(rs.getInt("RentalOrderID"));
                rental.setAccountID(rs.getInt("AccountID"));
                rental.setWarehouseID(rs.getInt("WarehouseID"));
                rental.setStartDate(rs.getDate("StartDate"));
                rental.setEndDate(rs.getDate("EndDate"));
                rental.setDeposit(rs.getDouble("Deposit"));
                rental.setTotalPrice(rs.getDouble("TotalPrice"));
                rental.setStatus(rs.getString("Status"));
                return rental;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getWarehouseIdByRentalOrderId(int rentalOrderId) {
    String sql = "SELECT WarehouseID FROM RentalOrder WHERE RentalOrderID = ?";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, rentalOrderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("WarehouseID");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
}
    
    public RentalOrder getLatestApprovedRentalByWarehouseId(int warehouseId) {
    String sql = """
        SELECT TOP 1 * FROM RentalOrder
        WHERE WarehouseID = ? AND Status = 'Approved'
        ORDER BY CreatedAt DESC
    """;
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, warehouseId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            RentalOrder rental = new RentalOrder();
            rental.setRentalOrderID(rs.getInt("RentalOrderID"));
            rental.setAccountID(rs.getInt("AccountID"));
            rental.setWarehouseID(rs.getInt("WarehouseID"));
            rental.setStartDate(rs.getDate("StartDate"));
            rental.setEndDate(rs.getDate("EndDate"));
            rental.setDeposit(rs.getDouble("Deposit"));
            rental.setTotalPrice(rs.getDouble("TotalPrice"));
            rental.setStatus(rs.getString("Status"));
            return rental;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    public List<RentalOrder> getUnnotifiedApprovedOrders(int accountId) throws SQLException {
        List<RentalOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM RentalOrder " +
                "WHERE AccountID = ? AND Status = 'Approved' AND IsNotificationSent = 0";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RentalOrder rental = new RentalOrder();
                    rental.setRentalOrderID(rs.getInt("RentalOrderID"));
                    rental.setAccountID(rs.getInt("AccountID"));
                    rental.setWarehouseID(rs.getInt("WarehouseID"));
                    rental.setStartDate(rs.getDate("StartDate"));
                    rental.setEndDate(rs.getDate("EndDate"));
                    rental.setDeposit(rs.getDouble("Deposit"));
                    rental.setTotalPrice(rs.getDouble("TotalPrice"));
                    rental.setStatus(rs.getString("Status"));
                    list.add(rental);
                }
            }
        }

        return list;
    }

    public boolean updateIsNotificationSent(int rentalOrderId) throws SQLException {
        String sql = "UPDATE RentalOrder SET IsNotificationSent = 0 WHERE RentalOrderID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rentalOrderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean markNotificationAsSent(int rentalOrderId) throws SQLException {
        String sql = "UPDATE RentalOrder SET IsNotificationSent = 1 WHERE RentalOrderID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rentalOrderId);
            return ps.executeUpdate() > 0;
        }
    }


}
