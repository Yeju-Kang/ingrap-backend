package com.ingrap.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

	public static void main(String[] args) {

		// .env 파일 자동 로드
		Dotenv dotenv = Dotenv.load();

		// DB 관련
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		// ✅ AWS 관련 추가
		System.setProperty("AWS_ACCESS_KEY", dotenv.get("AWS_ACCESS_KEY"));
		System.setProperty("AWS_SECRET_KEY", dotenv.get("AWS_SECRET_KEY"));

		SpringApplication.run(BackendApplication.class, args);
	}
}

