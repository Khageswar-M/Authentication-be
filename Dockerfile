# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Speed up by caching deps
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests dependency:go-offline

# Build the app
COPY src src
RUN ./mvnw -DskipTests package

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Spring Boot default port
ENV SERVER_PORT=8080
EXPOSE 8080

# Copy the built jar (wildcard so you never hardcode the name)
COPY --from=build /app/target/*.jar app.jar

# Run
ENTRYPOINT ["java","-jar","/app/app.jar"]
