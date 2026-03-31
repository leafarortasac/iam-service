# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Build da biblioteca de contratos
# Copiamos apenas o necessário para o build do shared
COPY shared-contracts /app/shared-contracts
RUN mvn -f /app/shared-contracts/pom.xml clean install -DskipTests

# 2. Build do IAM Service
WORKDIR /app/iam-service
COPY iam-service/pom.xml .
# Otimização de cache: baixa dependências antes do código fonte
RUN mvn dependency:go-offline

COPY iam-service/src ./src
RUN mvn clean package -DskipTests

# Estágio de Execução (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Configuração de fuso horário para Manaus
RUN apk add --no-cache tzdata
ENV TZ=America/Manaus

# Copia apenas o JAR final (ajustado para evitar conflito com sources.jar)
COPY --from=build /app/iam-service/target/iam-service-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-Duser.timezone=America/Manaus", "-jar", "app.jar"]