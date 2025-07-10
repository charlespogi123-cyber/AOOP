package oop_motorph;

import java.sql.*;

public class CheckTableSchema {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
            System.out.println("Checking employees table schema:");
            
            // Check table structure
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "employees", null);
            
            System.out.println("Columns in employees table:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("- " + columnName + " (" + dataType + ")");
            }
            
            // Check addresses table
            System.out.println("\nChecking addresses table schema:");
            columns = metaData.getColumns(null, null, "addresses", null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("- " + columnName + " (" + dataType + ")");
            }
            
            // Check governmentids table
            System.out.println("\nChecking governmentids table schema:");
            columns = metaData.getColumns(null, null, "governmentids", null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("- " + columnName + " (" + dataType + ")");
            }
            
            // Check compensation table columns in detail
            System.out.println("\nChecking compensation table columns with details:");
            columns = metaData.getColumns(null, null, "compensation", null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                String isGenerated = columns.getString("IS_GENERATEDCOLUMN");
                String defaultValue = columns.getString("COLUMN_DEF");
                System.out.println("- " + columnName + " (" + dataType + ")" + 
                                 (isGenerated != null && isGenerated.equals("YES") ? " [GENERATED]" : "") +
                                 (defaultValue != null ? " DEFAULT: " + defaultValue : ""));
            }
            
            // Check attendance table
            System.out.println("\nChecking attendance table schema:");
            columns = metaData.getColumns(null, null, "attendance", null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("- " + columnName + " (" + dataType + ")");
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
