FROM cuxuanthoai/chrome-firefox-edge
#FROM markhobson/maven-chrome:jdk-17

# Set the working directory inside the container
WORKDIR /app
COPY . .
#

#CMD ["./mvnw", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=chrome"]