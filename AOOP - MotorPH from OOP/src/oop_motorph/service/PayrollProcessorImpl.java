package oop_motorph.service;

import oop_motorph.model.Compensation;
import oop_motorph.model.GovernmentID;
import oop_motorph.model.AttendanceRecord;
import oop_motorph.model.PayrollRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * Concrete implementation of the PayrollProcessor interface.
 * Contains the actual logic for calculating various payroll components.
 * This class should be pure business logic and not directly interact with the database or UI.
 */
public class PayrollProcessorImpl implements PayrollProcessor {

    // These rates would ideally come from a configuration service or database
    private static final double PHILHEALTH_PREMIUM_RATE = 0.04; // 4% PhilHealth premium
    private static final double PAGIBIG_MAX_CONTRIBUTION = 100.00; // Max employee contribution to Pag-IBIG

    @Override
    public double calculateEESSSContribution(double monthlyGross) {
        // Implement SSS contribution table logic here
        // This is a simplified example; a real SSS table is complex.
        if (monthlyGross < 3250) return 135.00;
        if (monthlyGross < 3750) return 157.50;
        if (monthlyGross >= 24750) return 1125.00; // Example max
        return 0.0;
    }

    @Override
    public double calculateEEPhilhealthContribution(double basicProrata) {
        // PhilHealth contribution is typically based on monthly basic salary
        // and is shared equally by employer and employee.
        // For simplicity, assuming a fixed rate on basic prorata.
        return basicProrata * PHILHEALTH_PREMIUM_RATE / 2; // Employee share
    }

    @Override
    public double calculateEEPagibigContribution(double monthlyGross) {
        // Employee share is usually 1-2% with a max.
        double contribution = monthlyGross * 0.02; // Assuming 2% employee share
        return Math.min(contribution, PAGIBIG_MAX_CONTRIBUTION);
    }

    @Override
    public double calculateEETax(double taxableIncome) {
        // Implement BIR tax table logic here
        // This is a simplified example; a real tax table has brackets.
        if (taxableIncome <= 20833) return 0.0;
        if (taxableIncome <= 33333) return (taxableIncome - 20833) * 0.20;
        return taxableIncome * 0.35; // Example highest bracket
    }

    @Override
    public double calculateTotalAllowance(Compensation compensation) {
        if (compensation == null) return 0.0;
        return compensation.getRiceSubsidy() +
               compensation.getPhoneAllowance() +
               compensation.getClothingAllowance();
    }

    @Override
    public double calculateGrossIncome(double basicSalary, double totalAllowances, double totalHoursWorked, double hourlyRate) {
        // This calculation should consider semimonthly vs monthly, actual hours, overtime etc.
        // For simplicity, assuming basic monthly + allowances for a payroll period
        return basicSalary + totalAllowances; // Simplified gross for a period
    }

    @Override
    public PayrollRecord generatePayroll(
            int employeeId,
            Compensation compensation,
            GovernmentID governmentID,
            List<AttendanceRecord> attendanceRecords,
            LocalDate payrollPeriodStartDate,
            LocalDate payrollPeriodEndDate
    ) {
        // 1. Calculate Gross Income for the period
        // For simplicity, assuming full month's basic salary and allowances for now.
        double basicSalaryForPeriod = compensation.getBasicSalary(); // Assuming monthly
        double totalAllowancesForPeriod = calculateTotalAllowance(compensation);
        double hourlyRate = compensation.getHourlyRate() != null ? compensation.getHourlyRate() : 0.0; // Use actual hourly rate
        double totalHoursWorked = 0; 
        double grossIncome = basicSalaryForPeriod + totalAllowancesForPeriod;

        // 2. Calculate Deductions
        double sssContrib = calculateEESSSContribution(basicSalaryForPeriod); // SSS based on basic
        double philhealthContrib = calculateEEPhilhealthContribution(basicSalaryForPeriod); // PhilHealth based on basic
        double pagibigContrib = calculateEEPagibigContribution(basicSalaryForPeriod); // Pag-IBIG based on basic

        // Calculate Taxable Income
        // This is a simplified example. Real calculation involves YTD income, dependents, exemptions.
        double totalDeductionsBeforeTax = sssContrib + philhealthContrib + pagibigContrib;
        double taxableIncome = grossIncome - totalDeductionsBeforeTax;
        double withholdingTax = calculateEETax(taxableIncome);

        // 3. Calculate Net Pay
        double netPay = grossIncome - sssContrib - philhealthContrib - pagibigContrib - withholdingTax;

        return new PayrollRecord(
            employeeId,
            payrollPeriodStartDate,
            payrollPeriodEndDate,
            grossIncome,
            sssContrib,
            philhealthContrib,
            pagibigContrib,
            withholdingTax,
            netPay
        );
    }
}