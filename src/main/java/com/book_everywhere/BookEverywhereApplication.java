package com.book_everywhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BookEverywhereApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookEverywhereApplication.class, args);
	}
}
