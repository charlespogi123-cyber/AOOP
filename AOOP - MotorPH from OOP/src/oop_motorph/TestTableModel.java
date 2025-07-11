package oop_motorph;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TestTableModel {
    public static void main(String[] args) {
        System.out.println("Testing table model with payroll data:");
        
        // Get column names and data
        String[] columnNames = DatabaseHandler.getPayrollColumnNames();
        List<String[]> payrollData = DatabaseHandler.getPayrollData();
        
        System.out.println("Column names count: " + columnNames.length);
        for (int i = 0; i < columnNames.length; i++) {
            System.out.println("Column " + i + ": " + columnNames[i]);
        }
        
        System.out.println("\nData row length: " + (payrollData.isEmpty() ? 0 : payrollData.get(0).length));
        
        // Create table model like the form does
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columnNames);
        
        // Add some data and check if column 14 exists and is accessible
        if (!payrollData.isEmpty()) {
            String[] firstRow = payrollData.get(0);
            model.addRow(firstRow);
            
            System.out.println("Table model row count: " + model.getRowCount());
            System.out.println("Table model column count: " + model.getColumnCount());
            
            if (model.getColumnCount() > 14) {
                Object value = model.getValueAt(0, 14);
                System.out.println("Value at column 14: " + value);
                System.out.println("Value type: " + (value != null ? value.getClass().getSimpleName() : "null"));
            } else {
                System.out.println("Column 14 does not exist in the model!");
            }
        }
    }
}
