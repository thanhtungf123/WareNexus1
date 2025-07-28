package com.warenexus.dao;

import com.warenexus.model.ServiceFee;
import com.warenexus.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceFeesDAO {
    private final Connection conn;

    public ServiceFeesDAO() {
        conn = DBUtil.getConnection();
    }

    public List<ServiceFee> getAllServiceFees() throws SQLException {
        List<ServiceFee> list = new ArrayList<>();
        String sql = "SELECT * FROM ServiceFee";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ServiceFee fee = new ServiceFee();
                fee.setServiceCode(rs.getString("ServiceCode"));
                fee.setServiceName(rs.getString("ServiceName"));
                fee.setFee(rs.getInt("Fee"));
                list.add(fee);
            }
        }
        return list;
    }

    public ServiceFee getFeeByCode(String code) throws SQLException {
        String sql = "SELECT * FROM ServiceFee WHERE ServiceCode = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ServiceFee fee = new ServiceFee();
                    fee.setServiceCode(rs.getString("ServiceCode"));
                    fee.setServiceName(rs.getString("ServiceName"));
                    fee.setFee(rs.getInt("Fee"));
                    return fee;
                }
            }
        }
        return null;
    }

    public void insertRentalService(int rentalOrderId, String serviceCode) throws SQLException {
        String sql = "INSERT INTO RentalService (RentalOrderID, ServiceCode) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rentalOrderId);
            ps.setString(2, serviceCode);
            ps.executeUpdate();
        }
    }


}
