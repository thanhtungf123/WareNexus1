package com.warenexus.dao;

import com.warenexus.model.Review;
import com.warenexus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private final Connection conn;

    public ReviewDAO() {
        conn = DBUtil.getConnection();
    }

    // Lấy tất cả đánh giá theo WarehouseID
    public List<Review> getReviewsByWarehouse(int warehouseID) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.* FROM Review r " +
                     "JOIN RentalOrder ro ON r.RentalOrderID = ro.RentalOrderID " +
                     "WHERE ro.WarehouseID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, warehouseID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Review review = extractFromResultSet(rs);
            list.add(review);
        }

        rs.close();
        stmt.close();
        return list;
    }

    // Lấy đánh giá theo RentalOrderID (mỗi đơn chỉ có 1 review)
    public Review getByRentalOrderID(int rentalOrderID) throws SQLException {
        String sql = "SELECT * FROM Review WHERE RentalOrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, rentalOrderID);
        ResultSet rs = stmt.executeQuery();

        Review review = null;
        if (rs.next()) {
            review = extractFromResultSet(rs);
        }

        rs.close();
        stmt.close();
        return review;
    }

    // Thêm mới đánh giá
    public void insert(Review review) throws SQLException {
        String sql = "INSERT INTO Review (RentalOrderID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, review.getRentalOrderID());
        stmt.setInt(2, review.getRating());
        stmt.setString(3, review.getComment());
        stmt.setTimestamp(4, new Timestamp(review.getReviewDate().getTime()));
        stmt.executeUpdate();
        stmt.close();
    }

    // Xoá đánh giá
    public void deleteByRentalOrderID(int rentalOrderID) throws SQLException {
        String sql = "DELETE Review WHERE RentalOrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, rentalOrderID);
        stmt.executeUpdate();
        stmt.close();
    }

    // Hàm hỗ trợ: tạo đối tượng Review từ ResultSet
    private Review extractFromResultSet(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setReviewID(rs.getInt("ReviewID"));
        r.setRentalOrderID(rs.getInt("RentalOrderID"));
        r.setRating(rs.getInt("Rating"));
        r.setComment(rs.getString("Comment"));
        r.setReviewDate(rs.getTimestamp("ReviewDate"));
        return r;
    }
}
