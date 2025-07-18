package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.Compensation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object for Compensation entities.
 * Handles CRUD operations for employee compensation details.
 */
public class CompensationDAO {

    /**
     * Finds compensation details for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return An Optional containing the Compensation object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Compensation> findByEmployeeId(int employeeId) throws SQLException {
        String query = "SELECT EmployeeID, BasicSalary, RiceSubsidy, PhoneAllowance, ClothingAllowance, GrossSemimonthly, HourlyRate FROM compensation WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Compensation(
                            rs.getInt("EmployeeID"),
                            rs.getDouble("BasicSalary"),
                            rs.getDouble("RiceSubsidy"),
                            rs.getDouble("PhoneAllowance"),
                            rs.getDouble("ClothingAllowance"),
                            (Double) rs.getObject("GrossSemimonthly"), // Use getObject to handle potential nulls
                            (Double) rs.getObject("HourlyRate")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves new compensation details for an employee.
     *
     * @param compensation The Compensation object to save.
     * @throws SQLException if a database access error occurs.
     */
    public void save(Compensation compensation) throws SQLException {
        String query = "INSERT INTO compensation (EmployeeID, BasicSalary, RiceSubsidy, PhoneAllowance, ClothingAllowance) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, compensation.getEmployeeId());
            stmt.setDouble(2, compensation.getBasicSalary());
            stmt.setDouble(3, compensation.getRiceSubsidy());
            stmt.setDouble(4, compensation.getPhoneAllowance());
            stmt.setDouble(5, compensation.getClothingAllowance());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates existing compensation details for an employee.
     *
     * @param compensation The Compensation object with updated information. Must have a valid EmployeeID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(Compensation compensation) throws SQLException {
        // Exclude generated columns (GrossSemimonthly, HourlyRate) from update if they are truly generated
        String query = "UPDATE compensation SET BasicSalary = ?, RiceSubsidy = ?, PhoneAllowance = ?, ClothingAllowance = ? WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, compensation.getBasicSalary());
            stmt.setDouble(2, compensation.getRiceSubsidy());
            stmt.setDouble(3, compensation.getPhoneAllowance());
            stmt.setDouble(4, compensation.getClothingAllowance());
            stmt.setInt(5, compensation.getEmployeeId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes compensation details for a specific employee.
     *
     * @param employeeId The ID of the employee whose compensation details to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(int employeeId) throws SQLException {
        String query = "DELETE FROM compensation WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
        }
    }
}