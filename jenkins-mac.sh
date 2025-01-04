#!/bin/bash

 # chmod +x jenkins-mac.sh && ./jenkins-mac.sh
 # delete sudo rm -rf /Users/Shared/Jenkins
# Define Jenkins URL and download path
JENKINS_URL="https://get.jenkins.io/war-stable/latest/jenkins.war"
DOWNLOAD_PATH="$HOME/Downloads/jenkins.war"

# Check if the Jenkins WAR file already exists
if [ ! -f "$DOWNLOAD_PATH" ]; then
    echo "Downloading Jenkins..."
    curl -L -o "$DOWNLOAD_PATH" "$JENKINS_URL" --retry 3 --retry-delay 5
    if [ $? -ne 0 ]; then
        echo "Failed to download Jenkins. Please check your network connection."
        exit 1
    fi
else
    echo "Jenkins WAR file already exists at $DOWNLOAD_PATH."
fi

# Start Jenkins with admin credentials
echo "Starting Jenkins..."
java -jar "$DOWNLOAD_PATH"

# Wait for Jenkins to start and open the setup page
JENKINS_READY=false
MAX_WAIT_TIME=300  # Maximum wait time in seconds (5 minutes)
START_TIME=$(date +%s)

while [ "$JENKINS_READY" = false ] && [ $(( $(date +%s) - START_TIME )) -lt $MAX_WAIT_TIME ]; do
    echo "Waiting for Jenkins to start..."
    sleep 10  # Check every 10 seconds

    # Check if Jenkins is running on port 8080
    if nc -z localhost 8080; then
        JENKINS_READY=true
    fi
done

if [ "$JENKINS_READY" = true ]; then
    echo "Jenkins is running. Opening setup page in the default browser..."
    open "http://localhost:8080"
else
    echo "Jenkins did not start within the expected time frame."
    exit 1
fi

# Delete WAR file if necessary
# Uncomment the following line to delete the Jenkins WAR file after setup
# rm -f "$DOWNLOAD_PATH"