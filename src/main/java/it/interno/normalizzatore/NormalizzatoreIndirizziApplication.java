package it.interno.normalizzatore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NormalizzatoreIndirizziApplication {

	public static void main(String[] args) {
		SpringApplication.run(NormalizzatoreIndirizziApplication.class, args);
	}

}
