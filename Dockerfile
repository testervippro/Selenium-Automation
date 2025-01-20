# Use Ubuntu base image
FROM ubuntu:20.04

# Set the working directory inside the container
WORKDIR /opt/selenium

# Set non-interactive mode to avoid prompts during package installations
ENV DEBIAN_FRONTEND=noninteractive

# Install dependencies including OpenJDK 17, wget, curl, and other tools
RUN apt-get update && \
    apt-get install -y wget curl gnupg2 ca-certificates lsb-release unzip && \
    apt-get install -y openjdk-17-jdk && \
    apt-get install -y libx11-xcb1 libxcomposite1 libxdamage1 libfontconfig1 libgtk-3-0 libnss3 libasound2 fonts-liberation libappindicator3-1 xdg-utils && \
    rm -rf /var/lib/apt/lists/*



# Copy the selenium-java-automation-1.0.0.jar from the target folder to the container
COPY target/selenium-java-automation-1.0.0.jar /opt/selenium/selenium-java-automation-1.0.0.jar

# Copy the current directory (all files) into /opt/selenium in the container
COPY . /opt/selenium

# Ensure the Maven Wrapper (mvnw) is executable



