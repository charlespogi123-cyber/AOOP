package oop_motorph;

public class TestLogin {
    public static void main(String[] args) {
        System.out.println("Testing login functionality...");
        
        // Test loading credentials
        var credentials = DatabaseHandler.loadCredentials();
        System.out.println("Loaded " + credentials.size() + " user credentials");
        
        // Test getting employee data
        try {
            var employees = DatabaseHandler.getEmployeeData();
            System.out.println("Loaded " + employees.size() + " employees");
            
            if (!employees.isEmpty()) {
                System.out.println("First employee: " + employees.get(0).getFirstName() + " " + employees.get(0).getLastName());
            }
        } catch (Exception e) {
            System.err.println("Error loading employees: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test getting salary data
        try {
            var salaries = DatabaseHandler.getSalaryData();
            System.out.println("Loaded " + salaries.size() + " salary records");
        } catch (Exception e) {
            System.err.println("Error loading salaries: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test getting attendance data
        try {
            var attendance = DatabaseHandler.getAttendanceData();
            System.out.println("Loaded " + attendance.size() + " attendance records");
        } catch (Exception e) {
            System.err.println("Error loading attendance: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Test completed!");
    }
}
