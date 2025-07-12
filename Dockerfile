FROM openjdk:21-jdk-slim

LABEL maintainer="CulturalVault Team"
LABEL description="AI-Powered Cultural Heritage Platform"

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "target/cultural-vault-0.0.1-SNAPSHOT.jar"]
