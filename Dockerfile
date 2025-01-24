FROM cuxuanthoai/chrome-firefox-edge
#FROM mcr.microsoft.com/msedge/msedgedriver

# Set the working directory inside the container
WORKDIR /app

COPY . .
#
# Create the directory and copy msedgedriver
RUN mkdir -p /app/msedgedriver && \
    cp ./src/main/java/com/thoaikx/driver/msedgedriver/msedgedriver /app/msedgedriver/msedgedriver && \
    chmod +x /app/msedgedriver/msedgedriver

# Set the path to msedgedriver
ENV PATH="/app/msedgedriver:${PATH}"
# Set the default command to execute Selenium tests
CMD ["mvn", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=edge"]