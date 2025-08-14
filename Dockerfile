# Usa a imagem oficial do OpenJDK 17 como base
FROM eclipse-temurin:17-jdk-jammy

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o jar gerado para dentro do container
COPY target/acme-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot vai usar (exemplo 8080)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
