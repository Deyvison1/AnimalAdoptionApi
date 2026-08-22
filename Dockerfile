# Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY settings.xml /root/.m2/settings.xml
COPY . .

RUN mvn clean package -DskipTests

# Runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/target/animaladoption-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]