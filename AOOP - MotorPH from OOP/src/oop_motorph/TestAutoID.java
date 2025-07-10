package oop_motorph;

public class TestAutoID {
    public static void main(String[] args) {
        System.out.println("Testing auto-generated Employee ID...");
        
        // Test getting next employee ID
        int nextId = DatabaseHandler.getNextEmployeeId();
        System.out.println("Next Employee ID: " + nextId);
        
        // Test multiple calls to ensure it's working correctly
        for (int i = 0; i < 3; i++) {
            int id = DatabaseHandler.getNextEmployeeId();
            System.out.println("Next ID (call " + (i + 1) + "): " + id);
        }
        
        System.out.println("Test completed!");
    }
}
