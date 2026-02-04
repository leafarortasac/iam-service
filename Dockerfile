# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o POM e baixa as dependências (Cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio de Execução (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Ajuste no COPY: Usamos um curinga mais específico ou o nome final do JAR
# para evitar copiar múltiplos arquivos se houver mais de um .jar no target
COPY --from=build /app/target/*.jar app.jar

# Configuração de fuso horário (Ótimo para logs precisos no Brasil)
RUN apk add --no-cache tzdata
ENV TZ=America/Manaus

# Porta que você definiu para o IAM no docker-compose
EXPOSE 8080

# Adicionei o parâmetro de memória para evitar que o Java consuma todo o container (Boas práticas)
ENTRYPOINT ["java", "-Xmx512m", "-Duser.timezone=America/Manaus", "-jar", "app.jar"]