package com.savior.literalura.repositories;

import com.savior.literalura.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCase(String name);
    Optional<Author> findByNameIgnoreCaseContaining(String name);
}
