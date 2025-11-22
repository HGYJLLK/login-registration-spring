-- Database Schema for Course Management Module
-- This script creates the necessary tables for the course management system

-- Create course table
CREATE TABLE IF NOT EXISTS course (
    course_id VARCHAR(20) PRIMARY KEY COMMENT '课程编号',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    term VARCHAR(20) NOT NULL COMMENT '课程学期',
    description TEXT COMMENT '课程简介',
    credit INT NOT NULL COMMENT '课程学分'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';

-- Create student_course table (links students to courses)
CREATE TABLE IF NOT EXISTS student_course (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    student_id VARCHAR(20) NOT NULL COMMENT '学号',
    course_id VARCHAR(20) NOT NULL COMMENT '课程编号',
    FOREIGN KEY (student_id) REFERENCES students(student_no) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE,
    UNIQUE KEY uk_student_course (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课关联表';

-- Insert sample course data (optional)
INSERT INTO course (course_id, course_name, term, description, credit) VALUES
('CS101', '计算机科学导论', '2024春季', '介绍计算机科学的基础知识和编程入门', 4),
('MATH201', '高等数学', '2024春季', '微积分和线性代数基础', 5),
('ENG301', '大学英语', '2024春季', '提升英语听说读写能力', 3),
('PHY401', '大学物理', '2024秋季', '物理学基础理论和实验', 4),
('DB501', '数据库原理', '2024秋季', '数据库设计与SQL编程', 4);

-- Note: The student_course table will be populated when users select courses through the web interface
