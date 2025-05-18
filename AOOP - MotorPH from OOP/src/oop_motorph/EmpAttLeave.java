
package oop_motorph;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmpAttLeave extends EmpDetails {
    
    private LocalDate attdateFrom;
    private LocalDate attdateTo;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private double hoursWorked;
    private double duration;
    private String attendanceType;
    private String attendanceStat;
    private double vlCount;
    private double vlUsed;
    private double vlBal;
    private double slCount;
    private double slUsed;
    private double slBal;

    // Constructor
    public EmpAttLeave(String empID, String firstName, String lastName, String empStatus, String position, String immSupervisor, 
                       LocalDate attdateFrom, LocalDate attdateTo, LocalTime timeIn, LocalTime timeOut, 
                       double hoursWorked, double duration, String attendanceType, String attendanceStat, 
                       double vlCount, double vlUsed, double vlBal, double slCount, double slUsed, double slBal) {
        
        super(empID, firstName, lastName, empStatus, position, immSupervisor);
        this.attdateFrom = attdateFrom;
        this.attdateTo = attdateTo;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hoursWorked = hoursWorked;
        this.duration = duration;
        this.attendanceType = attendanceType;
        this.attendanceStat = attendanceStat;
        this.vlCount = vlCount;
        this.vlUsed = vlUsed;
        this.vlBal = vlBal;
        this.slCount = slCount;
        this.slUsed = slUsed;
        this.slBal = slBal;
    }

    // Getters and Setters
    public LocalDate getAttDateFrom() { return attdateFrom; }
    public void setAttDateFrom(LocalDate attdateFrom) { this.attdateFrom = attdateFrom; }

    public LocalDate getAttDateTo() { return attdateTo; }
    public void setAttDateTo(LocalDate attdateTo) { this.attdateTo = attdateTo; }

    public LocalTime getTimeIn() { return timeIn; }
    public void setTimeIn(LocalTime timeIn) { this.timeIn = timeIn; }
    
    public LocalTime getTimeOut() { return timeOut; }
    public void setTimeOut(LocalTime timeOut) { this.timeOut = timeOut; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
 
    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public String getAttendanceType() { return attendanceType; }
    public void setAttendanceType(String attendanceType) { this.attendanceType = attendanceType; }

    public String getAttendanceStatus() { return attendanceStat; }
    public void setAttendanceStatus(String attendanceStat) { this.attendanceStat = attendanceStat; }

    public double getVlCount() { return vlCount; }
    public void setVlCount(double vlCount) { this.vlCount = vlCount; }

    public double getVlUsed() { return vlUsed; }
    public void setVlUsed(double vlUsed) { this.vlUsed = vlUsed; }

    public double getVlBalance() { return vlBal; }
    public void setVlBalance(double vlBal) { this.vlBal = vlBal; }

    public double getSlCount() { return slCount; }
    public void setSlCount(double slCount) { this.slCount = slCount; }

    public double getSlUsed() { return slUsed; }
    public void setSlUsed(double slUsed) { this.slUsed = slUsed; }

    public double getSlBalance() { return slBal; }
    public void setSlBalance(double slBal) { this.slBal = slBal; }
}

