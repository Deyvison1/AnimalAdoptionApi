package com.animaladoption.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animaladoption.api.model.AnimalImage;

@Repository
public interface IAnimalImageRepository extends JpaRepository<AnimalImage, UUID> {
	boolean existsByAnimalIdAndActiveTrue(UUID animalId);

	List<AnimalImage> findByAnimalId(UUID animalId);

	boolean existsByAnimalIdAndFilename(UUID animalId, String fileName);
}
