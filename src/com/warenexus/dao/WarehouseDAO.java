package com.warenexus.dao;

import com.warenexus.model.Warehouse;
import com.warenexus.model.WarehouseImage;

import java.sql.*;
import java.util.*;

public class WarehouseDAO {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=WareHouseDB;" +
        "encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public Connection getCon() throws SQLException {
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

    public int insert(Warehouse w, Connection conn) {
        String sql = "INSERT INTO Warehouse (WarehouseTypeID, Name, AddressLine, Ward, District, Size, PricePerUnit, Status, Description, Latitude, Longitude, CreatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về ID vừa insert
                    }
                }
            }
            return -1; // Insert thất bại
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
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

    public boolean deleteWarehouseWithImages(int warehouseId) {
        String deleteImagesSQL = "DELETE FROM WarehouseImage WHERE WarehouseID = ?";
        String deleteWarehouseSQL = "DELETE FROM Warehouse WHERE WarehouseID = ?";

        try (Connection conn = getCon()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try (PreparedStatement ps1 = conn.prepareStatement(deleteImagesSQL);
                 PreparedStatement ps2 = conn.prepareStatement(deleteWarehouseSQL)) {

                // Xóa ảnh
                ps1.setInt(1, warehouseId);
                ps1.executeUpdate();

                // Xóa kho
                ps2.setInt(1, warehouseId);
                int affectedRows = ps2.executeUpdate();

                conn.commit(); // Commit nếu không lỗi
                return affectedRows > 0;

            } catch (SQLException e) {
                conn.rollback(); // Rollback nếu có lỗi
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true); // Khôi phục trạng thái ban đầu
            }

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

        List<WarehouseImage> listImg = getImagesByWarehouseID(id);
        String imgId = String.valueOf(listImg.get(0).getImageID());

        return new Warehouse(id, name, addr, w, d, sz, pr, sts, type, imgId, lat, lon, desc, created);
    }

    public boolean updateStatus(int warehouseId, String status) {
        String sql = "UPDATE Warehouse SET Status = ?, UpdatedAt = GETDATE() WHERE WarehouseID = ?";
        try (Connection c = getCon();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, warehouseId);
            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<WarehouseImage> getImagesByWarehouseID(int warehouseID){
        List<WarehouseImage> list = new ArrayList<>();
        String sql = "SELECT ImageID, ImageData, ImageFileName FROM WarehouseImage WHERE WarehouseID = ?";

        try (Connection conn = getCon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, warehouseID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WarehouseImage img = new WarehouseImage();
                    img.setImageID(rs.getInt("ImageID"));
                    img.setImageData(rs.getBytes("ImageData"));
                    img.setImageFileName(rs.getString("ImageFileName"));
                    list.add(img);
                }
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
                ex.printStackTrace();
        }
        return list;
    }

    public byte[] getImageDataFromDB(int imageID) {
        String sql = "SELECT ImageData FROM WarehouseImage WHERE ImageID = ?";
        try (Connection conn = getCon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imageID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("ImageData");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertWarehouseImage(WarehouseImage image, Connection conn) throws SQLException {
        String sql = "INSERT INTO WarehouseImage (WarehouseID, ImageData, ImageFileName, CreatedAt) " +
                "VALUES (?, ?, ?, GETDATE())";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, Integer.parseInt(image.getWarehouseID()));
            st.setBytes(2, image.getImageData());
            st.setString(3, image.getImageFileName());

            return st.executeUpdate() > 0;
        }
    }

}
