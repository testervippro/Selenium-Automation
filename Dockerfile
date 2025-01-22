
# Base image
FROM cuxuanthoai/chrome-firefox-edge

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project, including Maven wrapper files, configuration files, and source code
COPY . /app
## Run Maven to clean and package the application, skipping tests
RUN mvn clean package -DskipTests
#
## Copy the built JAR file into a specific location in the container
COPY target/selenium-java-automation-1.0.0.jar /app/selenium-java-automation-1.0.0.jar

# Set the entrypoint to run Selenium tests
CMD ["mvn", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=chrome"]
