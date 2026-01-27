FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy Maven wrapper FIRST and make executable ✅ FIXED
COPY mvnw .
RUN chmod +x mvnw
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build executable JAR
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Use EXACT JAR filename ✅ FIXED
CMD ["java", "-jar", "target/HomeAgent-0.0.1-SNAPSHOT.jar"]
