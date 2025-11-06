# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.4.5 login/registration system with a RESTful API backend and static HTML frontend. The application uses Spring Security with BCrypt for password encryption and MySQL for data persistence.

## Build and Run Commands

### Build the project
```bash
./mvnw clean install
```

### Run the application
```bash
./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw test
```

### Run a specific test class
```bash
./mvnw test -Dtest=ClassName
```

### Compile without running tests
```bash
./mvnw clean compile -DskipTests
```

## Architecture

### Layer Structure

The application follows a standard Spring Boot layered architecture:

1. **Controller Layer** (`controller/`)
   - `AuthController`: Handles `/api/auth/register` and `/api/auth/login` endpoints
   - Returns JSON responses with `{message, success}` format
   - Uses `@RequestBody Map<String, String>` for request parsing

2. **Service Layer** (`service/`)
   - `AuthService`: Business logic for registration and authentication
   - `UserDetailsServiceImpl`: Integration with Spring Security (if needed for advanced auth)
   - Uses `PasswordEncoder` (BCrypt) for password hashing

3. **Repository Layer** (`model/`)
   - `UserRepository`: Spring Data JPA repository interface
   - Provides `findByUsername()` and `existsByUsername()` methods
   - `User`: JPA entity mapped to `tb_user` table

4. **Security Configuration** (`SecurityConfig`)
   - CSRF is **disabled** (stateless API)
   - Form login and HTTP Basic are **disabled**
   - `/api/auth/**`, static resources, and HTML pages are publicly accessible
   - All other endpoints require authentication (though none exist currently)

### Database Schema

The application uses MySQL database named `jdbc` with a single table:

```sql
CREATE TABLE `tb_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,  -- Must be at least 100 chars for BCrypt
  `name` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phonenumber` varchar(50) DEFAULT NULL,  -- Currently stores email
  `identitycode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
```

**Important**: The `phonenumber` field is currently being used to store email addresses during registration (see [AuthService.java:29](src/main/java/com/example/login/service/AuthService.java#L29)).

### Frontend Integration

- Static resources in `src/main/resources/static/`
- Login page: `index.html` (root path `/`)
- Registration page: `register.html` (`/register.html`)
- Uses Fetch API to call backend REST endpoints
- No session management or JWT tokens currently implemented

## Configuration

Database configuration is in [application.properties](src/main/resources/application.properties):
- Database URL: `jdbc:mysql://localhost:3306/jdbc`
- Default credentials: root/123456789 (change for production)
- JPA DDL auto: `none` (manual schema management)
- SQL logging is enabled

## Key Design Decisions

1. **Password Storage**: BCrypt hashing with Spring Security's `PasswordEncoder`
2. **API Design**: Stateless REST API (no session state)
3. **Email Field Workaround**: The `phonenumber` database column is repurposed to store email addresses
4. **Manual Schema**: JPA is configured with `ddl-auto=none`, requiring manual database table creation
5. **Java Version**: Java 21 with Maven compiler configured for version 21

## Common Pitfalls

- Database `password` column must be at least 100 characters to accommodate BCrypt hashes (60 chars typical)
- The database must be created manually before running the application
- CSRF is disabled, so this is suitable for API-only backends but not recommended for traditional form-based apps
- No JWT or session tokens are implemented - authentication state is not maintained across requests
