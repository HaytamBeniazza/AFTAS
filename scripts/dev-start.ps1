# AFTAS Development Environment Starter
# This script starts the development environment with hot reloading

Write-Host "Starting AFTAS Development Environment..." -ForegroundColor Cyan

# Check if Docker is running
try {
    docker info | Out-Null
    Write-Host "Docker is running" -ForegroundColor Green
} catch {
    Write-Host "Docker is not running. Please start Docker Desktop." -ForegroundColor Red
    exit 1
}

# Navigate to project root
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptPath
Set-Location $projectRoot

Write-Host "Project root: $projectRoot" -ForegroundColor Yellow

# Stop any existing containers
Write-Host "Stopping existing containers..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml down --remove-orphans

# Start development environment
Write-Host "Starting development containers..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml up -d

# Wait for services to be ready
Write-Host "Waiting for services to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Check service status
Write-Host "Service Status:" -ForegroundColor Cyan
docker-compose -f docker-compose.dev.yml ps

Write-Host ""
Write-Host "Development environment is starting!" -ForegroundColor Green
Write-Host ""
Write-Host "URLs:" -ForegroundColor Cyan
Write-Host "   Frontend (Angular):  http://localhost:4200" -ForegroundColor White
Write-Host "   Backend API:         http://localhost:8080" -ForegroundColor White
Write-Host "   PgAdmin:            http://localhost:5050" -ForegroundColor White
Write-Host ""
Write-Host "Development Features:" -ForegroundColor Cyan
Write-Host "   Hot Reload:       Edit files and see changes instantly!" -ForegroundColor White
Write-Host "   Debug Backend:    Connect debugger to port 5005" -ForegroundColor White
Write-Host "   Live Logs:        docker-compose -f docker-compose.dev.yml logs -f" -ForegroundColor White
Write-Host ""
Write-Host "Stop: ./scripts/dev-stop.ps1" -ForegroundColor Yellow 