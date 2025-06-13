# AFTAS Development Environment Logs Viewer
# This script shows live logs from the development environment

param(
    [string]$Service = "",
    [switch]$Follow = $true
)

Write-Host "📋 AFTAS Development Logs" -ForegroundColor Cyan

# Navigate to project root
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptPath
Set-Location $projectRoot

if ($Service -eq "") {
    Write-Host "📊 Showing logs for all services..." -ForegroundColor Yellow
    Write-Host "Available services: frontend, backend, postgres, pgadmin" -ForegroundColor Gray
    Write-Host "To view specific service: ./scripts/dev-logs.ps1 -Service frontend" -ForegroundColor Gray
    Write-Host ""
    
    if ($Follow) {
        docker-compose -f docker-compose.dev.yml logs -f
    } else {
        docker-compose -f docker-compose.dev.yml logs
    }
} else {
    Write-Host "📊 Showing logs for service: $Service" -ForegroundColor Yellow
    
    if ($Follow) {
        docker-compose -f docker-compose.dev.yml logs -f $Service
    } else {
        docker-compose -f docker-compose.dev.yml logs $Service
    }
} 