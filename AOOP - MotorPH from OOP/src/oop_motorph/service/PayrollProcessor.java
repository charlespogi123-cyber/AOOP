// PayrollProcessor.java (Moved to service layer)
package oop_motorph.service;

import oop_motorph.model.Compensation;
import oop_motorph.model.GovernmentID;
import oop_motorph.model.AttendanceRecord;
import oop_motorph.model.PayrollRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for payroll calculation logic.
 * This defines the contract for any class that computes payroll.
 */
public interface PayrollProcessor {

    /**
     * Calculates the SSS employee contribution.
     * @param monthlyGross The employee's monthly gross income.
     * @return The calculated SSS contribution.
     */
    double calculateEESSSContribution(double monthlyGross); // Renamed from calculateEESSSRate

    /**
     * Calculates the PhilHealth employee contribution.
     * @param basicProrata The employee's basic prorated salary.
     * @return The calculated PhilHealth contribution.
     */
    double calculateEEPhilhealthContribution(double basicProrata); // Renamed from calculateEEPhilhealthRate

    /**
     * Calculates the Pag-IBIG employee contribution.
     * @param monthlyGross The employee's monthly gross income.
     * @return The calculated Pag-IBIG contribution.
     */
    double calculateEEPagibigContribution(double monthlyGross); // Renamed from calculateEEPagibigRate

    /**
     * Calculates the Withholding Tax (Tax) for an employee.
     * @param taxableIncome The employee's taxable income after deductions.
     * @return The calculated withholding tax.
     */
    double calculateEETax(double taxableIncome); // Renamed from calculateEETaxRate

    /**
     * Calculates the total allowances for an employee.
     * @param compensation The Compensation object containing allowance details.
     * @return The total allowance amount.
     */
    double calculateTotalAllowance(Compensation compensation); // Takes Compensation as input

    /**
     * Calculates the gross income for a given payroll period.
     * @param basicSalary The employee's basic salary.
     * @param totalAllowances The total calculated allowances.
     * @param totalHoursWorked The total hours worked in the period.
     * @param hourlyRate The employee's hourly rate.
     * @return The gross income for the period.
     */
    double calculateGrossIncome(double basicSalary, double totalAllowances, double totalHoursWorked, double hourlyRate);

    /**
     * Calculates the net pay for an employee for a specific payroll period.
     * This method orchestrates the various calculation steps.
     *
     * @param employeeId The ID of the employee.
     * @param compensation The employee's compensation details.
     * @param governmentID The employee's government ID details.
     * @param attendanceRecords The attendance records for the payroll period.
     * @param payrollPeriodStartDate The start date of the payroll period.
     * @param payrollPeriodEndDate The end date of the payroll period.
     * @return A PayrollRecord object containing all calculated payroll figures.
     */
    PayrollRecord generatePayroll(
        int employeeId,
        Compensation compensation,
        GovernmentID governmentID,
        List<AttendanceRecord> attendanceRecords,
        LocalDate payrollPeriodStartDate,
        LocalDate payrollPeriodEndDate
    );
}