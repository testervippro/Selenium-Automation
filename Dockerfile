FROM cuxuanthoai/chrome-firefox-edge
#FROM markhobson/maven-chrome:jdk-17

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project directory to the container
COPY . .
#
## Ensure chromedriver is placed at the expected path
RUN mkdir -p /app/chromedriver && \
 cp ./src/main/java/com/thoaikx/driver/chromedriver/chromedriver /app/chromedriver/chromedriver
#

USER root
# Ensure chromedriver has execute permissions
RUN chmod +x /app/chromedriver
# Set the default command to execute Selenium tests
CMD ["./mvnw", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=chrome"]
