FROM cuxuanthoai/chrome-firefox-edge:v1.2

# Set the working directory inside the container
WORKDIR /app
COPY . .
#

#CMD ["./mvnw", "test", "-Pweb-execution", "-Dsuite=local-suite", "-Dtarget=local-suite", "-Dheadless=true", "-Dbrowser=chrome"]