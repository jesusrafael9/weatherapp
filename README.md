# Proyecto Kotlin/Java con Gradle

## Requerimientos

- Docker
- Docker Compose
- JDK 11 o superior
- Gradle

## Instrucciones para levantar el proyecto con Docker

1. Clona el repositorio:
    ```sh
    git clone <URL_DEL_REPOSITORIO>
    cd <NOMBRE_DEL_REPOSITORIO>
    ```

2. Compila y levanta los contenedores:
    ```sh
    docker-compose up --build
    ```

3. Para entrar en la consola del contenedor Redis:
    ```sh
    docker exec -it wird-redis sh
    ```

4. Comandos de Redis:
    - Listar todas las keys:
        ```sh
        KEYS *
        ```
    - Obtener el valor de una key específica:
        ```sh
        GET Santiago
        ```

5. Para entrar en la consola del contenedor de la aplicación:
    ```sh
    docker exec -it wird-app sh
    ```

## Documentación

La documentación de la API está disponible en Swagger. Puedes acceder a ella en el siguiente enlace:

[Documentación Swagger](http://localhost:8080/swagger-ui.html)
