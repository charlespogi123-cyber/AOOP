package oop_motorph.model;

public class User {
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;

    // Constructor, Getters, Setters
    public User(String username, String password, String role, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}