Write-Host "Testing fish import by creating a few fish directly..." -ForegroundColor Cyan

$fishData = @(
    @{ name = "Atlantic Salmon"; averageWeight = 6.5; level_id = 3 },
    @{ name = "Rainbow Trout"; averageWeight = 2.3; level_id = 2 },
    @{ name = "Pacific Cod"; averageWeight = 12.0; level_id = 4 },
    @{ name = "Goldfish"; averageWeight = 0.3; level_id = 1 },
    @{ name = "Bluefin Tuna"; averageWeight = 45.0; level_id = 5 }
)

$baseUrl = "http://localhost:8080/fish"
$successCount = 0

foreach ($fish in $fishData) {
    try {
        $body = $fish | ConvertTo-Json
        $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $body -ContentType "application/json"
        Write-Host "Created fish: $($fish.name)" -ForegroundColor Green
        $successCount++
    }
    catch {
        Write-Host "Failed to create fish: $($fish.name) - $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "Fish import test complete! Created $successCount out of $($fishData.Count) fish." -ForegroundColor Cyan

# Test if we can retrieve the fish
try {
    $allFish = Invoke-RestMethod -Uri $baseUrl -Method GET
    Write-Host "Total fish in database: $($allFish.Count)" -ForegroundColor Yellow
}
catch {
    Write-Host "Failed to retrieve fish list: $($_.Exception.Message)" -ForegroundColor Red
} 