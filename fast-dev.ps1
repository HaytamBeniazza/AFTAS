# AFTAS Ultra-Fast Development Setup
# Runs Angular natively on host machine for maximum speed

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "      AFTAS ULTRA-FAST DEV MODE        " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Node.js is installed
try {
    $nodeVersion = node --version
    Write-Host "[OK] Node.js version: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Node.js is not installed!" -ForegroundColor Red
    Write-Host "Please install Node.js from https://nodejs.org/" -ForegroundColor Yellow
    exit 1
}

# Check if npm is installed
try {
    $npmVersion = npm --version
    Write-Host "[OK] npm version: $npmVersion" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] npm is not installed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[INFO] Starting ultra-fast development mode..." -ForegroundColor Yellow

# Navigate to frontend directory
Set-Location "frontend"

# Check if node_modules exists
if (-not (Test-Path "node_modules")) {
    Write-Host "[INFO] Installing dependencies (this may take a moment)..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Failed to install dependencies!" -ForegroundColor Red
        exit 1
    }
    Write-Host "[OK] Dependencies installed!" -ForegroundColor Green
} else {
    Write-Host "[OK] Dependencies already installed!" -ForegroundColor Green
}

# Start only the database in Docker (much faster)
Write-Host ""
Write-Host "[INFO] Starting database only..." -ForegroundColor Yellow
Set-Location ".."
docker-compose -f docker-compose.dev.yml up -d postgres

# Wait for database to be ready
Write-Host "[INFO] Waiting for database to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

# Go back to frontend and start Angular dev server natively
Set-Location "frontend"
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "      READY TO DEVELOP FAST!           " -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "[FAST] Angular Dev Server: http://localhost:4200" -ForegroundColor Green
Write-Host "[FAST] Database: PostgreSQL on port 5432" -ForegroundColor Green
Write-Host ""
Write-Host "[INFO] Starting Angular development server..." -ForegroundColor Yellow
Write-Host "[INFO] Changes will be reflected INSTANTLY!" -ForegroundColor Cyan
Write-Host ""

# Start Angular dev server natively (SUPER FAST!)
npm start 