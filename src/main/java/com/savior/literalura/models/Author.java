package com.savior.literalura.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long birthYear;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;
}
