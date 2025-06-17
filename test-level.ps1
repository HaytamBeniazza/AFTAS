$body = @'
{
    "code": 1,
    "description": "Small Fish",
    "points": 10
}
'@

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/level" -Method POST -Body $body -ContentType "application/json"
    Write-Host "Level created successfully!" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json)
}
catch {
    Write-Host "Error creating level:" -ForegroundColor Red
    Write-Host $_.Exception.Message
} 