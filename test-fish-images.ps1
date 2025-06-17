Write-Host "Testing fish image functionality..." -ForegroundColor Yellow

# Test fetching image for a specific fish
Write-Host "`n1. Testing single fish image fetch..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/fetch-image/salmon" -Method POST -ContentType "application/json"
    Write-Host "Single fish image fetch successful!" -ForegroundColor Green
    Write-Host "Fish: $($response.fishName)" -ForegroundColor White
    Write-Host "Image URL: $($response.imageUrl)" -ForegroundColor White
} catch {
    Write-Host "Error fetching single fish image:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

# Test fetching image for another fish
Write-Host "`n2. Testing another fish image fetch..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/fetch-image/tuna" -Method POST -ContentType "application/json"
    Write-Host "Another fish image fetch successful!" -ForegroundColor Green
    Write-Host "Fish: $($response.fishName)" -ForegroundColor White
    Write-Host "Image URL: $($response.imageUrl)" -ForegroundColor White
} catch {
    Write-Host "Error fetching another fish image:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

# Check current fish data with images
Write-Host "`n3. Checking fish data structure with images..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/paginated?page=0&size=3" -Method GET
    Write-Host "Fish data with images:" -ForegroundColor Green
    foreach ($fish in $response.content) {
        Write-Host "- Name: $($fish.name)" -ForegroundColor White
        Write-Host "  Weight: $($fish.averageWeight)" -ForegroundColor White
        Write-Host "  Image: $($fish.imageUrl)" -ForegroundColor White
        Write-Host ""
    }
} catch {
    Write-Host "Error getting fish data:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

# Test bulk image fetching (this will take a while)
Write-Host "`n4. Testing bulk image fetching for first 10 fish..." -ForegroundColor Cyan
Write-Host "This may take a while..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/fish/fetch-images" -Method POST -ContentType "application/json"
    Write-Host "Bulk image fetching started!" -ForegroundColor Green
    Write-Host "Response: $($response.message)" -ForegroundColor White
} catch {
    Write-Host "Error starting bulk image fetch:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host "`nFish image testing complete!" -ForegroundColor Yellow 