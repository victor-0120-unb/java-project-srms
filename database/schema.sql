-- Create the database
CREATE DATABASE IF NOT EXISTS chuka_university_db;
USE chuka_university_db;
-- Create students table
CREATE TABLE IF NOT EXISTS students (
student_id VARCHAR(50) PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
date_of_birth DATE,
major VARCHAR(50),
current_semester INT DEFAULT 1,
current_year INT DEFAULT 1,
academic_status VARCHAR(50) DEFAULT 'Active'
);
-- Create courses table
CREATE TABLE IF NOT EXISTS courses (
course_id VARCHAR(50) PRIMARY KEY,
course_name VARCHAR(100) NOT NULL,
credits INT NOT NULL,
department VARCHAR(50)
);
-- Create tutors table
CREATE TABLE IF NOT EXISTS tutors (
tutor_id VARCHAR(50) PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
department VARCHAR(50)
);
-- Create enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
enrollment_id VARCHAR(50) PRIMARY KEY,
student_id VARCHAR(50) NOT NULL,
course_id VARCHAR(50) NOT NULL,
semester INT NOT NULL,
year INT NOT NULL,
enrollment_date DATE,
FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE
CASCADE,
FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);-- Create examination_results table
CREATE TABLE IF NOT EXISTS examination_results (
result_id VARCHAR(50) PRIMARY KEY,
enrollment_id VARCHAR(50) NOT NULL,
score INT NOT NULL,
grade VARCHAR(5) NOT NULL,
semester INT NOT NULL,
year INT NOT NULL,
FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON
DELETE CASCADE
);
-- Create course_allocations table
CREATE TABLE IF NOT EXISTS course_allocations (
allocation_id VARCHAR(50) PRIMARY KEY,
course_id VARCHAR(50) NOT NULL,
tutor_id VARCHAR(50) NOT NULL,
semester INT NOT NULL,
year INT NOT NULL,
FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
FOREIGN KEY (tutor_id) REFERENCES tutors(tutor_id) ON DELETE CASCADE
);
-- Optional: Add some sample data
INSERT INTO students (student_id, first_name, last_name, date_of_birth,
major, current_semester, current_year, academic_status) VALUES
('CHUKA/COMP/2023/001', 'Alice', 'Smith', '2002-09-01', 'Computer Science',
1, 1, 'Active'),
('CHUKA/COMP/2023/002', 'Bob', 'Johnson', '2001-03-15', 'Computer Science',
1, 1, 'Active');
INSERT INTO courses (course_id, course_name, credits, department) VALUES
('CSCI223', 'Introduction to Java Programming', 3, 'Computer Science'),
('MATH210', 'Calculus I', 4, 'Mathematics'),
('PHY101', 'Physics for Engineers', 3, 'Physics');
INSERT INTO tutors (tutor_id, first_name, last_name, department) VALUES
('TUT001', 'Dr. Jane', 'Doe', 'Computer Science'),
('TUT002', 'Prof. John', 'Smith', 'Mathematics');
INSERT INTO enrollments (enrollment_id, student_id, course_id, semester,
year, enrollment_date) VALUES
('ENR2024S1C001', 'CHUKA/COMP/2023/001', 'CSCI223', 1, 1, '2024-01-10'),
('ENR2024S1C002', 'CHUKA/COMP/2023/001', 'MATH210', 1, 1, '2024-01-10');
INSERT INTO examination_results (result_id, enrollment_id, score, grade, semester, year) VALUES
('RES2024S1E001', 'ENR2024S1C001', 85, 'A', 1, 1),
('RES2024S1E002', 'ENR2024S1C002', 70, 'B', 1, 1);
INSERT INTO course_allocations (allocation_id, course_id, tutor_id,
semester, year) VALUES
('ALLOC2024S1C001', 'CSCI223', 'TUT001', 1, 1),
('ALLOC2024S1C002', 'MATH210', 'TUT002', 1, 1);