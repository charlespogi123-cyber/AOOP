package oop_motorph;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DataMigration {

    public static void main(String[] args) {
        migrateUsers();
        migrateEmployees();
        migrateSalaryData();
        migrateAttendanceData();
        migratePayrollData();
        System.out.println("Data migration completed!");
    }

    private static void migrateUsers() {
        String insertQuery = "INSERT INTO users (username, password, role, first_name, last_name) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery);
             InputStream in = DataMigration.class.getResourceAsStream("/oop_motorph/resources/CSV_Users.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length >= 5) {
                    stmt.setString(1, values[0].trim());
                    stmt.setString(2, values[1].trim());
                    stmt.setString(3, values[2].trim());
                    stmt.setString(4, values[3].trim());
                    stmt.setString(5, values[4].trim());
                    stmt.executeUpdate();
                }
            }
            System.out.println("Users migrated successfully");

        } catch (SQLException | IOException e) {
            System.err.println("Error migrating users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrateEmployees() {
        String insertQuery = "INSERT INTO employees (emp_id, first_name, last_name, birthday, address, phone_number, status, position, immediate_supervisor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery);
             InputStream in = DataMigration.class.getResourceAsStream("/oop_motorph/resources/CSV_Employees.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length >= 9) {
                    stmt.setString(1, values[0].trim());
                    stmt.setString(2, values[1].trim());
                    stmt.setString(3, values[2].trim());
                    stmt.setString(4, values[3].trim());
                    stmt.setString(5, values[4].trim());
                    stmt.setString(6, values[5].trim());
                    stmt.setString(7, values[6].trim());
                    stmt.setString(8, values[7].trim());
                    stmt.setString(9, values[8].trim());
                    stmt.executeUpdate();
                }
            }
            System.out.println("Employees migrated successfully");

        } catch (SQLException | IOException e) {
            System.err.println("Error migrating employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrateSalaryData() {
        String insertQuery = "INSERT INTO salary (emp_id, first_name, last_name, sss_no, philhealth_no, tin_no, pagibig_no, basic_salary, rice_allowance, phone_allowance, clothing_allowance, gross_semi, hourly_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery);
             InputStream in = DataMigration.class.getResourceAsStream("/oop_motorph/resources/CSV_Salary.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length >= 13) {
                    stmt.setString(1, values[0].trim());
                    stmt.setString(2, values[1].trim());
                    stmt.setString(3, values[2].trim());
                    stmt.setString(4, values[3].trim());
                    stmt.setString(5, values[4].trim());
                    stmt.setString(6, values[5].trim());
                    stmt.setString(7, values[6].trim());
                    stmt.setDouble(8, parseDouble(values[7]));
                    stmt.setDouble(9, parseDouble(values[8]));
                    stmt.setDouble(10, parseDouble(values[9]));
                    stmt.setDouble(11, parseDouble(values[10]));
                    stmt.setDouble(12, parseDouble(values[11]));
                    stmt.setDouble(13, parseDouble(values[12]));
                    stmt.executeUpdate();
                }
            }
            System.out.println("Salary data migrated successfully");

        } catch (SQLException | IOException e) {
            System.err.println("Error migrating salary data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrateAttendanceData() {
        String insertQuery = "INSERT INTO attendance (emp_id, first_name, last_name, employee_status, position, immediate_supervisor, date_from, date_to, time_in, time_out, hours_worked, duration, attendance_type, attendance_status, vl_count, vl_used, vl_balance, sl_count, sl_used, sl_balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String checkEmployeeQuery = "SELECT COUNT(*) FROM employees WHERE emp_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery);
             PreparedStatement checkStmt = conn.prepareStatement(checkEmployeeQuery);
             InputStream in = DataMigration.class.getResourceAsStream("/oop_motorph/resources/CSV_Attendance.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line;
            br.readLine(); // Skip header
            int processedCount = 0;
            int skippedCount = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length >= 20) {
                    String empId = values[0].trim();

                    // Check if employee exists
                    checkStmt.setString(1, empId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            stmt.setString(1, empId);
                            stmt.setString(2, values[1].trim());
                            stmt.setString(3, values[2].trim());
                            stmt.setString(4, values[3].trim());
                            stmt.setString(5, values[4].trim());
                            stmt.setString(6, values[5].trim());
                            stmt.setDate(7, parseDate(values[6]));
                            stmt.setDate(8, parseDate(values[7]));
                            stmt.setTime(9, parseTime(values[8]));
                            stmt.setTime(10, parseTime(values[9]));
                            stmt.setDouble(11, parseDouble(values[10]));
                            stmt.setInt(12, parseInt(values[11]));
                            stmt.setString(13, values[12].trim());
                            stmt.setString(14, values[13].trim());
                            stmt.setDouble(15, parseDouble(values[14]));
                            stmt.setDouble(16, parseDouble(values[15]));
                            stmt.setDouble(17, parseDouble(values[16]));
                            stmt.setDouble(18, parseDouble(values[17]));
                            stmt.setDouble(19, parseDouble(values[18]));
                            stmt.setDouble(20, parseDouble(values[19]));
                            stmt.executeUpdate();
                            processedCount++;
                        } else {
                            System.err.println("Employee with ID " + empId + " not found. Skipping attendance record.");
                            skippedCount++;
                        }
                    }
                }
            }
            System.out.println("Attendance data migrated successfully. Processed: " + processedCount + ", Skipped: " + skippedCount);

        } catch (SQLException | IOException e) {
            System.err.println("Error migrating attendance data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migratePayrollData() {
        String insertQuery = "INSERT INTO payroll (transaction_id, emp_id, first_name, last_name, pay_date_from, pay_date_to, basic_salary, hourly_rate, total_allowances, hrs_per_month, total_hrs_worked, adj_earnings, total_earnings, tardiness_absences, ee_tax, ee_sss, ee_pagibig, ee_philhealth, adj_deductions, total_deductions, net_pay, pay_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String checkEmployeeQuery = "SELECT COUNT(*) FROM employees WHERE emp_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery);
             PreparedStatement checkStmt = conn.prepareStatement(checkEmployeeQuery);
             InputStream in = DataMigration.class.getResourceAsStream("/oop_motorph/resources/CSV_Payroll.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line;
            br.readLine(); // Skip header
            int processedCount = 0;
            int skippedCount = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                if (values.length >= 22 && !values[0].trim().isEmpty()) {
                    String empId = values[1].trim();

                    // Check if employee exists
                    checkStmt.setString(1, empId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            stmt.setString(1, values[0].trim());
                            stmt.setString(2, empId);
                            stmt.setString(3, values[2].trim());
                            stmt.setString(4, values[3].trim());
                            stmt.setDate(5, parseDate(values[4]));
                            stmt.setDate(6, parseDate(values[5]));
                            stmt.setDouble(7, parseDouble(values[6]));
                            stmt.setDouble(8, parseDouble(values[7]));
                            stmt.setDouble(9, parseDouble(values[8]));
                            stmt.setDouble(10, parseDouble(values[9]));
                            stmt.setDouble(11, parseDouble(values[10]));
                            stmt.setDouble(12, parseDouble(values[11]));
                            stmt.setDouble(13, parseDouble(values[12]));
                            stmt.setDouble(14, parseDouble(values[13]));
                            stmt.setDouble(15, parseDouble(values[14]));
                            stmt.setDouble(16, parseDouble(values[15]));
                            stmt.setDouble(17, parseDouble(values[16]));
                            stmt.setDouble(18, parseDouble(values[17]));
                            stmt.setDouble(19, parseDouble(values[18]));
                            stmt.setDouble(20, parseDouble(values[19]));
                            stmt.setDouble(21, parseDouble(values[20]));
                            stmt.setString(22, values[21].trim());
                            stmt.executeUpdate();
                            processedCount++;
                        } else {
                            System.err.println("Employee with ID " + empId + " not found. Skipping payroll record.");
                            skippedCount++;
                        }
                    }
                }
            }
            System.out.println("Payroll data migrated successfully. Processed: " + processedCount + ", Skipped: " + skippedCount);

        } catch (SQLException | IOException e) {
            System.err.println("Error migrating payroll data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static java.sql.Date parseDate(String value) {
        try {
            if (value == null || value.trim().isEmpty() || value.equals("01-Jan-00")) {
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
            LocalDate date = LocalDate.parse(value.trim(), formatter);
            return java.sql.Date.valueOf(date);
        } catch (Exception e) {
            return null;
        }
    }

    private static java.sql.Time parseTime(String value) {
        try {
            if (value == null || value.trim().isEmpty() || value.equals("0:00:00")) {
                return null;
            }
            LocalTime time = LocalTime.parse(value.trim());
            return java.sql.Time.valueOf(time);
        } catch (Exception e) {
            return null;
        }
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}