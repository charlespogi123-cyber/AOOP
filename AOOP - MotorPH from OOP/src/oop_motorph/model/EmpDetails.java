package oop_motorph.model;

import java.time.LocalDate;

public class EmpDetails {
    private String employeeId;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phoneNumber;
    private Integer addressId; // Foreign key
    private Integer statusId; // Foreign key
    private Integer positionId; // Foreign key
    private Integer departmentId; // Foreign key
    private Integer immediateSupervisorId; // Foreign key
    // we might also want to include the full Address, Status, Position objects
    // for convenience, but the DAO will primarily work with IDs.
    private Address address;
    private EmployeeStatus employeeStatus;
    private Position position;
    private Department department;
    private EmpDetails immediateSupervisor; // Self-referencing

    public EmpDetails(String employeeId, String firstName, String lastName, LocalDate birthday,
                      String phoneNumber, Integer addressId, Integer statusId, Integer positionId,
                      Integer departmentId, Integer immediateSupervisorId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.addressId = addressId;
        this.statusId = statusId;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.immediateSupervisorId = immediateSupervisorId;
    }

    // Overloaded constructor for getting full employee details (with joined objects)
    public EmpDetails(String employeeId, String firstName, String lastName, LocalDate birthday,
                      String phoneNumber, Address address, EmployeeStatus employeeStatus,
                      Position position, Department department, EmpDetails immediateSupervisor) {
        this(employeeId, firstName, lastName, birthday, phoneNumber,
             address != null ? address.getAddressId() : null,
             employeeStatus != null ? employeeStatus.getStatusId() : null,
             position != null ? position.getPositionId() : null,
             department != null ? department.getDepartmentId() : null,
             immediateSupervisor != null ? Integer.parseInt(immediateSupervisor.getEmployeeId()) : null);
        this.address = address;
        this.employeeStatus = employeeStatus;
        this.position = position;
        this.department = department;
        this.immediateSupervisor = immediateSupervisor;
    }


    // Getters and Setters for all fields
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Integer getPositionId() { return positionId; }
    public void setPositionId(Integer positionId) { this.positionId = positionId; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public Integer getImmediateSupervisorId() { return immediateSupervisorId; }
    public void setImmediateSupervisorId(Integer immediateSupervisorId) { this.immediateSupervisorId = immediateSupervisorId; }

    // Getters and Setters for joined objects
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public EmployeeStatus getEmployeeStatus() { return employeeStatus; }
    public void setEmployeeStatus(EmployeeStatus employeeStatus) { this.employeeStatus = employeeStatus; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public EmpDetails getImmediateSupervisor() { return immediateSupervisor; }
    public void setImmediateSupervisor(EmpDetails immediateSupervisor) { this.immediateSupervisor = immediateSupervisor; }

    // For display convenience (matches our old EmpDetails constructor)
    public String getFullAddressString() {
        return address != null ? address.toFullString() : "Address not available";
    }
    public String getEmployeeStatusString() {
        return employeeStatus != null ? employeeStatus.getStatusName() : "Status not available";
    }
    public String getPositionString() {
        return position != null ? position.getPositionName() : "Position not available";
    }
    public String getSupervisorNameString() {
        return immediateSupervisor != null ? (immediateSupervisor.getFirstName() + " " + immediateSupervisor.getLastName()) : "No supervisor";
    }
}