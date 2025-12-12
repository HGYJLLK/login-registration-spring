-- 收藏功能数据库表创建脚本
-- 执行方式: mysql -u root -p2333 queqian < database_favorite_setup.sql

USE queqian;

-- 创建收藏表
CREATE TABLE IF NOT EXISTS tb_favorite (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL COMMENT '用户ID',
  song_id INT NOT NULL COMMENT '歌曲ID',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY uk_user_song (user_id, song_id),
  INDEX idx_user_id (user_id),
  INDEX idx_song_id (song_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 显示表结构
DESCRIBE tb_favorite;

SELECT '收藏表创建成功!' AS message;
