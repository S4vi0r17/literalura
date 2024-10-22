package com.savior.literalura.models;

import com.savior.literalura.dto.BookDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private String language;
    private String downloadCount;

    public Book() { }

    public Book(BookDTO bookDTO) {
        this.title = bookDTO.title();
        this.author = new Author(bookDTO.authors().getFirst());
        this.language = bookDTO.languages().getFirst();
        this.downloadCount = bookDTO.downloadCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", language='" + language + '\'' +
                ", downloadCount='" + downloadCount + '\'' +
                '}';
    }
}
