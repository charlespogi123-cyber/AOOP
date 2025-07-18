package oop_motorph.model;

/**
 * Represents an employee's contact information.
 */
public class ContactInfo {
    private int contactId; // Primary Key
    private String employeeId; // Foreign key to Employee
    private String phoneNumber;

    /**
     * Constructs a new ContactInfo object with a generated ID.
     * @param contactId The unique ID for the contact info record.
     * @param employeeId The ID of the employee this contact info belongs to.
     * @param phoneNumber The employee's phone number.
     */
    public ContactInfo(int contactId, String employeeId, String phoneNumber) {
        this.contactId = contactId;
        this.employeeId = employeeId;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Constructs a new ContactInfo object for adding to the database (ID will be auto-generated).
     * @param employeeId The ID of the employee this contact info belongs to.
     * @param phoneNumber The employee's phone number.
     */
    public ContactInfo(String employeeId, String phoneNumber) {
        this(-1, employeeId, phoneNumber); // Use -1 as a placeholder for auto-generated ID
    }

    // Getters and Setters
    public int getContactId() { return contactId; }
    public void setContactId(int contactId) { this.contactId = contactId; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "ContactInfo{" +
               "contactId=" + contactId +
               ", employeeId='" + employeeId + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               '}';
    }
}