# 🛠️ AFTAS - Development Guide

## 📋 **Table of Contents**
- [Development Environment Setup](#development-environment-setup)
- [Project Structure](#project-structure)
- [Development Workflow](#development-workflow)
- [Testing Strategy](#testing-strategy)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)
- [Contributing Guidelines](#contributing-guidelines)

---

## 🚀 **Development Environment Setup**

### **Prerequisites**
- **Java 17+** - [Download](https://adoptium.net/)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/)
- **Docker & Docker Compose** - [Download](https://www.docker.com/get-started)
- **Git** - [Download](https://git-scm.com/)

### **IDE Recommendations**
- **Backend**: IntelliJ IDEA / Visual Studio Code with Java extensions
- **Frontend**: Visual Studio Code with Angular extensions
- **Database**: pgAdmin / DBeaver

---

## 📁 **Project Structure**

```
AFTAS-SECURITY/
├── aftas-api/                  # Spring Boot Backend
│   ├── src/main/java/com/WI/WIGOLDFISH/
│   │   ├── controllers/        # REST Controllers
│   │   ├── services/          # Business Logic
│   │   ├── repositories/      # Data Access Layer
│   │   ├── entities/          # JPA Entities
│   │   ├── configs/           # Configuration Classes
│   │   ├── exceptions/        # Custom Exceptions
│   │   └── enums/             # Enumerations
│   ├── src/main/resources/
│   │   ├── application.properties.example
│   │   └── application.properties
│   └── pom.xml                # Maven Dependencies
├── WI-GOLD-FISH-FRONT-master/ # Angular Frontend
│   ├── src/app/
│   │   ├── components/        # Reusable Components
│   │   ├── pages/             # Page Components
│   │   ├── services/          # HTTP Services
│   │   ├── guards/            # Route Guards
│   │   ├── models/            # TypeScript Interfaces
│   │   └── enums/             # TypeScript Enums
│   ├── package.json           # Node Dependencies
│   └── angular.json           # Angular Configuration
├── docker-compose.yml         # Development Environment
├── README.md                  # Project Documentation
└── DEVELOPMENT.md             # This File
```

---

## 🔧 **Development Workflow**

### **1. Environment Setup**

```bash
# Clone the repository
git clone https://github.com/HaytamBeniazza/AFTAS-SECURITY.git
cd AFTAS-SECURITY

# Setup with Docker (Recommended)
docker-compose up -d

# OR Manual Setup
```

### **2. Manual Backend Setup**

```bash
cd aftas-api

# Configure database
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your database credentials

# Install dependencies and run
./mvnw clean install
./mvnw spring-boot:run

# API will be available at http://localhost:8080/api
# Swagger UI at http://localhost:8080/swagger-ui.html
```

### **3. Manual Frontend Setup**

```bash
cd WI-GOLD-FISH-FRONT-master

# Install dependencies
npm install

# Start development server
npm run start

# Application will be available at http://localhost:4200
```

---

## 🧪 **Testing Strategy**

### **Backend Testing**

```bash
cd aftas-api

# Run all tests
./mvnw test

# Run tests with coverage
./mvnw jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### **Frontend Testing**

```bash
cd WI-GOLD-FISH-FRONT-master

# Run unit tests
npm run test

# Run tests with coverage
npm run test:coverage

# Run e2e tests
npm run e2e
```

### **Integration Testing**

```bash
# Run full stack tests with Docker
docker-compose -f docker-compose.test.yml up --build
```

---

## 📚 **API Documentation**

### **Accessing Documentation**
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### **API Endpoints Overview**

#### **Authentication**
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/refresh` - Refresh JWT token

#### **Competitions**
- `GET /api/competitions` - List all competitions
- `POST /api/competitions` - Create new competition
- `GET /api/competitions/{id}` - Get competition details
- `PUT /api/competitions/{id}` - Update competition
- `DELETE /api/competitions/{id}` - Delete competition

#### **Participants**
- `GET /api/participants` - List participants
- `POST /api/participants` - Register participant
- `GET /api/participants/{id}` - Get participant details

---

## 🚀 **Deployment**

### **Docker Deployment**

```bash
# Production deployment
docker-compose -f docker-compose.prod.yml up -d

# Scaling services
docker-compose up -d --scale backend=3
```

### **Manual Deployment**

#### **Backend Deployment**
```bash
cd aftas-api
./mvnw clean package -DskipTests
java -jar target/WI-GOLD-FISH-0.0.1-SNAPSHOT.jar
```

#### **Frontend Deployment**
```bash
cd WI-GOLD-FISH-FRONT-master
npm run build --prod
# Deploy dist/ folder to web server
```

---

## 🤝 **Contributing Guidelines**

### **Code Style**
- **Java**: Follow Google Java Style Guide
- **TypeScript**: Use Angular Style Guide
- **Formatting**: Use Prettier for frontend, Maven formatter for backend

### **Git Workflow**
```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make changes and commit
git add .
git commit -m "feat: add new feature description"

# Push and create PR
git push origin feature/your-feature-name
```

### **Commit Message Convention**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance tasks

---

## 🔍 **Debugging**

### **Backend Debugging**
```bash
# Enable debug mode
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### **Frontend Debugging**
```bash
# Enable Angular debug mode
ng serve --source-map=true
```

---

## 📊 **Performance Monitoring**

### **Backend Metrics**
- Spring Boot Actuator endpoints
- JaCoCo code coverage reports
- Application performance monitoring

### **Frontend Metrics**
- Angular DevTools
- Chrome Lighthouse audits
- Bundle size analysis

---

## 🛡️ **Security Considerations**

- JWT token management
- CORS configuration
- Input validation and sanitization
- SQL injection prevention
- XSS protection
- HTTPS enforcement in production

---

## 📱 **Mobile Development**

The frontend is responsive and works on mobile devices. For native mobile apps:
- Consider using Ionic with Angular
- API is ready for mobile consumption
- JWT authentication works across platforms

---

*Happy coding! 🎉* 