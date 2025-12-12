-- 歌单管理数据库表创建脚本
-- 执行方式: mysql -u root -p2333 queqian < database_songlist_setup.sql

USE queqian;

-- 创建歌单表
CREATE TABLE IF NOT EXISTS tb_song_list (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL COMMENT '歌单标题',
  pic_url VARCHAR(500) DEFAULT NULL COMMENT '歌单封面URL',
  introduction TEXT DEFAULT NULL COMMENT '歌单简介',
  style VARCHAR(50) DEFAULT NULL COMMENT '歌单风格',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX idx_title (title),
  INDEX idx_style (style)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌单表';

-- 创建歌单-歌曲关联表
CREATE TABLE IF NOT EXISTS tb_list_song (
  id INT AUTO_INCREMENT PRIMARY KEY,
  song_list_id INT NOT NULL COMMENT '歌单ID',
  song_id INT NOT NULL COMMENT '歌曲ID',
  UNIQUE KEY uk_songlist_song (song_list_id, song_id),
  INDEX idx_song_list_id (song_list_id),
  INDEX idx_song_id (song_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌单-歌曲关联表';

-- 显示表结构
DESCRIBE tb_song_list;
DESCRIBE tb_list_song;

-- 插入示例数据(可选)
INSERT INTO tb_song_list (title, pic_url, introduction, style) VALUES
('热门华语', '/api/file/song-pics/default1.jpg', '最热门的华语流行歌曲精选', '流行'),
('经典老歌', '/api/file/song-pics/default2.jpg', '那些年我们一起听过的经典', '怀旧'),
('摇滚精选', '/api/file/song-pics/default3.jpg', '燃烧你的激情', '摇滚');

SELECT '歌单表创建成功!' AS message;
