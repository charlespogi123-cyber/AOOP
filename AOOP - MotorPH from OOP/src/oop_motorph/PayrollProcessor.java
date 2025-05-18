package oop_motorph;


public interface PayrollProcessor {
    
    
    double calculateEESSSRate(double monthlyGross);
    double calculateEEPhilhealthRate(double basicProrata);
    double calculateEEPagibigRate(double monthlyGross);
    double calculateEETaxRate(double monthlyGross);
    double calculateTotalAllowance();
 
    
}