package oop_motorph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static DatabaseConfig instance;
    private Connection connection;

    private DatabaseConfig() {
        // Private constructor to prevent direct instantiation
        try {
            String url = "jdbc:mysql://localhost:3306/aoop"; // Example for MySQL
            String user = "root";
            String password = "3513461u";
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver"); // For MySQL 8+
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        // Ensure connection is still valid or re-establish if needed (basic check)
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Re-establishing database connection...");
                // Replace with your actual database URL, username, and password
                String url = "jdbc:mysql://localhost:3306/aoop";
                String user = "root";
                String password = "3513461u";
                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection re-established.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to re-establish database connection: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}