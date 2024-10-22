package com.savior.literalura.main;

import com.savior.literalura.dto.DataDTO;
import com.savior.literalura.models.Author;
import com.savior.literalura.models.Book;
import com.savior.literalura.repositories.AuthorRepository;
import com.savior.literalura.repositories.BookRepository;
import com.savior.literalura.services.ApiConsumerService;
import com.savior.literalura.services.DataConverter;

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
                1. Buscar libro por título
                2. Listar libros registrados
                3. Listar autores registrados
                4. Lista de autores vivos en un determinado año
                5. Listar libros por idioma
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
                case 0 -> System.exit(0);
                default -> System.out.println("Opción no válida");
            }
        } while (true);
    }

    private void searchBookByTitle() {
        System.out.print("Introduce el título del libro: ");
        var title = scanner.nextLine();
        var results = apiConsumerService.getApiData(API_URL + "?search=" + title.replace(" ", "%20"));
        var dataDTO = dataConverter.convertJsonTo(results, DataDTO.class);
        var books = dataDTO.books().stream()
                .map(Book::new)
                .collect(Collectors.toList());

        books.forEach(book -> {
            var author = book.getAuthor();
            var authorOptional = authorRepository.findByNameIgnoreCase(author.getName());
            if (authorOptional.isPresent()) {
                book.setAuthor(authorOptional.get());
            } else {
                authorRepository.save(author);
            }
            var bookOptional = bookrepository.findByTitleIgnoreCase(book.getTitle());
            if (bookOptional.isPresent()) {
                System.out.println("El libro ya está registrado");
            } else {
                bookrepository.save(book);
            }
        });

        System.out.println("Libros encontrados:");
        System.out.println(books);
    }

    private void listAllBooks() {
        var results = apiConsumerService.getApiData(API_URL);
        var dataDTO = dataConverter.convertJsonTo(results, DataDTO.class);
        System.out.println(dataDTO.books());
    }

    private void listAuthors() {
        var results = apiConsumerService.getApiData(API_URL);
        var dataDTO = dataConverter.convertJsonTo(results, DataDTO.class);
        var authors = dataDTO.books().stream()
                .flatMap(book -> book.authors().stream()
                        .map(Author::new))
                .toList();
        authors.forEach(System.out::println);
        authors.forEach(authorRepository::save);
    }

    private void listAuthorsByYear() {
        System.out.print("Introduce el año: ");
        var year = scanner.nextInt();
        var results = apiConsumerService.getApiData(API_URL);
        var dataDTO = dataConverter.convertJsonTo(results, DataDTO.class);
        dataDTO.books().stream()
                .map(book -> book.authors())
                .filter(authors -> authors.stream()
                        .anyMatch(author -> author.deathYear() == null || author.deathYear() > year))
                .forEach(System.out::println);
    }
}
