package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.Address;
import oop_motorph.model.Department;
import oop_motorph.model.EmpDetails;
import oop_motorph.model.EmployeeStatus;
import oop_motorph.model.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Use java.sql.Date for DB interaction
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Employee entities.
 * Handles CRUD operations for employees, including loading related lookup data.
 */
public class EmployeeDAO {

    /**
     * Retrieves all employee details with joined lookup data.
     *
     * @return A list of EmpDetails objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<EmpDetails> findAll() throws SQLException {
        List<EmpDetails> employees = new ArrayList<>();
        String query = """
            SELECT e.EmployeeID, e.FirstName, e.LastName, e.Birthday, e.PhoneNumber, e.ImmediateSupervisorID,
                   a.AddressID, a.StreetAddress, a.City, a.Region,
                   es.StatusID, es.StatusName,
                   p.PositionID, p.PositionName,
                   d.DepartmentID, d.DepartmentName,
                   sup.EmployeeID AS SupervisorEmpID, sup.FirstName AS SupervisorFirstName, sup.LastName AS SupervisorLastName
            FROM employees e
            LEFT JOIN addresses a ON e.AddressID = a.AddressID
            LEFT JOIN employeestatuses es ON e.StatusID = es.StatusID
            LEFT JOIN positions p ON e.PositionID = p.PositionID
            LEFT JOIN departments d ON e.DepartmentID = d.DepartmentID
            LEFT JOIN employees sup ON e.ImmediateSupervisorID = sup.EmployeeID
            ORDER BY e.EmployeeID
            """;

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Address address = null;
                if (rs.getObject("AddressID") != null) {
                    address = new Address(
                            rs.getInt("AddressID"),
                            rs.getString("StreetAddress"),
                            rs.getString("City"),
                            rs.getString("Region")
                    );
                }

                EmployeeStatus status = null;
                if (rs.getObject("StatusID") != null) {
                    status = new EmployeeStatus(
                            rs.getInt("StatusID"),
                            rs.getString("StatusName")
                    );
                }

                Position position = null;
                if (rs.getObject("PositionID") != null) {
                    position = new Position(
                            rs.getInt("PositionID"),
                            rs.getString("PositionName")
                    );
                }

                Department department = null;
                if (rs.getObject("DepartmentID") != null) {
                    department = new Department(
                            rs.getInt("DepartmentID"),
                            rs.getString("DepartmentName")
                    );
                }

                EmpDetails supervisor = null;
                if (rs.getObject("SupervisorEmpID") != null) {
                    supervisor = new EmpDetails(
                            rs.getString("SupervisorEmpID"),
                            rs.getString("SupervisorFirstName"),
                            rs.getString("SupervisorLastName"),
                            null, null, null, null, null, null, null // Only basic info for supervisor
                    );
                }

                LocalDate birthday = null;
                if (rs.getDate("Birthday") != null) {
                    birthday = rs.getDate("Birthday").toLocalDate();
                }

                EmpDetails emp = new EmpDetails(
                        rs.getString("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        birthday,
                        rs.getString("PhoneNumber"),
                        address,
                        status,
                        position,
                        department,
                        supervisor
                );
                employees.add(emp);
            }
        }
        return employees;
    }

    /**
     * Finds an employee by their EmployeeID.
     *
     * @param employeeId The EmployeeID to search for.
     * @return An Optional containing the EmpDetails object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<EmpDetails> findById(String employeeId) throws SQLException {
        String query = """
            SELECT e.EmployeeID, e.FirstName, e.LastName, e.Birthday, e.PhoneNumber, e.ImmediateSupervisorID,
                   a.AddressID, a.StreetAddress, a.City, a.Region,
                   es.StatusID, es.StatusName,
                   p.PositionID, p.PositionName,
                   d.DepartmentID, d.DepartmentName,
                   sup.EmployeeID AS SupervisorEmpID, sup.FirstName AS SupervisorFirstName, sup.LastName AS SupervisorLastName
            FROM employees e
            LEFT JOIN addresses a ON e.AddressID = a.AddressID
            LEFT JOIN employeestatuses es ON e.StatusID = es.StatusID
            LEFT JOIN positions p ON e.PositionID = p.PositionID
            LEFT JOIN departments d ON e.DepartmentID = d.DepartmentID
            LEFT JOIN employees sup ON e.ImmediateSupervisorID = sup.EmployeeID
            WHERE e.EmployeeID = ?
            """;
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(employeeId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Address address = null;
                    if (rs.getObject("AddressID") != null) {
                        address = new Address(rs.getInt("AddressID"), rs.getString("StreetAddress"), rs.getString("City"), rs.getString("Region"));
                    }
                    EmployeeStatus status = null;
                    if (rs.getObject("StatusID") != null) {
                        status = new EmployeeStatus(rs.getInt("StatusID"), rs.getString("StatusName"));
                    }
                    Position position = null;
                    if (rs.getObject("PositionID") != null) {
                        position = new Position(rs.getInt("PositionID"), rs.getString("PositionName"));
                    }
                    Department department = null;
                    if (rs.getObject("DepartmentID") != null) {
                        department = new Department(rs.getInt("DepartmentID"), rs.getString("DepartmentName"));
                    }
                    EmpDetails supervisor = null;
                    if (rs.getObject("SupervisorEmpID") != null) {
                        supervisor = new EmpDetails(rs.getString("SupervisorEmpID"), rs.getString("SupervisorFirstName"), rs.getString("SupervisorLastName"), null, null, null, null, null, null, null);
                    }

                    LocalDate birthday = null;
                    if (rs.getDate("Birthday") != null) {
                        birthday = rs.getDate("Birthday").toLocalDate();
                    }

                    return Optional.of(new EmpDetails(
                            rs.getString("EmployeeID"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            birthday,
                            rs.getString("PhoneNumber"),
                            address,
                            status,
                            position,
                            department,
                            supervisor
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if an employee with the given ID exists, excluding an optionally provided existing ID.
     * This is useful for validation during add/update operations.
     *
     * @param empID The EmployeeID to check.
     * @param existingEmpID An optional EmployeeID to exclude from the check (e.g., the employee being updated).
     * @return true if an employee with empID exists (and is not existingEmpID), false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean exists(String empID, String existingEmpID) throws SQLException {
        String query;
        if (existingEmpID != null && !existingEmpID.isEmpty()) {
            query = "SELECT COUNT(*) FROM employees WHERE EmployeeID = ? AND EmployeeID != ?";
        } else {
            query = "SELECT COUNT(*) FROM employees WHERE EmployeeID = ?";
        }

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(empID));
            if (existingEmpID != null && !existingEmpID.isEmpty()) {
                stmt.setInt(2, Integer.parseInt(existingEmpID));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Saves a new employee record to the database.
     * Note: This method only inserts into the 'employees' table.
     * Related entities (Address, Status, Position, Compensation, GovIDs)
     * must be managed by their respective DAOs.
     *
     * @param employee The EmpDetails object containing the employee's information.
     * @throws SQLException if a database access error occurs.
     */
    public void save(EmpDetails employee) throws SQLException {
        String query = "INSERT INTO employees (EmployeeID, FirstName, LastName, Birthday, AddressID, PhoneNumber, StatusID, PositionID, DepartmentID, ImmediateSupervisorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(employee.getEmployeeId()));
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getLastName());
            stmt.setDate(4, employee.getBirthday() != null ? Date.valueOf(employee.getBirthday()) : null);
            stmt.setObject(5, employee.getAddressId(), java.sql.Types.INTEGER); // Allow null for foreign key
            stmt.setString(6, employee.getPhoneNumber());
            stmt.setObject(7, employee.getStatusId(), java.sql.Types.INTEGER);
            stmt.setObject(8, employee.getPositionId(), java.sql.Types.INTEGER);
            stmt.setObject(9, employee.getDepartmentId(), java.sql.Types.INTEGER);
            stmt.setObject(10, employee.getImmediateSupervisorId(), java.sql.Types.INTEGER); // Allow null
            stmt.executeUpdate();
        }
    }

    /**
     * Updates an existing employee record in the database.
     * Note: This method only updates the 'employees' table.
     * Related entities must be updated by their respective DAOs.
     *
     * @param employee The EmpDetails object with updated information. Must have a valid EmployeeID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(EmpDetails employee) throws SQLException {
        String query = "UPDATE employees SET FirstName = ?, LastName = ?, Birthday = ?, AddressID = ?, PhoneNumber = ?, StatusID = ?, PositionID = ?, DepartmentID = ?, ImmediateSupervisorID = ? WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setDate(3, employee.getBirthday() != null ? Date.valueOf(employee.getBirthday()) : null);
            stmt.setObject(4, employee.getAddressId(), java.sql.Types.INTEGER);
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setObject(6, employee.getStatusId(), java.sql.Types.INTEGER);
            stmt.setObject(7, employee.getPositionId(), java.sql.Types.INTEGER);
            stmt.setObject(8, employee.getDepartmentId(), java.sql.Types.INTEGER);
            stmt.setObject(9, employee.getImmediateSupervisorId(), java.sql.Types.INTEGER);
            stmt.setInt(10, Integer.parseInt(employee.getEmployeeId()));
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an employee record from the database by EmployeeID.
     * NOTE: This will likely require cascading deletes or manual deletion from related tables
     * (governmentids, compensation, attendance, payroll) due to foreign key constraints.
     * This DAO method only deletes from the 'employees' table. The service layer
     * should handle the full cascade.
     *
     * @param employeeId The EmployeeID of the employee to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(String employeeId) throws SQLException {
        String query = "DELETE FROM employees WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(employeeId));
            stmt.executeUpdate();
        }
    }

    /**
     * Gets the next available EmployeeID.
     *
     * @return The next EmployeeID.
     * @throws SQLException if a database access error occurs.
     */
    public int getNextEmployeeId() throws SQLException {
        String query = "SELECT MAX(EmployeeID) FROM employees";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1; // Default to 1 if no employees exist
    }

    /**
     * Retrieves a list of full names of all employees who can act as supervisors.
     *
     * @return A list of supervisor full names.
     * @throws SQLException if a database access error occurs.
     */
    public List<String> getAllSupervisorNames() throws SQLException {
        List<String> supervisors = new ArrayList<>();
        // Add a "No supervisor" option for UI convenience, if desired in the DAO layer itself
        // supervisors.add("N/A");

        String query = "SELECT CONCAT(FirstName, ' ', LastName) as FullName FROM employees WHERE EmployeeID IS NOT NULL ORDER BY FirstName, LastName";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("FullName");
                if (fullName != null && !fullName.trim().isEmpty()) {
                    supervisors.add(fullName);
                }
            }
        }
        return supervisors;
    }

    /**
     * Finds an employee's ID by their first and last name.
     * This is a utility method, primarily for backward compatibility with supervisor name lookup.
     *
     * @param firstName The first name of the employee.
     * @param lastName The last name of the employee.
     * @return An Optional containing the EmployeeID if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Integer> findIdByFirstAndLastName(String firstName, String lastName) throws SQLException {
        String query = "SELECT EmployeeID FROM employees WHERE FirstName = ? AND LastName = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("EmployeeID"));
                }
            }
        }
        return Optional.empty();
    }
}