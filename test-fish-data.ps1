Write-Host "Checking fish data structure..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/paginated?page=0&size=3" -Method GET
    Write-Host "Fish data structure:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 4
} catch {
    Write-Host "Error getting fish data:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
} 