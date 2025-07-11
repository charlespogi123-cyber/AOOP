package oop_motorph;

import java.sql.*;
import java.util.List;

public class TestPayrollColumnData {
    public static void main(String[] args) {
        System.out.println("Testing getPayrollData() method output:");
        
        List<String[]> data = DatabaseHandler.getPayrollData();
        
        System.out.println("Total rows: " + data.size());
        
        if (!data.isEmpty()) {
            String[] firstRow = data.get(0);
            System.out.println("First row length: " + firstRow.length);
            
            for (int i = 0; i < firstRow.length; i++) {
                System.out.println("Column " + i + ": " + firstRow[i]);
            }
            
            // Specifically check column 14 (the date column)
            if (firstRow.length > 14) {
                System.out.println("\nColumn 14 value (should be date): '" + firstRow[14] + "'");
                System.out.println("Column 14 length: " + firstRow[14].length());
            }
        }
    }
}
