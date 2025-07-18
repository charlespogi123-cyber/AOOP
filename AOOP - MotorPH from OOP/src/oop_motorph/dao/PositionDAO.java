package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Position entities.
 * Handles database operations related to positions.
 */
public class PositionDAO {

    /**
     * Finds a position by its ID.
     *
     * @param positionId The ID of the position to find.
     * @return An Optional containing the Position object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Position> findById(int positionId) throws SQLException {
        String query = "SELECT PositionID, PositionName FROM positions WHERE PositionID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, positionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Position(
                            rs.getInt("PositionID"),
                            rs.getString("PositionName")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a position by its name.
     *
     * @param positionName The name of the position to search for.
     * @return An Optional containing the Position object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Position> findByName(String positionName) throws SQLException {
        String query = "SELECT PositionID, PositionName FROM positions WHERE PositionName = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, positionName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Position(
                            rs.getInt("PositionID"),
                            rs.getString("PositionName")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves a new position to the database.
     * Assigns the generated PositionID to the Position object.
     *
     * @param position The Position object to save. Its positionId field will be updated.
     * @throws SQLException if a database access error occurs.
     */
    public void save(Position position) throws SQLException {
        String query = "INSERT INTO positions (PositionName) VALUES (?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, position.getPositionName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    position.setPositionId(rs.getInt(1));
                }
            }
        }
    }
}