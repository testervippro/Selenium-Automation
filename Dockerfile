FROM cuxuanthoai/chrome-firefox-edge

# Switch to root user to ensure permissions are not restricted
USER root

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project directory to the container
COPY . .
#
## Ensure chromedriver is placed at the expected path
#RUN mkdir -p /app/chromedriver && \
#    cp ./src/main/java/com/thoaikx/driver/chromedriver/chromedriver /app/chromedriver/chromedriver
#
## Grant execution permissions for chromedriver
#RUN chmod +x /app/chromedriver/chromedriver

# Optional: Install any additional dependencies if required
# RUN apt-get update && apt-get install -y <additional-packages>

# Expose any required ports (if needed for debugging or reporting)
# EXPOSE 8080

# Set the default command to execute Selenium tests
CMD ["./mvnw", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=firefox"]
