package oop_motorph.model;

public class Compensation {
    private Integer employeeId;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance; 
    private Double grossSemimonthly;
    private Double hourlyRate;

    public Compensation(Integer employeeId, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, Double grossSemimonthly, Double hourlyRate) {
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemimonthly = grossSemimonthly;
        this.hourlyRate = hourlyRate;
    }

    // Constructor without generated columns if they're not always present
    public Compensation(Integer employeeId, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        this(employeeId, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, null, null);
    }

    // Getters and Setters
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public Double getGrossSemimonthly() { return grossSemimonthly; }
    public void setGrossSemimonthly(Double grossSemimonthly) { this.grossSemimonthly = grossSemimonthly; }
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
}
