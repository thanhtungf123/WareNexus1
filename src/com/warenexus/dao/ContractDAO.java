package com.warenexus.dao;

import com.warenexus.model.Contract;
import com.warenexus.util.DBUtil;

import java.sql.*;

public class ContractDAO {
    private final Connection conn;

    public ContractDAO() {
        conn = DBUtil.getConnection();
    }

    public void createContract(Contract contract) throws SQLException {
        // Câu lệnh SQL chèn một bản ghi vào bảng Contract
        String sql = "INSERT INTO Contract (RentalOrderID, ContractNumber, PdfPath, Status, Price, Deposit, RentalPeriod, CreatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";

        // Sử dụng PreparedStatement để thiết lập các giá trị của câu lệnh SQL
        try (Connection conn = DBUtil.getConnection();  // Kết nối cơ sở dữ liệu
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán các giá trị vào câu lệnh SQL
            stmt.setInt(1, contract.getRentalOrderID());   // RentalOrderID
            stmt.setString(2, contract.getContractNumber()); // ContractNumber
            stmt.setString(3, contract.getPdfPath());       // PdfPath
            stmt.setString(4, contract.getStatus());        // Status
            stmt.setDouble(5, contract.getPrice());         // Giá tiền (Price)
            stmt.setDouble(6, contract.getDeposit());       // Tiền đặt cọc (Deposit)
            stmt.setInt(7, contract.getRentalPeriod());   // Thời gian thuê (RentalPeriod)

            // Thực hiện câu lệnh chèn dữ liệu vào cơ sở dữ liệu
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Xử lý lỗi nếu có
            throw new SQLException("Lỗi khi tạo hợp đồng: " + e.getMessage(), e);
        }
    }
    
    /** Return the contract (or null) for a rental order. */
    public Contract getByRentalOrderId(int rentalOrderId) throws SQLException {

        String sql = "SELECT * FROM Contract WHERE RentalOrderID = ?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, rentalOrderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("[DEBUG] ContractDAO: orderId " + rentalOrderId + " ➜ no row");
                    return null;
                }

                System.out.println("[DEBUG] ContractDAO: orderId " + rentalOrderId + " ➜ FOUND");
                Contract ct = new Contract();
                ct.setRentalOrderID(rs.getInt("RentalOrderID"));
                ct.setContractNumber(rs.getString("ContractNumber"));
                ct.setPdfPath(rs.getString("PdfPath"));
                ct.setStatus(rs.getString("Status"));
                ct.setPrice(rs.getDouble("Price"));
                ct.setDeposit(rs.getDouble("Deposit"));
                ct.setRentalPeriod(rs.getInt("RentalPeriod"));
                return ct;
            }
        }
    }
}
