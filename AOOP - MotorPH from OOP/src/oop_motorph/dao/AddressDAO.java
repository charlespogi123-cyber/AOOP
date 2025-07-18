package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Data Access Object for Address entities.
 * Handles database operations related to addresses.
 */
public class AddressDAO {

    /**
     * Finds an address by its ID.
     *
     * @param addressId The ID of the address to find.
     * @return An Optional containing the Address object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Address> findById(int addressId) throws SQLException {
        String query = "SELECT AddressID, StreetAddress, City, Region FROM addresses WHERE AddressID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, addressId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Address(
                            rs.getInt("AddressID"),
                            rs.getString("StreetAddress"),
                            rs.getString("City"),
                            rs.getString("Region")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds an address by its street address.
     *
     * @param streetAddress The street address to search for.
     * @return An Optional containing the Address object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<Address> findByStreetAddress(String streetAddress) throws SQLException {
        String query = "SELECT AddressID, StreetAddress, City, Region FROM addresses WHERE StreetAddress = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, streetAddress);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Address(
                            rs.getInt("AddressID"),
                            rs.getString("StreetAddress"),
                            rs.getString("City"),
                            rs.getString("Region")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Saves a new address to the database.
     * Assigns the generated AddressID to the Address object.
     *
     * @param address The Address object to save. Its addressId field will be updated.
     * @throws SQLException if a database access error occurs.
     */
    public void save(Address address) throws SQLException {
        String query = "INSERT INTO addresses (StreetAddress, City, Region) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, address.getStreetAddress());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getRegion());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    address.setAddressId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Updates an existing address in the database.
     *
     * @param address The Address object with updated information. Must have a valid AddressID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(Address address) throws SQLException {
        String query = "UPDATE addresses SET StreetAddress = ?, City = ?, Region = ? WHERE AddressID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, address.getStreetAddress());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getRegion());
            stmt.setInt(4, address.getAddressId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an address by its ID.
     *
     * @param addressId The ID of the address to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(int addressId) throws SQLException {
        String query = "DELETE FROM addresses WHERE AddressID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, addressId);
            stmt.executeUpdate();
        }
    }
}