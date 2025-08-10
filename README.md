# LiterAlura

**Bienvenido a LiterAlura** — una aplicación de consola en Java que consume la API pública **Gutendex** para buscar libros y autores, y guarda los resultados en una base de datos PostgreSQL usando Spring Data JPA.

Al iniciar la aplicación verás un menú interactivo. Introduce el número de la opción que quieras ejecutar:

```
**********  Bienvenido a LiterAlura  **********

Seleccione una acción introduciendo su número:

1) Buscar libro por título
2) Mostrar libros guardados
3) Mostrar autores guardados
4) Mostrar autores vivos en un año dado
5) Mostrar libros por idioma
0) Salir

**********************************************
```

---

## Qué hace este proyecto

* Consume la API pública de libros **Gutendex** mediante `HttpClient` y mapea la respuesta JSON con **Jackson Databind**.
* Persiste las entidades `Libro` y `Autor` en **PostgreSQL** usando **Spring Data JPA**.
* Permite buscar en la API por título y guardar el libro/autor en la BD; además muestra listados y filtros (por idioma, autores vivos en un año, etc.).

---

## Tecnologías utilizadas

* **Java** (JDK 11+ recomendado)
* **Spring Boot** y **Spring Data JPA** (inyección de dependencias y persistencia)
* **Jackson Databind** (deserialización JSON)
* **PostgreSQL** (almacenamiento relacional)
* **Git / GitHub** (control de versiones)
* **IDE recomendado:** IntelliJ IDEA (pero puedes usar Eclipse, VSCode, NetBeans, etc.)

---

## Requisitos previos

* Java JDK instalado (recomendado JDK 11 o 17).
* Maven (o Gradle si adaptas el proyecto).
* PostgreSQL en ejecución y una base de datos creada.
* Conexión a Internet para consumir la API Gutendex.

---

## Configuración rápida

1. **Clona el repositorio**

```bash
git clone <URL_DEL_REPOSITORIO>
cd literalura
```

2. **Configura la conexión a la BD** en `src/main/resources/application.properties` (o `application.yml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mi_basedatos
spring.datasource.username=mi_usuario
spring.datasource.password=mi_contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> `spring.jpa.hibernate.ddl-auto=update` crea/actualiza las tablas automáticamente a partir de tus entidades JPA (útil para desarrollo). Para entornos productivos, usa migraciones (Flyway/Liquibase).

3. **Dependencias principales** (extracto `pom.xml`):

```xml
<!-- Spring Boot + JPA -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Jackson Databind -->
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
</dependency>

<!-- Driver PostgreSQL -->
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```



---

## Ejecutar la aplicación

Con Maven:

```bash
mvn clean spring-boot:run
```

O construir el JAR y ejecutarlo:

```bash
mvn clean package
java -jar target/literalura-0.0.1-SNAPSHOT.jar
```

Al arrancar verás el menú de consola. Usa las teclas para elegir opciones y sigue las instrucciones en pantalla.

---

## Uso típico

* **Buscar y guardar un libro por título:** selecciona la opción correspondiente, escribe el título; la aplicación consultará Gutendex y guardará el libro y su autor (si no existen).
* **Listar libros/autores:** recupera los registros almacenados en la BD.
* **Autores vivos en un año:** introduce un año (por ejemplo `1950`) y la app mostrará autores cuya vida cubra ese año (incluye autores con `deathYear == null` como “posiblemente vivos”).
* **Listar por idioma:** selecciona el idioma del menú para ver libros filtrados por el enum `Idioma`.



---






