package com.example.hackingthefuture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static Connection getConnection(String jdbcUrl) throws SQLException {
        String username = "root";
        String password = "";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static void createDatabase(String username){
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            createDatabase(connection, username);
            System.out.println();
            // Connect to the created database
            String dbUrl = JDBC_URL + username;
            try (Connection dbConnection = DriverManager.getConnection(dbUrl, USER, PASSWORD)) {
                // Create tables
                createTable(dbConnection, "hackingthefuture", "CREATE TABLE hackingthefuture (id INT AUTO_INCREMENT PRIMARY KEY, date DATE)");
                // Add more tables if needed
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase(Connection connection, String dbName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
            statement.executeUpdate(sql);
            System.out.println("Database created successfully: " + dbName);
        }
    }

    private static void createTable(Connection connection, String tableName, String createTableQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            System.out.println("Table created successfully: " + tableName);
        }
    }

    static void change(String oldUsername, String NewUsername){
        try (Connection conn = DriverManager.getConnection(JDBC_URL + oldUsername, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE "+ NewUsername);
            stmt.close();
            Statement stmt_price = conn.createStatement();
            stmt_price.executeUpdate("RENAME TABLE "+oldUsername+".hackingthefuture to "+NewUsername+".hackingthefuture;");
            stmt_price.close();

            Statement stmt_d = conn.createStatement();

            stmt_d.executeUpdate("DROP DATABASE "+oldUsername+";");
            stmt_d.close();
            System.out.println("Database name changed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
