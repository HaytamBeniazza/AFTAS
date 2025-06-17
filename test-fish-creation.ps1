Write-Host "Testing fish creation endpoints..." -ForegroundColor Yellow

# Test the simple test import first
Write-Host "`nTesting test fish creation..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/test-import" -Method POST -ContentType "application/json"
    Write-Host "Test fish created successfully!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json)" -ForegroundColor Cyan
} catch {
    Write-Host "Error creating test fish:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

# Check how many fish we have now
Write-Host "`nChecking current fish count..." -ForegroundColor Cyan
try {
    $fishResponse = Invoke-RestMethod -Uri "http://localhost:8080/fish" -Method GET
    Write-Host "Current fish count: $($fishResponse.length)" -ForegroundColor Green
} catch {
    Write-Host "Error getting fish count:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

# Test the full import (this might take longer)
Write-Host "`nTesting full fish import from external APIs..." -ForegroundColor Cyan
Write-Host "This may take a while..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/import-all" -Method POST -ContentType "application/json"
    Write-Host "Full import completed!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json)" -ForegroundColor Cyan
} catch {
    Write-Host "Error during full import:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

# Final fish count
Write-Host "`nFinal fish count..." -ForegroundColor Cyan
try {
    $fishResponse = Invoke-RestMethod -Uri "http://localhost:8080/fish" -Method GET
    Write-Host "Final fish count: $($fishResponse.length)" -ForegroundColor Green
} catch {
    Write-Host "Error getting final fish count:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host "`nFish creation testing complete!" -ForegroundColor Yellow 