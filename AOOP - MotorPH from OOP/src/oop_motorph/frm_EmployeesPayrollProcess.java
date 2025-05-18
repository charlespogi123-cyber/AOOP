
package oop_motorph;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

public class frm_EmployeesPayrollProcess extends javax.swing.JFrame {

      // Default constructor
    public frm_EmployeesPayrollProcess() {
        initComponents();
        displaySalaryData();
        
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Employees Payroll Summary");
        //to disable tbl edit
        tbl_Salary.setDefaultEditor(Object.class, null);
        
        // Set default dates for cal_payDateFrom and cal_payDateTo
        setDefaultPayrollDates();
       
       //JOYCE USER ACCESS PER ROLE        
       // Retrieve current employee details
        EmpDetails employee = EmpUserSession.getInstance().getCurrentUser();
        
        // Get role from CSVHandler
        String role = CSVHandler.loadCredentials().get(employee.getEmpID())[1];
        
        // Set role-based access
        setRoleBasedAccess(role);
    }

        public void setRoleBasedAccess(String role) {
        // Default: Disable all buttons
        btn_MyRecords.setEnabled(false);
        btn_EmpRecords.setEnabled(false);
        btn_Profile.setEnabled(false);
        btn_Attendance.setEnabled(false);
        btn_computePay.setEnabled(false);
        btn_ProcessPay.setEnabled(false);
        btn_paySummary.setEnabled(false);
        btn_editAdj.setEnabled(false);
        btn_editPayDate.setEnabled(false);

        // Enable buttons based on role (case-insensitive)
        switch (role.toUpperCase()) {
            case "EMPLOYEE":
                // Employees cannot access payroll processing
                break;
            case "FINANCE":
                // Finance can access:
                btn_MyRecords.setEnabled(true);
                btn_Profile.setEnabled(true);
                btn_EmpRecords.setEnabled(true);
                btn_computePay.setEnabled(true);
                btn_ProcessPay.setEnabled(true);
                btn_paySummary.setEnabled(true);
                break;
            case "HR":
                // HR can access:
                //no access
                break;
            case "DEPTHEAD":
                // Dept-Head can access:
                //no access
                break;
            case "FINANCE TL":
                // Finance TL can access:
                btn_MyRecords.setEnabled(true);
                btn_Profile.setEnabled(true);
                btn_EmpRecords.setEnabled(true);
                btn_computePay.setEnabled(true);
                btn_ProcessPay.setEnabled(true);
                btn_paySummary.setEnabled(true);
                btn_editAdj.setEnabled(true);
                btn_editPayDate.setEnabled(true);
                break;
            case "PAYROLL MANAGER": 
                // Payroll Manager can access:
                btn_MyRecords.setEnabled(true);
                btn_Profile.setEnabled(true);
                btn_EmpRecords.setEnabled(true);
                btn_paySummary.setEnabled(true);
                btn_editAdj.setEnabled(true);
                btn_editPayDate.setEnabled(true);
                btn_computePay.setEnabled(true);
                break;
            case "ACCOUNTING":
                // Accounting can access:
                btn_MyRecords.setEnabled(true);
                btn_Profile.setEnabled(true);
                btn_EmpRecords.setEnabled(true);
                btn_paySummary.setEnabled(true);
                btn_computePay.setEnabled(true);
                btn_editAdj.setEnabled(true);
                btn_editPayDate.setEnabled(true);
                break;
            case "ADMIN": 
                // Admin can access:
                btn_EmpRecords.setEnabled(true);
                btn_Profile.setEnabled(true);
                btn_MyRecords.setEnabled(true);
                btn_Attendance.setEnabled(true);
                btn_EmpRecords.setEnabled(true);
                btn_paySummary.setEnabled(true);
                btn_editAdj.setEnabled(true);
                btn_editPayDate.setEnabled(true);
                btn_computePay.setEnabled(true);
                btn_ProcessPay.setEnabled(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }      
    
    
    

    // Parameterized constructor
    public frm_EmployeesPayrollProcess(String empID, String firstName, String lastName, String sssNo, String philhealthNo,
                                       String pagibigNo, String tinNo, String basicSalary, String riceSubsidy,
                                       String phoneAllowance, String clothingAllowance, String grossSemi, String hourlyRate) {
        initComponents();
        displaySalaryData();
        
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Employees Payroll Process");
        
        txt_empID.setText(empID);
        txt_firstName.setText(firstName);
        txt_lastName.setText(lastName);
        txt_basicSalary.setText(basicSalary);
        txt_hourlyRate.setText(hourlyRate);
       
    }
    
    private void displaySalaryData() {
        List<EmpSalaryDetails> salaries = CSVHandler.getSalaryData();
        DefaultTableModel model = (DefaultTableModel) tbl_Salary.getModel();

        // Clear existing data
        model.setRowCount(0);

        // Add rows from salary objects
        for (EmpSalaryDetails salary : salaries) {
            model.addRow(new Object[]{
                salary.getEmpID(), salary.getFirstName(), salary.getLastName(), salary.getSssNo(), salary.getPhilhealthNo(), salary.getTinNo(), salary.getPagibigNo(),
                salary.getBasicSalary(), salary.getRiceSubsidy(), salary.getPhoneAllowance(),
                salary.getClothingAllowance(), salary.getGrossSemi(), salary.getHourlyRate()
            });
        }
    }
// Method to set default dates for JDateChooser components
    private void setDefaultPayrollDates() {
        try {
            // Get first and last day of current month
            LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
            LocalDate lastDay = LocalDate.now().withDayOfMonth(firstDay.lengthOfMonth());

            // Convert LocalDate to Date
            Date firstDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date lastDate = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Set JDateChooser default values
            cal_payDateFrom.setDate(firstDate);
            cal_payDateTo.setDate(lastDate);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error setting default dates: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Salary = new javax.swing.JTable();
        btn_computePay = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btn_ProcessPay = new javax.swing.JButton();
        btn_editAdj = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_empID = new javax.swing.JTextField();
        txt_firstName = new javax.swing.JTextField();
        txt_lastName = new javax.swing.JTextField();
        txt_basicSalary = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_totalAllow = new javax.swing.JTextField();
        txt_hrsPerMonth = new javax.swing.JTextField();
        txt_totalHrsWorked = new javax.swing.JTextField();
        txt_basicProrata = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_tardiness = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txt_eeSSS = new javax.swing.JTextField();
        txt_eePagibig = new javax.swing.JTextField();
        txt_eePhilhealth = new javax.swing.JTextField();
        txt_deductions = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_netPay = new javax.swing.JTextField();
        btn_paySummary = new javax.swing.JButton();
        txt_erSSS = new javax.swing.JTextField();
        txt_erPagibig = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txt_erPhilhealth = new javax.swing.JTextField();
        txt_hourlyRate = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txt_eeTax = new javax.swing.JTextField();
        txt_erTax = new javax.swing.JTextField();
        cal_payDateFrom = new com.toedter.calendar.JDateChooser();
        cal_payDateTo = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        txt_payStatus = new javax.swing.JTextField();
        txt_adjEarn = new javax.swing.JTextField();
        txt_adjDeduct = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txt_earnings = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        btn_editPayDate = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_Profile = new javax.swing.JButton();
        btn_Attendance = new javax.swing.JButton();
        btn_SalaryAndStatutory = new javax.swing.JButton();
        btn_PayrollProcessing = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_MyRecords = new javax.swing.JButton();
        btn_EmpRecords = new javax.swing.JButton();
        btn_Logout = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.lightGray, null, null));

        tbl_Salary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Employee ID", "First Name", "Last Name", "SSS No.", "Philhealth No.", "TIN No.", "Pagibig No.", "Basic", "Rice Allow", "Phone Allow", "Clothing Allow", "Gross Semi Rate", "Hourly Rate"
            }
        ));
        jScrollPane1.setViewportView(tbl_Salary);

        btn_computePay.setBackground(new java.awt.Color(0, 0, 204));
        btn_computePay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_computePay.setForeground(new java.awt.Color(255, 255, 255));
        btn_computePay.setText("COMPUTE");
        btn_computePay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_computePayActionPerformed(evt);
            }
        });

        jLabel6.setText("Date From:");

        jLabel7.setText("Date To:");

        btn_ProcessPay.setBackground(new java.awt.Color(0, 0, 204));
        btn_ProcessPay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_ProcessPay.setForeground(new java.awt.Color(255, 255, 255));
        btn_ProcessPay.setText("PROCESS");
        btn_ProcessPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ProcessPayActionPerformed(evt);
            }
        });

        btn_editAdj.setBackground(new java.awt.Color(0, 0, 204));
        btn_editAdj.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_editAdj.setForeground(new java.awt.Color(255, 255, 255));
        btn_editAdj.setText("EDIT ADJ");
        btn_editAdj.setPreferredSize(new java.awt.Dimension(144, 23));
        btn_editAdj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editAdjActionPerformed(evt);
            }
        });

        jLabel8.setText("Emp ID:");

        txt_empID.setEditable(false);
        txt_empID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_empIDActionPerformed(evt);
            }
        });

        txt_firstName.setEditable(false);

        txt_lastName.setEditable(false);

        txt_basicSalary.setEditable(false);

        jLabel9.setText("First Name:");

        jLabel10.setText("Last Name:");

        jLabel11.setText("Basic Monthly Rate:");

        jLabel12.setText("Allowance:");

        jLabel13.setText("Total Hrs Worked:");

        jLabel14.setText("Standard Hrs Per Month:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Earnings:");

        txt_totalAllow.setEditable(false);

        txt_hrsPerMonth.setEditable(false);
        txt_hrsPerMonth.setBackground(new java.awt.Color(204, 255, 255));

        txt_totalHrsWorked.setEditable(false);
        txt_totalHrsWorked.setBackground(new java.awt.Color(204, 255, 255));

        txt_basicProrata.setEditable(false);

        jLabel16.setText("Tardiness/Absences (Hrs):");

        txt_tardiness.setEditable(false);
        txt_tardiness.setBackground(new java.awt.Color(204, 255, 255));

        jLabel17.setText("EE SSS:");

        jLabel18.setText("EE Pagibig:");

        jLabel19.setText("EE Philhealth:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText(".");

        txt_eeSSS.setEditable(false);

        txt_eePagibig.setEditable(false);

        txt_eePhilhealth.setEditable(false);

        txt_deductions.setEditable(false);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Net Pay:");

        txt_netPay.setEditable(false);
        txt_netPay.setBackground(new java.awt.Color(255, 255, 204));

        btn_paySummary.setBackground(new java.awt.Color(0, 0, 204));
        btn_paySummary.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_paySummary.setForeground(new java.awt.Color(255, 255, 255));
        btn_paySummary.setText("PAYROLL SUMMARY");
        btn_paySummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_paySummaryActionPerformed(evt);
            }
        });

        txt_erSSS.setEditable(false);

        txt_erPagibig.setEditable(false);

        jLabel22.setText("ER SSS:");

        jLabel23.setText("ER Pagibig:");

        jLabel24.setText("ER Philhealth:");

        txt_erPhilhealth.setEditable(false);

        txt_hourlyRate.setEditable(false);

        jLabel25.setText("Hourly Rate:");

        jLabel26.setText("EE Tax:");

        txt_eeTax.setEditable(false);

        txt_erTax.setEditable(false);

        cal_payDateFrom.setEnabled(false);

        cal_payDateTo.setEnabled(false);

        jLabel28.setText("Status:");

        txt_payStatus.setEditable(false);
        txt_payStatus.setBackground(new java.awt.Color(255, 255, 204));

        txt_adjEarn.setEditable(false);

        txt_adjDeduct.setEditable(false);

        jLabel29.setText("Adj Earnings:");

        jLabel31.setText("Basic Prorata:");

        txt_earnings.setEditable(false);

        jLabel32.setText("Adj Deductions:");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Deductions:");

        jLabel27.setText("ER Tax:");

        btn_editPayDate.setBackground(new java.awt.Color(0, 0, 204));
        btn_editPayDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_editPayDate.setForeground(new java.awt.Color(255, 255, 255));
        btn_editPayDate.setText("EDIT PAYDATE");
        btn_editPayDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editPayDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel14)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel29)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(24, 24, 24))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_hourlyRate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txt_earnings, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_totalAllow, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_adjEarn, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txt_basicProrata, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txt_hrsPerMonth, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_tardiness)
                                    .addComponent(txt_totalHrsWorked)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_basicSalary, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                    .addComponent(txt_empID))))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(18, 18, Short.MAX_VALUE)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(455, 455, 455))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(110, 110, 110)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_adjDeduct, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(txt_deductions, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(135, 135, 135)
                                                .addComponent(jLabel28))
                                            .addComponent(txt_eeSSS, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_eePagibig, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_eeTax, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_eePhilhealth, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(111, 111, 111)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(110, 110, 110)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(29, 29, 29)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_payStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                    .addComponent(txt_erPhilhealth)
                                    .addComponent(txt_netPay, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_erPagibig)
                                    .addComponent(txt_erSSS)
                                    .addComponent(txt_erTax)
                                    .addComponent(txt_lastName))
                                .addGap(111, 111, 111))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cal_payDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cal_payDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_computePay, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_editAdj, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_editPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_ProcessPay, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_paySummary)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_computePay)
                        .addComponent(btn_ProcessPay)
                        .addComponent(btn_editAdj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_paySummary)
                        .addComponent(btn_editPayDate))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(cal_payDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cal_payDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_empID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_firstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txt_lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(txt_basicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_hourlyRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel25)
                                .addComponent(jLabel26)
                                .addComponent(txt_eeTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(txt_erTax, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(txt_erSSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(txt_erPagibig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(txt_erPhilhealth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_hrsPerMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(11, 11, 11)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_tardiness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_totalHrsWorked, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(txt_eePhilhealth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_basicProrata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 21, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(17, 17, 17))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_totalAllow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_eeSSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txt_eePagibig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txt_adjEarn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(txt_adjDeduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_netPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel15)
                    .addComponent(txt_deductions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(txt_earnings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(txt_payStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        jLabel2.setText("MotorPH");

        jPanel3.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        btn_Profile.setBackground(new java.awt.Color(51, 51, 255));
        btn_Profile.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Profile.setForeground(new java.awt.Color(255, 255, 255));
        btn_Profile.setText("Employees Profile");
        btn_Profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ProfileActionPerformed(evt);
            }
        });

        btn_Attendance.setBackground(new java.awt.Color(51, 51, 255));
        btn_Attendance.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Attendance.setForeground(new java.awt.Color(255, 255, 255));
        btn_Attendance.setText("Employees Attendance & Leave");
        btn_Attendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AttendanceActionPerformed(evt);
            }
        });

        btn_SalaryAndStatutory.setBackground(new java.awt.Color(51, 51, 255));
        btn_SalaryAndStatutory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_SalaryAndStatutory.setForeground(new java.awt.Color(255, 255, 255));
        btn_SalaryAndStatutory.setText("Employees Salary & Statutory");
        btn_SalaryAndStatutory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SalaryAndStatutoryActionPerformed(evt);
            }
        });

        btn_PayrollProcessing.setBackground(new java.awt.Color(51, 51, 255));
        btn_PayrollProcessing.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_PayrollProcessing.setForeground(new java.awt.Color(255, 255, 0));
        btn_PayrollProcessing.setText("Payroll Processing");
        btn_PayrollProcessing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PayrollProcessingActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(245, 28, 71));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        btn_MyRecords.setBackground(new java.awt.Color(51, 51, 255));
        btn_MyRecords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_MyRecords.setForeground(new java.awt.Color(255, 255, 255));
        btn_MyRecords.setText("My Records");
        btn_MyRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MyRecordsActionPerformed(evt);
            }
        });

        btn_EmpRecords.setBackground(new java.awt.Color(51, 51, 255));
        btn_EmpRecords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_EmpRecords.setForeground(new java.awt.Color(255, 255, 0));
        btn_EmpRecords.setText("Employee Records");

        btn_Logout.setBackground(new java.awt.Color(51, 51, 255));
        btn_Logout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Logout.setForeground(new java.awt.Color(255, 255, 255));
        btn_Logout.setText("Logout");
        btn_Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LogoutActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/oop_motorph/img/img_myrecord.png"))); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/oop_motorph/img/img_emprecords.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/oop_motorph/img/img_logout.png"))); // NOI18N

        jPanel5.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel5.setText("Version 1.30");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_EmpRecords)
                            .addComponent(btn_MyRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_Profile, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btn_Attendance, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btn_SalaryAndStatutory, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_PayrollProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Profile)
                    .addComponent(btn_Attendance)
                    .addComponent(btn_SalaryAndStatutory)
                    .addComponent(btn_PayrollProcessing)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_MyRecords)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_EmpRecords)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Logout)
                        .addGap(0, 193, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.getAccessibleContext().setAccessibleName("Profile");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_PayrollProcessingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PayrollProcessingActionPerformed
        new frm_EmployeesPayrollPaid().setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btn_PayrollProcessingActionPerformed

    private void btn_SalaryAndStatutoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SalaryAndStatutoryActionPerformed
        
        new frm_EmployeesSalary().setVisible(true);
       this.dispose();
        
        
    }//GEN-LAST:event_btn_SalaryAndStatutoryActionPerformed

    private void btn_AttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AttendanceActionPerformed
        
       new frm_EmployeesAttLeave().setVisible(true);
       this.dispose();
        
    }//GEN-LAST:event_btn_AttendanceActionPerformed

    private void btn_ProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ProfileActionPerformed
        new frm_EmployeesRecords().setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btn_ProfileActionPerformed

    private void btn_MyRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MyRecordsActionPerformed
        
        new frm_EmpProfile().setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btn_MyRecordsActionPerformed

    private void btn_LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LogoutActionPerformed
        CSVHandler.handleLogout(this); 
        
    }//GEN-LAST:event_btn_LogoutActionPerformed

    private void btn_computePayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_computePayActionPerformed
    int selectedRow = tbl_Salary.getSelectedRow();

    if (selectedRow != -1) {  
        DefaultTableModel model = (DefaultTableModel) tbl_Salary.getModel();
        
        // Parse numeric values as double and store them
        double basicSalary = Double.parseDouble(model.getValueAt(selectedRow, 7).toString());
        double riceAllow = Double.parseDouble(model.getValueAt(selectedRow, 8).toString());
        double phoneAllow = Double.parseDouble(model.getValueAt(selectedRow, 9).toString());
        double clothingAllow = Double.parseDouble(model.getValueAt(selectedRow, 10).toString());
        double grossSemi = Double.parseDouble(model.getValueAt(selectedRow, 11).toString());
        double hourlyRate = Double.parseDouble(model.getValueAt(selectedRow, 12).toString());
        

        // Create an EmpSalaryDetails object using parsed data
        EmpSalaryDetails empDetails = new EmpSalaryDetails(
            model.getValueAt(selectedRow, 0).toString(),  // empID
            model.getValueAt(selectedRow, 1).toString(),  // firstName
            model.getValueAt(selectedRow, 2).toString(),  // lastName
            model.getValueAt(selectedRow, 3).toString(),  // sssNo
            model.getValueAt(selectedRow, 4).toString(),  // philhealthNo
            model.getValueAt(selectedRow, 5).toString(),  // tinNo
            model.getValueAt(selectedRow, 6).toString(),  // pagibigNo
            basicSalary, riceAllow, phoneAllow, clothingAllow, grossSemi, hourlyRate
        );

        // Ensure pay date fields are selected
        if (cal_payDateFrom.getDate() == null || cal_payDateTo.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select both Pay Date From and Pay Date To.");
            return;
        }

        // Convert selected dates to LocalDate
        LocalDate payDateFrom = cal_payDateFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate payDateTo = cal_payDateTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // Validate that Pay Date From is not greater than Pay Date To
        if (payDateFrom.isAfter(payDateTo)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Pay Date From cannot be greater than Pay Date To.", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
            return;
        }
     
        List<EmpAttLeave> attendanceList = CSVHandler.getAttendanceData();
        double totalHoursWorked = 0.0;

        // Compute total hours worked only within the selected pay period
        for (EmpAttLeave att : attendanceList) {
            if (att.getEmpID().equals(empDetails.getEmpID()) &&
                (att.getAttendanceType().equals("Work") || att.getAttendanceType().equals("Overtime")|| att.getAttendanceType().equals("VL Request") || att.getAttendanceType().equals("SL Request")) &&
                (att.getAttendanceStatus().equals("Closed") || att.getAttendanceStatus().equals("Closed-Late") || 
                 att.getAttendanceStatus().equals("Closed-On Time") || att.getAttendanceStatus().equals("Approved"))) {
                
                LocalDate attDateFrom = att.getAttDateFrom();
                LocalDate attDateTo = att.getAttDateTo();

                // Ensure attendance period falls within the selected pay period
                if (!(attDateTo.isBefore(payDateFrom) || attDateFrom.isAfter(payDateTo))) {
                    LocalDate effectiveStart = attDateFrom.isBefore(payDateFrom) ? payDateFrom : attDateFrom;
                    LocalDate effectiveEnd = attDateTo.isAfter(payDateTo) ? payDateTo : attDateTo;
                    
                    // Compute the duration based on the effective range
                    long daysInRange = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;
       
                    double dailyHours = att.getHoursWorked() / ChronoUnit.DAYS.between(attDateFrom, attDateTo.plusDays(1));
                    
                    totalHoursWorked += dailyHours * daysInRange;
                }
            }
        }

        // Set the text fields using the encapsulated data
        txt_empID.setText(empDetails.getEmpID());
        txt_firstName.setText(empDetails.getFirstName());
        txt_lastName.setText(empDetails.getLastName());
        
        // Ensure double values remain as double but are formatted for display
        txt_basicSalary.setText(String.format("%.2f", empDetails.getBasicSalary()));
        txt_hourlyRate.setText(String.format("%.2f", empDetails.getHourlyRate()));
        txt_totalAllow.setText(String.format("%.2f", empDetails.calculateTotalAllowance())); 
        
        // Set default hours per month
        txt_hrsPerMonth.setText(String.format("%.2f", 168.00)); // Default 168 hours per month (Basic / Per Hour Rate)
        
        // Display computed total hours worked
        txt_totalHrsWorked.setText(String.format("%.2f", totalHoursWorked));
        
        double adjEarnings = 0;
        try {
        String adjEarningsText = txt_adjEarn.getText();
        if (!adjEarningsText.isEmpty()) {
        adjEarnings = Double.parseDouble(adjEarningsText);
        }
        } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Invalid Adjustment Earnings value. Defaulting to 0.");
        adjEarnings = 0;
        }

        System.out.println("Adjusted Earnings: " + adjEarnings);
        txt_adjEarn.setText(String.format("%.2f", adjEarnings));
        
        
        // Calculate earnings
        double earnings = (((basicSalary / 168.00) * totalHoursWorked)+ empDetails.calculateTotalAllowance() + adjEarnings);
        txt_earnings.setText(String.format("%.2f", earnings));
        
        double monthlyGross = earnings;
        
        
        double basicProrata = hourlyRate * totalHoursWorked;
        txt_basicProrata.setText(String.format("%.2f", basicProrata));
      
        txt_tardiness.setText(String.format("%.2f", 168.00 - totalHoursWorked));
        
        double eeTaxRate = empDetails.calculateEETaxRate(monthlyGross);
        txt_eeTax.setText(String.format("%.2f", eeTaxRate));
        
        double eesssRate = empDetails.calculateEESSSRate(monthlyGross);
        txt_eeSSS.setText(String.format("%.2f", eesssRate));
        
        double eephilRate = empDetails.calculateEEPhilhealthRate(basicProrata);
        txt_eePhilhealth.setText(String.format("%.2f", eephilRate));
         
        double eePagibig = empDetails.calculateEEPagibigRate(monthlyGross);
        txt_eePagibig.setText(String.format("%.2f", eePagibig));
        
     
        double adjDeduct = 0;
        try {
        String adjDeductText = txt_adjDeduct.getText().trim(); // Trim whitespace to avoid errors
        if (!adjDeductText.isEmpty()) {
        adjDeduct = Double.parseDouble(adjDeductText);
        }
        } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Invalid Adjustment Deduction value. Defaulting to 0.");
        adjDeduct = 0; // Correctly assigning default value instead of modifying a String variable
        }
        txt_adjDeduct.setText(String.format("%.2f", adjDeduct)); // Formatting output properly
        
        
        // Calculate deductions
        double deductions = (eesssRate + eephilRate + eePagibig + adjDeduct);
        txt_deductions.setText(String.format("%.2f", deductions));
        
        double netPay = (earnings - deductions);
        txt_netPay.setText(String.format("%.2f", netPay));
        
       
        txt_payStatus.setText(String.format("Pending"));
        
        //ER SHARE not included in the payroll
        txt_erSSS.setText(String.format("%.2f", eesssRate));
        txt_erPagibig.setText(String.format("%.2f", eePagibig));
        txt_erPhilhealth.setText(String.format("%.2f", eephilRate));
        txt_erTax.setText(String.format("%.2f", eeTaxRate));
        
     
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a row first.");
    }

    }//GEN-LAST:event_btn_computePayActionPerformed

    private void btn_editAdjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editAdjActionPerformed
        txt_adjEarn.setEditable(true);
        txt_adjDeduct.setEditable(true);
        
    }//GEN-LAST:event_btn_editAdjActionPerformed

    private void txt_empIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_empIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_empIDActionPerformed

    private void btn_paySummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_paySummaryActionPerformed
        new frm_EmployeesPayrollPaid().setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btn_paySummaryActionPerformed

    private void btn_ProcessPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ProcessPayActionPerformed
  //  btn_ProcessPay.addActionListener(e -> {
    String empID = txt_empID.getText();
    String firstName = txt_firstName.getText();
    String lastName = txt_lastName.getText();
    
    // Format date from JCalendar
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
    String payDateFrom = sdf.format(cal_payDateFrom.getDate());
    String payDateTo = sdf.format(cal_payDateTo.getDate());

    String basicSalary = txt_basicSalary.getText();
    String hourlyRate = txt_hourlyRate.getText();
    String totalAllowances = txt_totalAllow.getText();
    String hrsPerMonth = txt_hrsPerMonth.getText();
    String totalHrsWorked = txt_totalHrsWorked.getText();
    String adjEarnings = txt_adjEarn.getText();
    String totalEarnings = txt_earnings.getText();
    String tardinessAbsences = txt_tardiness.getText();
    String eeTax = txt_eeTax.getText();
    String eeSSS = txt_eeSSS.getText();
    String eePagibig = txt_eePagibig.getText();
    String eePhilhealth = txt_eePhilhealth.getText();
    String adjDeductions = txt_adjDeduct.getText();
    String totalDeductions = txt_deductions.getText();
    String netPay = txt_netPay.getText();
    String payStatus = txt_payStatus.getText();


    // Save data to CSV
    CSVHandler.savePayrollData(empID, firstName, lastName, payDateFrom, payDateTo, 
        basicSalary, hourlyRate, totalAllowances, hrsPerMonth, totalHrsWorked, adjEarnings,
        totalEarnings, tardinessAbsences, eeTax, eeSSS, eePagibig, eePhilhealth, adjDeductions,
        totalDeductions, netPay, payStatus);
    
    JOptionPane.showMessageDialog(null, "Payroll approved and processed successfully!");

 
    }//GEN-LAST:event_btn_ProcessPayActionPerformed

    private void btn_editPayDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editPayDateActionPerformed
        cal_payDateFrom.setEnabled(true);
        cal_payDateTo.setEnabled(true);
    }//GEN-LAST:event_btn_editPayDateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm_EmployeesPayrollProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_EmployeesPayrollProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_EmployeesPayrollProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_EmployeesPayrollProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
  
       
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_EmployeesPayrollProcess().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Attendance;
    private javax.swing.JButton btn_EmpRecords;
    private javax.swing.JButton btn_Logout;
    private javax.swing.JButton btn_MyRecords;
    private javax.swing.JButton btn_PayrollProcessing;
    private javax.swing.JButton btn_ProcessPay;
    private javax.swing.JButton btn_Profile;
    private javax.swing.JButton btn_SalaryAndStatutory;
    private javax.swing.JButton btn_computePay;
    private javax.swing.JButton btn_editAdj;
    private javax.swing.JButton btn_editPayDate;
    private javax.swing.JButton btn_paySummary;
    private com.toedter.calendar.JDateChooser cal_payDateFrom;
    private com.toedter.calendar.JDateChooser cal_payDateTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_Salary;
    private javax.swing.JTextField txt_adjDeduct;
    private javax.swing.JTextField txt_adjEarn;
    private javax.swing.JTextField txt_basicProrata;
    private javax.swing.JTextField txt_basicSalary;
    private javax.swing.JTextField txt_deductions;
    private javax.swing.JTextField txt_earnings;
    private javax.swing.JTextField txt_eePagibig;
    private javax.swing.JTextField txt_eePhilhealth;
    private javax.swing.JTextField txt_eeSSS;
    private javax.swing.JTextField txt_eeTax;
    private javax.swing.JTextField txt_empID;
    private javax.swing.JTextField txt_erPagibig;
    private javax.swing.JTextField txt_erPhilhealth;
    private javax.swing.JTextField txt_erSSS;
    private javax.swing.JTextField txt_erTax;
    private javax.swing.JTextField txt_firstName;
    private javax.swing.JTextField txt_hourlyRate;
    private javax.swing.JTextField txt_hrsPerMonth;
    private javax.swing.JTextField txt_lastName;
    private javax.swing.JTextField txt_netPay;
    private javax.swing.JTextField txt_payStatus;
    private javax.swing.JTextField txt_tardiness;
    private javax.swing.JTextField txt_totalAllow;
    private javax.swing.JTextField txt_totalHrsWorked;
    // End of variables declaration//GEN-END:variables
}
