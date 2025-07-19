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
        String sql = "INSERT INTO Contract (RentalOrderID, ContractNumber, PdfPath, Status, CreatedAt) "
           + "VALUES (?, ?, ?, ?, GETDATE())";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, contract.getRentalOrderID());
        stmt.setString(2, contract.getContractNumber());
        stmt.setString(3, contract.getPdfPath());
        stmt.setString(4, contract.getStatus());
        stmt.executeUpdate();
    }
}
