$podmanPath = Join-Path -Path $env:USERPROFILE -ChildPath ".m2\repository\mylib\bin\podman.exe"

Write-Host "Podman detected: $($podmanPath)" -ForegroundColor Green
Write-Host "Starting Podman machine..."
& $podmanPath machine start

Write-Host "Creating Podman network 'grid'..."
& $podmanPath network create grid

Write-Host "Starting Selenium Standalone Chrome..."
& $podmanPath run -d -p 4444:4444 -p 6900:5900 --net grid --name selenium --shm-size="2g" selenium/standalone-chrome:4.30.0-20250323

Write-Host "🎥 Starting video recording container..."
& $podmanPath run -d --net grid --name video -v ${PWD}/videos:/videos selenium/video:ffmpeg-7.1.1.1-20250323

Write-Host " Running Maven tests..."
mvn test -Dsuite=local

Write-Host "Stopping and removing containers..."
& $podmanPath stop video
& $podmanPath rm video
& $podmanPath stop selenium
& $podmanPath rm selenium
& $podmanPath network rm grid

Write-Host "Cleanup complete!"
