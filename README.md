# Literalura

**Literalura** es una aplicación de consola desarrollada con Java 23, Spring Boot y JPA. Forma parte del **Challenge de Alura Latam** y permite buscar y registrar libros y autores consumiendo datos de la API de Gutendex, así como generar reportes de descargas.

## Requisitos

- **Java 23**
- **PostgreSQL**
- **Jackson 2.16+**

## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/S4v!0r17/literalura.git
   cd literalura
   ```

2. Llena las variables de base de datos en `application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://${DB_HOST}:5432/${DB_NAME}
   spring.datasource.username=${DB_USER}
   spring.datasource.password=${DB_PASSWORD}
   ```

3. Ejecuta la aplicación

## Uso

Interacciona con la aplicación a través de un menú en la consola para realizar las siguientes acciones:
- Buscar libros por título.
- Listar libros y autores registrados.
- Filtrar autores por año de vida.
- Generar reportes de descargas.

## Contribuciones

Abierto a contribuciones. ¡Siéntete libre de hacer un fork y enviar un pull request!
