-- 为tb_user表添加email字段
USE queqian;

-- 添加email列（如果已存在会报错，可忽略）
ALTER TABLE tb_user
ADD COLUMN email VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱';

-- 查看表结构
DESCRIBE tb_user;
