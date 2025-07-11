package oop_motorph;

import java.sql.*;
// Note: Using DatabaseConfig from same package

public class TestPayrollColumns {
    public static void main(String[] args) {
        String query = """
            SELECT 
                p.EmployeeID,
                e.FirstName,
                e.LastName,
                pos.PositionName,
                d.DepartmentName,
                p.Gross as total_earnings,
                g.SSSNumber,
                p.SSSContribution as ee_sss,
                g.PhilHealthNumber,
                p.PhilHealthContribution as ee_philhealth,
                g.PagIBIGNumber,
                p.PagIBIGContribution as ee_pagibig,
                g.TINNumber,
                p.Tax as ee_tax,
                p.NetPay as net_pay,
                p.PayrollPeriodStartDate
            FROM Payroll p
            LEFT JOIN employees e ON p.EmployeeID = e.EmployeeID
            LEFT JOIN governmentids g ON e.EmployeeID = g.EmployeeID
            LEFT JOIN positions pos ON e.PositionID = pos.PositionID
            LEFT JOIN departments d ON e.DepartmentID = d.DepartmentID
            WHERE p.EmployeeID IS NOT NULL
            ORDER BY p.EmployeeID DESC
            LIMIT 2
            """;

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            
            System.out.println("Total columns: " + columnCount);
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ": " + meta.getColumnLabel(i));
            }
            
            System.out.println("\nFirst row data:");
            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    System.out.println("Column " + i + " (" + meta.getColumnLabel(i) + "): " + value);
                }
                
                // Specifically check the PayrollPeriodStartDate column
                Date payrollDate = rs.getDate("PayrollPeriodStartDate");
                System.out.println("\nPayrollPeriodStartDate as Date: " + payrollDate);
                System.out.println("PayrollPeriodStartDate as String: " + rs.getString("PayrollPeriodStartDate"));
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
