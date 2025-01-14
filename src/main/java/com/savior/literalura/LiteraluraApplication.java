package com.savior.literalura;

import com.savior.literalura.dto.DataDTO;
import com.savior.literalura.main.Main;
import com.savior.literalura.repositories.AuthorRepository;
import com.savior.literalura.repositories.BookRepository;
import com.savior.literalura.services.ApiConsumerService;
import com.savior.literalura.services.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(bookRepository, authorRepository);
		main.run();
	}
}
