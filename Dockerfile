# Use selenium/standalone-firefox as base image
FROM selenium/standalone-firefox:91.0-20210823

# Set the working directory inside the container
WORKDIR /opt/selenium

# Install OpenJDK 11 (or another version of Java if necessary)

# Copy the current directory (all files) into /opt/selenium in the container
COPY . /opt/selenium


# Now run the tests via Maven Wrapper (this will download Maven and use it)
CMD ["./mvnw", "test"]
