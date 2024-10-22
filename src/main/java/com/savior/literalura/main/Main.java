package com.savior.literalura.main;

import com.savior.literalura.dto.DataDTO;
import com.savior.literalura.models.Book;
import com.savior.literalura.repositories.AuthorRepository;
import com.savior.literalura.repositories.BookRepository;
import com.savior.literalura.services.ApiConsumerService;
import com.savior.literalura.services.DataConverter;

import java.util.DoubleSummaryStatistics;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private final String API_URL = "https://gutendex.com/books/";
    private final ApiConsumerService apiConsumerService = new ApiConsumerService();
    private final DataConverter dataConverter = new DataConverter();
    private final Scanner scanner = new Scanner(System.in);
    private final BookRepository bookrepository;
    private final AuthorRepository authorRepository;

    public Main(BookRepository bookrepository, AuthorRepository authorRepository) {
        this.bookrepository = bookrepository;
        this.authorRepository = authorRepository;
    }

    public void run() {
        var menu = """
                \n1. Buscar libro por título
                2. Listar libros registrados
                3. Listar autores registrados
                4. Lista de autores vivos en un determinado año
                5. Listar libros por idioma
                6. Top 10 libros más descargados
                7. Buscar autor por nombre
                8. Generar reporte de estadísticas
                0. Salir
                """;
        do {
            System.out.println(menu);
            System.out.print("Introduce una opción: ");
            var option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> searchBookByTitle();
                case 2 -> listAllBooks();
                case 3 -> listAuthors();
                case 4 -> listAuthorsByYear();
                case 5 -> listBooksByLanguage();
                case 6 -> top10MostDownloadedBooks();
                case 7 -> searchAuthorByName();
                case 8 -> statisticsReport();
                case 0 -> System.exit(0);
                default -> System.out.println("Opción no válida");
            }
        } while (true);
    }

    private void searchBookByTitle() {
        System.out.print("\nIntroduce el título del libro: ");
        var title = scanner.nextLine();
        var results = apiConsumerService.getApiData(API_URL + "?search=" + title.replace(" ", "%20"));
        var dataDTO = dataConverter.convertJsonTo(results, DataDTO.class);
        var books = dataDTO.books().stream()
                .map(Book::new)
                .findFirst();

        if (books.isEmpty()) {
            System.out.println("Libro no encontrado");
            return;
        }

        showBook(books.get());

        authorRepository.findByNameIgnoreCase(books.get().getAuthor().getName())
                .ifPresentOrElse(author -> {
                    books.get().setAuthor(author);
                }, () -> {
                    authorRepository.save(books.get().getAuthor());
                });

        bookrepository.findByTitleIgnoreCase(books.get().getTitle())
                .ifPresentOrElse(book -> {
                    System.out.println("El libro ya está registrado");
                }, () -> {
                    bookrepository.save(books.get());
                });
    }

    private void showBook(Book book) {
        System.out.printf("""
                --------------------------------
                Título: %s
                Autor: %s
                Idioma: %s
                Descargas: %s
                --------------------------------
                """, book.getTitle(), book.getAuthor().getName(), book.getLanguage(), book.getDownloadCount());
    }

    private void listAllBooks() {
        var books = bookrepository.findAll();
        System.out.println("\nLibros registrados:");
        books.forEach(this::showBook);
    }

    private void listAuthors() {
        var authors = authorRepository.findAll();
        System.out.println("\nAutores registrados:");
        authors.forEach(System.out::println);
    }

    private void listAuthorsByYear() {
        System.out.print("\nIntroduce el año: ");
        var year = scanner.nextInt();

        var authors = authorRepository.findAll().stream()
                .filter(author -> author.getDeathYear() == null || author.getDeathYear() > year)
                .collect(Collectors.toList());

        System.out.println("\nAutores vivos en " + year + ":");
        authors.forEach(System.out::println);
    }

    private void listBooksByLanguage() {
        var languages = bookrepository.findAll().stream()
                .map(Book::getLanguage)
                .distinct()
                .collect(Collectors.toList());

        System.out.println("\n# Idiomas disponibles #");
        languages.forEach(language -> {
            var books = bookrepository.findByLanguage(language);
            System.out.printf("Idioma: %s, Cantidad de libros: %d\n", language, books.size());
        });

        System.out.print("\nIntroduce el idioma: ");
        var language = scanner.nextLine();

        var books = bookrepository.findAll().stream()
                .filter(book -> book.getLanguage().equalsIgnoreCase(language))
                .collect(Collectors.toList());

        System.out.println("Libros en " + language + ":");
        books.forEach(this::showBook);
    }

    private void top10MostDownloadedBooks() {
        var books = bookrepository.findTop10ByOrderByDownloadCountDesc();

        System.out.println("\n# Top 10 libros más descargados #");
        books.forEach(this::showBook);
    }

    private void searchAuthorByName() {
        System.out.print("\nIntroduce el nombre del autor: ");
        var name = scanner.nextLine();
        var author = authorRepository.findByNameIgnoreCaseContaining(name);

        if (author.isEmpty()) {
            System.out.println("Autor no encontrado");
            return;
        }

        System.out.println("Autor encontrado:");
        System.out.println(author.get());
    }

    private void statisticsReport() {
        var books = apiConsumerService.getApiData(API_URL);
        var dataDTO = dataConverter.convertJsonTo(books, DataDTO.class);
        var booksList = dataDTO.books().stream()
                .collect(Collectors.toList());

        DoubleSummaryStatistics statistics = booksList.stream()
                .mapToDouble(book -> Double.parseDouble(book.downloadCount()))
                .summaryStatistics();

        System.out.printf("""
                --------------------------------
                Total de libros: %d
                Total de descargas: %.0f
                Promedio de descargas: %.2f
                Máximo de descargas: %.0f
                Mínimo de descargas: %.0f
                --------------------------------
                """, statistics.getCount(), statistics.getSum(), statistics.getAverage(), statistics.getMax(), statistics.getMin());
    }
}
