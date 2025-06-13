@echo off
echo ========================================
echo      AFTAS ULTRA-FAST DEVELOPMENT
echo ========================================
echo.

echo [INFO] Starting database...
docker-compose -f docker-compose.dev.yml up -d postgres

echo [INFO] Waiting for database to be ready...
timeout /t 3 /nobreak > nul

echo [INFO] Starting Angular development server...
cd frontend
start "AFTAS Angular Server" cmd /k "npm start"

echo.
echo ========================================
echo      DEVELOPMENT ENVIRONMENT READY!
echo ========================================
echo.
echo Frontend: http://localhost:4200
echo Database: PostgreSQL on port 5432
echo.
echo Press any key to open the website...
pause
start http://localhost:4200

echo.
echo Development environment is running!
echo Close this window to stop the database.
echo.
pause 