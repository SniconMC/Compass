# Use a minimal base image with Java
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the server JAR into the container
COPY ./Compass-0.1.jar /app/server.jar

# Expose the port
EXPOSE 25565

# Start the server with dynamic port configuration
CMD ["java", "-jar", "/app/server.jar", "--port", "${SERVER_PORT:-25565}"]
