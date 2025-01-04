# Define Jenkins URL and download path
$JenkinsURL = "https://get.jenkins.io/war-stable/latest/jenkins.war"
$DownloadPath = Join-Path -Path ([Environment]::GetFolderPath("UserProfile")) -ChildPath "Downloads\jenkins.war"

# Check if the Jenkins WAR file already exists
if (-not (Test-Path $DownloadPath)) {
    # Download Jenkins WAR file
    Invoke-WebRequest -Uri $JenkinsURL -OutFile $DownloadPath -UseBasicParsing
}

# Start Jenkins
Start-Process "java" -ArgumentList "-jar `"$DownloadPath`"

# Wait for Jenkins to start and open the setup page
$JenkinsReady = $false
$MaxWaitTime = 300  # Maximum wait time in seconds (5 minutes)
$StartTime = (Get-Date).Ticks

Do {
    Start-Sleep -Seconds 10  # Check every 10 seconds
    $JenkinsReady = (Test-NetConnection -ComputerName localhost -Port 8080 -ErrorAction SilentlyContinue)

    $CurrentTime = (Get-Date).Ticks
    $ElapsedTime = [math]::Floor(($CurrentTime - $StartTime) / [timespan]::FromSeconds(1).Ticks)
} until ($JenkinsReady -or $ElapsedTime -ge $MaxWaitTime)

if ($JenkinsReady) {
    Start-Process -FilePath "http://localhost:8080"
} else {
    Write-Host "Jenkins did not start within the expected time frame."
}

# Delete WAR file if necessary
# rmdir /s /q "%USERPROFILE%\.jenkins"
