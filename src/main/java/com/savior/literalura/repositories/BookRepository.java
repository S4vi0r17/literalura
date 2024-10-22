package com.savior.literalura.repositories;

import com.savior.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleIgnoreCase(String title);
}
