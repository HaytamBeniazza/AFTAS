Write-Host "Creating initial levels for fish import..." -ForegroundColor Cyan

$levels = @(
    @{ code = 1; description = "Small Fish"; points = 10 },
    @{ code = 2; description = "Medium Fish"; points = 25 },
    @{ code = 3; description = "Large Fish"; points = 50 },
    @{ code = 4; description = "Very Large Fish"; points = 100 },
    @{ code = 5; description = "Giant Fish"; points = 200 }
)

$baseUrl = "http://localhost:8080/level"

foreach ($level in $levels) {
    try {
        $body = $level | ConvertTo-Json
        $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $body -ContentType "application/json"
        Write-Host "Created level: $($level.description)" -ForegroundColor Green
    }
    catch {
        Write-Host "Level may already exist: $($level.description)" -ForegroundColor Yellow
    }
}

Write-Host "Level creation complete!" -ForegroundColor Green
Write-Host "Navigate to http://localhost:4200/fish and click Import ALL Fish Species" -ForegroundColor Cyan 