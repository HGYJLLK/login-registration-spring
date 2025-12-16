# 月签作业 - 管理系统前端

基于 Vue 3 + Element Plus 的管理系统前端项目

## 技术栈

- Vue 3
- Vue Router 4
- Element Plus（UI组件库）
- Axios（HTTP请求）
- Vite（构建工具）

## 安装依赖

```bash
npm install
```

## 运行开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

## 构建生产版本

```bash
npm run build
```

## 功能说明

### 登录页面
- 用户名密码登录
- 登录成功后跳转到管理系统主页

### 管理系统主页
- **顶部栏**：左边显示"管理系统"，右边显示用户名和退出登录
- **左侧导航栏**：
  - 导航一：用户管理
  - 导航二：待开发
  - 导航三：待开发
  - 导航四：待开发

### 用户管理（导航一）
- 显示所有已注册用户的信息表格
- 表格列：姓名、用户名、性别、手机号码、操作
- 操作按钮：
  - **修改**：弹出对话框修改用户的姓名、性别、手机号码
  - **删除**：删除数据库中的用户数据

## 后端API接口

后端服务运行在 http://localhost:8082

- `POST /api/auth/login` - 用户登录
- `GET /api/users` - 获取所有用户
- `PUT /api/users/{id}` - 更新用户信息
- `DELETE /api/users/{id}` - 删除用户

## 项目结构

```
src/
├── api/              # API接口封装
│   ├── request.js    # Axios配置
│   └── user.js       # 用户相关API
├── router/           # 路由配置
│   └── index.js
├── views/            # 页面组件
│   ├── Login.vue     # 登录页
│   ├── Layout.vue    # 主布局
│   ├── Nav1.vue      # 用户管理
│   ├── Nav2.vue      # 导航二
│   ├── Nav3.vue      # 导航三
│   └── Nav4.vue      # 导航四
├── App.vue           # 根组件
└── main.js           # 入口文件
```
