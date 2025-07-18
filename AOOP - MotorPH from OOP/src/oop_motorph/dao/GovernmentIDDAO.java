package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.GovernmentID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object for GovernmentID entities.
 * Handles CRUD operations for employee government IDs.
 */
public class GovernmentIDDAO {

    /**
     * Finds government ID details for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return An Optional containing the GovernmentID object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<GovernmentID> findByEmployeeId(int employeeId) throws SQLException {
        String query = "SELECT EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber FROM governmentids WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new GovernmentID(
                            rs.getInt("EmployeeID"),
                            rs.getString("SSSNumber"),
                            rs.getString("PhilHealthNumber"),
                            rs.getString("TINNumber"),
                            rs.getString("PagIBIGNumber")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves new government ID details for an employee.
     *
     * @param governmentID The GovernmentID object to save.
     * @throws SQLException if a database access error occurs.
     */
    public void save(GovernmentID governmentID) throws SQLException {
        String query = "INSERT INTO governmentids (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, governmentID.getEmployeeId());
            stmt.setString(2, governmentID.getSssNumber());
            stmt.setString(3, governmentID.getPhilHealthNumber());
            stmt.setString(4, governmentID.getTinNumber());
            stmt.setString(5, governmentID.getPagIbigNumber());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates existing government ID details for an employee.
     *
     * @param governmentID The GovernmentID object with updated information. Must have a valid EmployeeID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(GovernmentID governmentID) throws SQLException {
        String query = "UPDATE governmentids SET SSSNumber = ?, PhilHealthNumber = ?, TINNumber = ?, PagIBIGNumber = ? WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, governmentID.getSssNumber());
            stmt.setString(2, governmentID.getPhilHealthNumber());
            stmt.setString(3, governmentID.getTinNumber());
            stmt.setString(4, governmentID.getPagIbigNumber());
            stmt.setInt(5, governmentID.getEmployeeId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes government ID details for a specific employee.
     *
     * @param employeeId The ID of the employee whose government ID details to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(int employeeId) throws SQLException {
        String query = "DELETE FROM governmentids WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
        }
    }
}