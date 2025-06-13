# 🛠️ AFTAS Development Guide

## 🚀 Quick Start (Hot Reloading)

### **Start Development Environment**
```powershell
# Option 1: Use the convenience script (Recommended)
./scripts/dev-start.ps1

# Option 2: Manual start
docker-compose -f docker-compose.dev.yml up -d
```

### **Stop Development Environment**
```powershell
# Option 1: Use the convenience script
./scripts/dev-stop.ps1

# Option 2: Manual stop
docker-compose -f docker-compose.dev.yml down
```

## 🔥 Hot Reloading Features

### **Frontend (Angular)**
- ✨ **Live Reload**: Edit any TypeScript, HTML, or CSS file → See changes instantly in browser
- 🎯 **Auto Refresh**: Browser automatically refreshes when files change
- 📱 **Component Development**: Perfect for UI development and testing

### **Backend (Spring Boot)**
- 🔄 **Hot Swap**: Java class changes are reflected without full restart
- 🐛 **Debug Ready**: Remote debugging available on port 5005
- 📊 **Dev Profile**: Uses development-specific configurations

## 📍 Development URLs

| Service | URL | Description |
|---------|-----|-------------|
| Frontend | http://localhost:4200 | Angular development server |
| Backend API | http://localhost:8080 | Spring Boot with hot reload |
| PgAdmin | http://localhost:5050 | Database management |
| Debug Port | localhost:5005 | Remote debugging |

## 🔧 Development Commands

### **View Logs**
```powershell
# All services
./scripts/dev-logs.ps1

# Specific service
./scripts/dev-logs.ps1 -Service frontend
./scripts/dev-logs.ps1 -Service backend
```

### **Service Management**
```powershell
# Restart a service
docker-compose -f docker-compose.dev.yml restart frontend

# Rebuild a service (if needed)
docker-compose -f docker-compose.dev.yml up --build frontend -d

# Check service status
docker-compose -f docker-compose.dev.yml ps
```

## 🎯 Development Workflow

### **Frontend Development**
1. **Start dev environment**: `./scripts/dev-start.ps1`
2. **Edit files**: Modify TypeScript, HTML, CSS files in `frontend/src/`
3. **See changes**: Browser auto-refreshes with your changes
4. **No rebuild needed**: Changes are instant!

### **Backend Development**
1. **Start dev environment**: `./scripts/dev-start.ps1`
2. **Edit Java files**: Modify files in `backend/src/`
3. **Changes applied**: Most changes hot-swapped automatically
4. **For major changes**: Service restarts automatically when needed

### **Database Development**
1. **Access PgAdmin**: http://localhost:5050
2. **Connect to DB**: Host: `postgres`, Port: `5432`
3. **Credentials**: Username: `postgres`, Password: `admin`

## 🐛 Debugging

### **Frontend Debugging**
- **Browser DevTools**: F12 → Console/Network/Elements tabs
- **Angular DevTools**: Install Angular DevTools browser extension
- **Source Maps**: Full TypeScript debugging in browser

### **Backend Debugging**
- **Remote Debug**: Connect IDE to `localhost:5005`
- **IntelliJ IDEA**: Run → Edit Configurations → Remote JVM Debug
- **VS Code**: Configure launch.json for remote debugging

## 📊 Performance Benefits

| Aspect | Traditional Docker | Hot Reload Dev |
|--------|-------------------|----------------|
| **Initial Start** | ~3-5 minutes | ~30 seconds |
| **Code Changes** | ~2-3 minutes rebuild | **Instant** |
| **Feedback Loop** | Slow | **Real-time** |
| **Productivity** | Low | **High** |

## 🔄 Environment Comparison

### **Development** (`docker-compose.dev.yml`)
- ✅ Hot reloading enabled
- ✅ Volume mounts for live code changes
- ✅ Debug ports exposed
- ✅ Development-specific configurations
- ⚡ **Fast feedback loop**

### **Production** (`docker-compose.yml`)
- ✅ Optimized builds
- ✅ Multi-stage Dockerfiles
- ✅ Production configurations
- ✅ Security hardening
- 🏭 **Production ready**

## 🛠️ Troubleshooting

### **Common Issues**

#### **Service Won't Start**
```powershell
# Check logs
./scripts/dev-logs.ps1 -Service [service-name]

# Restart service
docker-compose -f docker-compose.dev.yml restart [service-name]
```

#### **Port Already in Use**
```powershell
# Find what's using the port
netstat -ano | findstr :4200
netstat -ano | findstr :8080

# Kill the process (replace PID)
taskkill /PID [PID] /F
```

#### **Changes Not Reflecting**
```powershell
# Restart the specific service
docker-compose -f docker-compose.dev.yml restart frontend

# Clear browser cache
# Press Ctrl+F5 for hard refresh
```

#### **Database Connection Issues**
```powershell
# Check if postgres is healthy
docker-compose -f docker-compose.dev.yml ps

# Restart database
docker-compose -f docker-compose.dev.yml restart postgres
```

## 📁 Project Structure

```
AFTAS-SECURITY/
├── 📄 docker-compose.dev.yml    # Development environment
├── 📄 docker-compose.yml        # Production environment
├── 📁 frontend/                 # Angular app (hot reload)
├── 📁 backend/                  # Spring Boot app (hot reload)
├── 📁 scripts/                  # Development scripts
│   ├── 📄 dev-start.ps1        # Start development
│   ├── 📄 dev-stop.ps1         # Stop development
│   └── 📄 dev-logs.ps1         # View logs
└── 📄 DEVELOPMENT.md           # This file
```

## 🎉 Tips for Maximum Productivity

1. **Use Multiple Terminals**: 
   - Terminal 1: Development environment
   - Terminal 2: Git operations
   - Terminal 3: Logs viewing

2. **Browser Setup**:
   - Keep DevTools open
   - Use Angular DevTools extension
   - Enable auto-refresh

3. **IDE Configuration**:
   - Set up remote debugging for backend
   - Configure auto-save for instant changes
   - Use TypeScript language service

4. **Git Workflow**:
   - Make frequent small commits
   - Test changes before committing
   - Use feature branches

---

## 🚀 **Ready to develop? Run: `./scripts/dev-start.ps1`** 