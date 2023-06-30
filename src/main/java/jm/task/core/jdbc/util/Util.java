package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String dbUrl = "jdbc:mysql://192.168.89.21:3306/firstdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "root";
    public static Connection getConnection() {

        Connection connection;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("\nConnection established\n");
        } catch (SQLException e) {
            throw new RuntimeException("Database properties file not available or no connection ", e);
        }
        return connection;
    }

    public static void setRollback (Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
