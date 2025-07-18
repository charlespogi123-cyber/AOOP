package oop_motorph.model;

public class EmployeeStatus {
    private Integer statusId;
    private String statusName;

    public EmployeeStatus(Integer statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    public EmployeeStatus(String statusName) {
        this(null, statusName);
    }

    // Getters and Setters
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}