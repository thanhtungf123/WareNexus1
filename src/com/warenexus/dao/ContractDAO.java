package com.warenexus.dao;

import com.warenexus.model.Contract;
import com.warenexus.util.DBUtil;

import java.sql.*;

/** Data-access helper for the Contract table. */
public class ContractDAO {

    private final Connection conn = DBUtil.getConnection();

    /* ---------- create (unchanged) ---------- */

    public void createContract(Contract contract) throws SQLException {
        String sql = """
                INSERT INTO Contract
                (RentalOrderID, ContractNumber, PdfPath, Status,
                 Price, Deposit, RentalPeriod, CreatedAt)
                VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())
                """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt   (1, contract.getRentalOrderID());
            stmt.setString(2, contract.getContractNumber());
            stmt.setString(3, contract.getPdfPath());
            stmt.setString(4, contract.getStatus());
            stmt.setDouble(5, contract.getPrice());
            stmt.setDouble(6, contract.getDeposit());
            stmt.setInt   (7, contract.getRentalPeriod());

            stmt.executeUpdate();
        }
    }

    /* ---------- read ---------- */

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
