-- 音乐播放器完整数据库初始化脚本
-- 数据库名: queqian
-- 执行方式: mysql -u root -p2333 < init_database.sql

-- 创建数据库
CREATE DATABASE IF NOT EXISTS queqian CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE queqian;

-- ==================== 用户相关表 ====================

-- 创建用户表
CREATE TABLE IF NOT EXISTS tb_user (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
  name VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  gender VARCHAR(10) DEFAULT NULL COMMENT '性别',
  phonenumber VARCHAR(50) DEFAULT NULL COMMENT '手机号码',
  identitycode VARCHAR(50) DEFAULT NULL COMMENT '身份证号',
  avatar_url VARCHAR(500) DEFAULT NULL COMMENT '用户头像URL',
  email VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
  PRIMARY KEY (id),
  INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ==================== 音乐相关表 ====================

-- 创建歌手表
CREATE TABLE IF NOT EXISTS tb_singer (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL COMMENT '歌手名称',
  pic_url VARCHAR(500) DEFAULT NULL COMMENT '歌手图片路径',
  introduction TEXT COMMENT '歌手简介',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌手表';

-- 创建歌曲表
CREATE TABLE IF NOT EXISTS tb_song (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL COMMENT '歌曲名称',
  singer_id INT NOT NULL COMMENT '歌手ID',
  singer_name VARCHAR(100) NOT NULL COMMENT '歌手名称',
  song_url VARCHAR(500) NOT NULL COMMENT '歌曲文件路径',
  pic_url VARCHAR(500) DEFAULT NULL COMMENT '歌曲封面图片路径',
  lyric TEXT COMMENT '歌词',
  duration INT DEFAULT 0 COMMENT '歌曲时长(秒)',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  INDEX idx_singer_id (singer_id),
  INDEX idx_name (name),
  FOREIGN KEY (singer_id) REFERENCES tb_singer(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌曲表';

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

-- ==================== 示例数据 ====================

-- 插入示例歌手数据
INSERT INTO tb_singer (name, pic_url) VALUES
('周杰伦', '/music-server/img/singerPic/zhoujielun.jpg'),
('林俊杰', '/music-server/img/singerPic/linjunjie.jpg'),
('张杰', '/music-server/img/singerPic/zhangjie.jpg'),
('陈奕迅', '/music-server/img/singerPic/chenyixun.jpg'),
('李荣浩', '/music-server/img/singerPic/lironghao.jpg'),
('毛不易', '/music-server/img/singerPic/maobuyi.jpg'),
('王力宏', '/music-server/img/singerPic/wanglihong.jpg'),
('田馥甄', '/music-server/img/singerPic/tianfuzhen.jpg'),
('G.E.M.邓紫棋', '/music-server/img/singerPic/dengziqi.jpg'),
('张国荣', '/music-server/img/singerPic/zhangguorong.jpg')
ON DUPLICATE KEY UPDATE name=name;

-- 插入示例歌曲数据
INSERT INTO tb_song (name, singer_id, singer_name, song_url, pic_url) VALUES
('晴天', 1, '周杰伦', '/music-server/song/周杰伦-晴天.mp3', '/music-server/img/songPic/qingtian.jpg'),
('七里香', 1, '周杰伦', '/music-server/song/周杰伦-七里香.mp3', '/music-server/img/songPic/qilixiang.jpg'),
('稻香', 1, '周杰伦', '/music-server/song/周杰伦-稻香.mp3', '/music-server/img/songPic/daoxiang.jpg'),
('烟花易冷', 1, '周杰伦', '/music-server/song/周杰伦-烟花易冷.mp3', '/music-server/img/songPic/yanhuayileng.jpg'),
('告白气球', 1, '周杰伦', '/music-server/song/周杰伦-告白气球.mp3', '/music-server/img/songPic/gaobaiqiqui.jpg'),
('江南', 2, '林俊杰', '/music-server/song/林俊杰-江南.mp3', '/music-server/img/songPic/109951163520335473.jpg'),
('醉赤壁', 2, '林俊杰', '/music-server/song/林俊杰-醉赤壁.mp3', '/music-server/img/songPic/109951163187393370.jpg'),
('关键词', 2, '林俊杰', '/music-server/song/林俊杰-关键词.mp3', '/music-server/img/songPic/guanjianci.jpg'),
('逆战', 3, '张杰', '/music-server/song/张杰-逆战.mp3', '/music-server/img/songPic/nizhan.jpg'),
('天下', 3, '张杰', '/music-server/song/张杰-天下.mp3', '/music-server/img/songPic/109951163272878671.jpg'),
('红玫瑰', 4, '陈奕迅', '/music-server/song/陈奕迅-红玫瑰.mp3', '/music-server/img/songPic/hongmeigui.jpg'),
('不要说话', 4, '陈奕迅', '/music-server/song/陈奕迅-不要说话.mp3', '/music-server/img/songPic/buyaoshuohua.jpg'),
('年少有为', 5, '李荣浩', '/music-server/song/李荣浩-年少有为.mp3', '/music-server/img/songPic/lironghao.jpg'),
('李白', 5, '李荣浩', '/music-server/song/李荣浩-李白.mp3', '/music-server/img/songPic/lironghao.jpg'),
('无问', 6, '毛不易', '/music-server/song/毛不易-无问.mp3', '/music-server/img/songPic/109951163681898984.jpg'),
('唯一', 7, '王力宏', '/music-server/song/王力宏-唯一.mp3', '/music-server/img/songPic/109951163187405670.jpg'),
('小幸运', 8, '田馥甄', '/music-server/song/田馥甄-小幸运.mp3', '/music-server/img/songPic/109951163099854364.jpg'),
('泡沫', 9, 'G.E.M.邓紫棋', '/music-server/song/G.E.M.邓紫棋-泡沫.mp3', '/music-server/img/songPic/paomo.jpg')
ON DUPLICATE KEY UPDATE name=name;

-- 插入示例歌单数据
INSERT INTO tb_song_list (title, pic_url, introduction, style) VALUES
('热门华语', '/api/file/song-pics/default1.jpg', '最热门的华语流行歌曲精选', '流行'),
('经典老歌', '/api/file/song-pics/default2.jpg', '那些年我们一起听过的经典', '怀旧'),
('摇滚精选', '/api/file/song-pics/default3.jpg', '燃烧你的激情', '摇滚')
ON DUPLICATE KEY UPDATE title=title;

SELECT '数据库初始化完成！' AS message;
SELECT '已创建的表：' AS info;
SHOW TABLES;
