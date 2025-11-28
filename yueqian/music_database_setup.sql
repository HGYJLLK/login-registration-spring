-- 音乐播放器数据库表结构

USE queqian;

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
CREATE TABLE IF NOT EXISTS tb_playlist (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL COMMENT '歌单名称',
  description TEXT COMMENT '歌单描述',
  pic_url VARCHAR(500) DEFAULT NULL COMMENT '歌单封面图片路径',
  user_id INT DEFAULT NULL COMMENT '创建用户ID',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  INDEX idx_user_id (user_id),
  FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌单表';

-- 创建歌单歌曲关联表
CREATE TABLE IF NOT EXISTS tb_playlist_song (
  id INT NOT NULL AUTO_INCREMENT,
  playlist_id INT NOT NULL COMMENT '歌单ID',
  song_id INT NOT NULL COMMENT '歌曲ID',
  sort_order INT DEFAULT 0 COMMENT '排序顺序',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_playlist_song (playlist_id, song_id),
  INDEX idx_playlist_id (playlist_id),
  INDEX idx_song_id (song_id),
  FOREIGN KEY (playlist_id) REFERENCES tb_playlist(id) ON DELETE CASCADE,
  FOREIGN KEY (song_id) REFERENCES tb_song(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌单歌曲关联表';

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
('张国荣', '/music-server/img/singerPic/zhangguorong.jpg');

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
('泡沫', 9, 'G.E.M.邓紫棋', '/music-server/song/G.E.M.邓紫棋-泡沫.mp3', '/music-server/img/songPic/paomo.jpg');
