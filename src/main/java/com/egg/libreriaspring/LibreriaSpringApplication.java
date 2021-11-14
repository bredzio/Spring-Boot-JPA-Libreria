package com.egg.libreriaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class LibreriaSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaSpringApplication.class, args);
	}

}
