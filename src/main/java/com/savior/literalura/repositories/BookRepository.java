package com.savior.literalura.repositories;

import com.savior.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleIgnoreCase(String title);

    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> findByLanguage(String language);
}
