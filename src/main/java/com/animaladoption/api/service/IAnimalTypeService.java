package com.animaladoption.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.animaladoption.api.dto.animal.AnimalTypeDTO;

public interface IAnimalTypeService {
	Page<AnimalTypeDTO> findAll(Pageable page);

	AnimalTypeDTO findByIdToDto(UUID id);

	AnimalTypeDTO add(AnimalTypeDTO dto);

	AnimalTypeDTO update(UUID id, AnimalTypeDTO dto);

	void delete(UUID id);
}
