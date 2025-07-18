package oop_motorph.model;

import java.time.LocalDate;

/**
 * Represents an employee's leave request.
 */
public class LeaveRequest {
    private int requestId; // Primary Key
    private String employeeId; // Foreign key to Employee
    private String leaveType; // e.g., "Sick Leave", "Vacation Leave"
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfDays;
    private String status; // e.g., "Pending", "Approved", "Rejected"
    private String reason;
    private LocalDate dateFiled;

    /**
     * Constructs a new LeaveRequest object.
     * @param requestId The unique ID for the leave request.
     * @param employeeId The ID of the employee making the request.
     * @param leaveType The type of leave (e.g., Sick Leave, Vacation Leave).
     * @param startDate The start date of the leave.
     * @param endDate The end date of the leave.
     * @param numberOfDays The total number of leave days requested.
     * @param status The current status of the leave request (e.g., Pending, Approved).
     * @param reason The reason for the leave.
     * @param dateFiled The date the leave request was filed.
     */
    public LeaveRequest(int requestId, String employeeId, String leaveType, LocalDate startDate, LocalDate endDate,
                        int numberOfDays, String status, String reason, LocalDate dateFiled) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.status = status;
        this.reason = reason;
        this.dateFiled = dateFiled;
    }

    /**
     * Constructs a new LeaveRequest object for adding to the database (ID will be auto-generated).
     * @param employeeId The ID of the employee making the request.
     * @param leaveType The type of leave (e.g., Sick Leave, Vacation Leave).
     * @param startDate The start date of the leave.
     * @param endDate The end date of the leave.
     * @param numberOfDays The total number of leave days requested.
     * @param status The current status of the leave request (e.g., Pending, Approved).
     * @param reason The reason for the leave.
     * @param dateFiled The date the leave request was filed.
     */
    public LeaveRequest(String employeeId, String leaveType, LocalDate startDate, LocalDate endDate,
                        int numberOfDays, String status, String reason, LocalDate dateFiled) {
        this(-1, employeeId, leaveType, startDate, endDate, numberOfDays, status, reason, dateFiled);
    }

    // Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(int numberOfDays) { this.numberOfDays = numberOfDays; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDate getDateFiled() { return dateFiled; }
    public void setDateFiled(LocalDate dateFiled) { this.dateFiled = dateFiled; }

    @Override
    public String toString() {
        return "LeaveRequest{" +
               "requestId=" + requestId +
               ", employeeId='" + employeeId + '\'' +
               ", leaveType='" + leaveType + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", numberOfDays=" + numberOfDays +
               ", status='" + status + '\'' +
               ", reason='" + reason + '\'' +
               ", dateFiled=" + dateFiled +
               '}';
    }
}