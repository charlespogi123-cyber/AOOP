package oop_motorph;

import java.sql.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseHandler {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static Connection getConnection() {
        return DatabaseConfig.getInstance().getConnection();
    }

    // User Credentials
    public static Map<String, String[]> loadCredentials() {
        Map<String, String[]> credentials = new HashMap<>();
        String query = "SELECT username, password, role, first_name, last_name FROM Users";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                credentials.put(username, new String[]{password, role, firstName, lastName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    // Employee Operations
    public static List<EmpDetails> getEmployeeData() {
        List<EmpDetails> employees = new ArrayList<>();
        String query = "SELECT e.EmployeeID, e.FirstName, e.LastName, e.Birthday, " +
                      "a.StreetAddress, a.City, a.Region, " +
                      "e.PhoneNumber, es.StatusName, p.PositionName, " +
                      "CONCAT(sup.FirstName, ' ', sup.LastName) as SupervisorName " +
                      "FROM employees e " +
                      "LEFT JOIN addresses a ON e.AddressID = a.AddressID " +
                      "LEFT JOIN employeestatuses es ON e.StatusID = es.StatusID " +
                      "LEFT JOIN positions p ON e.PositionID = p.PositionID " +
                      "LEFT JOIN employees sup ON e.ImmediateSupervisorID = sup.EmployeeID " +
                      "ORDER BY e.EmployeeID";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Build address string
                String address = "";
                String street = rs.getString("StreetAddress");
                String city = rs.getString("City");
                String region = rs.getString("Region");
                
                if (street != null && !street.isEmpty()) {
                    address = street;
                }
                if (city != null && !city.isEmpty()) {
                    address += (address.isEmpty() ? "" : ", ") + city;
                }
                if (region != null && !region.isEmpty()) {
                    address += (address.isEmpty() ? "" : ", ") + region;
                }

                EmpDetails emp = new EmpDetails(
                        rs.getString("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Birthday"),
                        address.isEmpty() ? "Address not available" : address,
                        rs.getString("PhoneNumber"),
                        rs.getString("StatusName") != null ? rs.getString("StatusName") : "Status not available",
                        rs.getString("PositionName") != null ? rs.getString("PositionName") : "Position not available",
                        rs.getString("SupervisorName") != null ? rs.getString("SupervisorName") : "No supervisor"
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee data: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
    }

    public static boolean employeeExists(String empID, String existingEmpID) {
        String query = "SELECT COUNT(*) FROM employees WHERE EmployeeID = ? AND EmployeeID != ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, empID);
            stmt.setString(2, existingEmpID != null ? existingEmpID : "");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking employee existence: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static void addEmployee(EmpDetails newEmp) {
        Connection conn = DatabaseConfig.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            // First, insert address if not exists
            int addressId = insertOrGetAddressId(conn, newEmp.getAddress());
            
            // Get or create status ID
            int statusId = getOrCreateStatusId(conn, newEmp.getEmployeeStatus());
            
            // Get or create position ID
            int positionId = getOrCreatePositionId(conn, newEmp.getPosition());
            
            // Find supervisor ID if exists
            Integer supervisorId = getSupervisorId(conn, newEmp.getImmediateSupervisor());

            // Insert into employees table
            String empQuery = "INSERT INTO employees (EmployeeID, FirstName, LastName, Birthday, AddressID, PhoneNumber, StatusID, PositionID, DepartmentID, ImmediateSupervisorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, ?)";
            try (PreparedStatement empStmt = conn.prepareStatement(empQuery)) {
                empStmt.setInt(1, Integer.parseInt(newEmp.getEmpID()));
                empStmt.setString(2, newEmp.getFirstName());
                empStmt.setString(3, newEmp.getLastName());
                empStmt.setString(4, newEmp.getBirthdate());
                empStmt.setInt(5, addressId);
                empStmt.setString(6, newEmp.getPhoneNumber());
                empStmt.setInt(7, statusId);
                empStmt.setInt(8, positionId);
                empStmt.setObject(9, supervisorId);
                empStmt.executeUpdate();
            }

            // Insert default compensation record (exclude generated columns)
            String compQuery = "INSERT INTO compensation (EmployeeID, BasicSalary, RiceSubsidy, PhoneAllowance, ClothingAllowance) VALUES (?, 15000, 1500, 500, 500)";
            try (PreparedStatement compStmt = conn.prepareStatement(compQuery)) {
                compStmt.setInt(1, Integer.parseInt(newEmp.getEmpID()));
                compStmt.executeUpdate();
            }

            // Insert default government IDs record
            String govQuery = "INSERT INTO governmentids (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber) VALUES (?, '', '', '', '')";
            try (PreparedStatement govStmt = conn.prepareStatement(govQuery)) {
                govStmt.setInt(1, Integer.parseInt(newEmp.getEmpID()));
                govStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Employee added successfully");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error adding employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public static void updateEmployee(EmpDetails updatedEmp) {
        Connection conn = DatabaseConfig.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            // Update address
            int addressId = insertOrGetAddressId(conn, updatedEmp.getAddress());
            
            // Get or create status ID
            int statusId = getOrCreateStatusId(conn, updatedEmp.getEmployeeStatus());
            
            // Get or create position ID
            int positionId = getOrCreatePositionId(conn, updatedEmp.getPosition());
            
            // Find supervisor ID if exists
            Integer supervisorId = getSupervisorId(conn, updatedEmp.getImmediateSupervisor());

            // Update employees table
            String empQuery = "UPDATE employees SET FirstName = ?, LastName = ?, Birthday = ?, AddressID = ?, PhoneNumber = ?, StatusID = ?, PositionID = ?, ImmediateSupervisorID = ? WHERE EmployeeID = ?";
            try (PreparedStatement empStmt = conn.prepareStatement(empQuery)) {
                empStmt.setString(1, updatedEmp.getFirstName());
                empStmt.setString(2, updatedEmp.getLastName());
                empStmt.setString(3, updatedEmp.getBirthdate());
                empStmt.setInt(4, addressId);
                empStmt.setString(5, updatedEmp.getPhoneNumber());
                empStmt.setInt(6, statusId);
                empStmt.setInt(7, positionId);
                empStmt.setObject(8, supervisorId);
                empStmt.setInt(9, Integer.parseInt(updatedEmp.getEmpID()));
                empStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Employee updated successfully");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public static void deleteEmployee(String empID) {
        Connection conn = DatabaseConfig.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            int employeeId = Integer.parseInt(empID);

            // Delete from related tables first (foreign key constraints)
            String[] deleteQueries = {
                    "DELETE FROM governmentids WHERE EmployeeID = ?",
                    "DELETE FROM compensation WHERE EmployeeID = ?",
                    "DELETE FROM attendance WHERE EmployeeID = ?",
                    "DELETE FROM employees WHERE EmployeeID = ?"
            };

            for (String query : deleteQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, employeeId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Employee deleted successfully");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    // Salary Operations
    public static List<EmpSalaryDetails> getSalaryData() {
        List<EmpSalaryDetails> salaryList = new ArrayList<>();
        String query = "SELECT e.EmployeeID, e.FirstName, e.LastName, " +
                      "g.SSSNumber, g.PhilHealthNumber, g.TINNumber, g.PagIBIGNumber, " +
                      "c.BasicSalary, c.RiceSubsidy, c.PhoneAllowance, c.ClothingAllowance, " +
                      "c.GrossSemimonthly, c.HourlyRate " +
                      "FROM employees e " +
                      "LEFT JOIN governmentids g ON e.EmployeeID = g.EmployeeID " +
                      "LEFT JOIN compensation c ON e.EmployeeID = c.EmployeeID " +
                      "ORDER BY e.EmployeeID";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EmpSalaryDetails salary = new EmpSalaryDetails(
                        rs.getString("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("SSSNumber") != null ? rs.getString("SSSNumber") : "",
                        rs.getString("PhilHealthNumber") != null ? rs.getString("PhilHealthNumber") : "",
                        rs.getString("TINNumber") != null ? rs.getString("TINNumber") : "",
                        rs.getString("PagIBIGNumber") != null ? rs.getString("PagIBIGNumber") : "",
                        rs.getDouble("BasicSalary"),
                        rs.getDouble("RiceSubsidy"),
                        rs.getDouble("PhoneAllowance"),
                        rs.getDouble("ClothingAllowance"),
                        rs.getDouble("GrossSemimonthly"),
                        rs.getDouble("HourlyRate")
                );
                salaryList.add(salary);
            }
        } catch (SQLException e) {
            System.err.println("Error getting salary data: " + e.getMessage());
            e.printStackTrace();
        }

        return salaryList;
    }

    public static void updateEmployeeSalary(EmpSalaryDetails updatedSalary) {
        String query = "UPDATE salary SET first_name = ?, last_name = ?, sss_no = ?, philhealth_no = ?, tin_no = ?, pagibig_no = ?, basic_salary = ?, rice_allowance = ?, phone_allowance = ?, clothing_allowance = ?, gross_semi = ?, hourly_rate = ? WHERE emp_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updatedSalary.getFirstName());
            stmt.setString(2, updatedSalary.getLastName());
            stmt.setString(3, updatedSalary.getSssNo());
            stmt.setString(4, updatedSalary.getPhilhealthNo());
            stmt.setString(5, updatedSalary.getTinNo());
            stmt.setString(6, updatedSalary.getPagibigNo());
            stmt.setDouble(7, updatedSalary.getBasicSalary());
            stmt.setDouble(8, updatedSalary.getRiceSubsidy());
            stmt.setDouble(9, updatedSalary.getPhoneAllowance());
            stmt.setDouble(10, updatedSalary.getClothingAllowance());
            stmt.setDouble(11, updatedSalary.getGrossSemi());
            stmt.setDouble(12, updatedSalary.getHourlyRate());
            stmt.setString(13, updatedSalary.getEmpID());

            stmt.executeUpdate();
            System.out.println("Salary updated successfully");

        } catch (SQLException e) {
            System.err.println("Error updating salary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Attendance Operations
    public static List<EmpAttLeave> getAttendanceData() {
        List<EmpAttLeave> attendanceList = new ArrayList<>();
        String query = "SELECT a.EmployeeID, e.FirstName, e.LastName, " +
                      "es.StatusName, p.PositionName, " +
                      "CONCAT(sup.FirstName, ' ', sup.LastName) as SupervisorName, " +
                      "a.AttendanceDate, a.AttendanceDate, a.LoginTime, a.LogoutTime, " +
                      "a.TotalHours, 0 as OvertimeHours, 'Regular' as AttendanceType, 'Present' as AttendanceStatus " +
                      "FROM attendance a " +
                      "JOIN employees e ON a.EmployeeID = e.EmployeeID " +
                      "LEFT JOIN employeestatuses es ON e.StatusID = es.StatusID " +
                      "LEFT JOIN positions p ON e.PositionID = p.PositionID " +
                      "LEFT JOIN employees sup ON e.ImmediateSupervisorID = sup.EmployeeID " +
                      "ORDER BY a.EmployeeID, a.AttendanceDate";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EmpAttLeave attendance = new EmpAttLeave(
                        rs.getString("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("StatusName") != null ? rs.getString("StatusName") : "Status not available",
                        rs.getString("PositionName") != null ? rs.getString("PositionName") : "Position not available",
                        rs.getString("SupervisorName") != null ? rs.getString("SupervisorName") : "No supervisor",
                        rs.getDate("AttendanceDate") != null ? rs.getDate("AttendanceDate").toLocalDate() : null,
                        rs.getDate("AttendanceDate") != null ? rs.getDate("AttendanceDate").toLocalDate() : null,
                        rs.getTime("LoginTime") != null ? rs.getTime("LoginTime").toLocalTime() : null,
                        rs.getTime("LogoutTime") != null ? rs.getTime("LogoutTime").toLocalTime() : null,
                        rs.getDouble("TotalHours"),
                        rs.getDouble("OvertimeHours"),
                        rs.getString("AttendanceType"),
                        rs.getString("AttendanceStatus"),
                        0.0, // vl_count - not available in normalized schema
                        0.0, // vl_used - not available in normalized schema
                        0.0, // vl_balance - not available in normalized schema
                        0.0, // sl_count - not available in normalized schema
                        0.0, // sl_used - not available in normalized schema
                        0.0  // sl_balance - not available in normalized schema
                );
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance data: " + e.getMessage());
            e.printStackTrace();
        }

        return attendanceList;
    }

    public static void saveAttendanceRequest(EmpAttLeave empAttLeave) {
        String query = "INSERT INTO attendance (emp_id, first_name, last_name, status, position, immediate_supervisor, date_from, date_to, time_in, time_out, hours_worked, duration, attendance_type, attendance_status, vl_count, vl_used, vl_balance, sl_count, sl_used, sl_balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, empAttLeave.getEmpID());
            stmt.setString(2, empAttLeave.getFirstName());
            stmt.setString(3, empAttLeave.getLastName());
            stmt.setString(4, empAttLeave.getEmployeeStatus());
            stmt.setString(5, empAttLeave.getPosition());
            stmt.setString(6, empAttLeave.getImmediateSupervisor());
            stmt.setDate(7, Date.valueOf(empAttLeave.getAttDateFrom()));
            stmt.setDate(8, Date.valueOf(empAttLeave.getAttDateTo()));
            stmt.setTime(9, empAttLeave.getTimeIn() != null ? Time.valueOf(empAttLeave.getTimeIn()) : null);
            stmt.setTime(10, empAttLeave.getTimeOut() != null ? Time.valueOf(empAttLeave.getTimeOut()) : null);
            stmt.setDouble(11, empAttLeave.getHoursWorked());
            stmt.setDouble(12, empAttLeave.getDuration());
            stmt.setString(13, empAttLeave.getAttendanceType());
            stmt.setString(14, empAttLeave.getAttendanceStatus());
            stmt.setDouble(15, empAttLeave.getVlCount());
            stmt.setDouble(16, empAttLeave.getVlUsed());
            stmt.setDouble(17, empAttLeave.getVlBalance());
            stmt.setDouble(18, empAttLeave.getSlCount());
            stmt.setDouble(19, empAttLeave.getSlUsed());
            stmt.setDouble(20, empAttLeave.getSlBalance());

            stmt.executeUpdate();
            System.out.println("Attendance saved successfully");

        } catch (SQLException e) {
            System.err.println("Error saving attendance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String[]> readPayrollFromDatabase() {
        List<String[]> payrollData = new ArrayList<>();
        String query = "SELECT * FROM Payroll ORDER BY EmployeeID, PayrollPeriodStartDate";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // Optionally include header row
            String[] header = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                header[i - 1] = meta.getColumnLabel(i);
            }
            payrollData.add(header);

            // Fetch each row as String[]
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                payrollData.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error reading payroll from database: " + e.getMessage());
            e.printStackTrace();
        }

        return payrollData;
    }


    // Utility methods for validation
    public static String capitalizeWords(String input, int maxLength) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split("\\s+");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        String result = capitalizedString.toString().trim();
        return (result.length() > maxLength) ? result.substring(0, maxLength) : result;
    }

    public static String validateAddress(String address) {
        return capitalizeWords(address, 100);
    }

    public static boolean isValidPhone(String phoneNo) {
        return phoneNo.matches("^(\\d+-?)+\\d$") && phoneNo.length() <= 11;
    }

    // Helper methods for normalized database operations
    
    private static int insertOrGetAddressId(Connection conn, String address) throws SQLException {
        if (address == null || address.trim().isEmpty() || address.equals("Address not available")) {
            address = "Unknown Address";
        }
        
        // Try to find existing address
        String selectQuery = "SELECT AddressID FROM addresses WHERE StreetAddress = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, address);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("AddressID");
                }
            }
        }
        
        // Insert new address
        String insertQuery = "INSERT INTO addresses (StreetAddress, City, Region) VALUES (?, 'Unknown', 'Unknown')";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, address);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 1; // Default address ID
    }
    
    private static int getOrCreateStatusId(Connection conn, String statusName) throws SQLException {
        if (statusName == null || statusName.trim().isEmpty() || statusName.equals("Status not available")) {
            statusName = "Regular";
        }
        
        // Try to find existing status
        String selectQuery = "SELECT StatusID FROM employeestatuses WHERE StatusName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, statusName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("StatusID");
                }
            }
        }
        
        // Insert new status
        String insertQuery = "INSERT INTO employeestatuses (StatusName) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, statusName);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 1; // Default status ID
    }
    
    private static int getOrCreatePositionId(Connection conn, String positionName) throws SQLException {
        if (positionName == null || positionName.trim().isEmpty() || positionName.equals("Position not available")) {
            positionName = "General";
        }
        
        // Try to find existing position
        String selectQuery = "SELECT PositionID FROM positions WHERE PositionName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, positionName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PositionID");
                }
            }
        }
        
        // Insert new position
        String insertQuery = "INSERT INTO positions (PositionName) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, positionName);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 1; // Default position ID
    }
    
    private static Integer getSupervisorId(Connection conn, String supervisorName) throws SQLException {
        if (supervisorName == null || supervisorName.trim().isEmpty() || supervisorName.equals("No supervisor")) {
            return null;
        }
        
        // Try to find supervisor by name
        String[] names = supervisorName.split("\\s+");
        if (names.length < 2) {
            return null;
        }
        
        String firstName = names[0];
        String lastName = names[names.length - 1];
        
        String selectQuery = "SELECT EmployeeID FROM employees WHERE FirstName = ? AND LastName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("EmployeeID");
                }
            }
        }
        
        return null; // No supervisor found
    }

    public static int getNextEmployeeId() {
        String query = "SELECT MAX(EmployeeID) FROM employees";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return maxId + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error getting next employee ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 1; // Default to 1 if no employees exist or error occurs
    }
}