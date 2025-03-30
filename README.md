
---

# **Automation Testing Framework Features**

### **Key Features**

1. **Parallel Execution**: Leverage TestNG and Selenium Grid.
2. **Centralized Configuration**: Manage from a properties file.
3. **Detailed Reporting**: Generate reports with Allure.
4. **Automated Email Notifications**: Set up via Jenkins.
5. **Video & Screenshot Recording**: Works on Selenium Grid and locally.
6. **Data-Driven Testing**: Utilize Excel, JSON, etc.
7. **Telegram Bot Integration**: Get real-time updates.
8. **Dynamic Selenium Grid Scaling**: Automatically scales nodes.
9. **Ngrok Integration**: Expose local ports to the internet.
10. **Cross-Platform Execution**: Compatible with macOS, Windows, Linux, and containers(future  will suport record  in  container).
11. **Page Object Model (POM)**: Promotes scalable and maintainable test code.
12. **Factory Method Design Pattern**: Enables flexible creation of platform and page instances.
13. **Jenkins as code**: Config Jenkins use Jenkins container have pre-install tools, user
---

# **Execution Types**

## Local Execution  
- **Local Machine**:  
  Run tests directly in your IDE.   
  - The `createLocalDriver()` method in `BrowserFactory` handles browser instances.

---

## Local Suite  
- Browser info is specified in the TestNG suite file for multi-browser tests.  

### Profiles in `pom.xml` for Local Suite Execution  
- A profile called `web-execution` is created to execute a specific test suite (e.g., `local.xml`) in the `src/test/resources/suites` folder.  
- **Command Example**:  
  ```bash
  mvn test -Pweb-execution -Dtestng.dtd.http=true
  ```
- **Parameterizing Suites**:
    - Create a `suite` property in the `pom.xml`:
      ```xml
      <properties>
          <suite>local</suite>
      </properties>
      ```
    - Update the profile to use `${suite}`:
      ```xml
      <profile>
          <id>web-execution</id>
          <configuration>
              <suiteXmlFiles>
                  <suiteXmlFile>src/test/resources/suites/${suite}.xml</suiteXmlFile>
              </suiteXmlFiles>
          </configuration>
      </profile>
      ```
    - Use the `-Dsuite=suite_name` parameter to run a specific suite:
      ```bash
      mvn test -Dsuite=parallel
      ```

---

## Remote Execution

### Selenium Grid
- Executes tests on remote or cloud machines.
- The `getOptions` method in `BrowserFactory` provides browser capabilities.
- Requires setting `grid.url` and `grid.port` in `config.properties`.

---

## Execution with Docker Selenium
- Use `docker-compose.yml` for parallel test execution.

### Steps:
1. Install Docker.
2. Run `docker-compose-v3-dynamic-grid.yml` or `docker-compose-v3-dynamic-grid-standalone.yml` to set up the dynamic grid (it will auto-pull images from `config.toml`).
3. On Mac M1/M2, enable Rosetta in Docker Desktop:  
   `Settings -> Features in development -> Use Rosetta`.

### Run Command:
```bash
mvn test-Dsuite=selenium-grid -Dtarget=selenium-grid -Dheadless=true -Dgrid.video=true 
```

### Key Notes:
- The `createRemoteInstance` method in `DriverFactory` handles `RemoteWebDriver`.
- Check `grid.properties` for correct grid configuration.
- Verify `selenium-grid.xml` for `parallel="tests"` configuration.

---

# **Selenium Grid with Dynamic Video Recording (set grid.video= true)**

### **Step 1: Set Up Docker Containers**

1. Use the provided Docker Compose file to start Selenium Grid with dynamic nodes and video recording.
   ```bash
   docker-compose -f docker-compose-v3-dynamic-grid.yml up -d
   ```

2. Ensure your `docker-compose.yml` includes configurations for video recording.

---

### **Step 2: Enable Video Recording**

1. Add the `se:recordVideo` capability in your WebDriver options.
2. Specify the screen resolution if required.

#### Example (Java with Chrome):
```java
ChromeOptions options = new ChromeOptions();
options.setCapability("se:recordVideo", true);
options.setCapability("se:screenResolution", "1920x1080");
options.setCapability("se:name", "test_case_name");

WebDriver driver = new RemoteWebDriver(new URL("http://selenium-grid-url"), options);
```

---

### **Step 3: Access Video Files**

1. Videos are stored in the `${PWD}/assets/<sessionId>/` directory.
2. Files follow this format:  
   `test_case_name_<browserName>.mp4`.

---

## **Run Tests Inside a Docker Container**

### **Dockerfile Example**
```dockerfile
FROM cuxuanthoai/chrome-firefox-edge:v1.2

WORKDIR /app

COPY . .
// can change base on your test case
CMD ["mvn", "test"]
```

# Chrome Crashes in Docker

Chrome uses `/dev/shm` (shared memory) for runtime data, which is 64MB by default under Docker. If this memory is insufficient, it can cause Chrome to crash.

## Possible Workarounds

1. **Increase the size of `/dev/shm`**
    - Run your Docker container with the `--shm-size` flag to increase the shared memory size.
      ```bash
      docker run --shm-size=1g <image>
      ```

2. **Mount `/dev/shm` to the host's shared memory**
    - Use a bind mount to link the container’s `/dev/shm` to the host’s shared memory.
      ```bash
      docker run -v /dev/shm:/dev/shm <image>
      ```

3. **Start Chrome with the `--disable-dev-shm-usage` flag**
    - Disable Chrome's use of `/dev/shm` by adding this flag to your Chrome launch options.
    - Example usage in Selenium (Java):
      ```java
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--disable-dev-shm-usage");
      WebDriver driver = new ChromeDriver(options);
      ```


### **Build and Run**
1. **Build Image**:  
   ```bash
   docker build -t selenium-tests .
   ```

2. **Run Container(with chrome brower need shm_size: '2gb' or can run docker-compose-chrome-firefox-edge.yml it will auto build and run base on Dockerfile)**:  
   ```bash
   docker run selenium-tests
   ```

---

## **Jenkins as Code  with Docker Compose**
 Docker image `cuxuanthoai/jenkins-docker:v1.2`, which comes with pre-installed necessary tools to run UI automation.

### **Step 1: Start Jenkins (in jenkins folder)**
Run Jenkins with pre-configured tools and plugins:
```bash
docker-compose -f docker-compose-jenkins-as-code.yml up -d
```

### **Step 2: Log in to Jenkins**
- Default users (`admin`, `viewer`, `developer`) are created with passwords from the configuration file.
- Tools like Maven, Git, Node.js, and Allure are pre-installed.

---

## **Set Up Email Notifications in Jenkins**

### **Step 1: Google SMTP Configuration**
1. **Enable 2-Step Verification**:
   - Go to [Google Account Security](https://myaccount.google.com/security).
   - Turn **2-Step Verification** **ON**.

2. **Generate an App Password**:
   - Go to [App Passwords](https://myaccount.google.com/apppasswords).
   - Choose **Mail** as the app and **Other** for the device (e.g., "Jenkins").
   - Click **Generate** and copy the generated password.

### **Step 2: Configure Jenkins**
1. Go to **Manage Jenkins > Configure System**.
2. Fill in the **Extended E-mail Notification** section:
   - SMTP Server: `smtp.gmail.com`
   - Port: `465`
   - Use SSL: Enabled
   - Authentication: Enabled
   - Username: Your Gmail.
   - Password: App Password.
3. Save the configuration.

---

### **Step 3: Jenkins Pipeline with Email Notifications(gmaill not allow send some file it's related to security problem)**
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }
    }
    post {
        always {
emailext(
        to: EMAIL_RECIPIENT,
        subject: emailSubject,
        body: emailBody,
        mimeType: 'text/html',
        attachLog: true, // Attach the build log file
        //compressLog: true // Compress the build log before attaching zip 
        attachmentsPattern: '**/build.log'
        
    )
        }
    }
}
```

---


## **Ngrok Integration ( shoule create user in jenkins just have view permission)**

### **Step 1: Start Ngrok**
Run Ngrok to expose your local port:
```bash
docker run --net=host -it -e NGROK_AUTHTOKEN=your_ngrok_authtoken -p 8080:8080 ngrok/ngrok http 8080
```

Replace `your_ngrok_authtoken` with your Ngrok token. After starting, Ngrok provides a public URL to access your local server.

---

## **Telegram Bot Integration**

### **Step 1: Create Telegram Bot**
1. Use [BotFather](https://core.telegram.org/bots#botfather) to create a bot and get the token.
2. Use `https://api.telegram.org/bot<TOKEN>/getUpdates` to find your chat ID.

---

### **Step 2: Send Messages via Jenkins (can combine send zip file, becaue gmaill not allow send some file it's related to security problem) **
```groovy
pipeline {
    agent any

    environment {
        JENKINS_SERVER_URL = 'https://96-46-98.ngrok-free.app'
        EMAIL_RECIPIENT = '@gmail.com'
        TELEGRAM_TOKEN = ':AAEAJ5GDawMGUC4Ofv9SvD3YBn5UGmVii7Q'
        TELEGRAM_CHAT_ID = '5321745388'
    }

    stages {
        stage('Test Execution') {
            steps {
                script {
                    echo "Executing Test Stage..."
                }
            }
        }
    }

    post {
        always {
            script {
                // Zip and send Allure report via Telegram
                sendTelegramReport()
            }
        }

        success {
            echo 'Tests completed successfully!'
        }

        failure {
            echo 'Tests failed!'
        }
    }
}


// Function to zip and send the Allure report via Telegram
def sendTelegramReport() {
    echo "Zipping Allure report..."

    // Compress the Allure report directory into a zip file
    sh """
        zip -r allure-report.zip ./allure-report
    """
    echo "Allure report zipped successfully."

    echo "Sending Telegram report..."

    def caption = "Allure Report for Jenkins Build #${env.BUILD_NUMBER}"

    // Use Telegram API to send the zip file
    sh """
        curl -X POST \
        -F chat_id=${TELEGRAM_CHAT_ID} \
        -F document=@allure-report.zip \
        -F caption=\"${caption}\" \
        https://api.telegram.org/bot${TELEGRAM_TOKEN}/sendDocument
    """
    echo "Telegram report sent successfully."
}
```
Example when integrate with Ngrok
Sent mail 
![image](https://github.com/user-attachments/assets/c8e90d05-df8f-4c1e-aa8a-58b124cd527e)

Report allure 
![image](https://github.com/user-attachments/assets/00289fd7-c58b-4b39-af2d-923d73d9f91a)

Detail build
![image](https://github.com/user-attachments/assets/11e25eec-27bf-4c84-beb0-b83b4999d6f0)

Send zip report to telegram 
![image](https://github.com/user-attachments/assets/51fba5dd-3162-4b8e-ab62-5f6b511b66b9)

Run inside container 

Run cmd
 ```bash mvn test -Dtest=TestLabNG```

![image](https://github.com/user-attachments/assets/b51ef04e-a881-4fe1-9e6b-fd3dd61e64d4)



# **Java Class to Download `jenkins.war` and `selenium-server.jar`**

This Java class (`App`) provides a simple way to download the `jenkins.war` and `selenium-server.jar` files and store them in the `lib` folder.


## **References**
- eliasnogueira(Java Developer)
- [Selenium Docker GitHub Repository](https://github.com/SeleniumHQ/docker-selenium.git)  
- [Ngrok Documentation](https://ngrok.com/docs)  
- [Telegram Bot API](https://core.telegram.org/bots/api)
- https://github.com/SeleniumHQ/seleniumhq.github.io/pull/2139
- https://support.google.com/mail/thread/183285153/i-m-trying-to-send-an-attachment-and-i-m-unable-to-send-saying-it-was-blocked-for-a-security-issue?hl=en
- https://stackoverflow.com/questions/35783964/jenkins-html-publisher-plugin-no-css-is-displayed-when-report-is-viewed-in-j/35785788#35785788
- https://github.com/SeleniumHQ/selenium/issues/13077
- https://issues.chromium.org/issues/40432240
- https://github.com/mozilla/geckodriver/issues/2199


---
