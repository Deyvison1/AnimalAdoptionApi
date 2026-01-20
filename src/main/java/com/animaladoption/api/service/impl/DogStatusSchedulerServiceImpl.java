package com.animaladoption.api.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.model.Dog;
import com.animaladoption.api.repository.IDogRepository;
import com.animaladoption.api.service.DogStatusSchedulerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogStatusSchedulerServiceImpl implements DogStatusSchedulerService {
	private final IDogRepository dogRepository;

	private static final long INTERVAL_MINUTES = 10;

	@Transactional
	public void processRepublishedAnimals() {
		LocalDateTime now = LocalDateTime.now();

		List<Dog> dogs = dogRepository.findByStatusRepublished(StatusAnimal.REPUBLISHED);

		for (Dog dog : dogs) {
			LocalDateTime updatedAt = dog.getDateUpdateStatus();

			if (updatedAt == null) {
				continue;
			}

			long minutes = ChronoUnit.MINUTES.between(updatedAt, now);

			if (minutes >= INTERVAL_MINUTES) {
				dog.setStatus(StatusAnimal.PUBLISHED);
				dog.setDateUpdateStatus(now);
			}
			log.info("Dog {} voltou para PUBLISHED após {} minutos", dog.getId(), minutes);
		}
	}
}
