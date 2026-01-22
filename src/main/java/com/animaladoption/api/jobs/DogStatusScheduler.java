package com.animaladoption.api.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.animaladoption.api.service.AnimalStatusSchedulerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DogStatusScheduler {

	private final AnimalStatusSchedulerService service;

	@Scheduled(cron = "0 */10 * * * *")
	public void run() {
		log.debug("⏱️ Verificando cães republicados...");
		service.processRepublishedAnimals();
	}
}
