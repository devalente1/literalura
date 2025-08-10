package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.IRepositorio;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private IRepositorio repositorio;

    public Principal(IRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            String menu = """
                    **********   ¡Bienvenido usuario a LiterAlura!   **********
                    Elija la opción a través de su número
                    1) Buscar libro por título
                    2) Listar libros registrados
                    3) Listar autores registrados
                    4) Listar autores vivos en un determinado año
                    5) Listar libros por idioma
                    0) Salir
                    ***********************************************************
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibrosGuardadosBD();
                    break;
                case 3:
                    mostraAutoresGuardadosBD();
                    break;
                case 4:
                    mostrarAutoresVivosBD();
                    break;
                case 5:
                    mostrarLibrosPorIdiomaBD();
                    break;
                case 0:
                    System.out.println("Gracias por visitar LiterAlura, hasta pronto!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    public void buscarLibro() {
        System.out.println("Ingrese el nombre del libro que desee buscar");
        var tituloLibro = teclado.nextLine();
        var tituloLibroCodificado = URLEncoder.encode(tituloLibro, StandardCharsets.UTF_8);
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibroCodificado);
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados()
                .stream()
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("**********   Libro encontrado   **********" +
                    "\nTítulo= " + libroBuscado.get().titulo() +
                    "\nAutor= " + libroBuscado.get().autor().stream()
                    .map(datosAutor -> datosAutor.nombre())
                    .limit(1).collect(Collectors.joining()) +
                    "\nIdioma= " + libroBuscado.get().idiomas().stream()
                    .collect(Collectors.joining()) +
                    "\nNúmero descargas= " + libroBuscado.get().numeroDeDescargas() +
                    "\n******************************\n");

            //System.out.println(libroBuscado.get());
            try {
                List<Libro> libroEncontrado = libroBuscado.stream()
                        .map(a -> new Libro(a))
                        .collect(Collectors.toList());

                Autor autorAPI = libroBuscado.stream()
                        .flatMap(l -> l.autor().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList()).stream().findFirst().get();

                Optional<Autor> autorBD = repositorio.buscarAutorPorNombre(libroBuscado.get().autor().stream()
                        .map(a -> a.nombre())
                        .collect(Collectors.joining()));

                Optional<Libro> libroOptional = repositorio.buscarLibroBD(tituloLibro);

                if (libroOptional.isPresent()) {
                    System.out.println("El libro ya está guardado en la BD.");
                } else {
                    Autor autor;
                    if (autorBD.isPresent()) {
                        autor = autorBD.get();
                        System.out.println("EL autor ya esta guardado en la BD");
                    } else {
                        autor = autorAPI;
                        repositorio.save(autor);
                    }
                    autor.setLibros(libroEncontrado);
                    repositorio.save(autor);
                }
            } catch (Exception e) {
                System.out.println("Warning! " + e.getMessage());
            }
        } else {
            System.out.println("Libro no encontrado");

        }
    }

    public void mostrarLibrosGuardadosBD() {
        List<Libro> libros = repositorio.librosGuardadosBD();
        libros.forEach(System.out::println);
    }

    public void mostraAutoresGuardadosBD() {
        List<Autor> autores = repositorio.autoresGuardadosBD();
        autores.forEach(a -> System.out.println(a));
    }

    private void mostrarAutoresVivosBD() {
        System.out.println("Ingrese un año para verificar el autor que desea buscar (ej. 1950):");
        String linea = teclado.nextLine().trim();

        try {
            int fecha = Integer.parseInt(linea);
            List<Autor> autores = repositorio.AutoresVivosBD(fecha);
            if (autores.isEmpty()) {
                System.out.println("Ningún autor vivo encontrado en el año " + fecha + ".");
                return;
            }
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);

        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Ingresa un número entero, por ejemplo: 1950.");
        }
    }

    private void mostrarLibrosPorIdiomaBD() {
        var menuIdiomas = """
                Elija una opción
                
                1 - Inglés
                2 - Español
                3 - Francés
                4 - Portugués
                5 - Alemán
                6 - Italiano
                7 - Neerlandés
                8 - Tagalo
                0 - Atrás
                """;
        System.out.println(menuIdiomas);

        try {
            var opcionIdioma = Integer.parseInt(teclado.nextLine().trim());

            Idioma idioma = switch (opcionIdioma) {
                case 1 -> Idioma.ENGLISH;
                case 2 -> Idioma.SPANISH;
                case 3 -> Idioma.FRENCH;
                case 4 -> Idioma.PORTUGUESE;
                case 5 -> Idioma.GERMAN;
                case 6 -> Idioma.ITALIAN;
                case 7 -> Idioma.DUTCH;
                case 8 -> Idioma.TAGALOG;
                case 0 -> null;
                default -> {
                    System.out.println("Ingrese una opción válida");
                    yield null;
                }
            };

            if (idioma == null) {
                if (opcionIdioma == 0) System.out.println("Regresando ...");
                return;
            }
            buscarLibrosPorIdiomaBD(idioma);
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida. Ingresa un número.");
        }
    }

    private void buscarLibrosPorIdiomaBD(Idioma idioma) {
        List<Libro> libros = repositorio.librosPorIdiomaBD(idioma);
        if (libros == null || libros.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma: " + idioma);
            return;
        }
        libros.forEach(System.out::println);
    }
}
