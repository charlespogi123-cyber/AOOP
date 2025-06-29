package oop_motorph;

import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

public class CSVHandler {

    // File paths
    public static final String CSV_AttendancePath = "oop_motorph/resources/CSV_Attendance.csv";
    public static final String CSV_EmployeesPath = "oop_motorph/resources/CSV_Employees.csv";
    public static final String CSV_PayrollPath = "oop_motorph/resources/CSV_Payroll.csv";
    public static final String CSV_UsersPath = "oop_motorph/resources/CSV_Users.csv";
    public static final String CSV_SalaryPath = "oop_motorph/resources/CSV_Salary.csv";

    // private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm:ss");

    // User Credentials
    public static Map<String, String[]> loadCredentials() {
        Map<String, String[]> credentialsMap = new HashMap<>();
        List<String[]> userData = readCSV(CSV_UsersPath);

        for (String[] row : userData) {
            if (row.length >= 3) { // Ensure the row has the correct number of columns
                String username = row[0].trim();
                String[] userDetails = Arrays.copyOfRange(row, 1, row.length);
                credentialsMap.put(username, userDetails);
            }
        }

        return credentialsMap;
    }


    // Read CSV and return data as a list of String arrays
    private static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        InputStream in = CSVHandler.class.getResourceAsStream("/" + filePath);
        if (in == null) {
            System.err.println("Could not find resource: " + filePath);
            return data;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            boolean isFirstRow = true;
            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    data.add(line.split(",", -1));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
        return data;
    }

    public static boolean employeeExists(String empID, String existingEmpID) {
        List<EmpDetails> employees = getEmployeeData();

        for (EmpDetails emp : employees) {
            if (existingEmpID != null && emp.getEmpID().equals(existingEmpID)) {
                continue;
            }
            if (emp.getEmpID().equals(empID)) {
                return true;  // Found another employee with the same ID
            }
        }
        return false;  // No duplicates found
    }

    private static void saveCSV(String filePath, List<String[]> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File does not exist. A new file will be created: " + filePath);
            }

            String header = "";
            if (filePath.equals(CSV_EmployeesPath)) {
                header = "EmpID,First Name,Last Name,Birthday,Address,Phone Number,Status,Position,Immediate Supervisor";
            } else if (filePath.equals(CSV_AttendancePath)) {
                //      header = "EmpID,First Name,Last Name,Date,TimeIn,TimeOut,HoursWorked,Duration,AttendanceType,AttendanceStat,VLCount,VLUsed,VLBal,SLCount,SLUsed,SLBal";
                header = "EmpID,First Name,Last Name,Status,Position,ImmediateSup,DateFrom,DateTo,TimeIn,TimeOut,HoursWorked,Duration,AttendanceType,AttendanceStat,VLCount,VLUsed,VLBal,SLCount,SLUsed,SLBal";

            } else if (filePath.equals(CSV_SalaryPath)) {
                header = "EmpID,First Name,Last Name,SSS No,PhilHealth No,TIN No,PAGIBIG No,BasicSalary,RiceAllow,PhoneAllow,ClothingAllow,GrossSemi,HourlyRate";
            } else {
                System.err.println("Unknown file path: " + filePath);
                return;
            }

            // Write header
            bw.write(header);
            bw.newLine();

            // Check if data is empty
            if (data.isEmpty()) {
                System.out.println("No data to save.");
            } else {
                // Write each row with proper formatting
                for (String[] row : data) {
                    String formattedRow = formatCSVRow(row);
                    bw.write(formattedRow);
                    bw.newLine();
                }
            }

            bw.flush();


            // Debug: Verify saved data
            List<String[]> savedData = readCSV(filePath);
            for (String[] row : savedData) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to format CSV rows properly
    private static String formatCSVRow(String[] row) {
        return Arrays.stream(row)
                .map(field -> field.contains(",") ? "\"" + field + "\"" : field) // Handle commas in data
                .collect(Collectors.joining(","));
    }

    // Read employee data and return a list of EmpDetails objects
    public static List<EmpDetails> getEmployeeData() {
        List<String[]> employeeData = readCSV(CSV_EmployeesPath);
        List<EmpDetails> employees = new ArrayList<>();

        // Debug: Print the raw data read from CSV_Employees.csv
        System.out.println("Raw data from CSV_Employees.csv:");
        for (String[] row : employeeData) {
            System.out.println(Arrays.toString(row));
        }

        for (String[] row : employeeData) {
            if (row.length == 9) { // Ensure the row has the correct number of columns
                EmpDetails emp = new EmpDetails(
                        row[0].trim(), // EmpID
                        row[1].trim(), // First Name
                        row[2].trim(), // Last Name
                        row[3].trim(), // Birthday
                        row[4].trim(), // Address
                        row[5].trim(), // Phone Number
                        row[6].trim(), // Status
                        row[7].trim(), // Position
                        row[8].trim()  // Immediate Supervisor
                );
                employees.add(emp);

                // Debug: Print the employee details being added to the list
                System.out.println("Adding employee: " + emp);
            } else {
                System.out.println("Skipping invalid row (not enough columns): " + Arrays.toString(row));
            }
        }

        return employees;
    }

    // Update employee record
    public static void updateEmployee(EmpDetails updatedEmp) {
        List<String[]> employeeData = readCSV(CSV_EmployeesPath);
        for (int i = 0; i < employeeData.size(); i++) {
            String[] row = employeeData.get(i);
            if (row[0].equals(updatedEmp.getEmpID())) {
                row[1] = updatedEmp.getFirstName();
                row[2] = updatedEmp.getLastName();
                row[3] = updatedEmp.getBirthdate();
                row[4] = updatedEmp.getAddress();
                row[5] = updatedEmp.getPhoneNumber();
                row[6] = updatedEmp.getEmployeeStatus();
                row[7] = updatedEmp.getPosition();
                row[8] = updatedEmp.getImmediateSupervisor();
                break;
            }
        }
        saveCSV(CSV_EmployeesPath, employeeData);

        // You might need to update corresponding records in Attendance and Salary as well
        updateAttendanceOrSalary(updatedEmp);
    }

    private static void updateAttendanceOrSalary(EmpDetails updatedEmp) {      //-----------???????
        // You can update the Attendance CSV file
        List<String[]> attendanceData = readCSV(CSV_AttendancePath);
        for (int i = 0; i < attendanceData.size(); i++) {
            String[] row = attendanceData.get(i);
            if (row[0].equals(updatedEmp.getEmpID())) {
                row[1] = updatedEmp.getFirstName();
                row[2] = updatedEmp.getLastName();
                // Update any other fields if needed
                break;
            }
        }
        saveCSV(CSV_AttendancePath, attendanceData);

        // Similarly, update the Salary CSV file if needed
        List<String[]> salaryData = readCSV(CSV_SalaryPath);
        for (int i = 0; i < salaryData.size(); i++) {
            String[] row = salaryData.get(i);
            if (row[0].equals(updatedEmp.getEmpID())) {
                row[1] = updatedEmp.getFirstName();
                row[2] = updatedEmp.getLastName();
                // Update any other fields if needed
                break;
            }
        }
        saveCSV(CSV_SalaryPath, salaryData);
    }


    public static void addEmployee(EmpDetails newEmp) {
        List<String[]> employeeData = readCSV(CSV_EmployeesPath);
        String[] newEmployee = new String[]{
                newEmp.getEmpID(), newEmp.getFirstName(), newEmp.getLastName(),
                newEmp.getBirthdate(), newEmp.getAddress(), newEmp.getPhoneNumber(),
                newEmp.getEmployeeStatus(), newEmp.getPosition(), newEmp.getImmediateSupervisor()
        };
        employeeData.add(newEmployee);
        saveCSV(CSV_EmployeesPath, employeeData);

        // Add to Attendance file
        List<String[]> attendanceData = readCSV(CSV_AttendancePath);
        for (String[] row : attendanceData) {

        }

        // Add attendance data for new employee
        String[] newAttendance = new String[]{
                newEmp.getEmpID(), newEmp.getFirstName(), newEmp.getLastName(), newEmp.getEmployeeStatus(), newEmp.getPosition(), newEmp.getImmediateSupervisor(),
                "01-Jan-00", "01-Jan-00", // Default date for attdateFrom and attdateTo
                "", "", "", "", "", "", "0", "0", "", "", "0", "0", "0", "0", "0", "0"
        };
        attendanceData.add(newAttendance);
        saveCSV(CSV_AttendancePath, attendanceData);


        // Add to Salary file
        List<String[]> salaryData = readCSV(CSV_SalaryPath);
        for (String[] row : salaryData) {

        }

        // Add salary data for new employee
        String[] newSalary = new String[]{
                newEmp.getEmpID(), newEmp.getFirstName(), newEmp.getLastName(),
                "", "", "", "", "0", "0", "0", "0", "0", "0"
        };
        salaryData.add(newSalary);
        saveCSV(CSV_SalaryPath, salaryData);

    }


    public static void deleteEmployee(String empID) {
        // Delete from Employee file
        List<String[]> employeeData = readCSV(CSV_EmployeesPath);
        Iterator<String[]> employeeIterator = employeeData.iterator();
        while (employeeIterator.hasNext()) {
            String[] row = employeeIterator.next();
            if (row[0].equals(empID)) { // empID is assumed to be in the first column
                employeeIterator.remove();
                break;
            }
        }
        saveCSV(CSV_EmployeesPath, employeeData);

        // Delete from Attendance file
        List<String[]> attendanceData = readCSV(CSV_AttendancePath);
        Iterator<String[]> attendanceIterator = attendanceData.iterator();
        while (attendanceIterator.hasNext()) {
            String[] row = attendanceIterator.next();
            if (row[0].equals(empID)) {
                attendanceIterator.remove();
                break;
            }
        }
        saveCSV(CSV_AttendancePath, attendanceData);

        // Delete from Salary file
        List<String[]> salaryData = readCSV(CSV_SalaryPath);
        Iterator<String[]> salaryIterator = salaryData.iterator();
        while (salaryIterator.hasNext()) {
            String[] row = salaryIterator.next();
            if (row[0].equals(empID)) {
                salaryIterator.remove();
                break;
            }
        }
        saveCSV(CSV_SalaryPath, salaryData);
    }


    // Reads attendance data and returns a list of EmpAttLeave objects
    public static List<EmpAttLeave> getAttendanceData() {
        List<EmpAttLeave> attendanceList = new ArrayList<>();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
        DateTimeFormatter tableDateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");

        // Use getResourceAsStream instead of FileReader
        InputStream in = CSVHandler.class.getResourceAsStream("/" + CSV_AttendancePath);
        if (in == null) {
            System.err.println("Could not find resource: " + CSV_AttendancePath);
            return attendanceList;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Handles empty values

                if (values.length >= 19) {
                    try {
                        String empID = values[0].trim();
                        String firstName = values[1].trim();
                        String lastName = values[2].trim();
                        String empStatus = values[3].trim();
                        String position = values[4].trim();
                        String immSupervisor = values[5].trim();

                        // Validate and parse dates
                        LocalDate attdateFrom = parseDate(values[6]);
                        LocalDate attdateTo = parseDate(values[7]);

                        // Validate and parse times
                        LocalTime timeIn = parseTime(values[8]);
                        LocalTime timeOut = parseTime(values[9]);

                        // Handle numeric values safely
                        double hoursWorked = parseDouble(values[10]);
                        double duration = parseDouble(values[11]);
                        String attendanceType = values[12].trim();
                        String attendanceStat = values[13].trim();

                        double vlCount = parseDouble(values[14]);
                        double vlUsed = parseDouble(values[15]);
                        double vlBal = parseDouble(values[16]);
                        double slCount = parseDouble(values[17]);
                        double slUsed = parseDouble(values[18]);
                        double slBal = parseDouble(values[19]);

                        EmpAttLeave record = new EmpAttLeave(empID, firstName, lastName, empStatus, position, immSupervisor,
                                attdateFrom, attdateTo, timeIn, timeOut,
                                hoursWorked, duration, attendanceType, attendanceStat,
                                vlCount, vlUsed, vlBal, slCount, slUsed, slBal);

                        attendanceList.add(record);
                    } catch (Exception e) {
                        System.err.println("Skipping row due to parsing error: " + Arrays.toString(values));
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Skipping row due to missing columns: " + Arrays.toString(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return attendanceList;
    }

    // Reads salary data and returns a list of EmpSalaryDetails objects
    public static List<EmpSalaryDetails> getSalaryData() {
        List<EmpSalaryDetails> salaryList = new ArrayList<>();

        // Use getResourceAsStream instead of FileReader
        InputStream in = CSVHandler.class.getResourceAsStream("/" + CSV_SalaryPath);
        if (in == null) {
            System.err.println("Could not find resource: " + CSV_SalaryPath);
            return salaryList;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Handles empty values

                if (values.length >= 13) {
                    try {
                        String empId = values[0].trim();
                        String firstName = values[1].trim();
                        String lastName = values[2].trim();
                        String sssNo = values[3].trim();
                        String philhealthNo = values[4].trim();
                        String tinNo = values[5].trim();
                        String pagibigNo = values[6].trim();

                        // Parse numeric values safely
                        double basicSalary = parseDouble(values[7]);
                        double riceAllow = parseDouble(values[8]);
                        double phoneAllow = parseDouble(values[9]);
                        double clothingAllow = parseDouble(values[10]);
                        double grossSemi = parseDouble(values[11]);
                        double hourlyRate = parseDouble(values[12]);

                        salaryList.add(new EmpSalaryDetails(empId, firstName, lastName, sssNo, philhealthNo, tinNo, pagibigNo,
                                basicSalary, riceAllow, phoneAllow, clothingAllow, grossSemi, hourlyRate));

                    } catch (NumberFormatException e) {
                        System.err.println("Skipping row due to invalid number format: " + Arrays.toString(values));
                    }
                } else {
                    System.err.println("Skipping row due to insufficient data: " + Arrays.toString(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return salaryList;
    }

    public static void addEmployeeSalary(EmpSalaryDetails newEmpSalary) {
        List<String[]> salaryData = readCSV(CSV_SalaryPath);

        // Check if the employee already exists in the salary data
        boolean employeeExists = false;
        for (String[] row : salaryData) {
            if (row[0].equals(newEmpSalary.getEmpID())) {
                employeeExists = true;
                break;
            }
        }

        // If employee does not exist, add a new salary record
        if (!employeeExists) {
            String[] newSalary = new String[]{
                    newEmpSalary.getEmpID(), newEmpSalary.getFirstName(), newEmpSalary.getLastName(),
                    newEmpSalary.getSssNo(), newEmpSalary.getPhilhealthNo(), newEmpSalary.getTinNo(),
                    newEmpSalary.getPagibigNo(), String.valueOf(newEmpSalary.getBasicSalary()),
                    String.valueOf(newEmpSalary.getRiceSubsidy()), String.valueOf(newEmpSalary.getPhoneAllowance()),
                    String.valueOf(newEmpSalary.getClothingAllowance()), String.valueOf(newEmpSalary.getGrossSemi()),
                    String.valueOf(newEmpSalary.getHourlyRate())
            };
            salaryData.add(newSalary);
            saveCSV(CSV_SalaryPath, salaryData);  // Save updated salary data to CSV
        } else {
            // Optionally, you could update the existing record instead of adding a duplicate
            System.out.println("Employee salary already exists. Skipping addition.");
        }
    }

    public static void updateEmployeeSalary(EmpSalaryDetails updatedSalary) {
        List<String[]> salaryData = readCSV(CSV_SalaryPath);
        for (int i = 0; i < salaryData.size(); i++) {
            String[] row = salaryData.get(i);
            if (row[0].equals(updatedSalary.getEmpID())) {
                row[1] = updatedSalary.getFirstName();
                row[2] = updatedSalary.getLastName();
                row[3] = updatedSalary.getSssNo();
                row[4] = updatedSalary.getPhilhealthNo();
                row[5] = updatedSalary.getTinNo();
                row[6] = updatedSalary.getPagibigNo();
                row[7] = String.valueOf(updatedSalary.getBasicSalary());
                row[8] = String.valueOf(updatedSalary.getRiceSubsidy());
                row[9] = String.valueOf(updatedSalary.getPhoneAllowance());
                row[10] = String.valueOf(updatedSalary.getClothingAllowance());
                row[11] = String.valueOf(updatedSalary.getGrossSemi());
                row[12] = String.valueOf(updatedSalary.getHourlyRate());
                break;
            }
        }
        saveCSV(CSV_SalaryPath, salaryData);
    }

    // Add or Update employee details in the Employee, Attendance, and Salary files
    public static void addOrUpdateEmployeeInSalary(EmpDetails updatedEmp) {
        // Update Employee file: Only update EmpID, FirstName, and LastName
        List<String[]> employeeData = readCSV(CSV_EmployeesPath);

        // Check if the employee exists and update their details (EmpID, FirstName, LastName)
        boolean employeeUpdated = false;
        for (String[] row : employeeData) {
            if (row[0].equals(updatedEmp.getEmpID())) {
                row[1] = updatedEmp.getFirstName();
                row[2] = updatedEmp.getLastName();
                employeeUpdated = true;
                break;
            }
        }

        // If employee does not exist, add a new record
        if (!employeeUpdated) {
            String[] newEmployee = new String[]{
                    updatedEmp.getEmpID(), updatedEmp.getFirstName(), updatedEmp.getLastName(),
                    updatedEmp.getBirthdate(), updatedEmp.getAddress(), updatedEmp.getPhoneNumber(),
                    updatedEmp.getEmployeeStatus(), updatedEmp.getPosition(), updatedEmp.getImmediateSupervisor()
            };
            employeeData.add(newEmployee);
        }

        saveCSV(CSV_EmployeesPath, employeeData);

        // Update Attendance file: Only update EmpID, FirstName, and LastName
        List<String[]> attendanceData = readCSV(CSV_AttendancePath);
        boolean attendanceUpdated = false;

        // Check if the employee exists and update their details (EmpID, FirstName, LastName)
        for (String[] row : attendanceData) {
            if (row[0].equals(updatedEmp.getEmpID())) {
                row[1] = updatedEmp.getFirstName();
                row[2] = updatedEmp.getLastName();
                attendanceUpdated = true;
                break;
            }
        }

        // If attendance record does not exist, add a new record
        if (!attendanceUpdated) {
            String[] newAttendance = new String[]{
                    updatedEmp.getEmpID(), updatedEmp.getFirstName(), updatedEmp.getLastName(),
                    "", "", "", "", "", "", "", "0", "0", "", "", "0", "0", "0", "0", "0", "0"
            };
            attendanceData.add(newAttendance);
        }

        saveCSV(CSV_AttendancePath, attendanceData);

    }

    public static void saveAttendanceRequest(EmpAttLeave empAttLeave) {
        List<String[]> attendanceData = readCSV(CSV_AttendancePath);


        // Ensure time-in and time-out are not null
        String timeInStr = (empAttLeave.getTimeIn() != null) ? empAttLeave.getTimeIn().format(TIME_FORMATTER) : "08:00";
        String timeOutStr = (empAttLeave.getTimeOut() != null) ? empAttLeave.getTimeOut().format(TIME_FORMATTER) : "17:00";


        // Prepare new row
        String[] newRow = {
                empAttLeave.getEmpID(),
                empAttLeave.getFirstName(),
                empAttLeave.getLastName(),
                empAttLeave.getEmployeeStatus(),
                empAttLeave.getPosition(),
                empAttLeave.getImmediateSupervisor(),
                empAttLeave.getAttDateFrom().format(DATE_FORMATTER),
                empAttLeave.getAttDateTo().format(DATE_FORMATTER),
                empAttLeave.getTimeIn() != null ? empAttLeave.getTimeIn().format(TIME_FORMATTER) : "",
                empAttLeave.getTimeOut() != null ? empAttLeave.getTimeOut().format(TIME_FORMATTER) : "",
                String.valueOf(empAttLeave.getHoursWorked()),
                String.valueOf(empAttLeave.getDuration()),
                empAttLeave.getAttendanceType(),
                empAttLeave.getAttendanceStatus(),
                String.valueOf(empAttLeave.getVlCount()),
                String.valueOf(empAttLeave.getVlUsed()),
                String.valueOf(empAttLeave.getVlBalance()),
                String.valueOf(empAttLeave.getSlCount()),
                String.valueOf(empAttLeave.getSlUsed()),
                String.valueOf(empAttLeave.getSlBalance())
        };

        // Add the new row
        attendanceData.add(newRow);

        // Save back to CSV
        saveCSV(CSV_AttendancePath, attendanceData);
    }


    public static void saveEditAttendanceRequest(EmpAttLeave empAttLeave, String oldDateFrom, String oldDateTo) {
        List<String[]> attendanceData = readCSV(CSV_AttendancePath);
        boolean updated = false;

        for (int i = 0; i < attendanceData.size(); i++) {
            String[] row = attendanceData.get(i);

            // Find the row using EmpID and the OLD date values
            if (row[0].equals(empAttLeave.getEmpID()) &&
                    row[6].equals(oldDateFrom) &&
                    row[7].equals(oldDateTo)) {


                // Update the row with new values
                row[1] = empAttLeave.getFirstName();
                row[2] = empAttLeave.getLastName();
                row[3] = empAttLeave.getEmployeeStatus();
                row[4] = empAttLeave.getPosition();
                row[5] = empAttLeave.getImmediateSupervisor();
                row[6] = empAttLeave.getAttDateFrom().format(DATE_FORMATTER); // New date from
                row[7] = empAttLeave.getAttDateTo().format(DATE_FORMATTER);   // New date to
                row[8] = empAttLeave.getTimeIn() != null ? empAttLeave.getTimeIn().format(TIME_FORMATTER) : "";
                row[9] = empAttLeave.getTimeOut() != null ? empAttLeave.getTimeOut().format(TIME_FORMATTER) : "";
                row[10] = String.valueOf(empAttLeave.getHoursWorked());
                row[11] = String.valueOf(empAttLeave.getDuration());
                row[12] = empAttLeave.getAttendanceType();
                row[13] = empAttLeave.getAttendanceStatus();
                row[14] = String.valueOf(empAttLeave.getVlCount());
                row[15] = String.valueOf(empAttLeave.getVlUsed());
                row[16] = String.valueOf(empAttLeave.getVlBalance());
                row[17] = String.valueOf(empAttLeave.getSlCount());
                row[18] = String.valueOf(empAttLeave.getSlUsed());
                row[19] = String.valueOf(empAttLeave.getSlBalance());

                updated = true;
                break; // Stop after updating the first matching row
            }
        }

        // Save back to CSV only if an update was made
        if (updated) {
            saveCSV(CSV_AttendancePath, attendanceData);
            System.out.println("Attendance record updated successfully.");
        } else {
            System.out.println("Matching attendance record not found. No update was made.");
        }
    }


    // Attendance Approval
    public static void updateAttendanceStatus(String empID, String newStatus, String message) {
        String filePath = CSV_AttendancePath; // Update this with your actual file path
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming CSV is comma-separated
                if (values.length > 13 && values[0].equals(empID)) {
                    values[13] = newStatus; // Updating the AttendanceStatus column
                    //revised due to approval issue
                    //    if (values.length > 13 && values[0].equals(empID)) {
                    //    values[13] = newStatus; // Updating the AttendanceStatus column  //revised due to approval issue

                }
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading the attendance file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Writing the updated data back to the CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating the attendance file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void savePayrollData(

            String empID, String firstName, String lastName,
            String payDateFrom, String payDateTo, String basicSalary, String hourlyRate,
            String totalAllowances, String hrsPerMonth, String totalHrsWorked, String adjEarnings,
            String totalEarnings, String tardinessAbsences, String eeTax,
            String eeSSS, String eePagibig, String eePhilhealth, String adjDeductions,
            String totalDeductions, String netPay, String payStatus) {

        String transacID = generateTransactionID();
        List<String[]> allRows = new ArrayList<>();
        boolean updated = false;

        // Read existing CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PayrollPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                // Check if the row matches the employee ID and payDateFrom
                if (row.length > 1 && row[1].equals(empID) && row[4].equals(payDateFrom)) {
                    // Update existing entry
                    row = new String[]{
                            transacID, empID, firstName, lastName, payDateFrom, payDateTo,
                            basicSalary, hourlyRate, totalAllowances, hrsPerMonth, totalHrsWorked, adjEarnings,
                            totalEarnings, tardinessAbsences, eeTax, eeSSS, eePagibig, eePhilhealth, adjDeductions,
                            totalDeductions, netPay, payStatus
                    };
                    updated = true;
                }
                allRows.add(row);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + CSV_PayrollPath);
            e.printStackTrace();
        }

        // If no update happened, append a new entry
        if (!updated) {
            allRows.add(new String[]{
                    transacID, empID, firstName, lastName, payDateFrom, payDateTo,
                    basicSalary, hourlyRate, totalAllowances, hrsPerMonth, totalHrsWorked, adjEarnings,
                    totalEarnings, tardinessAbsences, eeTax, eeSSS, eePagibig, eePhilhealth, adjDeductions,
                    totalDeductions, netPay, payStatus
            });
        }

        // Rewrite CSV with updated data
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PayrollPath))) {
            for (String[] row : allRows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + CSV_PayrollPath);
            e.printStackTrace();
        }
    }

    // Method to generate a 7-character alphanumeric transaction ID in Payroll
    private static String generateTransactionID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 7; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    // Method to append a new row to a CSV file
    private static void appendToCSV(String filePath, String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String row = String.join(",", data);
            bw.write(row);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    }

    // Read the payroll CSV file
    public static List<String[]> readPayrollCSV() {
        List<String[]> payrollData = new ArrayList<>();

        // Use getResourceAsStream instead of FileReader
        InputStream in = CSVHandler.class.getResourceAsStream("/" + CSV_PayrollPath);
        if (in == null) {
            System.err.println("Could not find resource: " + CSV_PayrollPath);
            return payrollData;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    payrollData.add(line.split(",", -1));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + CSV_PayrollPath);
            e.printStackTrace();
        }

        return payrollData;
    }

    // Display Payroll Data (Debugging)
    public static void displayPayrollData() {
        List<String[]> payrollData = readPayrollCSV();
        if (payrollData.isEmpty()) {
            System.out.println("No payroll records found.");
        } else {
            System.out.println("Payroll Data:");
            for (String[] row : payrollData) {
                System.out.println(Arrays.toString(row));
            }
        }
    }

    public static void saveUpdatedPayrollData(List<String[]> payrollData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PayrollPath, false))) {
            for (String[] row : payrollData) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing updated payroll data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void updatePayrollCSV(String empID, String firstName, String lastName, String payDateFrom,
                                        String payDateTo, String basicSalary, String hourlyRate,
                                        String totalAllowances, String hrsPerMonth, String totalHrsWorked,
                                        String adjEarnings, String totalEarnings, String tardinessAbsences,
                                        String eeTax, String eeSSS, String eePagibig, String eePhilhealth,
                                        String adjDeductions, String totalDeductions, String netPay,
                                        String payStatus) {
        String csvFile = "payroll.csv";
        List<String[]> payrollData = readPayrollCSV();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (String[] row : payrollData) {
                if (row.length >= 21 && row[1].equals(empID)) {
                    // Update only the PayStatus column
                    row[20] = payStatus;
                }
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePayrollStatus(String employeeID, String newStatus, String successMessage) {
        String filePath = CSV_PayrollPath;
        List<String[]> payrollData = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length > 21 && row[1].equals(employeeID)) {  // Assuming Employee ID is column 1
                    row[21] = newStatus;  // Update Pay Status column
                    updated = true;
                }
                payrollData.add(row);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading payroll file: " + e.getMessage());
            return;
        }

        if (updated) {
            // Write the modified data back to CSV
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String[] row : payrollData) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(null, successMessage);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving payroll file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Employee ID not found in payroll.");
        }
    }


    // To print only "Approved" payrolls
    public static void printApprovedPayroll(JTable tbl_Payroll, String dateFrom, String dateTo) {
        try {
            TableModel model = tbl_Payroll.getModel();
            List<String[]> approvedRows = new ArrayList<>();

            // Filter rows by Approved payStatus and date range
            for (int i = 0; i < model.getRowCount(); i++) {
                String payStatus = model.getValueAt(i, 21).toString(); // Column 21 = Pay Status
                String payDateFrom = model.getValueAt(i, 4).toString(); // Column 4 = Pay Date From
                String payDateTo = model.getValueAt(i, 5).toString(); // Column 5 = Pay Date To

                // Now it correctly checks the date range using dateFrom and dateTo
                if (payStatus.equalsIgnoreCase("Approved") && isWithinDateRange(payDateFrom, payDateTo, dateFrom, dateTo)) {
                    String[] rowData = {
                            model.getValueAt(i, 0).toString(), // Transaction ID
                            model.getValueAt(i, 1).toString(), // Emp ID
                            model.getValueAt(i, 2).toString(), // First Name
                            model.getValueAt(i, 3).toString(), // Last Name
                            model.getValueAt(i, 4).toString(), // Pay Date From
                            model.getValueAt(i, 5).toString(), // Pay Date To
                            model.getValueAt(i, 20).toString(), // Net Pay
                            model.getValueAt(i, 21).toString()  // Pay Status
                    };
                    approvedRows.add(rowData);
                }
            }

            if (approvedRows.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No approved payroll data found within the selected date range.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Print the filtered data
            printTable(approvedRows);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error while printing: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Data Validation for Date Range in Payroll
    private static boolean isWithinDateRange(String payDateFrom, String payDateTo, String dateFrom, String dateTo) {
        try {
            // Ensure format matches CSV format exactly
            SimpleDateFormat csvDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

            // Parse CSV dates
            Date payStartDate = csvDateFormat.parse(payDateFrom);
            Date payEndDate = csvDateFormat.parse(payDateTo);

            // Parse selected dates from calendar
            Date selectedStartDate = csvDateFormat.parse(dateFrom);
            Date selectedEndDate = csvDateFormat.parse(dateTo);

            // Check if payroll period falls within selected range
            return (payStartDate.compareTo(selectedStartDate) >= 0 && payEndDate.compareTo(selectedEndDate) <= 0);

        } catch (ParseException e) {
            System.err.println("Error parsing dates: " + e.getMessage());
            return false;
        }
    }

    // Payroll Print Report
    private static void printTable(List<String[]> data) {
        try {
            StringBuilder printableData = new StringBuilder();
            printableData.append("Transaction ID | Emp ID | First Name | Last Name | Pay Date From | Pay Date To | Net Pay | Pay Status\n");
            printableData.append("-------------------------------------------------------------------------------------------\n");

            for (String[] row : data) {
                printableData.append(String.join(" | ", row)).append("\n");
            }

            JTextArea textArea = new JTextArea(printableData.toString());
            textArea.setColumns(80);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            PrinterJob job = PrinterJob.getPrinterJob();
            PageFormat format = job.defaultPage();
            format.setOrientation(PageFormat.LANDSCAPE);
            job.setPrintable(textArea.getPrintable(null, null), format);

            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(null, "Payroll report printed successfully!", "Print Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Print cancelled!", "Print Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, "Printing failed: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // For Logout
    public static void handleLogout(JFrame currentFrame) {
        int confirmLogout = JOptionPane.showConfirmDialog(currentFrame,
                "Do you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmLogout == JOptionPane.YES_OPTION) {
            // Clear the user session
            EmpUserSession.getInstance().clearSession();

            // Open the login form
            new frm_Login().setVisible(true);

            // Close the current frame
            currentFrame.dispose();
        }
    }


    // Validate First Name & Last Name in Employee Records (Auto-Capitalize, Max 50 characters)
    public static String capitalizeWords(String input, int maxLength) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split("\\s+");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        String result = capitalizedString.toString().trim();
        return (result.length() > maxLength) ? result.substring(0, maxLength) : result;
    }

    // Validate Address (Auto-Capitalize, Max 100 characters)
    public static String validateAddress(String address) {
        return capitalizeWords(address, 100);
    }


    // Validate Phone Number (Only numeric and hyphen, Max 11 characters)
    public static boolean isValidPhone(String phoneNo) {
        return phoneNo.matches("^(\\d+-?)+\\d$") && phoneNo.length() <= 11;
    }

    // Validate Birthdate (Format: DD-MMM-YY)
    public static String formatBirthdate(String birthdate) {
        String birthdatePattern = "^\\d{2}-(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)-\\d{2}$";
        Pattern pattern = Pattern.compile(birthdatePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(birthdate);

        if (!matcher.matches()) {
            return null; // Invalid format, return null instead of boolean
        }

        // Convert month to "Mmm" format (First letter uppercase, rest lowercase)
        String[] parts = birthdate.split("-");
        String formattedMonth = parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase();

        return parts[0] + "-" + formattedMonth + "-" + parts[2]; // Return formatted birthdate
    }


    // Helper method to safely parse LocalDate values in Attendance
    private static LocalDate parseDate(String value) {
        if (value.trim().isEmpty()) return LocalDate.of(2000, 1, 1); // Default fallback date

        List<String> patterns = Arrays.asList("M/d/yyyy", "MM/dd/yyyy", "d/M/yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "dd-MMM-yy"); //JOYCE--- added "dd-MMM-yy"
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
            } catch (Exception ignored) {
            }
        }

        System.err.println("Invalid date format: " + value);
        return LocalDate.of(2000, 1, 1); // Return default if parsing fails
    }

    // Helper method to safely parse LocalTime values
    private static LocalTime parseTime(String value) {
        if (value.trim().isEmpty()) return LocalTime.of(9, 0); // Default fallback

        List<String> patterns = Arrays.asList("H:mm:ss", "HH:mm");
        for (String pattern : patterns) {
            try {
                return LocalTime.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
            } catch (Exception ignored) {
            }
        }

        System.err.println("Invalid time format: " + value);
        return LocalTime.of(9, 0); // Return default if parsing fails
    }

    // Helper method to safely parse double values
    private static double parseDouble(String value) {
        try {
            return new BigDecimal(value.trim()).doubleValue();
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + value);
            return 0.0;
        }
    }
}



