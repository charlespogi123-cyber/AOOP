package oop_motorph.dao;

import oop_motorph.model.ContactInfo;
import oop_motorph.config.DatabaseConfig; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

/**
 * Data Access Object for ContactInfo entities.
 * Handles CRUD operations for employee contact details (phone number only).
 */
public class ContactInfoDAO {

    /**
     * Finds contact information for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return An Optional containing the ContactInfo object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<ContactInfo> findByEmployeeId(String employeeId) throws SQLException {
        String query = "SELECT ContactID, EmployeeID, PhoneNumber FROM ContactInfo WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToContactInfo(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds contact information by its unique ContactID.
     *
     * @param contactId The unique ID of the contact record.
     * @return An Optional containing the ContactInfo object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<ContactInfo> findById(int contactId) throws SQLException {
        String query = "SELECT ContactID, EmployeeID, PhoneNumber FROM ContactInfo WHERE ContactID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contactId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToContactInfo(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all contact information records.
     *
     * @return A list of all ContactInfo objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ContactInfo> findAll() throws SQLException {
        List<ContactInfo> contactInfos = new ArrayList<>();
        String query = "SELECT ContactID, EmployeeID, PhoneNumber FROM ContactInfo";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contactInfos.add(mapResultSetToContactInfo(rs));
            }
        }
        return contactInfos;
    }

    /**
     * Saves new contact information for an employee.
     *
     * @param contactInfo The ContactInfo object to save. Its ContactID will be updated with the generated ID.
     * @throws SQLException if a database access error occurs.
     */
    public void save(ContactInfo contactInfo) throws SQLException {
        String query = "INSERT INTO ContactInfo (EmployeeID, PhoneNumber) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contactInfo.getEmployeeId());
            stmt.setString(2, contactInfo.getPhoneNumber());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contactInfo.setContactId(generatedKeys.getInt(1)); // Set the generated ID back to the model
                }
            }
        }
    }

    /**
     * Updates existing contact information for an employee.
     *
     * @param contactInfo The ContactInfo object with updated information. Must have a valid ContactID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(ContactInfo contactInfo) throws SQLException {
        String query = "UPDATE ContactInfo SET PhoneNumber = ? WHERE ContactID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, contactInfo.getPhoneNumber());
            stmt.setInt(2, contactInfo.getContactId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes contact information by its unique ContactID.
     *
     * @param contactId The ID of the contact record to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(int contactId) throws SQLException {
        String query = "DELETE FROM ContactInfo WHERE ContactID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contactId);
            stmt.executeUpdate();
        }
    }

    private ContactInfo mapResultSetToContactInfo(ResultSet rs) throws SQLException {
        int contactId = rs.getInt("ContactID");
        String employeeId = rs.getString("EmployeeID");
        String phoneNumber = rs.getString("PhoneNumber");
        return new ContactInfo(contactId, employeeId, phoneNumber);
    }
}