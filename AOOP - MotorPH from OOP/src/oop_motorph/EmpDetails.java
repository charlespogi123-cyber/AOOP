
//ENCAPSULATION

package oop_motorph;

public class EmpDetails {
    private String empID;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String address;
    private String phoneNo;
    private String empStatus;
    private String position;
    private String immSupervisor;
   
    public EmpDetails(String empID, String firstName, String lastName, String birthdate, 
                    String address, String phoneNo, String empStatus, String position, String immSupervisor) {
         
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.address = address;
        this.phoneNo = phoneNo;
        this.empStatus = empStatus;
        this.position = position;
        this.immSupervisor =immSupervisor;
  
    }

// Constructor for EmpSalaryDetails 
    public EmpDetails(String empID, String firstName, String lastName) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
//Constructor for EmpAttLeave
    public EmpDetails(String empID, String firstName, String lastName, String empStatus, String position, String immSupervisor) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.empStatus = empStatus;
        this.position = position;
        this.immSupervisor = immSupervisor;
                
    }
    
    // Getters and Setters (Encapsulation)
    public String getEmpID() { return empID; }
    public void setEmpID(String empID) { this.empID = empID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhoneNumber() { return phoneNo; }
    public void setPhoneNumber(String phoneNo) { this.phoneNo = phoneNo; }
    
    public String getEmployeeStatus() { return empStatus; }
    public void setEmployeeStatus(String empStatus) { this.empStatus = empStatus; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getImmediateSupervisor() { return immSupervisor; }
    public void setImmediateSupervisor(String immSupervisor) { this.immSupervisor = immSupervisor; }

     // Override toString method for CSV output
        @Override
        public String toString() {
            return empID + "," + firstName + "," + lastName + "," + birthdate + "," + address + "," +
                   phoneNo + "," + empStatus + "," + position + "," + immSupervisor;
        }
    }


