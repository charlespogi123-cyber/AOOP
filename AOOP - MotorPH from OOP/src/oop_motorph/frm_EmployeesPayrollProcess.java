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
        try {
            // Generate HTML content for the payroll summary
            String reportHTML = generatePayrollSummaryHTML();
            
            if (reportHTML != null) {
                // Create a new frame for the formatted report
                javax.swing.JFrame reportFrame = new javax.swing.JFrame("Payroll Summary Report");
                reportFrame.setSize(1400, 900);
                reportFrame.setLocationRelativeTo(this);
                
                // Create an HTML editor pane for better formatting
                javax.swing.JEditorPane editorPane = new javax.swing.JEditorPane();
                editorPane.setContentType("text/html");
                editorPane.setText(reportHTML);
                editorPane.setEditable(false);
                
                // Set preferred size for better display
                editorPane.setPreferredSize(new java.awt.Dimension(1350, 1000));
                
                // Add to scroll pane
                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(editorPane);
                reportFrame.add(scrollPane);
                
                // Add button panel
                javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
                buttonPanel.setBackground(new java.awt.Color(240, 240, 240));
                buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                javax.swing.JButton printBtn = new javax.swing.JButton("Print Report");
                printBtn.setBackground(new java.awt.Color(0, 123, 255));
                printBtn.setForeground(java.awt.Color.WHITE);
                printBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                
                javax.swing.JButton pdfBtn = new javax.swing.JButton("Save as PDF");
                pdfBtn.setBackground(new java.awt.Color(220, 53, 69));
                pdfBtn.setForeground(java.awt.Color.WHITE);
                pdfBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                
                javax.swing.JButton closeBtn = new javax.swing.JButton("Close");
                closeBtn.setBackground(new java.awt.Color(108, 117, 125));
                closeBtn.setForeground(java.awt.Color.WHITE);
                closeBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                
                pdfBtn.addActionListener(e -> {
                    try {
                        saveToPDF(reportHTML);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(reportFrame, 
                            "Error saving PDF: " + ex.getMessage(), 
                            "PDF Export Error", 
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                });
                
                printBtn.addActionListener(e -> {
                    try {
                        // Use the same printing approach as the payslip
                        editorPane.setSize(1200, editorPane.getPreferredSize().height);
                        
                        // Create BufferedImage for printing
                        int width = editorPane.getWidth();
                        int height = editorPane.getPreferredSize().height;
                        
                        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
                            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
                        java.awt.Graphics2D g2d = image.createGraphics();
                        editorPane.printAll(g2d);
                        g2d.dispose();
                        
                        // Print the image
                        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
                        job.setPrintable((graphics, pageFormat, pageIndex) -> {
                            if (pageIndex > 0) return java.awt.print.Printable.NO_SUCH_PAGE;
                            
                            double x = pageFormat.getImageableX();
                            double y = pageFormat.getImageableY();
                            double width2 = pageFormat.getImageableWidth();
                            double height2 = pageFormat.getImageableHeight();
                            
                            // Scale image to fit page
                            double scaleX = width2 / image.getWidth();
                            double scaleY = height2 / image.getHeight();
                            double scale = Math.min(scaleX, scaleY);
                            
                            int scaledWidth = (int) (image.getWidth() * scale);
                            int scaledHeight = (int) (image.getHeight() * scale);
                            
                            graphics.drawImage(image, (int) x, (int) y, scaledWidth, scaledHeight, null);
                            return java.awt.print.Printable.PAGE_EXISTS;
                        });
                        
                        if (job.printDialog()) {
                            job.print();
                            JOptionPane.showMessageDialog(reportFrame, 
                                "Payroll summary report sent to printer successfully!", 
                                "Print Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(reportFrame, 
                            "Error printing report: " + ex.getMessage(), 
                            "Print Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                closeBtn.addActionListener(e -> reportFrame.dispose());
                
                buttonPanel.add(printBtn);
                buttonPanel.add(javax.swing.Box.createHorizontalStrut(10));
                buttonPanel.add(pdfBtn);
                buttonPanel.add(javax.swing.Box.createHorizontalStrut(10));
                buttonPanel.add(closeBtn);
                
                reportFrame.add(buttonPanel, java.awt.BorderLayout.SOUTH);
                reportFrame.setVisible(true);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error generating payroll summary: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generatePayrollSummaryHTML() {
        try {
            // Get filtered data from the table
            DefaultTableModel model = (DefaultTableModel) tbl_Employees.getModel();
            int rowCount = tbl_Employees.getRowCount();
            int columnCount = model.getColumnCount();
            
            if (rowCount == 0) {
                return """
                    <html>
                    <head>
                        <style>
                            body { font-family: Arial, sans-serif; padding: 20px; text-align: center; }
                            .no-data { color: #666; font-size: 18px; margin: 50px; }
                        </style>
                    </head>
                    <body>
                        <h2>MOTORPH Payroll Summary Report</h2>
                        <div class="no-data">No payroll data to display.</div>
                    </body>
                    </html>
                    """;
            }
            
            // Debug: Print column information
            System.out.println("=== PAYROLL SUMMARY DEBUG INFO ===");
            System.out.println("Total columns: " + columnCount);
            for (int i = 0; i < columnCount; i++) {
                System.out.println("Column " + i + ": " + model.getColumnName(i));
            }
            
            // Define specific columns based on the image
            String[] requiredColumns = {
                "Employee No", "Employee Full Name", "Position", "Department", "Gross Income",
                "Social Security No.", "Social Security Contribution", "Philhealth No.", 
                "Philhealth Contribution", "Pag-ibig No.", "Pag-ibig Contribution", 
                "TIN", "Withholding Tax", "Net Pay"
            };
            
            // Generate table headers with specific columns
            StringBuilder tableHeaders = new StringBuilder();
            for (String columnName : requiredColumns) {
                tableHeaders.append(String.format("<th>%s</th>", escapeHtml(columnName)));
            }
            
            System.out.println("Generated headers HTML: " + tableHeaders.toString());
            
            StringBuilder employeeRows = new StringBuilder();
            double[] columnTotals = new double[requiredColumns.length];
            
            // Process each visible row
            for (int i = 0; i < rowCount; i++) {
                int actualRow = tbl_Employees.convertRowIndexToModel(i);
                
                employeeRows.append("<tr>");
                
                // Process each required column
                for (int colIndex = 0; colIndex < requiredColumns.length; colIndex++) {
                    String requiredColumnName = requiredColumns[colIndex];
                    String cellValue = "";
                    
                    // Find the column in the table model by name
                    for (int col = 0; col < columnCount; col++) {
                        String actualColumnName = model.getColumnName(col);
                        if (actualColumnName.equalsIgnoreCase(requiredColumnName) || 
                            actualColumnName.toLowerCase().contains(requiredColumnName.toLowerCase().replace(" ", "")) ||
                            requiredColumnName.toLowerCase().contains(actualColumnName.toLowerCase().replace("_", "").replace(" ", ""))) {
                            cellValue = getTableValueSafe(model, actualRow, col, columnCount);
                            break;
                        }
                    }
                    
                    // Format the cell based on column type
                    String columnNameLower = requiredColumnName.toLowerCase();
                    if (columnNameLower.contains("contribution") || columnNameLower.contains("income") || 
                        columnNameLower.contains("tax") || columnNameLower.contains("net pay")) {
                        double numValue = parseDouble(cellValue);
                        columnTotals[colIndex] += numValue;
                        employeeRows.append(String.format("<td class='amount'>₱%,.2f</td>", numValue));
                    } else {
                        employeeRows.append(String.format("<td>%s</td>", escapeHtml(cellValue)));
                    }
                }
                
                employeeRows.append("</tr>");
            }
            
            // Generate totals row
            StringBuilder totalsRow = new StringBuilder("<tr class='totals-row'>");
            for (int colIndex = 0; colIndex < requiredColumns.length; colIndex++) {
                String columnName = requiredColumns[colIndex];
                String columnNameLower = columnName.toLowerCase();
                
                if (colIndex == 0) {
                    totalsRow.append("<td><strong>TOTALS</strong></td>");
                } else if (columnNameLower.contains("no.") || columnNameLower.contains("no") ||
                          columnNameLower.contains("name") || columnNameLower.contains("position") ||
                          columnNameLower.contains("department") || columnNameLower.contains("tin")) {
                    // Don't show totals for ID, name, position, department, or TIN columns
                    totalsRow.append("<td></td>");
                } else if (columnNameLower.contains("contribution") || columnNameLower.contains("income") || 
                          columnNameLower.contains("tax") || columnNameLower.contains("net pay")) {
                    totalsRow.append(String.format("<td class='amount'>₱%,.2f</td>", columnTotals[colIndex]));
                } else {
                    totalsRow.append("<td></td>");
                }
            }
            totalsRow.append("</tr>");
            
            // Calculate key statistics from specific columns
            double totalGross = 0;
            double totalDeductions = 0;
            double totalNet = 0;
            
            // Debug: Print column information for statistics
            System.out.println("=== STATISTICS CALCULATION DEBUG ===");
            
            // Calculate statistics based on specific columns
            for (int colIndex = 0; colIndex < requiredColumns.length; colIndex++) {
                String columnName = requiredColumns[colIndex];
                double columnTotal = columnTotals[colIndex];
                
                System.out.println("Column " + colIndex + ": " + columnName + " = " + columnTotal);
                
                if (columnName.equalsIgnoreCase("Gross Income")) {
                    totalGross = columnTotal;
                    System.out.println("  -> Set as Total Gross: " + columnTotal);
                } else if (columnName.equalsIgnoreCase("Net Pay")) {
                    totalNet = columnTotal;
                    System.out.println("  -> Set as Total Net: " + columnTotal);
                } else if (columnName.contains("Contribution") || columnName.contains("Tax")) {
                    totalDeductions += columnTotal;
                    System.out.println("  -> Added to Total Deductions: " + columnTotal);
                }
            }
            
            // If we didn't find net pay directly, calculate it as gross - deductions
            if (totalNet == 0 && totalGross > 0) {
                totalNet = totalGross - totalDeductions;
                System.out.println("Calculated Net Pay: " + totalGross + " - " + totalDeductions + " = " + totalNet);
            }
            
            System.out.println("Final Statistics:");
            System.out.println("  Total Employees: " + rowCount);
            System.out.println("  Total Gross: " + totalGross);
            System.out.println("  Total Deductions: " + totalDeductions);
            System.out.println("  Total Net: " + totalNet);
            System.out.println("  Average Gross: " + (rowCount > 0 ? totalGross / rowCount : 0));
            System.out.println("  Average Net: " + (rowCount > 0 ? totalNet / rowCount : 0));
            
            // Get filter information
            String selectedMonth = (String) cmbMonthFilter.getSelectedItem();
            String filterInfo = "";
            if (selectedMonth != null && !"All Months".equals(selectedMonth)) {
                filterInfo = "<p><strong>Filtered by:</strong> " + selectedMonth + "</p>";
            }
            
            String finalHTML = String.format("""
                <html>
                <head>
                    <style>
                        /* Perfect PDF Export Settings - Matches Browser Display Exactly */
                        @page {
                            size: A4 landscape;
                            margin: 0.3in 0.2in;
                        }
                        
                        /* Ensure print styles match screen display */
                        @media screen, print {
                            * {
                                -webkit-print-color-adjust: exact !important;
                                color-adjust: exact !important;
                                box-sizing: border-box !important;
                            }
                            
                            html, body {
                                margin: 0 !important;
                                padding: 0 !important;
                                width: 100%% !important;
                                height: auto !important;
                                font-family: Arial, sans-serif !important;
                                font-size: 12px !important;
                                line-height: 1.3 !important;
                                background: white !important;
                                color: #000 !important;
                            }
                            
                            .header {
                                text-align: center !important;
                                background: #2c3e50 !important;
                                color: white !important;
                                padding: 15px !important;
                                margin-bottom: 10px !important;
                                page-break-inside: avoid !important;
                            }
                            
                            .header h1 {
                                font-size: 24px !important;
                                margin: 0 !important;
                                font-weight: bold !important;
                                color: white !important;
                            }
                            
                            .header p {
                                font-size: 12px !important;
                                margin: 4px 0 0 0 !important;
                                color: white !important;
                            }
                            
                            table {
                                width: 100%% !important;
                                border-collapse: collapse !important;
                                margin-bottom: 10px !important;
                                font-size: 10px !important;
                                table-layout: fixed !important;
                                page-break-inside: auto !important;
                                border-spacing: 0 !important;
                            }
                            
                            th {
                                background-color: #007bff !important;
                                color: white !important;
                                font-weight: bold !important;
                                text-align: center !important;
                                padding: 8px 2px !important;
                                border: 1px solid #ddd !important;
                                font-size: 9px !important;
                                line-height: 1.1 !important;
                                word-wrap: break-word !important;
                                page-break-inside: avoid !important;
                                white-space: nowrap !important;
                                overflow: hidden !important;
                                text-overflow: ellipsis !important;
                            }
                            
                            td {
                                padding: 6px 2px !important;
                                border: 1px solid #ddd !important;
                                text-align: center !important;
                                font-size: 8px !important;
                                line-height: 1.1 !important;
                                word-wrap: break-word !important;
                                overflow: hidden !important;
                                text-overflow: ellipsis !important;
                                white-space: nowrap !important;
                                vertical-align: middle !important;
                            }
                            
                            /* Left align text columns and allow text wrapping for names */
                            td:nth-child(2), td:nth-child(3), td:nth-child(4) {
                                text-align: left !important;
                                white-space: normal !important;
                                word-wrap: break-word !important;
                            }
                            
                            /* Keep numbers and IDs as no-wrap */
                            td:nth-child(1), td:nth-child(5), td:nth-child(6), td:nth-child(7), 
                            td:nth-child(8), td:nth-child(9), td:nth-child(10), td:nth-child(11), 
                            td:nth-child(12), td:nth-child(13), td:nth-child(14) {
                                white-space: nowrap !important;
                                text-align: right !important;
                            }
                            
                            /* Fixed column widths for perfect layout - No overlap */
                            th:nth-child(1), td:nth-child(1) { width: 5%% !important; min-width: 50px !important; }  /* Employee No */
                            th:nth-child(2), td:nth-child(2) { width: 15%% !important; min-width: 120px !important; } /* Employee Full Name */
                            th:nth-child(3), td:nth-child(3) { width: 10%% !important; min-width: 80px !important; }  /* Position */
                            th:nth-child(4), td:nth-child(4) { width: 8%% !important; min-width: 60px !important; }   /* Department */
                            th:nth-child(5), td:nth-child(5) { width: 8%% !important; min-width: 70px !important; }   /* Gross Income */
                            th:nth-child(6), td:nth-child(6) { width: 9%% !important; min-width: 80px !important; }   /* SSS No */
                            th:nth-child(7), td:nth-child(7) { width: 7%% !important; min-width: 60px !important; }   /* SSS Contribution */
                            th:nth-child(8), td:nth-child(8) { width: 9%% !important; min-width: 80px !important; }   /* PhilHealth No */
                            th:nth-child(9), td:nth-child(9) { width: 7%% !important; min-width: 60px !important; }   /* PhilHealth Contribution */
                            th:nth-child(10), td:nth-child(10) { width: 7%% !important; min-width: 60px !important; } /* Pag-ibig No */
                            th:nth-child(11), td:nth-child(11) { width: 7%% !important; min-width: 60px !important; } /* Pag-ibig Contribution */
                            th:nth-child(12), td:nth-child(12) { width: 6%% !important; min-width: 50px !important; } /* TIN */
                            th:nth-child(13), td:nth-child(13) { width: 6%% !important; min-width: 50px !important; } /* Withholding Tax */
                            th:nth-child(14), td:nth-child(14) { width: 6%% !important; min-width: 50px !important; } /* Net Pay */
                            
                            .totals-row {
                                background-color: #28a745 !important;
                                color: white !important;
                                font-weight: bold !important;
                                page-break-inside: avoid !important;
                            }
                            
                            .totals-row td {
                                background-color: #28a745 !important;
                                color: white !important;
                                border: 1px solid #ddd !important;
                                font-weight: bold !important;
                            }
                            
                            .amount {
                                text-align: right !important;
                                font-family: monospace !important;
                            }
                            
                            .number {
                                text-align: center !important;
                                font-family: monospace !important;
                            }
                            
                            .summary-stats {
                                background-color: #f8f9fa !important;
                                border: 1px solid #ddd !important;
                                padding: 15px !important;
                                margin-top: 15px !important;
                                border-radius: 5px !important;
                                page-break-inside: avoid !important;
                            }
                            
                            .stats-row {
                                display: flex !important;
                                justify-content: space-around !important;
                                text-align: center !important;
                                margin-bottom: 10px !important;
                            }
                            
                            .stat-item {
                                flex: 1 !important;
                                margin: 0 10px !important;
                            }
                            
                            .stat-value {
                                font-size: 16px !important;
                                font-weight: bold !important;
                                color: #007bff !important;
                                display: block !important;
                            }
                            
                            .stat-label {
                                font-size: 10px !important;
                                color: #666 !important;
                                margin-top: 2px !important;
                                display: block !important;
                            }
                            
                            .footer {
                                text-align: center !important;
                                margin-top: 20px !important;
                                padding: 10px !important;
                                font-size: 10px !important;
                                color: #666 !important;
                                border-top: 1px solid #ddd !important;
                                page-break-inside: avoid !important;
                            }
                        }
                        body {
                            font-family: Arial, sans-serif;
                            background: white;
                            padding: 0;
                            margin: 0;
                            font-size: 12px;
                            width: 100%%;
                            min-height: 100vh;
                            color: #000;
                            line-height: 1.3;
                        }
                        .header {
                            text-align: center;
                            background: #2c3e50;
                            color: white;
                            padding: 20px 15px;
                            margin-bottom: 15px;
                            width: 100%%;
                            box-sizing: border-box;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                            font-weight: bold;
                            letter-spacing: 1px;
                        }
                        .header p {
                            margin: 8px 0 0 0;
                            font-size: 14px;
                            opacity: 0.95;
                        }
                        table {
                            border-collapse: collapse;
                            width: 100%%;
                            background-color: white;
                            border: 2px solid #333;
                            margin-bottom: 15px;
                            font-size: 11px;
                            table-layout: auto;
                            color: #000;
                        }
                        th {
                            background-color: #2c3e50;
                            color: white;
                            padding: 10px 6px;
                            text-align: center;
                            font-weight: bold;
                            font-size: 11px;
                            border: 1px solid #fff;
                            white-space: nowrap;
                            word-wrap: break-word;
                            min-width: 70px;
                            line-height: 1.2;
                        }
                        td {
                            padding: 8px 6px;
                            border: 1px solid #ddd;
                            vertical-align: middle;
                            font-size: 10px;
                            text-align: center;
                            word-wrap: break-word;
                            color: #000;
                            line-height: 1.2;
                        }
                        .amount {
                            text-align: right;
                            font-weight: bold;
                            color: #27ae60;
                            font-size: 13px;
                        }
                        .number {
                            text-align: right;
                            font-size: 12px;
                        }
                        .totals-row {
                            background-color: #28a745 !important;
                            color: white !important;
                            font-weight: bold;
                        }
                        .totals-row td {
                            border: 1px solid #fff !important;
                            padding: 12px 8px;
                            font-size: 13px;
                            color: white !important;
                        }
                        .totals-row .amount {
                            color: white !important;
                            text-align: right;
                        }
                        .totals-row .number {
                            color: white !important;
                            text-align: right;
                        }
                        .summary-stats {
                            background: white;
                            padding: 15px;
                            margin-top: 15px;
                            border: 2px solid #ddd;
                            border-radius: 5px;
                            box-sizing: border-box;
                        }
                        .stats-grid {
                            display: table;
                            width: 100%%;
                            border-collapse: collapse;
                        }
                        .stat-card {
                            display: table-cell;
                            background: #f8f9fa;
                            padding: 12px 8px;
                            border: 1px solid #ddd;
                            text-align: center;
                            vertical-align: middle;
                        }
                        .stat-label {
                            font-size: 11px;
                            color: #6c757d;
                            margin-bottom: 4px;
                            font-weight: bold;
                        }
                        .stat-value {
                            font-size: 14px;
                            font-weight: bold;
                            color: #2c3e50;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 20px;
                            padding: 12px;
                            color: #6c757d;
                            font-size: 10px;
                            border-top: 2px solid #ddd;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>MOTORPH Payroll Summary Report</h1>
                        <p>Generated on: %s</p>
                        %s
                    </div>
                    
                    <table>
                        <thead>
                            <tr>%s</tr>
                        </thead>
                        <tbody>
                            %s
                            %s
                        </tbody>
                    </table>
                    
                    <div class="summary-stats">
                        <h3 style="margin-top: 0; color: #2c3e50;">Summary Statistics</h3>
                        <div class="stats-grid">
                            <div class="stat-card">
                                <div class="stat-label">Total Employees</div>
                                <div class="stat-value">%d</div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-label">Total Gross Pay</div>
                                <div class="stat-value">₱%,.2f</div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-label">Total Deductions</div>
                                <div class="stat-value">₱%,.2f</div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-label">Total Net Pay</div>
                                <div class="stat-value">₱%,.2f</div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-label">Average Gross Pay</div>
                                <div class="stat-value">₱%,.2f</div>
                            </div>
                            <div class="stat-card">
                                <div class="stat-label">Average Net Pay</div>
                                <div class="stat-value">₱%,.2f</div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p>This is a system-generated payroll summary report from MotorPH Payroll System.</p>
                        <p>Report contains %d employee payroll records. All amounts are in Philippine Pesos (₱).</p>
                    </div>
                </body>
                </html>
                """,
                new java.util.Date().toString(),
                filterInfo,
                tableHeaders.toString(),
                employeeRows.toString(),
                totalsRow.toString(),
                rowCount, totalGross, totalDeductions, totalNet,
                rowCount > 0 ? totalGross / rowCount : 0,
                rowCount > 0 ? totalNet / rowCount : 0,
                rowCount
            );
            
            // Debug: Save HTML to file to inspect
            try {
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("debug_payroll_report.html"), 
                    finalHTML.getBytes()
                );
                System.out.println("HTML saved to debug_payroll_report.html for inspection");
            } catch (Exception ex) {
                System.out.println("Could not save debug HTML: " + ex.getMessage());
            }
            
            return finalHTML;
            
        } catch (Exception e) {
            e.printStackTrace(); // Debug: Print stack trace
            return "<html><body><h2>Error generating report: " + e.getMessage() + "</h2></body></html>";
        }
    }
    
    private boolean isMonetaryColumn(String columnName) {
        String lowerName = columnName.toLowerCase();
        return lowerName.contains("salary") || lowerName.contains("allowance") || 
               lowerName.contains("gross") || lowerName.contains("income") ||
               lowerName.contains("contribution") || lowerName.contains("tax") ||
               lowerName.contains("deduction") || lowerName.contains("netpay") ||
               lowerName.contains("net_pay") || lowerName.contains("amount") ||
               lowerName.contains("sss") || lowerName.contains("philhealth") ||
               lowerName.contains("pagibig") || lowerName.contains("rice") ||
               lowerName.contains("phone") || lowerName.contains("clothing");
    }
    
    private boolean isNumericColumn(String columnName) {
        String lowerName = columnName.toLowerCase();
        // Exclude ID columns from numeric calculations
        if (isIdColumn(columnName)) {
            return false;
        }
        return lowerName.contains("hours") || lowerName.contains("number") || 
               lowerName.contains("count") || lowerName.contains("days");
    }
    
    private boolean isIdColumn(String columnName) {
        String lowerName = columnName.toLowerCase();
        return lowerName.contains("payrollid") || lowerName.contains("payroll_id") ||
               lowerName.contains("employeeid") || lowerName.contains("employee_id") ||
               lowerName.contains("emp_id") || 
               (lowerName.contains("id") && (lowerName.endsWith("id") || lowerName.startsWith("id")));
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    private String getTableValueSafe(DefaultTableModel model, int row, int col, int columnCount) {
        if (row >= 0 && row < model.getRowCount() && col >= 0 && col < columnCount) {
            Object value = model.getValueAt(row, col);
            return value != null ? value.toString() : "";
        }
        return "";
    }

    private String getTableValue(DefaultTableModel model, int row, int col) {
        Object value = model.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }
    
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[₱,]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    private void btn_AddEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddEmpActionPerformed
        int selectedRow = tbl_Employees.getSelectedRow();
        if (selectedRow >= 0) {
            // Get payroll information
            String payrollID = (String) tbl_Employees.getValueAt(selectedRow, 2);
            String empName = (String) tbl_Employees.getValueAt(selectedRow, 4);
            String netPay = (String) tbl_Employees.getValueAt(selectedRow, 26);
            
            // Process payroll for selected record
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Process payroll payment?\n" +
                "Payroll ID: " + payrollID + "\n" +
                "Employee: " + empName + "\n" +
                "Net Pay: ₱" + netPay, 
                "Confirm Process", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, 
                    "Payroll processed successfully!\n" +
                    "Payment of ₱" + netPay + " for " + empName + " has been approved.", 
                    "Process Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the table
                displayEmployeeData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a payroll record to process.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_AddEmpActionPerformed

    private void btn_DelEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DelEmpActionPerformed
        int selectedRow = tbl_Employees.getSelectedRow();
        if (selectedRow >= 0) {
            // Get payroll information
            String payrollID = (String) tbl_Employees.getValueAt(selectedRow, 2);
            String empName = (String) tbl_Employees.getValueAt(selectedRow, 4);
            String currentNetPay = (String) tbl_Employees.getValueAt(selectedRow, 26);
            
            // Edit adjustments for selected payroll record
            String adjustment = JOptionPane.showInputDialog(this, 
                "Current Net Pay: ₱" + currentNetPay + "\n" +
                "Enter adjustment amount for " + empName + "\n" +
                "(Payroll ID: " + payrollID + "):", 
                "Edit Adjustments", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (adjustment != null && !adjustment.trim().isEmpty()) {
                try {
                    double adjAmount = Double.parseDouble(adjustment.trim());
                    JOptionPane.showMessageDialog(this, 
                        "Adjustment saved successfully!\n" +
                        "Payroll ID: " + payrollID + "\n" +
                        "Employee: " + empName + "\n" +
                        "Adjustment: ₱" + String.format("%.2f", adjAmount), 
                        "Adjustment Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid adjustment amount. Please enter a valid number.", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a payroll record to edit adjustments.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_DelEmpActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        new frm_EmployeesPayrollPaid().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_refreshActionPerformed

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

    private void saveToPDF(String htmlContent) {
        // Show instructions first and wait for user to click OK
        int result = javax.swing.JOptionPane.showConfirmDialog(this,
            "MotorPH Payroll Report - PDF Export Instructions\n\n" +
            "After clicking OK, the payroll report will open in your browser.\n\n" +
            "File naming format: MotorPH_PayrollReport_[Month]_YYYY-MM-DD_HHMM\n" +
            "Example: MotorPH_PayrollReport_January2024_2025-07-13_1430.pdf\n\n" +
            "To save as PDF with EXACT formatting:\n\n" +
            "1. Press Ctrl+P (or Cmd+P on Mac) to open print dialog\n" +
            "2. Select 'Save as PDF' or 'Microsoft Print to PDF' as destination\n" +
            "3. IMPORTANT: Set these print settings:\n" +
            "   • Paper size: A4\n" +
            "   • Orientation: Landscape\n" +
            "   • Margins: None (or Minimum)\n" +
            "   • Scale: 100% (Default)\n" +
            "   • Background graphics: ON (Enable)\n" +
            "4. Use the suggested filename or create your own\n" +
            "5. Click 'Save' and choose your desired location\n\n" +
            "Note: These settings ensure the PDF matches the browser display exactly.\n\n" +
            "Click OK to continue and open the report in your browser.",
            "MotorPH PDF Export Instructions - Please Read",
            javax.swing.JOptionPane.OK_CANCEL_OPTION,
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        // Only proceed if user clicked OK
        if (result == javax.swing.JOptionPane.OK_OPTION) {
            try {
                // Create better file naming convention
                String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                String currentTime = new java.text.SimpleDateFormat("HHmm").format(new java.util.Date());
                String selectedMonth = (String) cmbMonthFilter.getSelectedItem();
                
                // Create descriptive filename components
                String monthSuffix = "";
                if (selectedMonth != null && !"All Months".equals(selectedMonth)) {
                    // Clean the month filter for filename (remove spaces and special characters)
                    monthSuffix = "_" + selectedMonth.replaceAll("[^a-zA-Z0-9]", "");
                }
                
                String baseFileName = "MotorPH_PayrollReport" + monthSuffix + "_" + currentDate + "_" + currentTime;
                
                // Create a temporary HTML file to convert to PDF
                java.io.File tempHtml = java.io.File.createTempFile(baseFileName, ".html");
                
                // Write the HTML content with enhanced print-optimized CSS for exact browser matching
                String printOptimizedHTML = htmlContent
                    // Ensure print styles apply to screen as well for perfect consistency
                    .replace("@media print {", "@media screen, print {");
                
                try (java.io.FileWriter writer = new java.io.FileWriter(tempHtml)) {
                    writer.write(printOptimizedHTML);
                }
                
                // Use Java's Desktop API to open and print the HTML (which allows save as PDF)
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                    
                    // Open the HTML file in the default browser for printing/PDF saving
                    desktop.browse(tempHtml.toURI());
                    
                    // Clean up temp file after delay
                    java.util.Timer timer = new java.util.Timer();
                    timer.schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            try {
                                tempHtml.delete();
                            } catch (Exception e) {
                                // Ignore cleanup errors
                            }
                            timer.cancel();
                        }
                    }, 10000); // Delete after 10 seconds
                } else {
                    // Fallback: save HTML file directly to Desktop
                    String userHome = System.getProperty("user.home");
                    java.io.File desktopDir = new java.io.File(userHome, "Desktop");
                    if (!desktopDir.exists()) {
                        desktopDir = new java.io.File(userHome); // fallback to home directory
                    }
                    
                    String fallbackFileName = baseFileName + ".html";
                    java.io.File htmlFile = new java.io.File(desktopDir, fallbackFileName);
                    
                    java.nio.file.Files.copy(tempHtml.toPath(), htmlFile.toPath(), 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    javax.swing.JOptionPane.showMessageDialog(this,
                        "MotorPH Payroll Report saved as HTML file:\n" + htmlFile.getAbsolutePath() + "\n\n" +
                        "You can open this file in a browser and save as PDF using Ctrl+P\n" +
                        "The filename follows the format: MotorPH_PayrollReport_[Month]_YYYY-MM-DD_HHMM.html",
                        "HTML Export Complete - " + fallbackFileName,
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        
                    tempHtml.delete();
                }
                
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error exporting PDF: " + ex.getMessage(),
                    "Export Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
