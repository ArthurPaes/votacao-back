package com.example.pautachallenge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PautaChallenge {

	public static void main(String[] args) {
		log.info("Iniciando aplicação PautaChallenge");
		SpringApplication.run(PautaChallenge.class, args);
		log.info("Aplicação PautaChallenge iniciada com sucesso");
	}

}
