package com.animaladoption.api.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.repository.IAnimalRepository;
import com.animaladoption.api.service.AnimalStatusSchedulerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalStatusSchedulerServiceImpl implements AnimalStatusSchedulerService {

	private final IAnimalRepository animalRepository;

	private static final long INTERVAL_MINUTES = 10;

	@Transactional
	public void processRepublishedAnimals() {
		List<Animal> animais = animalRepository.findByStatusRepublished(StatusAnimal.REPUBLISHED);

		execute(animais);
	}

	private void execute(List<Animal> animais) {
		animais.forEach(this::executeAnimal);
	}

	private void executeAnimal(Animal animal) {
		LocalDateTime updatedAt = animal.getDateUpdateStatus();
		LocalDateTime now = LocalDateTime.now();

		long minutes = ChronoUnit.MINUTES.between(updatedAt, now);

		if (minutes >= INTERVAL_MINUTES) {
			animal.setStatus(StatusAnimal.PUBLISHED);
			animal.setDateUpdateStatus(now);
		}

		log.info("Animal {} voltou para PUBLISHED após {} minutos", animal.getId(), minutes);
	}
}