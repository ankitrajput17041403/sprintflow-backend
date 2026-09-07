# Use Java 17 runtime
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR into the container
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8081

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]