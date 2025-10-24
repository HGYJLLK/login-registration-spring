-- 创建数据库
CREATE DATABASE IF NOT EXISTS queqian CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE queqian;

-- 创建用户表
CREATE TABLE IF NOT EXISTS tb_user (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
  name VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  gender VARCHAR(10) DEFAULT NULL COMMENT '性别',
  phonenumber VARCHAR(50) DEFAULT NULL COMMENT '手机号码',
  identitycode VARCHAR(50) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (id),
  INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

UPDATE tb_user SET password = '123456' WHERE username = '用户名';