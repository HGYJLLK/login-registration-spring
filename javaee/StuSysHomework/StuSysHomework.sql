CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

CREATE TABLE students (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          student_no VARCHAR(20) NOT NULL UNIQUE,
                          name VARCHAR(50) NOT NULL,
                          gender ENUM('男', '女') NOT NULL,
                          age INT NOT NULL,
                          birth_date DATE NOT NULL,
                          phone VARCHAR(20) NOT NULL,
                          class_name VARCHAR(50) NOT NULL,
                          department VARCHAR(50) NOT NULL,
                          status ENUM('在读', '休学', '毕业') NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入一些测试数据
INSERT INTO students (student_no, name, gender, age, birth_date, phone, class_name, department, status) VALUES
('2023001001', '张三', '男', 20, '2003-05-15', '13800138001', '计算机科学与技术1班', '计算机学院', '在读'),
('2023001002', '李四', '女', 19, '2004-08-22', '13800138002', '软件工程2班', '软件学院', '在读'),
('2023001003', '王五', '男', 21, '2002-11-30', '13800138003', '网络工程1班', '计算机学院', '在读'),
('2023001004', '赵六', '女', 20, '2003-03-10', '13800138004', '数据科学1班', '数学学院', '休学');