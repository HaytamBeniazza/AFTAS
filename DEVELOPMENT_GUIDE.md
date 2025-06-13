# 🛠️ AFTAS - Development Guide

## 📋 **Table of Contents**
- [Development Environment Setup](#development-environment-setup)
- [Project Structure](#project-structure)
- [Development Workflow](#development-workflow)
- [Testing Strategy](#testing-strategy)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)
- [Contributing Guidelines](#contributing-guidelines)

## 🚀 **Development Environment Setup**

### **Prerequisites**
- **Java 17+** - [Download](https://adoptium.net/)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/)
- **Docker & Docker Compose** - [Download](https://www.docker.com/get-started)

### **IDE Recommendations**
- **Backend**: IntelliJ IDEA / Visual Studio Code
- **Frontend**: Visual Studio Code with Angular extensions
- **Database**: pgAdmin / DBeaver

## 📁 **Project Structure**

```
aftas/
├── aftas-api/                  # Spring Boot Backend
│   ├── src/main/java/com/WI/WIGOLDFISH/
│   │   ├── controllers/        # REST Controllers
│   │   ├── services/          # Business Logic
│   │   ├── repositories/      # Data Access Layer
│   │   ├── entities/          # JPA Entities
│   │   ├── configs/           # Configuration Classes
│   │   └── exceptions/        # Custom Exceptions
│   └── pom.xml                # Maven Dependencies
├── WI-GOLD-FISH-FRONT-master/ # Angular Frontend
│   ├── src/app/
│   │   ├── components/        # Reusable Components
│   │   ├── pages/             # Page Components
│   │   ├── services/          # HTTP Services
│   │   └── models/            # TypeScript Interfaces
│   └── package.json           # Node Dependencies
└── docker-compose.yml         # Development Environment
```

## 🔧 **Development Workflow**

### **Quick Start with Docker**
```bash
# Clone and start everything
git clone https://github.com/HaytamBeniazza/aftas.git
cd aftas
docker-compose up -d

# Access applications
# Frontend: http://localhost:4200
# Backend API: http://localhost:8080/api
# Swagger UI: http://localhost:8080/swagger-ui.html
# pgAdmin: http://localhost:5050
```

### **Manual Setup**

#### **Backend Setup**
```bash
cd aftas-api
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your database credentials
./mvnw spring-boot:run
```

#### **Frontend Setup**
```bash
cd WI-GOLD-FISH-FRONT-master
npm install
npm run start
```

## 🧪 **Testing Strategy**

### **Backend Testing**
```bash
cd aftas-api
./mvnw test                    # Run tests
./mvnw jacoco:report          # Generate coverage
```

### **Frontend Testing**
```bash
cd WI-GOLD-FISH-FRONT-master
npm run test                  # Unit tests
npm run test:coverage        # Coverage report
npm run e2e                  # E2E tests
```

## 📚 **API Documentation**

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### **Key Endpoints**
- `POST /api/auth/login` - Authentication
- `GET /api/competitions` - List competitions
- `POST /api/competitions` - Create competition
- `GET /api/participants` - List participants

## 🚀 **Deployment**

### **Production Deployment**
```bash
# Build and deploy with Docker
docker-compose -f docker-compose.prod.yml up -d

# Or manual deployment
cd aftas-api && ./mvnw clean package
cd ../WI-GOLD-FISH-FRONT-master && npm run build --prod
```

## 🤝 **Contributing**

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'feat: add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### **Commit Convention**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `style:` - Code formatting
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance

---

*Happy coding! 🎉* 