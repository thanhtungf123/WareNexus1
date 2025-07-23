package com.warenexus.dao;

import com.warenexus.model.Customer;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void insert(Customer customer) throws Exception {
        String sql = "INSERT INTO Customer (AccountID, FullName, Phone) VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, customer.getAccountId());
            ps.setString(2, customer.getFullName());
            ps.setString(3, customer.getPhone());
            ps.executeUpdate();
        }
    }

    public Customer getByAccountId(int accountId) throws Exception {
        String sql = "SELECT c.AccountID, c.FullName, c.Phone, a.Email, a.IsActive " +
                     "FROM Customer c JOIN Account a ON c.AccountID = a.AccountID " +
                     "WHERE c.AccountID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public void update(Customer customer) throws Exception {
        if (customer == null || customer.getAccountId() <= 0) {
            throw new IllegalArgumentException("Invalid customer data.");
        }

        String fullName = customer.getFullName();
        String phone = customer.getPhone();
        String email = customer.getEmail();

        if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name and email are required.");
        }

        String sql = "UPDATE Customer SET FullName = ?, Phone = ?, Email = ? WHERE AccountID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, fullName.trim());
            ps.setString(2, phone != null ? phone.trim() : null);
            ps.setString(3, email.trim());
            ps.setInt(4, customer.getAccountId());

            ps.executeUpdate();
        }
    }


    public void delete(int accountId) throws Exception {
        String sql = "DELETE FROM Customer WHERE AccountID = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
        }
    }

    public List<Customer> getAll() throws Exception {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<Customer> getAllCustomers() throws Exception {
        List<Customer> list = new ArrayList<>();
        String sql = """
            SELECT c.AccountID, c.FullName, c.Phone, 
                   a.Email, a.IsActive
            FROM Customer c
            JOIN Account a ON c.AccountID = a.AccountID
        """;
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<Customer> searchCustomers(String keyword, Boolean isActive, int offset, int limit) throws Exception {
        List<Customer> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT c.AccountID, c.FullName, c.Phone, a.Email, a.IsActive
            FROM Customer c
            JOIN Account a ON c.AccountID = a.AccountID
            WHERE (c.FullName LIKE ? OR a.Email LIKE ?)
        """);
        if (isActive != null) {
            sql.append(" AND a.IsActive = ").append(isActive ? 1 : 0);
        }
        sql.append(" ORDER BY c.FullName ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, offset);
            ps.setInt(4, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setAccountId(rs.getInt("AccountID"));
        c.setFullName(rs.getString("FullName"));
        c.setPhone(rs.getString("Phone"));
        try { c.setEmail(rs.getString("Email")); } catch (SQLException ignore) {}
        try { c.setActive(rs.getBoolean("IsActive")); } catch (SQLException ignore) {}
        return c;
    }
    
    public Customer getCustomerByAccountId(int accountId) {
        String sql = "SELECT * FROM Customer WHERE AccountID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer c = new Customer();
                c.setAccountId(rs.getInt("AccountID"));
                c.setFullName(rs.getString("FullName"));
                c.setPhone(rs.getString("Phone"));
                c.setEmail(rs.getString("Email"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
