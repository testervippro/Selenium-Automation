# Base image
FROM cuxuanthoai/chrome-firefox-edge

# Set the working directory inside the container
WORKDIR /app
# Copy the entire source code (including src, target, etc.) into the container
COPY . /app/
# Create a directory to store Maven dependencies (this will be cached)
RUN mkdir -p /app/.m2/repository

# Set the environment variable to specify the location of the Maven repository
ENV MAVEN_OPTS="-Dmaven.repo.local=/app/.m2/repository"
# Copy the Maven wrapper files, configuration files, and pom.xml to leverage Docker caching
COPY mvnw mvnw.cmd .mvn/ pom.xml /app/

# Make the Maven wrapper executable
RUN chmod +x ./mvnw
# Download dependencies to leverage Docker cache
RUN ./mvnw dependency:go-offline




# Set the default command to run Maven tests with specific profile and configurations
CMD ["./mvnw", "clean", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=chrome"]
