package com.warenexus.dao;

import com.warenexus.model.Account;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public boolean existsByEmail(String email) throws Exception {
        String sql = "SELECT 1 FROM Account WHERE Email = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insert(Account acc) throws Exception {
        String sql = "INSERT INTO Account (Email, Password, IsActive, CreatedAt) VALUES (?, ?, ?, GETDATE())";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, acc.getEmail());
            ps.setString(2, acc.getPassword());
            ps.setBoolean(3, acc.isIsActive());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) acc.setAccountId(rs.getInt(1));
            }
        }
    }

    public Account login(String email, String password) throws Exception {
        String sql = """
            SELECT a.AccountID, a.Email, a.Password, a.IsActive, a.CreatedAt, ar.RoleType
            FROM Account a
            JOIN AccountRole ar ON a.AccountID = ar.AccountID
            WHERE a.Email = ? AND a.Password = ? AND a.IsActive = 1
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setAccountId(rs.getInt("AccountID"));
                    acc.setEmail(rs.getString("Email"));
                    acc.setPassword(rs.getString("Password"));
                    acc.setIsActive(rs.getBoolean("IsActive"));
                    Timestamp ts = rs.getTimestamp("CreatedAt");
                    if (ts != null) {
                        acc.setCreatedAt(ts.toLocalDateTime());
                    }

                    String roleType = rs.getString("RoleType");
                    switch (roleType) {
                        case "Admin" -> acc.setRoleId(1);
                        case "Staff" -> acc.setRoleId(2);
                        case "Customer" -> acc.setRoleId(3);
                        default -> acc.setRoleId(0); // fallback
                    }

                    return acc;
                }
            }
        }
        return null;
    }

    public Account getById(int id) throws Exception {
        String sql = "SELECT * FROM Account WHERE AccountID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAccount(rs);
                }
            }
        }
        return null;
    }

    public List<Account> getAll() throws Exception {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractAccount(rs));
            }
        }
        return list;
    }

    public void update(Account acc) throws Exception {
        String sql = "UPDATE Account SET Email = ?, Password = ?, IsActive = ? WHERE AccountID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, acc.getEmail());
            ps.setString(2, acc.getPassword());
            ps.setBoolean(3, acc.isIsActive());
            ps.setInt(4, acc.getAccountId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Account WHERE AccountID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Account extractAccount(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setAccountId(rs.getInt("AccountID"));
        acc.setEmail(rs.getString("Email"));
        acc.setPassword(rs.getString("Password"));
        acc.setIsActive(rs.getBoolean("IsActive"));
        Timestamp ts = rs.getTimestamp("CreatedAt");
        if (ts != null) {
            acc.setCreatedAt(ts.toLocalDateTime());
        }
        return acc;
    }
}
