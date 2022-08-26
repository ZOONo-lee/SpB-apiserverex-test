package org.zerock.apiserverex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApiserverexApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiserverexApplication.class, args);
	}

}
