package oop_motorph;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import javax.swing.JEditorPane;
import java.awt.*;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.swing.JScrollPane;

public class frm_EmployeesPayrollProcess extends javax.swing.JFrame {

    public frm_EmployeesPayrollProcess() {
        initComponents();
        
        // Set window properties - larger size to show all content
        setResizable(true);
        setTitle("Payroll Processing");
        setSize(1400, 950); // Increased size to accommodate all content
        setMinimumSize(new java.awt.Dimension(1150, 800));
        setLocationRelativeTo(null); // Center the window after setting size
        
        // Apply modern styling after components are initialized
        applyModernStyling();
        addHoverEffects();

        // Display payroll data in table
        displayEmployeeData();
        
        // Populate month filter dropdown after data is loaded
        populateMonthFilter();
        
        EmpDetails employee = EmpUserSession.getInstance().getCurrentUser();
        String role = EmpUserSession.getInstance().getRole();
        
        if (employee != null && role != null) {
            // Set role-based access
            setRoleBasedAccess(role);
        } else {
            JOptionPane.showMessageDialog(this, "No employee data or role found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayEmployeeData() {
        List<String[]> payrollData = DatabaseHandler.getPayrollData();
        DefaultTableModel model = (DefaultTableModel) tbl_Employees.getModel();
        
        // Disable table editing
        tbl_Employees.setDefaultEditor(Object.class, null);
        
        // Clear existing rows
        model.setRowCount(0);

        // Add all payroll data to the table
        for (String[] row : payrollData) {
            model.addRow(row);
        }
        
        // Hide the date column (column 14) - it's used only for filtering
        if (tbl_Employees.getColumnCount() > 14) {
            tbl_Employees.getColumnModel().getColumn(14).setMinWidth(0);
            tbl_Employees.getColumnModel().getColumn(14).setMaxWidth(0);
            tbl_Employees.getColumnModel().getColumn(14).setWidth(0);
        }
        
        model.fireTableDataChanged();  
        
        // Populate month filter after data is loaded
        populateMonthFilter();
    }

    private void populateMonthFilter() {
        Set<java.util.Date> uniqueDates = new TreeSet<>();
        DefaultTableModel model = (DefaultTableModel) tbl_Employees.getModel();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM yyyy");
        
        // Extract unique months from PayrollPeriodStartDate (column 14 - hidden date column)
        for (int i = 0; i < model.getRowCount(); i++) {
            Object dateValue = model.getValueAt(i, 14); // Date column is at index 14
            if (dateValue != null) {
                try {
                    String dateStr = dateValue.toString();
                    java.util.Date date = inputFormat.parse(dateStr);
                    // Add first day of month to ensure proper sorting
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(date);
                    cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                    uniqueDates.add(cal.getTime());
                } catch (ParseException e) {
                    // Skip invalid dates
                    System.err.println("Error parsing date: " + dateValue);
                }
            }
        }
        
        // Clear and populate the dropdown in chronological order
        cmbMonthFilter.removeAllItems();
        cmbMonthFilter.addItem("All Months");
        for (java.util.Date date : uniqueDates) {
            String monthYear = outputFormat.format(date);
            cmbMonthFilter.addItem(monthYear);
        }
    }

    private void filterTableByMonth() {
        String selectedMonth = (String) cmbMonthFilter.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) tbl_Employees.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_Employees.setRowSorter(sorter);
        
        if (selectedMonth == null || "All Months".equals(selectedMonth)) {
            // Show all rows
            sorter.setRowFilter(null);
        } else {
            // Filter by selected month
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM yyyy");
            
            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    Object dateValue = entry.getValue(14); // PayrollPeriodStartDate column (hidden date column)
                    if (dateValue != null) {
                        try {
                            String dateStr = dateValue.toString();
                            java.util.Date date = inputFormat.parse(dateStr);
                            String monthYear = outputFormat.format(date);
                            return selectedMonth.equals(monthYear);
                        } catch (ParseException e) {
                            return false;
                        }
                    }
                    return false;
                }
            };
            sorter.setRowFilter(filter);
        }
    }

    private void cmbMonthFilterActionPerformed(java.awt.event.ActionEvent evt) {
        filterTableByMonth();
    }

    private void applyModernStyling() {
        // Style the main panels with clean background
        jPanel1.setBackground(new Color(249, 250, 251));
        jPanel2.setBackground(new Color(255, 255, 255));
        jPanel3.setBackground(new Color(255, 255, 255));
        
        // Style navigation panel
        if (jPanel4 != null) {
            jPanel4.setBackground(new Color(255, 255, 255));
        }
        if (jPanel5 != null) {
            jPanel5.setBackground(new Color(255, 255, 255));
        }
        if (jPanel6 != null) {
            jPanel6.setBackground(new Color(255, 255, 255));
        }
        
        // Add enhanced card-like styling to main content panel - reduced padding for more space
        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(4, 4, 8, 8),
                javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0, 8), 1)
            ),
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)            )
        ));
        
        // Style the navigation area with minimal border - reduced padding since title is removed
        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        // Style the table with enhanced styling
        styleTableEnhanced();
        
        // Style action buttons with enhanced styling
        styleActionButtonEnhanced(btn_ViewEmp, new Color(59, 130, 246));
        styleActionButtonEnhanced(btn_PrintPayslip, new Color(34, 197, 94)); // Green for payslip generation

        // Style navigation buttons with enhanced modern design and better spacing
        styleNavigationButtonEnhanced(btn_Profile, new Color(107, 114, 128), false);
        styleNavigationButtonEnhanced(btn_LeaveMgt, new Color(107, 114, 128), false);
        styleNavigationButtonEnhanced(btn_SalaryAndStatutory, new Color(107, 114, 128), false);
        styleNavigationButtonEnhanced(btn_PayrollSummary, new Color(34, 197, 94), true); // Active green - this is the Payroll Processing page
        
        // Style left sidebar buttons with different sizing
        styleSidebarButtonEnhanced(btn_MyRecords, new Color(107, 114, 128), false);
        styleSidebarButtonEnhanced(btn_EmpRecords, new Color(107, 114, 128), false);
        styleSidebarButtonEnhanced(btn_Logout, new Color(239, 68, 68), false); // Red for logout
        
        // Add minimal spacing improvements to the overall layout - reduced top padding
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 12, 12));
    }
    
    private void styleTableEnhanced() {
        tbl_Employees.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tbl_Employees.setBackground(new Color(255, 255, 255));
        tbl_Employees.setForeground(new Color(55, 65, 81));
        tbl_Employees.setGridColor(new Color(229, 231, 235));
        tbl_Employees.setRowHeight(35);
        tbl_Employees.setShowVerticalLines(true);
        tbl_Employees.setShowHorizontalLines(true);
        tbl_Employees.setIntercellSpacing(new java.awt.Dimension(1, 1));
        
        // Enable horizontal scrolling for many columns
        tbl_Employees.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        // Set minimum column widths for better visibility
        if (tbl_Employees.getColumnModel().getColumnCount() > 0) {
            // Set default width for all columns
            int columnCount = tbl_Employees.getColumnModel().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String columnName = tbl_Employees.getColumnName(i).toLowerCase();
                int width = 100; // Default width
                
                // Set specific widths based on column name/content type
                if (columnName.contains("id")) {
                    width = 80;
                } else if (columnName.contains("name")) {
                    width = 150;
                } else if (columnName.contains("date") || columnName.contains("period")) {
                    width = 120;
                } else if (columnName.contains("hour") || columnName.contains("time")) {
                    width = 90;
                } else if (columnName.contains("allowance") || columnName.contains("contribution") || 
                          columnName.contains("deduction") || columnName.contains("salary") || 
                          columnName.contains("gross") || columnName.contains("net") || 
                          columnName.contains("tax")) {
                    width = 110;
                } else if (columnName.contains("number") || columnName.contains("sss") || 
                          columnName.contains("tin") || columnName.contains("philhealth") || 
                          columnName.contains("pagibig")) {
                    width = 120;
                }
                
                tbl_Employees.getColumnModel().getColumn(i).setPreferredWidth(width);
                tbl_Employees.getColumnModel().getColumn(i).setMinWidth(80);
            }
        }
        
        // Style table header
        if (tbl_Employees.getTableHeader() != null) {
            tbl_Employees.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
            tbl_Employees.getTableHeader().setBackground(new Color(249, 250, 251));
            tbl_Employees.getTableHeader().setForeground(new Color(75, 85, 99));
            tbl_Employees.getTableHeader().setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
            tbl_Employees.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        }
        
        // Style scroll pane with enhanced scrolling
        if (jScrollPane1 != null) {
            jScrollPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            
            // Enable smooth scrolling
            jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
            
            // Set scroll bar policy to always show horizontal scrollbar
            jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
    }
    
    private void styleActionButtonEnhanced(javax.swing.JButton button, Color backgroundColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(backgroundColor);
        button.setForeground(new Color(255, 255, 255));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Set consistent preferred size for action buttons
        button.setPreferredSize(new java.awt.Dimension(80, 30));
        button.setMinimumSize(new java.awt.Dimension(80, 30));
        
        button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(1, 1, 2, 2),
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0, 8), 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12)
            )
        ));
    }
    
    private void styleNavigationButtonEnhanced(javax.swing.JButton button, Color backgroundColor, boolean isActive) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(new Color(255, 255, 255));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Set consistent preferred size for all navigation buttons - wider for better text display
        button.setPreferredSize(new java.awt.Dimension(220, 45));
        button.setMinimumSize(new java.awt.Dimension(220, 45));
    }
    
    private void styleSidebarButtonEnhanced(javax.swing.JButton button, Color backgroundColor, boolean isActive) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(new Color(255, 255, 255));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Set consistent preferred size for all sidebar buttons
        button.setPreferredSize(new java.awt.Dimension(170, 45));
        button.setMinimumSize(new java.awt.Dimension(170, 45));
    }
    
    private void addHoverEffects() {
        // Add hover effects to navigation buttons
        addButtonHoverEnhanced(btn_Profile, new Color(107, 114, 128), new Color(75, 85, 99));
        addButtonHoverEnhanced(btn_LeaveMgt, new Color(107, 114, 128), new Color(75, 85, 99));
        addButtonHoverEnhanced(btn_SalaryAndStatutory, new Color(107, 114, 128), new Color(75, 85, 99));
        addButtonHoverEnhanced(btn_PayrollSummary, new Color(34, 197, 94), new Color(22, 163, 74));
        
        // Add hover effects to sidebar buttons
        addButtonHoverEnhanced(btn_MyRecords, new Color(107, 114, 128), new Color(75, 85, 99));
        addButtonHoverEnhanced(btn_EmpRecords, new Color(107, 114, 128), new Color(75, 85, 99));
        addButtonHoverEnhanced(btn_Logout, new Color(239, 68, 68), new Color(220, 38, 38));
        
        // Add hover effects to action buttons
        addButtonHoverEnhanced(btn_ViewEmp, new Color(59, 130, 246), new Color(37, 99, 235));
        addButtonHoverEnhanced(btn_PrintPayslip, new Color(34, 197, 94), new Color(22, 163, 74)); // Green hover for payslip
    }
    
    private void addButtonHoverEnhanced(javax.swing.JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }

    // Set role-based access controls
    private void setRoleBasedAccess(String role) {
        // Implementation for role-based access
        switch (role.toUpperCase()) {
            case "EMPLOYEE":
                btn_ViewEmp.setEnabled(false);
                break;
            case "HR":
                btn_ViewEmp.setEnabled(true);
                break;
            default:
                btn_ViewEmp.setEnabled(false);
                break;
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
        tbl_Employees = new javax.swing.JTable();
        btn_ViewEmp = new javax.swing.JButton();
        btn_PrintPayslip = new javax.swing.JButton();
        cmbMonthFilter = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_Profile = new javax.swing.JButton();
        btn_LeaveMgt = new javax.swing.JButton();
        btn_SalaryAndStatutory = new javax.swing.JButton();
        btn_PayrollSummary = new javax.swing.JButton();
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

        // Get dynamic column names from database
        String[] columnNames = DatabaseHandler.getPayrollColumnNames();
        
        tbl_Employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            columnNames
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Make table non-editable
            }
        });
        jScrollPane1.setViewportView(tbl_Employees);

        btn_ViewEmp.setBackground(new java.awt.Color(0, 0, 204));
        btn_ViewEmp.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_ViewEmp.setForeground(new java.awt.Color(255, 255, 255));
        btn_ViewEmp.setText("PRINT PAYROLL SUMMARY");
        btn_ViewEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ViewEmpActionPerformed(evt);
            }
        });

        btn_PrintPayslip.setBackground(new java.awt.Color(34, 197, 94));
        btn_PrintPayslip.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_PrintPayslip.setForeground(new java.awt.Color(255, 255, 255));
        btn_PrintPayslip.setText("GENERATE PAYSLIP");
        btn_PrintPayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PrintPayslipActionPerformed(evt);
            }
        });

        cmbMonthFilter.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cmbMonthFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Months" }));
        cmbMonthFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthFilterActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Filter by Month:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_ViewEmp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_PrintPayslip)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMonthFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbMonthFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_ViewEmp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_PrintPayslip)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("MotorPH");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        btn_Profile.setBackground(new java.awt.Color(0, 0, 204));
        btn_Profile.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Profile.setForeground(new java.awt.Color(255, 255, 255));
        btn_Profile.setText("Employees Profile");
        btn_Profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ProfileActionPerformed(evt);
            }
        });

        btn_LeaveMgt.setBackground(new java.awt.Color(0, 0, 204));
        btn_LeaveMgt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_LeaveMgt.setForeground(new java.awt.Color(255, 255, 255));
        btn_LeaveMgt.setText("Employees Attendance & Leave");
        btn_LeaveMgt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LeaveMgtActionPerformed(evt);
            }
        });

        btn_SalaryAndStatutory.setBackground(new java.awt.Color(0, 0, 204));
        btn_SalaryAndStatutory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_SalaryAndStatutory.setForeground(new java.awt.Color(255, 255, 255));
        btn_SalaryAndStatutory.setText("Employees Salary & Statutory");
        btn_SalaryAndStatutory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SalaryAndStatutoryActionPerformed(evt);
            }
        });

        btn_PayrollSummary.setBackground(new java.awt.Color(0, 0, 204));
        btn_PayrollSummary.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_PayrollSummary.setForeground(new java.awt.Color(255, 255, 255));
        btn_PayrollSummary.setText("Payroll Processing");
        btn_PayrollSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PayrollSummaryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btn_MyRecords.setBackground(new java.awt.Color(102, 102, 102));
        btn_MyRecords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_MyRecords.setForeground(new java.awt.Color(255, 255, 255));
        btn_MyRecords.setText("My Records");
        btn_MyRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MyRecordsActionPerformed(evt);
            }
        });

        btn_EmpRecords.setBackground(new java.awt.Color(102, 102, 102));
        btn_EmpRecords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_EmpRecords.setForeground(new java.awt.Color(255, 255, 255));
        btn_EmpRecords.setText("Employee Records");
        btn_EmpRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EmpRecordsActionPerformed(evt);
            }
        });

        btn_Logout.setBackground(new java.awt.Color(102, 102, 102));
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Version 1.30");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_MyRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel3))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_EmpRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel4))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btn_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_Profile, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btn_LeaveMgt, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btn_SalaryAndStatutory, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btn_PayrollSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Profile)
                    .addComponent(btn_LeaveMgt)
                    .addComponent(btn_SalaryAndStatutory)
                    .addComponent(btn_PayrollSummary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btn_MyRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btn_EmpRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btn_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel1.getAccessibleContext().setAccessibleName("Employee Records");

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_PayrollSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PayrollSummaryActionPerformed
        // Already on Payroll Processing page
    }//GEN-LAST:event_btn_PayrollSummaryActionPerformed

    private void btn_SalaryAndStatutoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SalaryAndStatutoryActionPerformed
        new frm_EmployeesSalary().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_SalaryAndStatutoryActionPerformed

    private void btn_LeaveMgtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveMgtActionPerformed
        new frm_EmployeesAttLeave().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_LeaveMgtActionPerformed

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

    private void btn_EmpRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EmpRecordsActionPerformed
        new frm_EmployeesRecords().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_EmpRecordsActionPerformed

    private void btn_ViewEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ViewEmpActionPerformed
        // Generate and print payroll summary report
        try {
            // Create a formatted payroll summary report
            generatePayrollSummaryReport();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error generating payroll summary: " + ex.getMessage(), 
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_ViewEmpActionPerformed

    private void generatePayrollSummaryReport() {
        // This method generates and displays a payroll summary report
        JOptionPane.showMessageDialog(this, "Payroll Summary Report generation feature coming soon!", "Feature Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private String generatePayslip(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payroll record first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Convert view row index to model row index (important when table is filtered)
        int modelRow = selectedRow;
        if (tbl_Employees.getRowSorter() != null) {
            modelRow = tbl_Employees.getRowSorter().convertRowIndexToModel(selectedRow);
        }

        // Get employee ID and payroll date from the selected row in the current table
        DefaultTableModel model = (DefaultTableModel) tbl_Employees.getModel();
        String empID = model.getValueAt(modelRow, 0).toString(); // Employee ID is in first column
        String payrollStartDate = model.getValueAt(modelRow, 14).toString(); // Payroll start date is in column 14 (hidden)
        
        // Debug: Show which employee and date were selected
        System.out.println("Selected row (view): " + selectedRow + ", model row: " + modelRow);
        System.out.println("Selected Employee ID: " + empID);
        System.out.println("Selected Payroll Start Date: " + payrollStartDate);
        
        // Get complete payroll data from database using employee ID (similar to frm_EmpPayroll)
        List<String[]> payrollData = DatabaseHandler.readPayrollFromDatabase();
        String[] payrollRow = null;
        
        // Find the exact payroll record matching both Employee ID and payroll start date
        for (int i = 1; i < payrollData.size(); i++) { // Skip header row at index 0
            String[] row = payrollData.get(i);
            if (row.length >= 20 && row[3] != null && row[0] != null && 
                row[3].trim().equals(empID.trim()) && 
                row[0].trim().equals(payrollStartDate.trim())) { // Match both Employee ID (col 3) and start date (col 0)
                payrollRow = row;
                System.out.println("Found exact payroll record for Employee ID: " + row[3] + ", Date: " + row[0]); // Debug
                break;
            }
        }
        
        // If exact match not found, try to find by Employee ID only (fallback)
        if (payrollRow == null) {
            System.out.println("Exact match not found, searching by Employee ID only...");
            for (int i = payrollData.size() - 1; i >= 1; i--) { // Search from the end for most recent
                String[] row = payrollData.get(i);
                if (row.length >= 20 && row[3] != null && row[3].trim().equals(empID.trim())) {
                    payrollRow = row;
                    System.out.println("Found payroll record by Employee ID only: " + row[3] + ", Date: " + row[0]); // Debug
                    break;
                }
            }
        }
        
        if (payrollRow == null) {
            JOptionPane.showMessageDialog(this, "No payroll data found for Employee ID: " + empID + " and date: " + payrollStartDate, "No Data", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Extract data from the complete payroll record (using same structure as frm_EmpPayroll)
        String startDate = payrollRow[0] != null ? payrollRow[0] : "";  // Period Start
        String endDate = payrollRow[1] != null ? payrollRow[1] : "";    // Period End  
        String payrollID = payrollRow[2] != null ? payrollRow[2] : "";  // Payroll ID
        String employeeID = payrollRow[3] != null ? payrollRow[3] : ""; // Employee ID
        
        String regularHours = payrollRow[4] != null ? payrollRow[4] : "0";   // Regular Hours
        String overtimeHours = payrollRow[5] != null ? payrollRow[5] : "0";  // Overtime Hours
        String totalHours = payrollRow[6] != null ? payrollRow[6] : "0";     // Total Hours
        
        String salary = payrollRow[7] != null ? payrollRow[7] : "0.00";      // Salary
        String rice = payrollRow[8] != null ? payrollRow[8] : "0.00";        // Rice Allowance
        String phone = payrollRow[9] != null ? payrollRow[9] : "0.00";       // Phone Allowance
        String clothing = payrollRow[10] != null ? payrollRow[10] : "0.00";   // Clothing Allowance
        String totalAllow = payrollRow[11] != null ? payrollRow[11] : "0.00"; // Total Allowances
        
        String gross = payrollRow[12] != null ? payrollRow[12] : "0.00";      // Gross Pay
        String taxable = payrollRow[13] != null ? payrollRow[13] : "0.00";    // Taxable Income
        
        String sss = payrollRow[14] != null ? payrollRow[14] : "0.00";        // SSS
        String philhealth = payrollRow[15] != null ? payrollRow[15] : "0.00"; // PhilHealth
        String pagibig = payrollRow[16] != null ? payrollRow[16] : "0.00";    // Pag-IBIG
        String tax = payrollRow[17] != null ? payrollRow[17] : "0.00";        // Tax
        String deductions = payrollRow[18] != null ? payrollRow[18] : "0.00"; // Total Deductions
        String netPay = payrollRow[19] != null ? payrollRow[19] : "0.00";     // Net Pay

        // Debug: Print the data being used
        System.out.println("Generating payslip for Employee ID: " + employeeID);
        System.out.println("Payroll ID: " + payrollID + ", Period: " + startDate + " to " + endDate);

        return """
    <html>
    <head>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f3f3f3;
                padding: 20px;
            }
            table {
                border-collapse: collapse;
                width: 100%%;
                background-color: #fff;
                border: 1px solid #ccc;
            }
            th {
                background-color: #2c3e50;
                color: white;
                padding: 10px;
                text-align: left;
            }
            td {
                padding: 8px 12px;
                border-bottom: 1px solid #eee;
                vertical-align: top;
            }
            .section {
                background-color: #f9f9f9;
                font-weight: bold;
                color: #2c3e50;
            }
            .highlight {
                background-color: #eafaf1;
                color: #2e7d32;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <h2 style='text-align:center;'>MOTORPH Payslip</h2>
        <table>
            <tr class='section'><td colspan='6'>Employee & Payroll Info</td></tr>
            <tr>
                <td><strong>Payroll ID</strong></td><td>%s</td>
                <td><strong>Employee ID</strong></td><td>%s</td>
                <td><strong>Pay Period</strong></td><td>%s to %s</td>
            </tr>
            <tr class='section'><td colspan='6'>Work Hours</td></tr>
            <tr>
                <td><strong>Regular Hours</strong></td><td>%s hrs</td>
                <td><strong>Overtime Hours</strong></td><td>%s hrs</td>
                <td><strong>Total Hours</strong></td><td>%s hrs</td>
            </tr>
            <tr class='section'><td colspan='6'>Earnings & Allowances</td></tr>
            <tr>
                <td><strong>Basic Salary</strong></td><td>₱%s</td>
                <td><strong>Rice Allowance</strong></td><td>₱%s</td>
                <td><strong>Phone Allowance</strong></td><td>₱%s</td>
            </tr>
            <tr>
                <td><strong>Clothing Allowance</strong></td><td>₱%s</td>
                <td><strong>Total Allowances</strong></td><td colspan='3'>₱%s</td>
            </tr>
            <tr class='section'><td colspan='6'>Deductions</td></tr>
            <tr>
                <td><strong>SSS</strong></td><td>₱%s</td>
                <td><strong>PhilHealth</strong></td><td>₱%s</td>
                <td><strong>Pag-IBIG</strong></td><td>₱%s</td>
            </tr>
            <tr>
                <td><strong>Tax Withheld</strong></td><td>₱%s</td>
                <td><strong>Total Deductions</strong></td><td colspan='3'>₱%s</td>
            </tr>
            <tr class='section'><td colspan='6'>Summary</td></tr>
            <tr>
                <td><strong>Gross Pay</strong></td><td>₱%s</td>
                <td><strong>Taxable Income</strong></td><td>₱%s</td>
                <td class='highlight'><strong>NET PAY</strong></td><td class='highlight'>₱%s</td>
            </tr>
        </table>
        <p style='text-align:center; font-size:11px; color:#777; margin-top:15px;'>
            This is a system-generated payslip. No signature required.
        </p>
    </body>
    </html>
    """.formatted(
                payrollID, employeeID, startDate, endDate,
                regularHours, overtimeHours, totalHours,
                salary, rice, phone, clothing, totalAllow,
                sss, philhealth, pagibig, tax, deductions,
                gross, taxable, netPay
        );
    }

    private void btn_PrintPayslipActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbl_Employees.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payroll record first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String payslipHTML = generatePayslip(selectedRow);

        if (payslipHTML != null) {
            try {
                // Render HTML into a JEditorPane
                JEditorPane editorPane = new JEditorPane();
                editorPane.setContentType("text/html");
                editorPane.setText(payslipHTML);
                editorPane.setSize(1000, editorPane.getPreferredSize().height);
                editorPane.setEditable(false);

                // Force layout & calculate correct height
                editorPane.revalidate();
                editorPane.repaint();

                // Optional preview panel
                JScrollPane preview = new JScrollPane(editorPane);
                int option = JOptionPane.showConfirmDialog(null, preview, "Payslip Preview",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    // Force layout again just before image capture
                    editorPane.setSize(1000, editorPane.getPreferredSize().height);

                    // Create BufferedImage of appropriate height
                    int width = editorPane.getWidth();
                    int height = editorPane.getPreferredSize().height;

                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = image.createGraphics();
                    editorPane.printAll(g2d);
                    g2d.dispose();

                    // Set up printer job to print the image
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPrintable((graphics, pageFormat, pageIndex) -> {
                        if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

                        double x = pageFormat.getImageableX();
                        double y = pageFormat.getImageableY();

                        // Scale image if it's wider than printable area
                        double scaleX = pageFormat.getImageableWidth() / image.getWidth();
                        double scaleY = pageFormat.getImageableHeight() / image.getHeight();
                        double scale = Math.min(scaleX, scaleY);

                        Graphics2D g = (Graphics2D) graphics;
                        g.translate(x, y);
                        g.scale(scale, scale);
                        g.drawImage(image, 0, 0, null);

                        return Printable.PAGE_EXISTS;
                    });

                    if (job.printDialog()) {
                        job.print();
                        JOptionPane.showMessageDialog(this, "Printing successful!", "Print", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Print Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="NetBeans Generated Methods - Additional Methods">
    
    // Removed unused method
    
    // </editor-fold>

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_EmployeesPayrollProcess().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_EmpRecords;
    private javax.swing.JButton btn_LeaveMgt;
    private javax.swing.JButton btn_Logout;
    private javax.swing.JButton btn_MyRecords;
    private javax.swing.JButton btn_PayrollSummary;
    private javax.swing.JButton btn_PrintPayslip;
    private javax.swing.JButton btn_Profile;
    private javax.swing.JButton btn_SalaryAndStatutory;
    private javax.swing.JButton btn_ViewEmp;
    private javax.swing.JComboBox<String> cmbMonthFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_Employees;
    // End of variables declaration//GEN-END:variables
}
