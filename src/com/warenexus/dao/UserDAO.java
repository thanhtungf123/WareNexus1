package com.warenexus.dao;

import com.warenexus.model.Account;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO {

    // Insert new account and customer info
    public void insertCustomer(String fullName, String email, String phone, String password) throws SQLException {
        String insertAccountSQL = "INSERT INTO Account (Email, Password, CreatedAt, IsActive) VALUES (?, ?, GETDATE(), 1)";
        String insertCustomerSQL = "INSERT INTO Customer (AccountID, FullName, Phone) VALUES (?, ?, ?)";
        String insertRoleSQL = "INSERT INTO AccountRole (AccountID, RoleType, AdminLevel) VALUES (?, 'Customer', 1)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement accountStmt = conn.prepareStatement(insertAccountSQL, Statement.RETURN_GENERATED_KEYS)) {
                accountStmt.setString(1, email);
                accountStmt.setString(2, password);
                accountStmt.executeUpdate();

                try (ResultSet generatedKeys = accountStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int accountId = generatedKeys.getInt(1);

                        try (PreparedStatement customerStmt = conn.prepareStatement(insertCustomerSQL)) {
                            customerStmt.setInt(1, accountId);
                            customerStmt.setString(2, fullName);
                            customerStmt.setString(3, phone);
                            customerStmt.executeUpdate();
                        }

                        try (PreparedStatement roleStmt = conn.prepareStatement(insertRoleSQL)) {
                            roleStmt.setInt(1, accountId);
                            roleStmt.executeUpdate();
                        }
                    } else {
                        conn.rollback();
                        throw new SQLException("Failed to retrieve AccountID after insertion.");
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Get user info by email
    public Account findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Account WHERE Email = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setAccountId(rs.getInt("AccountID"));
                    acc.setEmail(rs.getString("Email"));
                    acc.setPassword(rs.getString("Password"));
                    acc.setIsActive(rs.getBoolean("IsActive"));

                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) {
                        acc.setCreatedAt(createdAt.toLocalDateTime());
                    }

                    return acc;
                }
            }
        }

        return null;
    }

    // Get role by AccountID
    public String getUserRole(int accountId) throws SQLException {
        String sql = "SELECT RoleType FROM AccountRole WHERE AccountID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RoleType");
                }
            }
        }
        return "Customer";
    }
    
    public void registerCustomer(String fullName, String email, String phone, String password) throws SQLException {
    String sql = "INSERT INTO Account (FullName, Email, Phone, Password, RoleID, IsActive, CreatedAt) " +
                 "VALUES (?, ?, ?, ?, 3, 1, GETDATE())"; // RoleID 3 = customer

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, fullName);
        stmt.setString(2, email);
        stmt.setString(3, phone);
        stmt.setString(4, password);
        stmt.executeUpdate();
    }
}
}
