package oop_motorph.model;

public class Address {
    private Integer addressId; // Can be null for new addresses
    private String streetAddress;
    private String city;
    private String region;

    public Address(Integer addressId, String streetAddress, String city, String region) {
        this.addressId = addressId;
        this.streetAddress = streetAddress;
        this.city = city;
        this.region = region;
    }

    public Address(String streetAddress, String city, String region) {
        this(null, streetAddress, city, region);
    }

    // Getters and Setters
    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
    public String getStreetAddress() { return streetAddress; }
    public String getCity() { return city; }
    public String getRegion() { return region; }

    // Utility to parse address string (we had this in DH)
    public static Address fromString(String fullAddress) {
        // This is a simplified parse. our old DH logic had more nuanced splitting.
        // we might need a more robust parsing logic here.
        if (fullAddress == null || fullAddress.trim().isEmpty() || fullAddress.equals("Address not available")) {
            return new Address("Unknown", "Unknown", "Unknown");
        }
        String[] parts = fullAddress.split(",\\s*");
        String street = parts.length > 0 ? parts[0] : "";
        String city = parts.length > 1 ? parts[1] : "";
        String region = parts.length > 2 ? parts[2] : "";
        return new Address(street, city, region);
    }

    public String toFullString() {
        String full = "";
        if (streetAddress != null && !streetAddress.isEmpty()) full += streetAddress;
        if (city != null && !city.isEmpty()) full += (full.isEmpty() ? "" : ", ") + city;
        if (region != null && !region.isEmpty()) full += (full.isEmpty() ? "" : ", ") + region;
        return full.isEmpty() ? "Address not available" : full;
    }
}