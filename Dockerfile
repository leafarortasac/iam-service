# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Build da biblioteca de contratos (Shared Contracts)
# Ajustamos o caminho de acordo com a estrutura da sua pasta raiz
COPY shared-contracts /app/shared-contracts
WORKDIR /app/shared-contracts
RUN mvn clean install -DskipTests

# 2. Build do IAM Service
WORKDIR /app/iam-service
COPY iam-service/pom.xml .
RUN mvn dependency:go-offline

COPY iam-service/src ./src
RUN mvn clean package -DskipTests

# Estágio de Execução (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR gerado no estágio de build
COPY --from=build /app/iam-service/target/*.jar app.jar

# Configuração de fuso horário para Manaus
RUN apk add --no-cache tzdata
ENV TZ=America/Manaus

EXPOSE 8080

# Parâmetros de memória e timezone para o Java 21
ENTRYPOINT ["java", "-Xmx512m", "-Duser.timezone=America/Manaus", "-jar", "app.jar"]