FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN apk add --no-cache tzdata
ENV TZ=America/Manaus

EXPOSE 8083

ENTRYPOINT ["java", "-Duser.timezone=America/Manaus", "-jar", "app.jar"]