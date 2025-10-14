# Etapa 1: Build usando Java 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar archivos de configuración primero (para cache de Docker)
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Crear directorio de la aplicación
WORKDIR /deployments

# Copiar la aplicación compilada
COPY --from=build /app/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/target/quarkus-app/*.jar ./
COPY --from=build /app/target/quarkus-app/app/ ./app/
COPY --from=build /app/target/quarkus-app/quarkus/ ./quarkus/

# Exponer puerto
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Dquarkus.profile=docker"

# Comando de inicio
CMD ["java", "-jar", "quarkus-run.jar"]