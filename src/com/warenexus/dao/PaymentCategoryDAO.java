package com.warenexus.dao;

import com.warenexus.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentCategoryDAO {

    public int getCategoryIdByName(String categoryName) throws Exception {
        String sql = "SELECT PaymentCategoryID FROM PaymentCategory WHERE CategoryName = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PaymentCategoryID");
                } else {
                    throw new Exception("Category '" + categoryName + "' not found in PaymentCategory table.");
                }
            }
        }
    }
}
