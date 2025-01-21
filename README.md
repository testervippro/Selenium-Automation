

---

### Set Up Jenkins
# Jenkins Installation Guide

Downloading and running Jenkins using a PowerShell script.

## Prerequisites
Before you begin, ensure that you have:
- Java installed on your machine.
- An internet connection to download the Jenkins WAR file.

## Step 1: Download Jenkins WAR file

Use the following PowerShell script to download the latest stable version of Jenkins:

```bash
docker run -it --name=jenkins -e JENKINS_USER=$(id -u) --rm -p 8080:8080 -p 50000:50000 \
-v ./jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock \
--name jenkins trion/jenkins-docker-client
```
## Run to setup jekin ( then open http://localhost:8080)
```bash

java -jar jenkins.war
```

![Set Up](https://github.com/user-attachments/assets/e12310e6-19b2-45ce-8b71-027ffabc291e)

---


### Set Up Git
![Set Up Git](https://github.com/user-attachments/assets/4b4887ab-d9b2-413b-8abd-1eb115fa2729)

---

```markdown
# Setting Up Jenkins with Docker and JCasC

This guide explains how to set up a Jenkins instance using Docker with Jenkins Configuration as Code (JCasC) for automatic configuration management.

## 1. Dockerfile Setup

The Dockerfile provided below sets up Jenkins with the necessary plugins and configurations using JCasC:

```Dockerfile
FROM jenkins/jenkins:lts-jdk17

# Install Jenkins plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Disable the setup wizard as we will set up Jenkins as code
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Copy the Configuration as Code (CasC) YAML file into the image
COPY jenkins-casc.yaml /var/jenkins_home/casc_configs/jenkins.yaml

# Tell the Jenkins Configuration as Code plugin where to find the YAML file
ENV CASC_JENKINS_CONFIG="/var/jenkins_home/casc_configs/jenkins.yaml"
```

- **Base Image**: The `jenkins/jenkins:lts-jdk17` image is used to ensure a stable Jenkins environment with JDK 17.
- **Plugin Installation**: The `jenkins-plugin-cli` installs plugins specified in `plugins.txt`.
- **Disabling Setup Wizard**: The `JAVA_OPTS` environment variable disables the setup wizard, as we are configuring Jenkins through JCasC.
- **JCasC Configuration**: The `jenkins-casc.yaml` file is copied into the Docker image, and the `CASC_JENKINS_CONFIG` environment variable points to this configuration file for Jenkins to apply settings at startup.

## 2. Specifying Plugins

To ensure Jenkins has the necessary plugins for its operations, create a `plugins.txt` file. This file lists the required plugins and their versions:

```txt
git:5.0.0
workflow-aggregator:600.vb_57cdd26fdd7
configuration-as-code:1810.v9b_c30a_249a_4c
matrix-auth:3.2.2
job-dsl:1.87
.....
```

Each line in `plugins.txt` specifies a plugin. You can include the version after a colon (e.g., `git:5.0.0`) to ensure a specific version is installed. If you omit the version (e.g., `git`), the latest version will be installed during the image build.

The `configuration-as-code` plugin is essential for JCasC functionality, while `matrix-auth` helps manage access control in Jenkins. The `job-dsl` plugin allows defining Jenkins jobs using Groovy DSL scripts.

## 3. Configuring Jenkins with JCasC

Create a `jenkins-casc.yaml` file to configure Jenkins. This file defines user authentication, tool installations, and a sample job.

### Example `jenkins-casc.yaml`:

```yaml
jenkins:
  systemMessage: "Jenkins configured automatically by Jenkins Configuration as Code plugin"

  # Security Realm for user authentication
  securityRealm:
    local:
      allowsSignup: false
      users:
        - id: "admin"
          password: "admin"
        - id: "developer"
          password: "developer"
        - id: "viewer"
          password: "viewer"

  # Authorization Strategy
  authorizationStrategy:
    projectMatrix:
      entries:
        - user:
            name: admin
            permissions:
              - Overall/Administer
        - user:
            name: developer
            permissions:
              - Overall/Read
              - Job/Build
        - user:
            name: viewer
            permissions:
              - Overall/Read

# Tool Configuration
tool:
  git:
    installations:
      - name: "Default"
        home: "/usr/bin/git"
  maven:
    installations:
      - name: maven3
        properties:
          - installSource:
              installers:
                - maven:
                    id: "3.8.4"

# Sample Job Configuration
jobs:
  - script: >
      pipelineJob('example-pipeline-job') {
      definition {
        cps {
            script('''
                pipeline {
                    agent any
                    stages {
                        stage('Build') {
                            steps {
                                echo 'Building...'
                            }
                        }
                        stage('Test') {
                            steps {
                                echo 'Testing...'
                            }
                        }
                        stage('Deploy') {
                            steps {
                                echo 'Deploying...'
                            }
                        }
                    }
                }
            ''')
            }
          }
        } 
```

### Key Sections:
- **Security Realm**: Configures local authentication with users `admin`, `developer`, and `viewer` and disables signup.
- **Authorization Strategy**: Uses the Project-based Matrix Authorization Strategy for granular permission control:
    - Admin has full control (`Overall/Administer`).
    - Developer can read and build jobs (`Overall/Read`, `Job/Build`).
    - Viewer has read-only access (`Overall/Read`).
- **Tool Configuration**: Specifies paths for `git` and `maven` tools that Jenkins will use.
- **Sample Job**: Defines a simple pipeline job with stages for Build, Test, and Deploy.

## 4. Build and Run the Docker Container

After setting up your Dockerfile and configuration files, build and run the Docker container:

```bash
# Build the Docker image
docker build -t jenkins-jcasc .

# Run the Docker container (window)
docker run --name jenkins -p 8080:8080 -v C:\Dowloads:/var/jenkins_home jenkins-jcasc
```

### Access Jenkins:
Once the container is running, you can access Jenkins at `http://localhost:8080` and log in using one of the following credentials:
- **Admin**: `admin` / `admin`
- **Developer**: `developer` / `developer`
- **Viewer**: `viewer` / `viewer`

### Login Page:
You will see the system message we defined earlier ("Jenkins configured automatically by Jenkins Configuration as Code plugin") and the sample pipeline.

