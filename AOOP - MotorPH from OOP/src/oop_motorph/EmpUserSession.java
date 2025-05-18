
package oop_motorph;


public class EmpUserSession {
    private static EmpUserSession instance;

    // Private fields to store the current user's data and role
    private EmpDetails currentUser;
    private String role;

    // Private constructor to prevent instantiation from outside
    private EmpUserSession() {
        // Constructor is private to enforce singleton pattern
    }

    // Public method to get the instance of the class
    public static EmpUserSession getInstance() {
        if (instance == null) {
            instance = new EmpUserSession();
        }
        return instance;
    }

    // Method to set the current user and their role
    public void setCurrentUser(EmpDetails user, String role) {
        this.currentUser = user;
        this.role = role;
    }

    // Method to get the current user
    public EmpDetails getCurrentUser() {
        return currentUser;
    }

    // Method to get the current user's role
    public String getRole() {
        return role;
    }

    // Method to clear the session (for logout)
    public void clearSession() {
        this.currentUser = null;
        this.role = null;
    }
}
