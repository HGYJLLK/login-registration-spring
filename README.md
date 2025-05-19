# login-registration-spring

## 功能特性

- 用户注册
- 用户登录
- 密码加密存储
- RESTful API设计
- 前后端分离架构

## 技术栈

- **后端**：
  - Spring Boot 3.4.5
  - Spring Security
  - Spring Data JPA
  - MySQL数据库
  - BCrypt密码加密

- **前端**：
  - 原生HTML/CSS/JavaScript
  - 简单的表单验证
  - Fetch API进行HTTP请求

## 如何运行

### 前提条件

- JDK 17或更高版本
- Maven 3.6+
- MySQL数据库

### 步骤

1. **克隆仓库**:
   ```
   git clone [仓库URL]
   cd login-registration
   ```

2. **配置数据库**:
   - 创建MySQL数据库
   - 修改`src/main/resources/application.properties`中的数据库连接信息

3. **构建并运行**:
   ```
   mvn spring-boot:run
   ```

4. **访问应用**:
   - 登录页面: http://localhost:8080
   - 注册页面: http://localhost:8080/register.html

## 数据库配置

本项目使用名为`jdbc`的数据库，主要表结构如下：

### tb_user表
```sql
CREATE TABLE `tb_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phonenumber` varchar(50) DEFAULT NULL,
  `identitycode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
```

> **注意**: 确保PASSWORD字段长度至少为100，因为BCrypt加密后的密码通常有60个字符左右。

## API端点

### 注册
- **URL**: `/api/auth/register`
- **方法**: POST
- **请求体**:
  ```json
  {
    "username": "用户名",
    "password": "密码",
    "name": "姓名",
    "email": "邮箱地址"
  }
  ```
- **响应**: 
  ```json
  {
    "message": "注册成功",
    "success": true
  }
  ```

### 登录
- **URL**: `/api/auth/login`
- **方法**: POST
- **请求体**:
  ```json
  {
    "username": "用户名",
    "password": "密码"
  }
  ```
- **响应**: 
  ```json
  {
    "message": "登录成功",
    "success": true
  }
  ```

---

*本项目是Spring Boot学习的起点，通过实现基本的用户认证功能。*
