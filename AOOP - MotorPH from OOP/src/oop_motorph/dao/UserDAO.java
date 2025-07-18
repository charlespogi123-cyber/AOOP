package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional; // Use Optional for methods that might return null

/**
 * Data Access Object for User entities.
 * Handles database operations related to user credentials.
 */
public class UserDAO {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return An Optional containing the User object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<User> findByUsername(String username) throws SQLException {
        String query = "SELECT username, password, role, first_name, last_name FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves a new user to the database.
     *
     * @param user The User object to save.
     * @throws SQLException if a database access error occurs.
     */
    public void save(User user) throws SQLException {
        String query = "INSERT INTO Users (username, password, role, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Remember to hash passwords in a real app!
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.executeUpdate();
        }
    }
}