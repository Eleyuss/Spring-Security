# 🔐 Secure WebApp with Spring Security

This project demonstrates the use of **Spring Security** to implement robust authentication, authorization, and session management for a Java web application.

## ✨ Features

- 🧑‍💻 User registration with username and password
- 🔒 Login and logout with secure session control
- 🔑 Role-based access control (`ADMIN`, `USER`)
- 🧼 Password encryption with BCrypt
- 🚷 CSRF protection and session expiration
- 👀 Show logged-in user name and role
- ⚠️ Custom error handling for unauthorized access
- 🖥️ Frontend built with Thymeleaf templates

## 🔧 Tech Stack

- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Thymeleaf**
- **Maven**
- **Lombok**

## 🧩 Architecture Overview

```text
UserService          --> Handles user registration
MyUserDetailsService --> Loads user details for login
SecurityConfig       --> Configures login, logout, session & role rules
AuthController       --> Manages registration & login endpoints
