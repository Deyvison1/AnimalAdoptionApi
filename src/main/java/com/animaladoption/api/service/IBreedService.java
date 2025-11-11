package com.animaladoption.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.animaladoption.api.dto.breed.BreedDTO;

public interface IBreedService {
	Page<BreedDTO> findAll(Pageable page);

	BreedDTO findByIdToDto(UUID id);

	BreedDTO add(BreedDTO dto);

	BreedDTO update(UUID id, BreedDTO dto);

	void remove(UUID id);
}
