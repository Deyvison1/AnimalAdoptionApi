# Etapa 1 - Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2 - Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# 👉 copia o certificado do Keycloak
COPY keycloak.crt /tmp/keycloak.crt

# 👉 importa no truststore do Java
RUN keytool -importcert \
  -alias keycloak \
  -file /tmp/keycloak.crt \
  -keystore $JAVA_HOME/lib/security/cacerts \
  -storepass changeit \
  -noprompt

# copia o JAR
COPY --from=builder /app/target/animaladoption-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
