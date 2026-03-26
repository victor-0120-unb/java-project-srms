## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Student Records Management System (SRMS)

## Chuka University - Academic Course Work

---

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [How to Run](#how-to-run)
- [Persistence Modes](#persistence-modes)
- [Academic Rules Implemented](#academic-rules-implemented)
- [Reports Available](#reports-available)
- [Screenshots](#screenshots)
- [Troubleshooting](#troubleshooting)
- [Author](#author)
- [Acknowledgments](#acknowledgments)

---

## Project Overview

The **Student Records Management System (SRMS)** is a comprehensive Java desktop application developed for Chuka University to manage student academic records. The system handles student registration, course management, tutor allocation, enrollment tracking, examination results, academic progression, and graduation clearance.

This project demonstrates the application of Object-Oriented Programming (OOP) principles, GUI development with Java Swing, file I/O operations, and JDBC database connectivity.

### Key Objectives
- ✅ Apply OOP principles to model real-world university entities
- ✅ Develop an intuitive GUI using Java Swing
- ✅ Implement both file-based and database-based persistence
- ✅ Handle academic progression logic (minimum course load, promotion, graduation)
- ✅ Generate various academic and administrative reports

---

# Features

# Student Management
- Add, update, delete, and search student records
- View all students in a tabular format
- Promote students to next class/year (minimum 10 courses required)
- Clear students for graduation (120 credits required)
- Academic status tracking (Active, On Probation, Cleared for Graduation, Graduated)

# Course Management
- Add, update, delete, and search courses
- View all courses with details (ID, name, credits, department)
- Filter courses by department

# Tutor Management
- Add, update, delete, and search tutors
- View all tutors with their department information

# Enrollment Management
- Register students for courses
- Enforce minimum 10 courses per semester rule
- Prevent duplicate enrollments
- Check course load for students

# Examination Results
- Record exam scores and automatically calculate grades
- Grade calculation: A (70+), B (60-69), C (50-59), D (40-49), F (<40)
- View results with student and course information

# Course Allocation
- Assign tutors to courses for specific semesters
- Track tutor teaching load per semester

# Reports
- Student Academic Transcript
- Course Enrollment Report
- Tutor Course Load Report
- Graduation Eligibility Report
- Students on Probation Report

# Persistence
- **File-based persistence**: CSV files in `data/` directory
- **Database persistence**: MySQL database with JDBC
- **Configurable**: Switch between modes via configuration file

---

## Technology Stack

| Technology        | Version        | Purpose 
|------------       |---------       |---------
| **Java**          | JDK 11+        | Core programming language 
| **Java Swing**    | -              | GUI framework 
| **MySQL**         | 8.0+           | Database for persistent storage 
| **JDBC**          | -              | Database connectivity 
| **Java NIO.2**    | -              | File I/O operations 
| **Maven**         | -              | Dependency management (optional) 

---

## Project Structure

SRMS/
├── src/com/chuka/srms/
│ ├── model/ # Entity classes
│ │ ├── Student.java
│ │ ├── Course.java
│ │ ├── Tutor.java
│ │ ├── Enrollment.javaSRMS/
├── src/com/chuka/srms/
│ ├── model/ # Entity classes
│ │ ├── Student.java
│ │ ├── Course.java
│ │ ├── Tutor.java
│ │ ├── Enrollment.java
│ │ ├── ExaminationResult.java
│ │ └── CourseAllocation.java
│ │
│ ├── view/ # GUI components
│ │ ├── MainFrame.java # Main application window
│ │ ├── StudentPanel.java # Student management
│ │ ├── CoursePanel.java # Course management
│ │ ├── TutorPanel.java # Tutor management
│ │ ├── EnrollmentPanel.java # Enrollment management
│ │ ├── ExaminationResultPanel.java
│ │ ├── CourseAllocationPanel.java
│ │ └── ReportPanel.java # Report generation
│ │
│ ├── service/ # Business logic layer
│ │ ├── StudentService.java
│ │ ├── CourseService.java
│ │ ├── TutorService.java
│ │ ├── EnrollmentService.java
│ │ ├── ExaminationResultService.java
│ │ ├── CourseAllocationService.java
│ │ ├── AcademicProgressionService.java
│ │ └── ReportService.java
│ │
│ ├── dao/ # Data Access Layer
│ │ ├── StudentDAO.java # Interface
│ │ ├── file/ # File implementations
│ │ │ ├── StudentFileDAO.java
│ │ │ └── ...
│ │ └── db/ # Database implementations
│ │ ├── StudentDatabaseDAO.java
│ │ └── ...
│ │
│ ├── util/ # Utility classes
│ │ ├── FileUtil.java # File I/O utilities
│ │ └── DatabaseConnection.java # JDBC connection manager
│ │
│ └── config/ # Configuration
│ └── AppConfig.java # Configuration management
│
├── lib/ # External libraries
│ └── mysql-connector-java-8.0.28.jar
│
├── data/ # File persistence data
│ ├── students.txt
│ ├── courses.txt
│ ├── tutors.txt
│ ├── enrollments.txt
│ ├── examination_results.txt
│ └── course_allocations.txt
│
├── config/ # Configuration files
│ └── application.properties
│
├── database/ # SQL scripts
│ └── schema.sql
│
└── README.md # This file
│ │ ├── ExaminationResult.java
│ │ └── CourseAllocation.java
│ │
│ ├── view/ # GUI components
│ │ ├── MainFrame.java # Main application window
│ │ ├── StudentPanel.java # Student management
│ │ ├── CoursePanel.java # Course management
│ │ ├── TutorPanel.java # Tutor management
│ │ ├── EnrollmentPanel.java # Enrollment management
│ │ ├── ExaminationResultPanel.java
│ │ ├── CourseAllocationPanel.java
│ │ └── ReportPanel.java # Report generation
│ │
│ ├── service/ # Business logic layer
│ │ ├── StudentService.java
│ │ ├── CourseService.java
│ │ ├── TutorService.java
│ │ ├── EnrollmentService.java
│ │ ├── ExaminationResultService.java
│ │ ├── CourseAllocationService.java
│ │ ├── AcademicProgressionService.java
│ │ └── ReportService.java
│ │
│ ├── dao/ # Data Access Layer
│ │ ├── StudentDAO.java # Interface
│ │ ├── file/ # File implementations
│ │ │ ├── StudentFileDAO.java
│ │ │ └── ...
│ │ └── db/ # Database implementations
│ │ ├── StudentDatabaseDAO.java
│ │ └── ...
│ │
│ ├── util/ # Utility classes
│ │ ├── FileUtil.java # File I/O utilities
│ │ └── DatabaseConnection.java # JDBC connection manager
│ │
│ └── config/ # Configuration
│ └── AppConfig.java # Configuration management
│
├── lib/ # External libraries
│ └── mysql-connector-java-8.0.28.jar
│
├── data/ # File persistence data
│ ├── students.txt
│ ├── courses.txt
│ ├── tutors.txt
│ ├── enrollments.txt
│ ├── examination_results.txt
│ └── course_allocations.txt
│
├── config/ # Configuration files
│ └── application.properties
│
├── database/ # SQL scripts
│ └── schema.sql
│
└── README.md # This file


---

## Installation & Setup

# Prerequisites
1. **Java JDK 11 or higher** installed
2. **MySQL Server 8.0+** installed and running
3. **Git** (optional, for cloning)

# Step 1: Clone or Download the Project

       git clone https://github.com/yourusername/srms.git
       cd srms

# Step 2: Add MySQL Connector
Download MySQL Connector/J from MySQL and place the JAR file in the lib/ folder:
        cp ~/Downloads/mysql-connector-java-8.0.28.jar lib/

# Step 3: Set Up Database
  # 1.Start MySQL server:
      sudo systemctl start mysql   # Linux
      OR
      net start MySQL80             # Windows

  # 2.Create the database and tables:
       mysql -u root -p < database/schema.sql

  # 3.Create a dedicated user for the application (optional but recommended):
    sql
      CREATE USER 'srms_user'@'localhost' IDENTIFIED BY 'Srms.149';
      GRANT ALL PRIVILEGES ON chuka_university_db.* TO 'srms_user'@'localhost';
      FLUSH PRIVILEGES;

# Step 4: Configure Application
Edit config/application.properties:

## How to Run

 # cmd
 "bin:lib/*" com.chuka.srms.view.MainFrame

 # bash
 cd ~/java-project/srms/srms && mkdir -p bin && find src -name "*.java" > sources.txt && javac -cp "lib/*:src" -d bin @sources.txt && java -cp "bin:lib/*" com.chuka.srms.view.MainFrame


### Sreenshots
