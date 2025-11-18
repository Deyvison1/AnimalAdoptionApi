# Etapa 1 - Build
FROM maven:3.9.6-eclipse-temurin-23 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2 - Runtime
FROM eclipse-temurin:23-jdk
WORKDIR /app

# Copia o JAR gerado da etapa de build e renomeia para app.jar
COPY --from=builder /app/target/animaladoption-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
