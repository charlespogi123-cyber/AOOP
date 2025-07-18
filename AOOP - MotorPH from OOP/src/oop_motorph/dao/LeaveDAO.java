package oop_motorph.dao;

import oop_motorph.model.LeaveRequest;
import oop_motorph.config.DatabaseConfig;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

/**
 * Data Access Object for LeaveRequest entities.
 * Handles CRUD operations for employee leave requests.
 */
public class LeaveDAO {

    /**
     * Finds a leave request by its unique RequestID.
     *
     * @param requestId The ID of the leave request.
     * @return An Optional containing the LeaveRequest object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<LeaveRequest> findById(int requestId) throws SQLException {
        String query = "SELECT RequestID, EmployeeID, LeaveType, StartDate, EndDate, NumberOfDays, Status, Reason, DateFiled FROM LeaveRequests WHERE RequestID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLeaveRequest(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all leave requests for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return A list of LeaveRequest objects for the specified employee.
     * @throws SQLException if a database access error occurs.
     */
    public List<LeaveRequest> findByEmployeeId(String employeeId) throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT RequestID, EmployeeID, LeaveType, StartDate, EndDate, NumberOfDays, Status, Reason, DateFiled FROM LeaveRequests WHERE EmployeeID = ? ORDER BY StartDate DESC";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(mapResultSetToLeaveRequest(rs));
                }
            }
        }
        return leaveRequests;
    }

    /**
     * Retrieves all leave requests in the system.
     *
     * @return A list of all LeaveRequest objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<LeaveRequest> findAll() throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT RequestID, EmployeeID, LeaveType, StartDate, EndDate, NumberOfDays, Status, Reason, DateFiled FROM LeaveRequests ORDER BY DateFiled DESC";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                leaveRequests.add(mapResultSetToLeaveRequest(rs));
            }
        }
        return leaveRequests;
    }

    /**
     * Retrieves leave requests based on their status ("Pending", "Approved").
     *
     * @param status The status to filter by.
     * @return A list of LeaveRequest objects matching the status.
     * @throws SQLException if a database access error occurs.
     */
    public List<LeaveRequest> findByStatus(String status) throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT RequestID, EmployeeID, LeaveType, StartDate, EndDate, NumberOfDays, Status, Reason, DateFiled FROM LeaveRequests WHERE Status = ? ORDER BY DateFiled DESC";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(mapResultSetToLeaveRequest(rs));
                }
            }
        }
        return leaveRequests;
    }

    /**
     * Retrieves leave requests that overlap with a specific date range.
     *
     * @param startDate The start of the date range to check.
     * @param endDate The end of the date range to check.
     * @return A list of LeaveRequest objects that fall within or overlap the given date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<LeaveRequest> findForDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        // This query finds requests where:
        // 1. The request starts before or on endDate AND ends after or on startDate (overlap)
        // 2. OR the request starts within the range [startDate, endDate]
        String query = "SELECT RequestID, EmployeeID, LeaveType, StartDate, EndDate, NumberOfDays, Status, Reason, DateFiled FROM LeaveRequests WHERE (StartDate <= ? AND EndDate >= ?) OR (StartDate >= ? AND StartDate <= ?) ORDER BY StartDate";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(endDate));
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setDate(4, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(mapResultSetToLeaveRequest(rs));
                }
            }
        }
        return leaveRequests;
    }

    /**
     * Saves a new leave request.
     *
     * @param leaveRequest The LeaveRequest object to save. Its RequestID will be updated with the generated ID.
     * @throws SQLException if a database access error occurs.
     */
    public void save(LeaveRequest leaveRequest) throws SQLException {
        String query = "INSERT INTO LeaveRequests (EmployeeID, LeaveType, StartDate, EndDate, NumberOfDays, Status, Reason, DateFiled) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, leaveRequest.getEmployeeId());
            stmt.setString(2, leaveRequest.getLeaveType());
            stmt.setDate(3, Date.valueOf(leaveRequest.getStartDate()));
            stmt.setDate(4, Date.valueOf(leaveRequest.getEndDate()));
            stmt.setInt(5, leaveRequest.getNumberOfDays());
            stmt.setString(6, leaveRequest.getStatus());
            stmt.setString(7, leaveRequest.getReason());
            stmt.setDate(8, Date.valueOf(leaveRequest.getDateFiled()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    leaveRequest.setRequestId(generatedKeys.getInt(1)); // Set the generated ID back to the model
                }
            }
        }
    }

    /**
     * Updates an existing leave request.
     *
     * @param leaveRequest The LeaveRequest object with updated information. Must have a valid RequestID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(LeaveRequest leaveRequest) throws SQLException {
        String query = "UPDATE LeaveRequests SET EmployeeID = ?, LeaveType = ?, StartDate = ?, EndDate = ?, NumberOfDays = ?, Status = ?, Reason = ?, DateFiled = ? WHERE RequestID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, leaveRequest.getEmployeeId());
            stmt.setString(2, leaveRequest.getLeaveType());
            stmt.setDate(3, Date.valueOf(leaveRequest.getStartDate()));
            stmt.setDate(4, Date.valueOf(leaveRequest.getEndDate()));
            stmt.setInt(5, leaveRequest.getNumberOfDays());
            stmt.setString(6, leaveRequest.getStatus());
            stmt.setString(7, leaveRequest.getReason());
            stmt.setDate(8, Date.valueOf(leaveRequest.getDateFiled()));
            stmt.setInt(9, leaveRequest.getRequestId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a leave request by its unique RequestID.
     *
     * @param requestId The ID of the leave request to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(int requestId) throws SQLException {
        String query = "DELETE FROM LeaveRequests WHERE RequestID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
        }
    }

    private LeaveRequest mapResultSetToLeaveRequest(ResultSet rs) throws SQLException {
        int requestId = rs.getInt("RequestID");
        String employeeId = rs.getString("EmployeeID");
        String leaveType = rs.getString("LeaveType");
        LocalDate startDate = rs.getDate("StartDate").toLocalDate();
        LocalDate endDate = rs.getDate("EndDate").toLocalDate();
        int numberOfDays = rs.getInt("NumberOfDays");
        String status = rs.getString("Status");
        String reason = rs.getString("Reason");
        LocalDate dateFiled = rs.getDate("DateFiled").toLocalDate();
        return new LeaveRequest(requestId, employeeId, leaveType, startDate, endDate, numberOfDays, status, reason, dateFiled);
    }
}