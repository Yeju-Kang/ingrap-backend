package com.ingrap.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

		// .env 파일 자동 로드
		Dotenv dotenv = Dotenv.load();

		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(BackendApplication.class, args);
	}

}
