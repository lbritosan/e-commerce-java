# Passo 1: Construir a aplicação usando o Java 21 (Atualizado)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copia os arquivos do projeto para dentro do container
COPY pom.xml .
COPY src ./src
# Compila e gera o arquivo .jar limpando os testes temporários
RUN mvn clean package -DskipTests

# Passo 2: Executar a aplicação usando o Java 21 Runtime (Atualizado)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia o arquivo .jar gerado no passo anterior para esta imagem limpa
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta padrão do Spring Boot
EXPOSE 8080
# Comando para rodar a API
ENTRYPOINT ["java", "-jar", "app.jar"]
