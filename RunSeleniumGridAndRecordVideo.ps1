# Create a Docker network
docker network create grid

# Run Selenium Standalone Chrome container
docker run -d -p 4444:4444 -p 6900:5900 --net grid --name selenium --shm-size="2g" selenium/standalone-chrome:4.30.0-20250323

# Run video recording container
docker run -d --net grid --name video -v ./videos:/videos selenium/video:ffmpeg-7.1.1.1-20250323

# Run your tests...

mvn test -Dsuite=local

# Cleanup: Stop and remove containers and network
docker stop video ; docker rm video
docker stop selenium ; docker rm selenium
docker network rm grid

# run this cmd powershell -ep Bypass -f RunSeleniumGridAndRecordVideo.ps1
