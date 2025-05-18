
package oop_motorph;

public class EmpSalaryDetails extends EmpDetails implements PayrollProcessor {
  
    private String sssNo;
    private String philhealthNo;
    private String pagibigNo;
    private String tinNo;
    private double basicSalary;
    private double riceAllow;
    private double phoneAllow;
    private double clothingAllow;
    private double grossSemi;
    private double hourlyRate;

    
    public EmpSalaryDetails(String empID, String firstName, String lastName,
                            String sssNo, String philhealthNo, String tinNo, String pagibigNo,
                            double basicSalary, double riceAllow, double phoneAllow,
                            double clothingAllow, double grossSemi, double hourlyRate) {
        
        // Call the superclass constructor 
        super(empID, firstName, lastName);

        
        this.sssNo = sssNo;
        this.philhealthNo = philhealthNo;
        this.tinNo = tinNo;
        this.pagibigNo = pagibigNo;
        this.basicSalary = basicSalary;
        this.riceAllow = riceAllow;
        this.phoneAllow = phoneAllow;
        this.clothingAllow = clothingAllow;
        this.grossSemi = grossSemi;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    public String getSssNo() { return sssNo; }
    public void setSssNo(String sssNo) { this.sssNo = sssNo; }

    public String getPhilhealthNo() { return philhealthNo; }
    public void setPhilhealthNo(String philhealthNo) { this.philhealthNo = philhealthNo; }

    public String getTinNo() { return tinNo; }
    public void setTinNo(String tinNo) { this.tinNo = tinNo; }

    public String getPagibigNo() { return pagibigNo; }
    public void setPagibigNo(String pagibigNo) { this.pagibigNo = pagibigNo; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getRiceSubsidy() { return riceAllow; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceAllow = riceSubsidy; }

    public double getPhoneAllowance() { return phoneAllow; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllow = phoneAllowance; }

    public double getClothingAllowance() { return clothingAllow; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllow = clothingAllowance; }

    public double getGrossSemi() { return grossSemi; }
    public void setGrossSemi(double grossSemi) { this.grossSemi = grossSemi; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    @Override  // JOYCE --------------------------
    public double calculateTotalAllowance() {
        return riceAllow + phoneAllow + clothingAllow; // Use getAdjustment() method instead of directly accessing adjustment
    }
    
    
    @Override  // EE SSS
    public double calculateEESSSRate(double monthlyGross) {
        if (monthlyGross <= 21750) {
            return 990.00;
        } else if (monthlyGross <= 22250) {
            return 1012.50;
        } else if (monthlyGross <= 22750) {
            return 1035.00;
        } else if (monthlyGross <= 23250) {
            return 1057.50;
        } else if (monthlyGross <= 23750) {
            return 1080.00;
        } else if (monthlyGross <= 24250) {
            return 1102.50;
        } else {
            return 1125.00;
        }   
    }
    
    @Override  // EE Philhealth
    public double calculateEEPhilhealthRate(double basicProrata) {
         if (basicProrata > 0) {
            return basicProrata * 0.03;
        }
        return 0;
    
    }
      @Override  // EE Philhealth
    public double calculateEEPagibigRate(double monthlyGross) {
        return 100;
    
    }
    
     @Override  // EE Tax
    public double calculateEETaxRate(double monthlyGross) {
        if (monthlyGross <= 20833) {
            return 0;
        } else if (monthlyGross <= 33332) {
            return (monthlyGross - 20833) * 0.20;
        } else if (monthlyGross <= 66666) {
            return 2500 + (monthlyGross - 33332) * 0.25;
        } else if (monthlyGross <= 166666) {
            return 10833 + (monthlyGross - 66666) * 0.30;
        } else if (monthlyGross <= 666666) {
            return 40833 + (monthlyGross - 166666) * 0.32;
        } else {
            return 200833 + (monthlyGross - 666666) * 0.35;
        }
    }   
}


