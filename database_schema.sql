-- Student Database Management System Schema
-- Oracle Database Setup Script

-- Create Students table
CREATE TABLE Students (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    course VARCHAR2(100) NOT NULL,
    enrollment_date DATE DEFAULT SYSDATE,
    gpa NUMBER(3,2) DEFAULT 0.00,
    status VARCHAR2(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Inactive', 'Graduated'))
);

-- Create sequence for auto-incrementing IDs
CREATE SEQUENCE student_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Create Courses table for better normalization
CREATE TABLE Courses (
    course_id NUMBER PRIMARY KEY,
    course_name VARCHAR2(100) UNIQUE NOT NULL,
    department VARCHAR2(100),
    credits NUMBER(2) DEFAULT 3
);

-- Create sequence for courses
CREATE SEQUENCE course_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Add foreign key relationship (optional enhancement)
-- ALTER TABLE Students ADD CONSTRAINT fk_student_course
-- FOREIGN KEY (course) REFERENCES Courses(course_name);

-- Insert sample courses
INSERT INTO Courses VALUES (course_seq.NEXTVAL, 'Computer Science', 'Engineering', 4);
INSERT INTO Courses VALUES (course_seq.NEXTVAL, 'Information Technology', 'Engineering', 4);
INSERT INTO Courses VALUES (course_seq.NEXTVAL, 'Business Administration', 'Business', 3);
INSERT INTO Courses VALUES (course_seq.NEXTVAL, 'Mathematics', 'Science', 3);

-- Insert sample students
INSERT INTO Students (id, name, email, course, gpa, status)
VALUES (student_seq.NEXTVAL, 'John Doe', 'john.doe@email.com', 'Computer Science', 3.8, 'Active');

INSERT INTO Students (id, name, email, course, gpa, status)
VALUES (student_seq.NEXTVAL, 'Jane Smith', 'jane.smith@email.com', 'Information Technology', 3.9, 'Active');

INSERT INTO Students (id, name, email, course, gpa, status)
VALUES (student_seq.NEXTVAL, 'Bob Johnson', 'bob.johnson@email.com', 'Business Administration', 3.5, 'Active');

-- Create indexes for better performance
CREATE INDEX idx_student_email ON Students(email);
CREATE INDEX idx_student_course ON Students(course);
CREATE INDEX idx_student_status ON Students(status);

COMMIT;