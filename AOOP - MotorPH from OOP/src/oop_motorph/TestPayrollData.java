package oop_motorph;

import java.sql.*;
import java.util.List;

public class TestPayrollData {
    public static void main(String[] args) {
        System.out.println("=== TESTING PAYROLL DATA ===");
        
        // Test 1: Check if Payroll table exists and has data
        testPayrollTableExists();
        
        // Test 2: Check what's returned by getPayrollData()
        testGetPayrollData();
        
        // Test 3: Check individual tables
        testIndividualTables();
    }
    
    private static void testPayrollTableExists() {
        System.out.println("\n1. Testing Payroll table existence and data:");
        
        try (Connection conn = DatabaseHandler.getConnection()) {
            // Check if table exists and count rows
            String query = "SELECT COUNT(*) as row_count FROM Payroll";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    int count = rs.getInt("row_count");
                    System.out.println("   Payroll table has " + count + " rows");
                    
                    if (count == 0) {
                        System.out.println("   WARNING: Payroll table is empty!");
                    }
                } else {
                    System.out.println("   Could not get row count");
                }
            }
            
            // Show table structure
            System.out.println("\n   Payroll table columns:");
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, "Payroll", null)) {
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    System.out.println("   - " + columnName + " (" + dataType + ")");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("   ERROR: " + e.getMessage());
            if (e.getMessage().contains("doesn't exist")) {
                System.out.println("   The Payroll table does not exist in the database!");
            }
        }
    }
    
    private static void testGetPayrollData() {
        System.out.println("\n2. Testing DatabaseHandler.getPayrollData():");
        
        try {
            List<String[]> payrollData = DatabaseHandler.getPayrollData();
            System.out.println("   getPayrollData() returned " + payrollData.size() + " rows");
            
            if (payrollData.isEmpty()) {
                System.out.println("   No payroll data returned!");
            } else {
                System.out.println("   Sample row (first record):");
                String[] firstRow = payrollData.get(0);
                String[] columnNames = DatabaseHandler.getPayrollColumnNames();
                
                for (int i = 0; i < Math.min(columnNames.length, firstRow.length); i++) {
                    System.out.println("   " + columnNames[i] + ": " + firstRow[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("   ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testIndividualTables() {
        System.out.println("\n3. Testing individual tables:");
        
        String[] tables = {"employees", "governmentids", "positions", "departments"};
        
        for (String tableName : tables) {
            try (Connection conn = DatabaseHandler.getConnection()) {
                String query = "SELECT COUNT(*) FROM " + tableName;
                try (PreparedStatement stmt = conn.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
                    
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("   " + tableName + " table: " + count + " rows");
                    }
                }
            } catch (SQLException e) {
                System.out.println("   " + tableName + " table: ERROR - " + e.getMessage());
            }
        }
    }
}
