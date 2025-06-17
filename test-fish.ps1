$body = @'
{
    "name": "Test Salmon",
    "averageWeight": 5.5,
    "level_id": 2
}
'@

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish" -Method POST -Body $body -ContentType "application/json"
    Write-Host "Fish created successfully!" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json)
}
catch {
    Write-Host "Error creating fish:" -ForegroundColor Red
    Write-Host $_.Exception.Message
} 