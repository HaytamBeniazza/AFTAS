# AFTAS Development Environment Stopper
# This script stops the development environment

Write-Host "🛑 Stopping AFTAS Development Environment..." -ForegroundColor Yellow

# Navigate to project root
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptPath
Set-Location $projectRoot

# Stop development containers
Write-Host "📂 Project root: $projectRoot" -ForegroundColor Yellow
Write-Host "🔄 Stopping development containers..." -ForegroundColor Yellow

docker-compose -f docker-compose.dev.yml down --remove-orphans

Write-Host ""
Write-Host "✅ Development environment stopped!" -ForegroundColor Green
Write-Host "🚀 To start again: ./scripts/dev-start.ps1" -ForegroundColor Cyan 