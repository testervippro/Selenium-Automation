# Base image
FROM markhobson/maven-chrome:jdk-17

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the image
COPY . /app

# Run Maven to clean and package the application, skipping tests
RUN mvn clean package -DskipTests

# Copy the built JAR file into a specific location in the container
COPY target/selenium-java-automation-1.0.0.jar /app/selenium-java-automation-1.0.0.jar

# Set the entrypoint to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/selenium-java-automation-1.0.0.jar"]
