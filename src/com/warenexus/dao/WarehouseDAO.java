package com.warenexus.dao;

import com.warenexus.model.Warehouse;

import java.sql.*;
import java.util.*;

public class WarehouseDAO {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=WareHouseDB;" +
        "encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123";

    private Connection getCon() throws SQLException {
        try { Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); }
        catch (ClassNotFoundException e) { throw new SQLException(e); }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public List<Warehouse> getAll() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM Warehouse";
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Warehouse getById(int id) {
        String sql = "SELECT * FROM Warehouse WHERE WarehouseID = ?";
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Warehouse> search(String keyword, Integer typeId, String status,
                                  Double sizeMin, Double sizeMax,
                                  Double priceMin, Double priceMax,
                                  String ward, String district) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Warehouse WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND Name LIKE ?");
            params.add('%' + keyword.trim() + '%');
        }
        if (typeId != null) {
            sql.append(" AND WarehouseTypeID = ?");
            params.add(typeId);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }
        if (sizeMin != null) {
            sql.append(" AND Size >= ?");
            params.add(sizeMin);
        }
        if (sizeMax != null) {
            sql.append(" AND Size <= ?");
            params.add(sizeMax);
        }
        if (priceMin != null) {
            sql.append(" AND PricePerUnit >= ?");
            params.add(priceMin);
        }
        if (priceMax != null) {
            sql.append(" AND PricePerUnit <= ?");
            params.add(priceMax);
        }
        if (ward != null && !ward.isBlank()) {
            sql.append(" AND Ward = ?");
            params.add(ward);
        }
        if (district != null && !district.isBlank()) {
            sql.append(" AND District = ?");
            params.add(district);
        }

        List<Warehouse> list = new ArrayList<>();
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean insert(Warehouse w) {
        String sql = "INSERT INTO Warehouse (WarehouseTypeID, Name, AddressLine, Ward, District, Size, PricePerUnit, Status, Description, Latitude, Longitude, CreatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, w.getWarehouseTypeId());
            st.setString(2, w.getName());
            st.setString(3, w.getAddress());
            st.setString(4, w.getWard());
            st.setString(5, w.getDistrict());
            st.setDouble(6, w.getSize());
            st.setDouble(7, w.getPricePerUnit());
            st.setString(8, w.getStatus());
            st.setString(9, w.getDescription());
            st.setObject(10, w.getLatitude());
            st.setObject(11, w.getLongitude());
            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Warehouse w) {
        String sql = "UPDATE Warehouse SET WarehouseTypeID=?, Name=?, AddressLine=?, Ward=?, District=?, Size=?, PricePerUnit=?, Status=?, Description=?, Latitude=?, Longitude=?, UpdatedAt=GETDATE() " +
                     "WHERE WarehouseID=?";
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, w.getWarehouseTypeId());
            st.setString(2, w.getName());
            st.setString(3, w.getAddress());
            st.setString(4, w.getWard());
            st.setString(5, w.getDistrict());
            st.setDouble(6, w.getSize());
            st.setDouble(7, w.getPricePerUnit());
            st.setString(8, w.getStatus());
            st.setString(9, w.getDescription());
            st.setObject(10, w.getLatitude());
            st.setObject(11, w.getLongitude());
            st.setInt(12, w.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Warehouse WHERE WarehouseID = ?";
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Warehouse map(ResultSet rs) throws SQLException {
        int id = rs.getInt("WarehouseID");
        String name = rs.getString("Name");
        String addr = rs.getString("AddressLine");
        String w = rs.getString("Ward");
        String d = rs.getString("District");
        double sz = rs.getDouble("Size");
        double pr = rs.getDouble("PricePerUnit");
        String sts = rs.getString("Status");
        int type = rs.getInt("WarehouseTypeID");

        Double lat = rs.getObject("Latitude") != null ? rs.getDouble("Latitude") : null;
        Double lon = rs.getObject("Longitude") != null ? rs.getDouble("Longitude") : null;
        String desc = rs.getString("Description");
        java.util.Date created = rs.getTimestamp("CreatedAt");

        int imgNum = ((id - 1) % 12) + 1;
        String img = "images/img" + imgNum + ".webp";

        return new Warehouse(id, name, addr, w, d, sz, pr, sts, type, img, lat, lon, desc, created);
    }
}
