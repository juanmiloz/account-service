# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /build

# Copiamos TODO el proyecto (pom padre, módulos, .mvn, etc)
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn/
COPY domain domain/
COPY infrastructure infrastructure/
COPY applications applications/

# Ahora compilamos desde la raíz, incluyendo todos los módulos
RUN mvn -B clean package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/applications/app-service/target/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 8002
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]