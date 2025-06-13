Write-Host "========================================" -ForegroundColor Cyan
Write-Host "      AFTAS ULTRA-FAST DEVELOPMENT      " -ForegroundColor Cyan  
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Navigate to project directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

Write-Host "[INFO] Starting database..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml up -d postgres

Write-Host "[INFO] Waiting for database to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

Write-Host "[INFO] Starting Angular development server..." -ForegroundColor Yellow
Set-Location "frontend"

# Start Angular in a new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "npm start"

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "      DEVELOPMENT ENVIRONMENT READY!    " -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "[SUCCESS] Frontend: http://localhost:4200" -ForegroundColor Green
Write-Host "[SUCCESS] Database: PostgreSQL on port 5432" -ForegroundColor Green
Write-Host ""
Write-Host "[INFO] Angular server is starting in a separate window..." -ForegroundColor Cyan
Write-Host "[INFO] Wait 10-15 seconds, then visit http://localhost:4200" -ForegroundColor Cyan
Write-Host ""

# Wait a bit then open browser
Start-Sleep -Seconds 10
Write-Host "[INFO] Opening website..." -ForegroundColor Green
Start-Process "http://localhost:4200"

Write-Host ""
Write-Host "Development environment is running!" -ForegroundColor Green
Write-Host "Press any key to continue..." -ForegroundColor Yellow
Read-Host 