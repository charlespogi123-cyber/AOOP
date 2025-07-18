package oop_motorph.model;

import java.time.LocalDate;

public class PayrollRecord {
    private Integer payrollId;
    private Integer employeeId;
    private LocalDate payrollPeriodStartDate;
    private LocalDate payrollPeriodEndDate;
    private double grossSalary; 
    private double sssContribution;
    private double philhealthContribution;
    private double pagibigContribution;
    private double tax;
    private double netPay;
    

    public PayrollRecord(Integer payrollId, Integer employeeId, LocalDate payrollPeriodStartDate, LocalDate payrollPeriodEndDate, double grossSalary, double sssContribution, double philhealthContribution, double pagibigContribution, double tax, double netPay) {
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.payrollPeriodStartDate = payrollPeriodStartDate;
        this.payrollPeriodEndDate = payrollPeriodEndDate;
        this.grossSalary = grossSalary;
        this.sssContribution = sssContribution;
        this.philhealthContribution = philhealthContribution;
        this.pagibigContribution = pagibigContribution;
        this.tax = tax;
        this.netPay = netPay;
    }

    // Constructor for creating new records (without auto-generated ID)
    public PayrollRecord(Integer employeeId, LocalDate payrollPeriodStartDate, LocalDate payrollPeriodEndDate, double grossSalary, double sssContribution, double philhealthContribution, double pagibigContribution, double tax, double netPay) {
        this(null, employeeId, payrollPeriodStartDate, payrollPeriodEndDate, grossSalary, sssContribution, philhealthContribution, pagibigContribution, tax, netPay);
    }

    // Getters and Setters
    public Integer getPayrollId() { return payrollId; }
    public void setPayrollId(Integer payrollId) { this.payrollId = payrollId; }
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public LocalDate getPayrollPeriodStartDate() { return payrollPeriodStartDate; }
    public void setPayrollPeriodStartDate(LocalDate payrollPeriodStartDate) { this.payrollPeriodStartDate = payrollPeriodStartDate; }
    public LocalDate getPayrollPeriodEndDate() { return payrollPeriodEndDate; }
    public void setPayrollPeriodEndDate(LocalDate payrollPeriodEndDate) { this.payrollPeriodEndDate = payrollPeriodEndDate; }
    public double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(double grossSalary) { this.grossSalary = grossSalary; }
    public double getSssContribution() { return sssContribution; }
    public void setSssContribution(double sssContribution) { this.sssContribution = sssContribution; }
    public double getPhilhealthContribution() { return philhealthContribution; }
    public void setPhilhealthContribution(double philhealthContribution) { this.philhealthContribution = philhealthContribution; }
    public double getPagibigContribution() { return pagibigContribution; }
    public void setPagibigContribution(double pagibigContribution) { this.pagibigContribution = pagibigContribution; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getNetPay() { return netPay; }
    public void setNetPay(double netPay) { this.netPay = netPay; }
}
