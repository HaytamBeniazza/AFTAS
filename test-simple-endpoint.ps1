Write-Host "Testing if backend is running..." -ForegroundColor Yellow

# Test a simple endpoint first
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish" -Method GET
    Write-Host "Backend is running! Found $($response.length) fish" -ForegroundColor Green
} catch {
    Write-Host "Backend connection error:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
    exit 1
}

# Test levels endpoint
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/level" -Method GET
    Write-Host "Levels endpoint working! Found $($response.length) levels" -ForegroundColor Green
} catch {
    Write-Host "Levels endpoint error:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host "Basic endpoints test complete!" -ForegroundColor Yellow 