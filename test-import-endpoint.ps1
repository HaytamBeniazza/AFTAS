Write-Host "Testing fish import endpoint..." -ForegroundColor Yellow

# Test the import endpoint
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/import-all" -Method POST -ContentType "application/json"
    Write-Host "Import started successfully!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json)" -ForegroundColor Cyan
} catch {
    Write-Host "Error testing import endpoint:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        Write-Host "Status Description: $($_.Exception.Response.StatusDescription)" -ForegroundColor Red
    }
}

Write-Host "Test complete!" -ForegroundColor Yellow 