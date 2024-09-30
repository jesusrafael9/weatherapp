# Etapa 1: Construcción
FROM gradle:7.5.1-jdk11 AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos de Gradle y del proyecto
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY src /app/src
COPY gradlew /app/
COPY gradle /app/gradle

# Hacer ejecutable el gradlew
RUN chmod +x gradlew

# Ejecutar la compilación del proyecto usando Gradle
RUN ./gradlew build --no-daemon

# Etapa 2: Crear la imagen para ejecutar la aplicación
FROM openjdk:11-jre-slim

# Establecer el directorio de trabajo dentro del contenedor para la aplicación
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/build/libs/wird-weatherapp-all.jar /app/app.jar

# Copiar el archivo de propiedades al directorio adecuado
COPY src/main/resources/apikey.properties /app/resources/apikey.properties

# Exponer el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
