package oop_motorph.dao;

import oop_motorph.DatabaseConfig;
import oop_motorph.model.PayrollRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for PayrollRecord entities.
 * Handles CRUD operations for employee payroll records.
 */
public class PayrollDAO {

    /**
     * Finds a payroll record by its ID.
     *
     * @param payrollId The ID of the payroll record.
     * @return An Optional containing the PayrollRecord object if found, or empty if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<PayrollRecord> findById(int payrollId) throws SQLException {
        String query = "SELECT PayrollID, EmployeeID, PayrollPeriodStartDate, PayrollPeriodEndDate, Gross, SSSContribution, PhilHealthContribution, PagIBIGContribution, Tax, NetPay FROM Payroll WHERE PayrollID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, payrollId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPayrollRecord(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all payroll data with selected joined employee and government ID details.
     * This method is designed to provide comprehensive payroll details for reporting.
     *
     * @return A list of PayrollRecord objects (potentially with some joined data mapped internally).
     * @throws SQLException if a database access error occurs.
     */
    public List<PayrollRecord> findAllWithDetails() throws SQLException {
        List<PayrollRecord> payrollList = new ArrayList<>();
        // Query adjusted to fetch data to populate the PayrollRecord model
        String query = """
            SELECT
                p.PayrollID,
                p.EmployeeID,
                p.PayrollPeriodStartDate,
                p.PayrollPeriodEndDate,
                p.Gross, -- Your original query called this 'total_earnings'
                p.SSSContribution, -- Your original query called this 'ee_sss'
                p.PhilHealthContribution, -- Your original query called this 'ee_philhealth'
                p.PagIBIGContribution, -- Your original query called this 'ee_pagibig'
                p.Tax, -- Your original query called this 'ee_tax'
                p.NetPay -- Your original query called this 'net_pay'
            FROM Payroll p
            ORDER BY p.EmployeeID DESC, p.PayrollPeriodStartDate DESC
            """;

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payrollList.add(mapResultSetToPayrollRecord(rs));
            }
        }
        return payrollList;
    }

    /**
     * Saves a new payroll record to the database.
     *
     * @param payrollRecord The PayrollRecord object to save.
     * @throws SQLException if a database access error occurs.
     */
    public void save(PayrollRecord payrollRecord) throws SQLException {
        String query = "INSERT INTO Payroll (" +
                       "EmployeeID, PayrollPeriodStartDate, PayrollPeriodEndDate, Gross, " +
                       "SSSContribution, PhilHealthContribution, PagIBIGContribution, Tax, NetPay" +
                       ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payrollRecord.getEmployeeId());
            stmt.setDate(2, Date.valueOf(payrollRecord.getPayrollPeriodStartDate()));
            stmt.setDate(3, Date.valueOf(payrollRecord.getPayrollPeriodEndDate()));
            stmt.setDouble(4, payrollRecord.getGrossSalary());
            stmt.setDouble(5, payrollRecord.getSssContribution());
            stmt.setDouble(6, payrollRecord.getPhilhealthContribution());
            stmt.setDouble(7, payrollRecord.getPagibigContribution());
            stmt.setDouble(8, payrollRecord.getTax());
            stmt.setDouble(9, payrollRecord.getNetPay());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payrollRecord.setPayrollId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Updates an existing payroll record in the database.
     *
     * @param payrollRecord The PayrollRecord object with updated information. Must have a valid PayrollID.
     * @throws SQLException if a database access error occurs.
     */
    public void update(PayrollRecord payrollRecord) throws SQLException {
        String query = "UPDATE Payroll SET " +
                       "EmployeeID = ?, PayrollPeriodStartDate = ?, PayrollPeriodEndDate = ?, Gross = ?, " +
                       "SSSContribution = ?, PhilHealthContribution = ?, PagIBIGContribution = ?, Tax = ?, NetPay = ? " +
                       "WHERE PayrollID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, payrollRecord.getEmployeeId());
            stmt.setDate(2, Date.valueOf(payrollRecord.getPayrollPeriodStartDate()));
            stmt.setDate(3, Date.valueOf(payrollRecord.getPayrollPeriodEndDate()));
            stmt.setDouble(4, payrollRecord.getGrossSalary());
            stmt.setDouble(5, payrollRecord.getSssContribution());
            stmt.setDouble(6, payrollRecord.getPhilhealthContribution());
            stmt.setDouble(7, payrollRecord.getPagibigContribution());
            stmt.setDouble(8, payrollRecord.getTax());
            stmt.setDouble(9, payrollRecord.getNetPay());
            stmt.setInt(10, payrollRecord.getPayrollId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a payroll record by its ID.
     *
     * @param payrollId The ID of the payroll record to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(int payrollId) throws SQLException {
        String query = "DELETE FROM Payroll WHERE PayrollID = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, payrollId);
            stmt.executeUpdate();
        }
    }

    /**
     * Helper method to map a ResultSet row to a PayrollRecord object.
     *
     * @param rs The ResultSet containing the payroll data.
     * @return A PayrollRecord object populated with data from the ResultSet.
     * @throws SQLException if a database access error occurs during mapping.
     */
    private PayrollRecord mapResultSetToPayrollRecord(ResultSet rs) throws SQLException {
        Integer payrollId = rs.getObject("PayrollID") != null ? rs.getInt("PayrollID") : null;
        Integer employeeId = rs.getObject("EmployeeID") != null ? rs.getInt("EmployeeID") : null;
        LocalDate startDate = rs.getDate("PayrollPeriodStartDate") != null ? rs.getDate("PayrollPeriodStartDate").toLocalDate() : null;
        LocalDate endDate = rs.getDate("PayrollPeriodEndDate") != null ? rs.getDate("PayrollPeriodEndDate").toLocalDate() : null; // Assuming this column exists in DB
        double gross = rs.getDouble("Gross");
        double sssContrib = rs.getDouble("SSSContribution");
        double philhealthContrib = rs.getDouble("PhilHealthContribution");
        double pagibigContrib = rs.getDouble("PagIBIGContribution");
        double tax = rs.getDouble("Tax");
        double netPay = rs.getDouble("NetPay");

        return new PayrollRecord(
            payrollId, employeeId, startDate, endDate, gross,
            sssContrib, philhealthContrib, pagibigContrib, tax, netPay
        );
    }
}