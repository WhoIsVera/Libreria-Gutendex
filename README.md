# Librería API

## Descripción

Este proyecto es una aplicación de gestión de libros que permite buscar y almacenar información sobre libros y autores. Utiliza una API externa para obtener datos de libros y almacena la información en una base de datos local.

## Características

- **Búsqueda de libros**: Permite buscar libros por título.
- **Almacenamiento de libros y autores**: Guarda información sobre libros y autores en una base de datos.
- **Filtrado por año de nacimiento del autor**: Permite filtrar autores por año de nacimiento.
- **Filtrado por idioma**: Muestra libros en un idioma específico.

## Tecnologías

- **Java**: Lenguaje de programación utilizado.
- **Spring Boot**: Framework para la creación de aplicaciones Java.
- **Hibernate**: ORM para la gestión de bases de datos.
- **JPA**: API para la persistencia de datos.
- **Jackson**: Para la serialización y deserialización de JSON.

## Requisitos

- Java 17 o superior
- Maven
- Base de datos (por ejemplo, MySQL, PostgreSQL)

## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tu_usuario/libreria-api.git
   cd libreria-api

   
## Configuración de la Base de Datos
  
  2. Configura la base de datos en `application.properties`:
  
    
    spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña


## Compilación del Proyecto

  3. Compila el proyecto con el siguiente comando:
    
    
    mvn clean install

## Ejecuta la aplicación

4. Ejecuta la aplicación con el siguiente comando:

       mvn spring-boot:run


## Uso
  Al ejecutar la aplicación, se te pedirá ingresar un título de libro para buscar.
    Puedes filtrar autores por año de nacimiento y buscar libros por idioma.
    La aplicación mostrará los resultados y guardará la información en la base de datos.
