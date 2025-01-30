
## **Selenium Grid with Dynamic Video Recording and Run test inside container**

### **Step 1: Set Up Docker Containers with Selenium Grid**

To run Selenium Grid dynamically with video recording, you will need to use the Docker Compose files provided for this purpose.

1. **Execute Docker Compose for Dynamic Grid:**

   Run the following command to start your dynamic grid using the `docker-compose-v3-dynamic-grid.yml` or `docker-compose-v3-dynamic-grid-standalone.yml`:

   ```bash
   docker-compose -f docker-compose-v3-dynamic-grid.yml up -d
   ```

   This will start the Selenium Grid along with dynamic nodes, and video recording capabilities.

---

### **Step 2: Enable Video Recording for WebDriver Sessions**

To record the video during WebDriver execution, you need to add the `se:recordVideo` capability in the desired WebDriver options. You can also specify screen resolution.



### **Step 3: Set Video Recording in Language Bindings**

In addition to the `se:recordVideo` capability, you can set the video file name dynamically using the `se:name` capability. Here's how you can do it in Java:

**Example in Java (with Chrome):**

```java
        // Set up Chrome options for video recording and screen resolution
        ChromeOptions options = new ChromeOptions();
        options.setCapability("se:recordVideo", true);
        options.setCapability("se:screenResolution", "1920x1080");
        options.setCapability("se:name", "test_visit_basic_auth_secured_page");

        // Define Selenium Grid URL
```

This script does the following:

1. Configures the WebDriver to record video.
2. Sets the screen resolution to `1920x1080`.
3. Specifies a custom test name for the video file.

After executing the test, you will find the video file stored under `(${PWD}/assets)` directory.

---

### **Step 4: Find the Video File**

Once the test completes, the video file will be saved in the following format:

```bash
<sessionId>/test_visit_basic_auth_secured_page_ChromeTests.mp4
```

- The session ID will dynamically generate for each test run.
- The video file name is trimmed to a maximum of 255 characters.
- Spaces in the file name are replaced with underscores (`_`), and only alphanumeric characters, hyphens (`-`), and underscores (`_`) are allowed.

The video and session information will be saved under the path:  
`${PWD}/assets/<sessionId>/`

---

### **Step 5: View Videos and Session Information**

To view the video files after the test run:

1. Go to the folder `(${PWD}/assets/<sessionId>/)`.
2. You will see a `.mp4` video file for the test.
3. The video will show your test run, and you can use it for debugging purposes.

For more information, you can visit the official [Selenium Docker GitHub repository](https://github.com/SeleniumHQ/docker-selenium.git).

---


To run your Selenium tests inside a container, you can use a custom Dockerfile. Below is a sample Dockerfile that uses the `cuxuanthoai/chrome-firefox-edge` Docker image (which already includes the necessary drivers for Chrome, Firefox, and Edge,Maven).

**Dockerfile:**

```dockerfile
# Use a custom image with Chrome, Firefox, and Edge drivers installed
FROM cuxuanthoai/chrome-firefox-edge
# Set the working directory inside the container
WORKDIR /app
# Copy your project files into the container
COPY . .
CMD ["mvn", "test"]  
```

### **Build and Run the Docker Image:**

1. **Build the Docker Image:**

   In the directory where your Dockerfile is located, run:

   ```bash
   docker build -t selenium-tests .
   ```

2. **Run the Docker Container:**

After building the Docker image, you can run the tests inside the container:


