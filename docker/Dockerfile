# Use a base image with OpenJDK installed
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Minestom JAR file into the container
COPY ./Compass-0.1.jar ./server.jar

# Expose the default Minecraft port
EXPOSE 25565

# Start the Minestom server on the internal port 25565
CMD ["java", "-jar", "server.jar"]
