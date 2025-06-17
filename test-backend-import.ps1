Write-Host "🌍 Testing comprehensive fish import from backend..." -ForegroundColor Cyan

try {
    Write-Host "📡 Starting fish import..." -ForegroundColor Yellow
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/import-all" -Method POST -Body '{}' -ContentType "application/json"
    
    Write-Host "✅ Import response:" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json) -ForegroundColor White
    
    # Wait a moment for import to complete
    Write-Host "⏳ Waiting for import to complete..." -ForegroundColor Yellow
    Start-Sleep -Seconds 5
    
    # Check how many fish we have now
    $allFish = Invoke-RestMethod -Uri "http://localhost:8080/fish" -Method GET
    Write-Host "🐟 Total fish in database: $($allFish.Count)" -ForegroundColor Cyan
    
    if ($allFish.Count -gt 0) {
        Write-Host "📊 Sample fish:" -ForegroundColor Yellow
        $allFish | Select-Object -First 5 | ForEach-Object {
            Write-Host "  - $($_.name) ($($_.averageWeight)kg, Level: $($_.level.description))" -ForegroundColor White
        }
    }
    
} catch {
    Write-Host "❌ Error during import test:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
} 