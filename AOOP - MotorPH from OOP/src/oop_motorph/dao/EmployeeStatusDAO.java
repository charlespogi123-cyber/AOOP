package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.EmployeeStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Data Access Object for EmployeeStatus entities.
 * Handles database operations related to employee statuses.
 */
public class EmployeeStatusDAO {

    /**
     * Finds an employee status by its ID.
     *
     * @param statusId The ID of the status to find.
     * @return An Optional containing the EmployeeStatus object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<EmployeeStatus> findById(int statusId) throws SQLException {
        String query = "SELECT StatusID, StatusName FROM employeestatuses WHERE StatusID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, statusId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new EmployeeStatus(
                            rs.getInt("StatusID"),
                            rs.getString("StatusName")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds an employee status by its name.
     *
     * @param statusName The name of the status to search for.
     * @return An Optional containing the EmployeeStatus object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<EmployeeStatus> findByName(String statusName) throws SQLException {
        String query = "SELECT StatusID, StatusName FROM employeestatuses WHERE StatusName = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, statusName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new EmployeeStatus(
                            rs.getInt("StatusID"),
                            rs.getString("StatusName")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves a new employee status to the database.
     * Assigns the generated StatusID to the EmployeeStatus object.
     *
     * @param status The EmployeeStatus object to save. Its statusId field will be updated.
     * @throws SQLException if a database access error occurs.
     */
    public void save(EmployeeStatus status) throws SQLException {
        String query = "INSERT INTO employeestatuses (StatusName) VALUES (?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, status.getStatusName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    status.setStatusId(rs.getInt(1));
                }
            }
        }
    }
}