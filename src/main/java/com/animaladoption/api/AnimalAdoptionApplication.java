package com.animaladoption.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnimalAdoptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalAdoptionApplication.class, args);
	}

}
