package com.warenexus.dao;

import com.warenexus.model.SupportTicket;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupportTicketDAO {
    private final Connection conn;

    public SupportTicketDAO() {
        conn = DBUtil.getConnection();
    }

    // Lấy tất cả phiếu hỗ trợ theo RentalOrderID
    public List<SupportTicket> getByRentalOrderID(int rentalOrderID) throws SQLException {
        List<SupportTicket> list = new ArrayList<>();
        String sql = "SELECT * FROM SupportTicket WHERE RentalOrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, rentalOrderID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            SupportTicket t = extractFromResultSet(rs);
            list.add(t);
        }

        rs.close();
        stmt.close();
        return list;
    }

    // Lấy phiếu theo TicketID
    public SupportTicket getByID(int ticketID) throws SQLException {
        String sql = "SELECT * FROM SupportTicket WHERE TicketID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ticketID);
        ResultSet rs = stmt.executeQuery();

        SupportTicket ticket = null;
        if (rs.next()) {
            ticket = extractFromResultSet(rs);
        }

        rs.close();
        stmt.close();
        return ticket;
    }

    // Thêm phiếu hỗ trợ mới
    public void insert(SupportTicket ticket) throws SQLException {
        String sql = "INSERT INTO SupportTicket (RentalOrderID, IssueTitle, IssueDescription, Status, CreatedAt, UpdatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ticket.getRentalOrderID());
        stmt.setString(2, ticket.getIssueTitle());
        stmt.setString(3, ticket.getIssueDescription());
        stmt.setString(4, ticket.getStatus());
        stmt.setTimestamp(5, new Timestamp(ticket.getCreatedAt().getTime()));
        stmt.setTimestamp(6, ticket.getUpdatedAt() != null ? new Timestamp(ticket.getUpdatedAt().getTime()) : null);
        stmt.executeUpdate();
        stmt.close();
    }

    // Cập nhật trạng thái và thời gian cập nhật
    public void updateStatus(int ticketID, String newStatus, Timestamp updatedAt) throws SQLException {
        String sql = "UPDATE SupportTicket SET Status = ?, UpdatedAt = ? WHERE TicketID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newStatus);
        stmt.setTimestamp(2, updatedAt);
        stmt.setInt(3, ticketID);
        stmt.executeUpdate();
        stmt.close();
    }

    // Hàm hỗ trợ: tạo đối tượng SupportTicket từ ResultSet
    private SupportTicket extractFromResultSet(ResultSet rs) throws SQLException {
        SupportTicket t = new SupportTicket();
        t.setTicketID(rs.getInt("TicketID"));
        t.setRentalOrderID(rs.getInt("RentalOrderID"));
        t.setIssueTitle(rs.getString("IssueTitle"));
        t.setIssueDescription(rs.getString("IssueDescription"));
        t.setStatus(rs.getString("Status"));
        t.setCreatedAt(rs.getTimestamp("CreatedAt"));
        t.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        return t;
    }
}
