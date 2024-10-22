package com.savior.literalura;

import com.savior.literalura.dto.BookDTO;
import com.savior.literalura.dto.DataDTO;
import com.savior.literalura.services.ApiConsumerService;
import com.savior.literalura.services.DataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ApiConsumerService apiConsumerService = new ApiConsumerService();
		DataConverter dataConverter = new DataConverter();
		var json = apiConsumerService.getApiData("https://gutendex.com/books");
		System.out.println(json);
	}
}
