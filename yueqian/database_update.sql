-- 数据库更新脚本 - 添加用户头像字段
-- 执行方式: mysql -u root -p2333 queqian < database_update.sql

USE queqian;

-- 为用户表添加头像URL字段
ALTER TABLE tb_user
ADD COLUMN avatar_url VARCHAR(500) DEFAULT NULL COMMENT '用户头像URL';

-- 显示更新后的表结构
DESCRIBE tb_user;
