
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
10. **Cross-Platform Execution**: Compatible with macOS, Windows, Linux, and containers.
11. **Page Object Model (POM)**: Promotes scalable and maintainable test code.
12. **Factory Method Design Pattern**: Enables flexible creation of platform and page instances.

---

# **Execution Types**

## Local Execution  
- **Local Machine**:  
  Run tests directly in your IDE.  
  - Uses `WebDriverManager` to set up the browser.  
  - The `createLocalDriver()` method in `BrowserFactory` handles browser instances.

---

## Local Suite  
- Uses `WebDriverManager` to set up the browser.  
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
      mvn test -Pweb-execution -Dsuite=parallel
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
mvn test -Pweb-execution -Dsuite=selenium-grid -Dtarget=selenium-grid -Dheadless=true
```

### Key Notes:
- The `createRemoteInstance` method in `DriverFactory` handles `RemoteWebDriver`.
- Check `grid.properties` for correct grid configuration.
- Verify `selenium-grid.xml` for `parallel="tests"` configuration.

---

# **Selenium Grid with Dynamic Video Recording**

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
FROM cuxuanthoai/chrome-firefox-edge

WORKDIR /app

COPY . .

CMD ["mvn", "test"]
```

### **Build and Run**
1. **Build Image**:  
   ```bash
   docker build -t selenium-tests .
   ```

2. **Run Container**:  
   ```bash
   docker run selenium-tests
   ```

---

## **Jenkins with Docker Compose**

### **Step 1: Start Jenkins**
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
1. Enable **2-Step Verification** in your Google account.
2. Generate an **App Password** for Jenkins.

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

### **Step 3: Jenkins Pipeline with Email Notifications**
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
                to: 'recipient@example.com',
                subject: "Build Status - ${currentBuild.currentResult}",
                body: "Build ${env.JOB_NAME} #${env.BUILD_NUMBER} finished with status: ${currentBuild.currentResult}."
            )
        }
    }
}
```

---

## **Ngrok Integration**

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

### **Step 2: Send Messages via Jenkins**
```groovy
pipeline {
    agent any
    environment {
        TELEGRAM_TOKEN = 'your_telegram_token'
        TELEGRAM_CHAT_ID = 'your_chat_id'
    }
    stages {
        stage('Notify Telegram') {
            steps {
                script {
                    sh """
                        curl -X POST https://api.telegram.org/bot$TELEGRAM_TOKEN/sendMessage \
                        -d chat_id=$TELEGRAM_CHAT_ID \
                        -d text="Test execution completed."
                    """
                }
            }
        }
    }
}
```

### **Step 3: Send Files to Telegram**
```groovy
pipeline {
    agent any
    environment {
        TELEGRAM_TOKEN = 'your_telegram_token'
        TELEGRAM_CHAT_ID = 'your_chat_id'
    }
    stages {
        stage('Send File') {
            steps {
                script {
                    sh """
                        curl -X POST https://api.telegram.org/bot$TELEGRAM_TOKEN/sendDocument \
                        -F chat_id=$TELEGRAM_CHAT_ID \
                        -F document=@path_to_file/example.txt
                    """
                }
            }
        }
    }
}
```

---

## **References**
- eliasnogueira(Java Developer)
- [Selenium Docker GitHub Repository](https://github.com/SeleniumHQ/docker-selenium.git)  
- [Ngrok Documentation](https://ngrok.com/docs)  
- [Telegram Bot API](https://core.telegram.org/bots/api)

---
