# Etapa 1: Build da aplicação com Maven
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o pom e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código
COPY src ./src

# Compila e empacota a aplicação
RUN mvn clean package -DskipTests -Pdocker

# Etapa 2: Imagem final leve para rodar o app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o jar do build anterior
COPY --from=build /app/target/acme-app.jar app.jar

# Define variáveis de ambiente para profile Docker
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Expõe porta
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
