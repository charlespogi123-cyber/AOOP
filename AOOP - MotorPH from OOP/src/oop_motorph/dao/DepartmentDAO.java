package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Data Access Object for Department entities.
 * Handles database operations related to departments.
 */
public class DepartmentDAO {

    /**
     * Finds a department by its ID.
     *
     * @param departmentId The ID of the department to find.
     * @return An Optional containing the Department object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Department> findById(int departmentId) throws SQLException {
        String query = "SELECT DepartmentID, DepartmentName FROM departments WHERE DepartmentID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Department(
                            rs.getInt("DepartmentID"),
                            rs.getString("DepartmentName")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a department by its name.
     *
     * @param departmentName The name of the department to search for.
     * @return An Optional containing the Department object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Department> findByName(String departmentName) throws SQLException {
        String query = "SELECT DepartmentID, DepartmentName FROM departments WHERE DepartmentName = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Department(
                            rs.getInt("DepartmentID"),
                            rs.getString("DepartmentName")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves a new department to the database.
     * Assigns the generated DepartmentID to the Department object.
     *
     * @param department The Department object to save. Its departmentId field will be updated.
     * @throws SQLException if a database access error occurs.
     */
    public void save(Department department) throws SQLException {
        String query = "INSERT INTO departments (DepartmentName) VALUES (?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, department.getDepartmentName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    department.setDepartmentId(rs.getInt(1));
                }
            }
        }
    }
}