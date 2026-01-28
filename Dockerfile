FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy Maven wrapper first (better layer caching)
COPY mvnw .
RUN chmod +x mvnw
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (offline cache)
RUN ./mvnw dependency:go-offline -B

# Copy source
COPY src src

# Build JAR
RUN ./mvnw clean package -DskipTests -B

EXPOSE 8080

# Render sets PORT env var
CMD ["sh", "-c", "java -jar target/HomeAgent-0.0.1-SNAPSHOT.jar"]
