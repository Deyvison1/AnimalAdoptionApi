package com.animaladoption.api.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.model.Cat;
import com.animaladoption.api.model.Dog;
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
		List<Dog> dogs = animais.stream().filter(a -> a instanceof Dog).map(a -> (Dog) a).toList();

		List<Cat> cats = animais.stream().filter(a -> a instanceof Cat).map(a -> (Cat) a).toList();

		cats.forEach(this::executeCat);
		dogs.forEach(this::executeDog);
	}

	private void executeDog(Dog dog) {
		LocalDateTime updatedAt = dog.getDateUpdateStatus();
		LocalDateTime now = LocalDateTime.now();

		long minutes = ChronoUnit.MINUTES.between(updatedAt, now);

		if (minutes >= INTERVAL_MINUTES) {
			dog.setStatus(StatusAnimal.PUBLISHED);
			dog.setDateUpdateStatus(now);
		}
		log.info("Dog {} voltou para PUBLISHED após {} minutos", dog.getId(), minutes);
	}

	private void executeCat(Cat cat) {
		LocalDateTime updatedAt = cat.getDateUpdateStatus();
		LocalDateTime now = LocalDateTime.now();

		long minutes = ChronoUnit.MINUTES.between(updatedAt, now);

		if (minutes >= INTERVAL_MINUTES) {
			cat.setStatus(StatusAnimal.PUBLISHED);
			cat.setDateUpdateStatus(now);
		}
		log.info("Cat {} voltou para PUBLISHED após {} minutos", cat.getId(), minutes);
	}

}
