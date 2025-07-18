package oop_motorph.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceRecord {
    private Integer attendanceId; 
    private Integer employeeId;
    private LocalDate attendanceDate;
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private double totalHours;
    // Potentially calculated or derived fields
    private double overtimeHours;
    private String attendanceType; // "Regular", "Overtime", "Leave"
    private String attendanceStatus; //  "Present", "Absent", "Late"

    // Leave-related fields (from EmpAttLeave, we separate Leave model)
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private double duration; // Total hours/days for leave
    private double vlCount; // Vacation Leave total
    private double vlUsed;
    private double vlBalance;
    private double slCount; // Sick Leave total
    private double slUsed;
    private double slBalance;

    public AttendanceRecord(Integer attendanceId, Integer employeeId, LocalDate attendanceDate, LocalTime loginTime, LocalTime logoutTime, double totalHours, double overtimeHours, String attendanceType, String attendanceStatus, LocalDate dateFrom, LocalDate dateTo, double duration, double vlCount, double vlUsed, double vlBalance, double slCount, double slUsed, double slBalance) {
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.totalHours = totalHours;
        this.overtimeHours = overtimeHours;
        this.attendanceType = attendanceType;
        this.attendanceStatus = attendanceStatus;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.duration = duration;
        this.vlCount = vlCount;
        this.vlUsed = vlUsed;
        this.vlBalance = vlBalance;
        this.slCount = slCount;
        this.slUsed = slUsed;
        this.slBalance = slBalance;
    }

    // Constructor for creating new records
    public AttendanceRecord(Integer employeeId, LocalDate attendanceDate, LocalTime loginTime, LocalTime logoutTime, double totalHours) {
        this(null, employeeId, attendanceDate, loginTime, logoutTime, totalHours, 0.0, "Regular", "Present", null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    // Constructor for attendance request 
    public AttendanceRecord(String empId, LocalDate attDateFrom, LocalDate attDateTo, LocalTime timeIn, LocalTime timeOut, double hoursWorked, double duration, String attendanceType, String attendanceStatus, double vlCount, double vlUsed, double vlBalance, double slCount, double slUsed, double slBalance) {
        this(null, Integer.parseInt(empId), attDateFrom, timeIn, timeOut, hoursWorked, 0.0, attendanceType, attendanceStatus, attDateFrom, attDateTo, duration, vlCount, vlUsed, vlBalance, slCount, slUsed, slBalance);
    }

    // Getters and Setters
    public Integer getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Integer attendanceId) { this.attendanceId = attendanceId; }
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    public LocalTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalTime loginTime) { this.loginTime = loginTime; }
    public LocalTime getLogoutTime() { return logoutTime; }
    public void setLogoutTime(LocalTime logoutTime) { this.logoutTime = logoutTime; }
    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }
    public String getAttendanceType() { return attendanceType; }
    public void setAttendanceType(String attendanceType) { this.attendanceType = attendanceType; }
    public String getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }
    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }
    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }
    public double getVlCount() { return vlCount; }
    public double getVlUsed() { return vlUsed; }
    public double getVlBalance() { return vlBalance; }
    public double getSlCount() { return slCount; }
    public double getSlUsed() { return slUsed; }
    public double getSlBalance() { return slBalance; }
}
