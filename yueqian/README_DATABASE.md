# 音乐播放器数据库说明

## 数据库信息
- **数据库名**: `queqian`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`

## 数据库表结构

### 用户相关表
1. **tb_user** - 用户表
   - 包含字段：id, username, password, name, gender, phonenumber, identitycode, avatar_url, email

### 音乐相关表
2. **tb_singer** - 歌手表
   - 包含字段：id, name, pic_url, introduction, create_time

3. **tb_song** - 歌曲表
   - 包含字段：id, name, singer_id, singer_name, song_url, pic_url, lyric, duration, create_time

4. **tb_song_list** - 歌单表
   - 包含字段：id, title, pic_url, introduction, style, create_time

5. **tb_list_song** - 歌单-歌曲关联表
   - 包含字段：id, song_list_id, song_id

6. **tb_favorite** - 用户收藏表
   - 包含字段：id, user_id, song_id, create_time

## 初始化数据库

### 方式一：使用命令行
```bash
mysql -u root -p2333 < init_database.sql
```

### 方式二：使用MySQL客户端
```sql
source /path/to/init_database.sql;
```

### 方式三：使用Navicat等图形工具
1. 打开Navicat
2. 连接到MySQL服务器
3. 选择"查询" -> "新建查询"
4. 复制init_database.sql的内容并执行

## 注意事项
1. 脚本包含示例数据，首次运行会自动创建所有表并插入测试数据
2. 重复执行不会造成错误（使用了`IF NOT EXISTS`和`ON DUPLICATE KEY UPDATE`）
3. 确保MySQL服务已启动
4. 确保使用的账号有创建数据库和表的权限

## 管理员登录
- 用户名：`admin`
- 密码：`admin`
- 说明：管理员账号在前端硬编码，无需在数据库中创建

## 数据库连接配置
配置文件位置：`src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/queqian?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&useUnicode=true&connectionCollation=utf8mb4_unicode_ci
spring.datasource.username=root
spring.datasource.password=2333
```
