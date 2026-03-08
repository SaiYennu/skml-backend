# Java 17 base image
FROM eclipse-temurin:17-jdk

# App work directory
WORKDIR /app

# Copy all project files
COPY . .

# 🔥 FIX: give execute permission
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose Spring Boot port
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/skml-backend-0.0.1-SNAPSHOT.jar"]
