package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.AttendanceRecord;
import oop_motorph.model.EmpDetails;
import oop_motorph.model.EmployeeStatus;
import oop_motorph.model.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for AttendanceRecord entities.
 * Handles CRUD operations for employee attendance.
 */
public class AttendanceDAO {

    /**
     * Finds an attendance record by its ID.
     *
     * @param attendanceId The ID of the attendance record.
     * @return An Optional containing the AttendanceRecord object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<AttendanceRecord> findById(int attendanceId) throws SQLException {
        String query = "SELECT * FROM attendance WHERE AttendanceID = ?"; // Assuming AttendanceID is the PK
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attendanceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAttendanceRecord(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all attendance data with joined employee details.
     *
     * @return A list of EmpAttLeave (now AttendanceRecord) objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<AttendanceRecord> findAll() throws SQLException {
        List<AttendanceRecord> attendanceList = new ArrayList<>();
        String query = """
            SELECT a.AttendanceID, a.EmployeeID, a.AttendanceDate, a.LoginTime, a.LogoutTime, a.TotalHours,
                   a.OvertimeHours, a.AttendanceType, a.AttendanceStatus, a.DateFrom, a.DateTo, a.Duration,
                   a.VL_Count, a.VL_Used, a.VL_Balance, a.SL_Count, a.SL_Used, a.SL_Balance,
                   e.FirstName, e.LastName,
                   es.StatusName,
                   p.PositionName,
                   CONCAT(sup.FirstName, ' ', sup.LastName) as SupervisorName
            FROM attendance a
            JOIN employees e ON a.EmployeeID = e.EmployeeID
            LEFT JOIN employeestatuses es ON e.StatusID = es.StatusID
            LEFT JOIN positions p ON e.PositionID = p.PositionID
            LEFT JOIN employees sup ON e.ImmediateSupervisorID = sup.EmployeeID
            ORDER BY a.EmployeeID, a.AttendanceDate
            """;

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendanceRecord(rs));
            }
        }
        return attendanceList;
    }

    /**
     * Saves a new attendance record (or attendance request) to the database.
     *
     * @param record The AttendanceRecord object to save.
     * @throws SQLException if a database access error occurs.
     */
    public void save(AttendanceRecord record) throws SQLException {
        // Adjust column names based on your actual 'attendance' table schema.
        // I'm using the names from your old saveAttendanceRequest.
        String query = "INSERT INTO attendance (" +
                       "EmployeeID, AttendanceDate, LoginTime, LogoutTime, TotalHours, OvertimeHours, " +
                       "AttendanceType, AttendanceStatus, DateFrom, DateTo, Duration, " +
                       "VL_Count, VL_Used, VL_Balance, SL_Count, SL_Used, SL_Balance) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, record.getEmployeeId());
            stmt.setDate(2, record.getAttendanceDate() != null ? Date.valueOf(record.getAttendanceDate()) : null);
            stmt.setTime(3, record.getLoginTime() != null ? Time.valueOf(record.getLoginTime()) : null);
            stmt.setTime(4, record.getLogoutTime() != null ? Time.valueOf(record.getLogoutTime()) : null);
            stmt.setDouble(5, record.getTotalHours());
            stmt.setDouble(6, record.getOvertimeHours());
            stmt.setString(7, record.getAttendanceType());
            stmt.setString(8, record.getAttendanceStatus());
            stmt.setDate(9, record.getDateFrom() != null ? Date.valueOf(record.getDateFrom()) : null);
            stmt.setDate(10, record.getDateTo() != null ? Date.valueOf(record.getDateTo()) : null);
            stmt.setDouble(11, record.getDuration());
            stmt.setDouble(12, record.getVlCount());
            stmt.setDouble(13, record.getVlUsed());
            stmt.setDouble(14, record.getVlBalance());
            stmt.setDouble(15, record.getSlCount());
            stmt.setDouble(16, record.getSlUsed());
            stmt.setDouble(17, record.getSlBalance());

            stmt.executeUpdate();
        }
    }

    /**
     * Helper method to map a ResultSet row to an AttendanceRecord object.
     *
     * @param rs The ResultSet containing the attendance data.
     * @return An AttendanceRecord object populated with data from the ResultSet.
     * @throws SQLException if a database access error occurs during mapping.
     */
    private AttendanceRecord mapResultSetToAttendanceRecord(ResultSet rs) throws SQLException {
        Integer attendanceId = rs.getObject("AttendanceID") != null ? rs.getInt("AttendanceID") : null;
        Integer employeeId = rs.getObject("EmployeeID") != null ? rs.getInt("EmployeeID") : null;
        LocalDate attendanceDate = rs.getDate("AttendanceDate") != null ? rs.getDate("AttendanceDate").toLocalDate() : null;
        LocalTime loginTime = rs.getTime("LoginTime") != null ? rs.getTime("LoginTime").toLocalTime() : null;
        LocalTime logoutTime = rs.getTime("LogoutTime") != null ? rs.getTime("LogoutTime").toLocalTime() : null;
        double totalHours = rs.getDouble("TotalHours");
        double overtimeHours = rs.getDouble("OvertimeHours"); // Assuming this column exists
        String attendanceType = rs.getString("AttendanceType"); // Assuming this column exists
        String attendanceStatus = rs.getString("AttendanceStatus"); // Assuming this column exists

        // Leave-related fields
        LocalDate dateFrom = rs.getDate("DateFrom") != null ? rs.getDate("DateFrom").toLocalDate() : null;
        LocalDate dateTo = rs.getDate("DateTo") != null ? rs.getDate("DateTo").toLocalDate() : null;
        double duration = rs.getDouble("Duration"); // Assuming this column exists
        double vlCount = rs.getDouble("VL_Count"); // Assuming this column exists
        double vlUsed = rs.getDouble("VL_Used");
        double vlBalance = rs.getDouble("VL_Balance");
        double slCount = rs.getDouble("SL_Count");
        double slUsed = rs.getDouble("SL_Used");
        double slBalance = rs.getDouble("SL_Balance");

        return new AttendanceRecord(
            attendanceId, employeeId, attendanceDate, loginTime, logoutTime, totalHours,
            overtimeHours, attendanceType, attendanceStatus, dateFrom, dateTo, duration,
            vlCount, vlUsed, vlBalance, slCount, slUsed, slBalance
        );
    }
}