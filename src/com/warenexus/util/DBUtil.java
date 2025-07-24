package com.warenexus.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=WareHouseDB;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa";         // sửa nếu user của bạn khác
    private static final String DB_PASSWORD = "123"; // sửa nếu mật khẩu của bạn khác

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to DB: " + e.getMessage(), e);
        }
    }
}
