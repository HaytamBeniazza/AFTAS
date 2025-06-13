# 🏆 AFTAS - Advanced Fishing Tournament Administration System

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=spring" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Angular-16-red?style=for-the-badge&logo=angular" alt="Angular">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" alt="Java">
  <img src="https://img.shields.io/badge/PostgreSQL-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/TypeScript-blue?style=for-the-badge&logo=typescript" alt="TypeScript">
</p>

## 📋 **Project Overview**

AFTAS is a comprehensive full-stack web application designed to manage fishing competitions and tournaments. The system provides complete tournament administration capabilities, from participant registration to competition scoring and results management.

### 🎯 **Key Features**

- **Tournament Management**: Create, configure, and manage fishing competitions
- **Participant Registration**: Secure user registration and authentication system
- **Real-time Scoring**: Live competition scoring and leaderboard updates
- **Results Analytics**: Comprehensive reporting and analytics dashboard
- **Admin Panel**: Full administrative control over competitions and participants
- **Mobile Responsive**: Optimized for both desktop and mobile devices

## 🏗️ **Architecture Overview**

### **Backend (Spring Boot)**
- **Framework**: Spring Boot 3.2.0 with Java 17
- **Security**: JWT-based authentication with Spring Security
- **Database**: PostgreSQL with JPA/Hibernate
- **Testing**: JUnit with JaCoCo code coverage
- **Documentation**: OpenAPI/Swagger integration

### **Frontend (Angular)**
- **Framework**: Angular 16 with TypeScript
- **UI Library**: Angular Material + PrimeNG
- **Styling**: Tailwind CSS for responsive design
- **State Management**: RxJS for reactive programming
- **Testing**: Jasmine & Karma

## 🚀 **Quick Start**

### **Prerequisites**
- Java 17+
- Node.js 18+
- PostgreSQL 12+
- Maven 3.8+

### **Backend Setup**
```bash
# Navigate to backend directory
cd aftas-api

# Configure database in application.properties
# Copy application.properties.example to application.properties
# Update database credentials

# Run the application
./mvnw spring-boot:run

# API will be available at http://localhost:8080
```

### **Frontend Setup**
```bash
# Navigate to frontend directory
cd WI-GOLD-FISH-FRONT-master

# Install dependencies
npm install

# Start development server
npm run start

# Application will be available at http://localhost:4200
```

## 📊 **Database Schema**

The application uses a PostgreSQL database with the following main entities:
- **Users**: Participant management and authentication
- **Competitions**: Tournament configuration and scheduling
- **Registrations**: Participant enrollment in competitions
- **Results**: Competition scoring and rankings

## 🔐 **Security Features**

- JWT-based authentication
- Role-based access control (Admin, Participant)
- Password encryption with BCrypt
- CORS configuration for cross-origin requests
- Input validation and sanitization

## 🧪 **Testing**

### **Backend Testing**
```bash
cd aftas-api
./mvnw test
./mvnw jacoco:report  # Generate coverage report
```

### **Frontend Testing**
```bash
cd WI-GOLD-FISH-FRONT-master
npm run test
npm run test:coverage
```

## 📚 **API Documentation**

Once the backend is running, access the API documentation at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🌟 **Key Technologies**

### **Backend Stack**
- Spring Boot (Web, Security, Data JPA)
- PostgreSQL Database
- JWT Authentication
- Maven Build Tool
- JaCoCo Code Coverage

### **Frontend Stack**
- Angular 16 Framework
- TypeScript Programming
- Angular Material UI
- PrimeNG Components
- Tailwind CSS Styling
- RxJS Reactive Programming

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 **Developer**

**Haytam Beniazza**
- Portfolio: [Your Portfolio URL]
- LinkedIn: [Your LinkedIn URL]
- Email: [Your Email]

---

*This project demonstrates full-stack development skills using modern technologies and best practices in software engineering.* 