package com.Libreria.principal;

import com.Libreria.model.*;
import com.Libreria.repository.AuthorRepository;
import com.Libreria.repository.LibreriaRepository;
import com.Libreria.service.ConsumoAPI;
import com.Libreria.service.ConvertirDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private final String URL_BASE = "https://gutendex.com";

    private ConvertirDatos convertirDatos = new ConvertirDatos();
    @Autowired
    private LibreriaRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    public Principal(LibreriaRepository repository) {
        this.repository = repository;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1-. Buscar libros por el titulo: 
                    2-. Mostrar libros registrados:
                    3-. Mostrar autores registrados:
                    4-. Mostrar autores vivos, en un determinado año:
                    5-. Mostrar libros por idioma:
                    6-. Salir.                
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibroBuscados();
                    break;
                case 3:
                    buscarAutores();
                    break;
                case 4:
                    mostrarAutoresPorFecha();
                    break;
                case 5:
                    mostarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicacion...");
                    break;
                default:
                    System.out.println("Opcion invalida, intentelo de nuevo.");
            }
        }
    }

    private Datosdatos buscarLibro() {
        System.out.println("Ingrese el título del libro que deseas buscar: ");
        var nombreLibro = teclado.nextLine();

        var json = consumoAPI.obtenerDatos(URL_BASE + "/books/?search=" + nombreLibro.replace(" ", "+"));
//        System.out.println("Respuesta de la API: " + json);

        if (json == null || json.trim().isEmpty()) {
            throw new RuntimeException("La respuesta de la API está vacía.");
        }


        return convertirDatos.obtenerSerealizacionDatos(json, Datosdatos.class);
    }

    private void mostrarLibroBuscados() {
        Datosdatos datos = buscarLibro(); // Asegúrate de que este método retorna datos válidos

        // Conjunto para almacenar los títulos de los libros ya agregados
        Set<String> librosAgregados = new HashSet<>();

        // Si datos no es null y contiene libros
        if (datos != null && datos.info() != null) {
            for (DatosLibro datosLibro : datos.info()) {
                String titulo = datosLibro.titulo();

                // Verificar si el libro ya ha sido agregado
                if (!librosAgregados.contains(titulo)) {
                    for (DatosAutor datosAutor : datosLibro.autor()) {
                        String autorNombre = datosAutor.nombre();
                        Author author = authorRepository.findByName(autorNombre); // Asume que findByName recibe el nombre del autor

                        if (author == null) {
                            // Crear un nuevo autor si no existe
                            author = new Author();
                            author.setName(autorNombre);
                            author.setBirthYear(datosAutor.fechaNacimineto()); // Cambiado a fechaNacimineto()
                            author.setDeathYear(datosAutor.fechaFallecimiento()); // Cambiado a fechaFallecimiento()
                            authorRepository.save(author);
                        }

                        // Crear una entidad Libro
                        Libros libro = new Libros(); // Asegúrate de que el constructor sin argumentos es válido
                        libro.setTitle(titulo);
                        libro.setLanguages(datosLibro.lenguaje());
                        libro.setAuthor(author); // Asignar el autor al libro

                        // Guardar el libro en la base de datos
                        repository.save(libro);

                        // Agregar el título al conjunto para evitar duplicados
                        librosAgregados.add(titulo);

                        // Mostrar la información del libro
                        System.out.println("----------------------------");
                        System.out.println("Título del libro: " + libro.getTitle());
                        System.out.println("Autor: " + author.getName());
                        System.out.println("Idiomas: " + String.join(", ", libro.getLanguages()));
                        System.out.println("----------------------------");
                    }
                } else {
                    System.out.println("El libro con el título '" + titulo + "' ya ha sido agregado.");
                }
            }
        } else {
            System.out.println("No se encontraron libros.");
        }
    }



    private void buscarAutores() {
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        var nombreAutor = teclado.nextLine();

        // Obtener el JSON desde la API
        var json = consumoAPI.obtenerDatos(URL_BASE + "/books/?search=" + nombreAutor);
//        System.out.println("JSON recibido: " + json);

        // Deserializar el JSON en un objeto Datosdatos
        Datosdatos datos = convertirDatos.obtenerSerealizacionDatos(json, Datosdatos.class);

        // Usar un conjunto para almacenar detalles únicos del autor
        Set<DatosAutor> autoresUnicos = new HashSet<>();

        // Procesar los datos y agregar autores únicos al conjunto
        if (datos != null && datos.info() != null) {
            for (DatosLibro libro : datos.info()) {
                if (libro.autor() != null) {
                    for (DatosAutor autor : libro.autor()) {
                        if (autor.nombre().toLowerCase().contains(nombreAutor.toLowerCase())) {
                            autoresUnicos.add(autor);  // Añadir el autor al conjunto (evita duplicados)
                        }
                    }
                }
            }

            // Imprimir detalles de los autores únicos
            if (!autoresUnicos.isEmpty()) {
                for (DatosAutor autor : autoresUnicos) {
                    System.out.println("----------------------------");
                    System.out.println("Nombre del autor: " + autor.nombre());
                    System.out.println("Fecha de nacimiento: " + (autor.fechaNacimineto() != null ? autor.fechaNacimineto() : "Desconocida"));
                    System.out.println("Fecha de fallecimiento: " + (autor.fechaFallecimiento() != null ? autor.fechaFallecimiento() : "Desconocida"));
                    System.out.println("----------------------------");
                }
            } else {
                System.out.println("No se encontraron autores.");
            }
        } else {
            System.out.println("No se encontraron resultados.");
        }
    }




    private void mostarLibrosPorIdioma() {

        System.out.println("Ingrese el idioma por el que desea filtrar los libros:");
        System.out.println("en -  ingles");
        System.out.println("es -  español");
        System.out.println("fr -  frances");
        System.out.println("pt -  portugues");
        var idiomaBuscado = teclado.nextLine();

        // Obtener la lista de libros filtrados por idioma
        List<Libros> librosFiltrados = repository.findByLanguagesContaining(idiomaBuscado);

        if (librosFiltrados != null && !librosFiltrados.isEmpty()) {
            System.out.println("Libros encontrados en el idioma '" + idiomaBuscado + "':");
            for (Libros libro : librosFiltrados) {
                System.out.println("----------------------------");
                System.out.println("Título: " + libro.getTitle());
                System.out.println("Autor: " + (libro.getAuthor() != null ? libro.getAuthor().getName() : "Desconocido"));
                System.out.println("Idiomas: " + (libro.getLanguages() != null ? String.join(", ", libro.getLanguages()) : "No especificados"));
                System.out.println("----------------------------");
            }
        } else {
            System.out.println("----------------------------");
            System.out.println("No se encontraron libros en el idioma '" + idiomaBuscado + "'.");
            System.out.println("----------------------------");
        }
    }


    private void mostrarAutoresPorFecha() {
        System.out.println("Ingrese el año de nacimiento mínimo del autor:");
         int anoMinimo = Integer.parseInt(teclado.nextLine());

        List<Author> autores = authorRepository.findAutoresByAnoMinimo(anoMinimo);

        if (!autores.isEmpty()) {
            for (Author autor : autores) {
                System.out.println("Nombre del autor: " + autor.getName());
                System.out.println("Fecha de nacimiento: " + (autor.getBirthYear() != null ? autor.getBirthYear() : "Desconocida"));
                System.out.println("Fecha de fallecimiento: " + (autor.getDeathYear() != null ? autor.getDeathYear() : "Desconocida"));
                System.out.println("----------------------------");
            }
        } else {
            System.out.println("No se encontraron autores.");
        }
    }
}
