package com.savior.literalura.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"), // Columna para esta entidad
            inverseJoinColumns = @JoinColumn(name = "author_id") // Columna para la otra entidad
    )
    private List<Author> authors;

    private List<String> languages;
    private String downloadCount;
}
