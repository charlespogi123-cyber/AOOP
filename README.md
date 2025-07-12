# AOOP---Group-5---S2101
# MotorPH Payroll System

A comprehensive Java-based payroll management system built using Object-Oriented Programming principles and Swing GUI framework for MotorPH company.

## Project Overview

**MotorPH Payroll System** is a desktop application designed to streamline employee management, attendance tracking, leave management, and payroll processing. The system provides role-based access control with different interfaces for employees and administrators.

### Authors
- **Juan Carlos Miguel Basa**
- **Charles Mikael Medel** 
- **Mikko Jerome Bautista**
- **Frances Balgos**
- **Clark Anton Juan Diaz**

## Key Features

### Authentication & Security
- Secure login system with password hashing
- Role-based access control (Employee/Administrator)
- Session management for logged-in users

### Employee Management
- Complete employee profile management
- Employee records viewing and editing
- Address and contact information management
- Employment status tracking

### Attendance & Leave Management
- Real-time attendance tracking with time in/out
- Leave request submission and approval workflow
- Vacation Leave (VL) and Sick Leave (SL) balance tracking
- Attendance status monitoring (Present, Absent, Late, etc.)
- Overtime hours calculation

### Payroll Processing
- Automated payroll calculation based on attendance
- Government contributions computation (SSS, PhilHealth, Pag-IBIG)
- Tax calculations and deductions
- Allowances management (Rice, Phone, Clothing)
- Payroll report generation

### Statutory Contributions
- **SSS (Social Security System)** rate calculations
- **PhilHealth** contribution computation
- **Pag-IBIG** fund calculations
- **Tax (BIR)** withholding tax computation

## System Architecture

### Core Classes
- **`OOP_MotorPH`** - Main application entry point
- **`EmpDetails`** - Base employee information class
- **`EmpSalaryDetails`** - Employee salary and benefits management
- **`EmpAttLeave`** - Attendance and leave tracking
- **`EmpUserSession`** - Session management (Singleton pattern)
- **`PayrollProcessor`** - Interface for payroll calculations

### Database Layer
- **`DatabaseHandler`** - Main database operations
- **`DatabaseConfig`** - Database connection configuration
- **`DataMigration`** - CSV to database migration utilities
- **`CSVHandler`** - CSV file operations

### User Interface Forms
- **`frm_Login`** - Authentication interface
- **`frm_EmpProfile`** - Employee profile management
- **`frm_EmployeesRecords`** - Employee records administration
- **`frm_EmployeesAttLeave`** - Attendance & leave management
- **`frm_EmployeesSalary`** - Salary and statutory management
- **`frm_EmployeesPayrollProcess`** - Payroll processing interface

## Technology Stack

- **Language**: Java 23
- **GUI Framework**: Java Swing
- **Database**: MySQL 8.0
- **Build Tool**: Apache Ant
- **IDE**: NetBeans IDE
- **External Libraries**:
  - MySQL Connector/J 8.0.33
  - JCalendar 1.4 (Date picker component)

## Project Structure

```
AOOP - MotorPH from OOP/
├── src/oop_motorph/           # Source code
│   ├── *.java                 # Java class files
│   ├── *.form                 # NetBeans form files
│   ├── img/                   # Application images
│   └── resources/             # CSV data files
├── lib/                       # External JAR libraries
│   ├── mysql-connector-j-8.0.33.jar
│   └── jcalendar-1.4.jar
├── build/classes/             # Compiled class files
├── nbproject/                 # NetBeans project files
├── run.bat                    # Windows batch execution script
└── README.md                  # Project documentation
```

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 23 or higher
- MySQL Server 8.0 or higher
- Git (for cloning the repository)

### 1. Clone the Repository
```bash
git clone https://github.com/rapidotech/AOOP.git
cd "AOOP/AOOP - MotorPH from OOP"
```

### 2. Database Setup
1. Install and start MySQL Server
2. Create a new database for the application
3. Configure database connection in `DatabaseConfig.java`
4. Run `DataMigration.java` to populate initial data from CSV files

### 3. Library Dependencies
Ensure the following JAR files are in the `lib/` directory:
- `mysql-connector-j-8.0.33.jar` - MySQL database connectivity
- `jcalendar-1.4.jar` - Date picker components

### 4. Compilation
Using the provided build script:
```bash
# Windows
run.bat

# Or compile manually
javac -d build/classes -cp "lib/*" src/oop_motorph/*.java
```

## Running the Application

### Windows
Double-click `run.bat` or execute in command prompt:
```batch
run.bat
```

### Manual Execution
```bash
java -cp "build/classes;lib/jcalendar-1.4.jar;lib/mysql-connector-j-8.0.33.jar" oop_motorph.OOP_MotorPH
```

### Using VS Code Task
```bash
# Build the project
Ctrl+Shift+P -> "Tasks: Run Task" -> "javac build AOOP"

# Run the application
java -cp "build/classes;lib/*" oop_motorph.OOP_MotorPH
```

## Default Login Credentials

The system includes pre-configured user accounts for testing:
- For Employee login:
  username: 10003
  password: 1234
  For HR Admin login:
  username: 10002
  password: 1234
- Different roles available: Employee, HR Admin

## Usage Guide

### For Employees
1. **Login** with your employee credentials
2. **View Profile** - Check personal information and employment details
3. **Attendance & Leave** - Submit leave requests and view attendance records
4. **My Records** - Access personal attendance and leave history
5. **Salary Information** - View salary details and statutory contributions

### For HR
1. **Employee Management** - Add, edit, and manage employee records
2. **Attendance Monitoring** - View and approve attendance/leave requests
3. **Payroll Processing** - Calculate and process employee payrolls
4. **Reports Generation** - Generate various payroll and attendance reports

## Development Features

- **Object-Oriented Design** with proper inheritance and polymorphism
- **Singleton Pattern** for user session management
- **Interface Implementation** for payroll processing standardization
- **Model-View separation** with dedicated form classes
- **Data validation** and error handling
- **Modern UI design** with hover effects and styling

## Testing

The application includes comprehensive error handling and validation:
- Input validation for all form fields
- Database connection error handling
- Date and time format validation
- Role-based access verification


---

**MotorPH Payroll System** - Streamlining payroll management with modern Java technology.
