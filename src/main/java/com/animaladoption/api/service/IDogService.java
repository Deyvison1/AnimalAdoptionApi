package com.animaladoption.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.animaladoption.api.dto.dog.DogCreateDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.dto.dog.DogUpdateDTO;

public interface IDogService {
	Page<DogDTO> findAll(Pageable page);

	DogDTO findByIdDTO(UUID id);

	void delete(UUID id);
	DogDTO add(DogCreateDTO dto);

	DogDTO update(UUID id, DogUpdateDTO dto);
}
