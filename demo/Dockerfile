# demo/Dockerfile (con JDK 25 explícito)
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Instalar Maven dentro del contenedor
RUN apt-get update && apt-get install -y maven

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]