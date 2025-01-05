# Define Jenkins URL and download path
$JenkinsURL = "https://get.jenkins.io/war-stable/latest/jenkins.war"
$DownloadPath = Join-Path -Path ([Environment]::GetFolderPath("UserProfile")) -ChildPath "Downloads\jenkins.war"

# Check if the Jenkins WAR file already exists
if (-not (Test-Path -Path $DownloadPath)) {
    try {
        # Download Jenkins WAR file
        Write-Host "Downloading Jenkins WAR file..."
        Invoke-WebRequest -Uri $JenkinsURL -OutFile $DownloadPath -UseBasicParsing -ErrorAction Stop
        Write-Host "Download completed successfully."
    } catch {
        Write-Error "Failed to download Jenkins WAR file: $_"
        exit 1
    }
} else {
    Write-Host "Jenkins WAR file already exists. Skipping download."
}

# Start Jenkins
try {
    Write-Host "Starting Jenkins..."
    Start-Process -FilePath "java" -ArgumentList "-jar `"$DownloadPath`"" -NoNewWindow
    Write-Host "Jenkins started successfully."
} catch {
    Write-Error "Failed to start Jenkins: $_"
    exit 1
}
