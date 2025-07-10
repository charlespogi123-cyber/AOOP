package oop_motorph;

public class TestCRUD {
    public static void main(String[] args) {
        System.out.println("Testing CRUD operations...");
        
        // Test getting existing data
        var employees = DatabaseHandler.getEmployeeData();
        System.out.println("Initial employee count: " + employees.size());
        
        // Test adding a new employee
        EmpDetails newEmp = new EmpDetails(
            "99999",
            "Test",
            "Employee",
            "1990-01-01",
            "123 Test Street, Test City, Test Region",
            "123-456-7890",
            "Regular",
            "Test Position",
            "Manuel III Garcia" // Use existing supervisor
        );
        
        System.out.println("Adding test employee...");
        DatabaseHandler.addEmployee(newEmp);
        
        // Check if employee was added
        employees = DatabaseHandler.getEmployeeData();
        System.out.println("Employee count after add: " + employees.size());
        
        // Test updating the employee
        newEmp.setFirstName("Updated");
        newEmp.setLastName("TestEmployee");
        newEmp.setAddress("456 Updated Street, Updated City, Updated Region");
        
        System.out.println("Updating test employee...");
        DatabaseHandler.updateEmployee(newEmp);
        
        // Test deleting the employee
        System.out.println("Deleting test employee...");
        DatabaseHandler.deleteEmployee("99999");
        
        // Check final count
        employees = DatabaseHandler.getEmployeeData();
        System.out.println("Final employee count: " + employees.size());
        
        System.out.println("CRUD test completed!");
    }
}
