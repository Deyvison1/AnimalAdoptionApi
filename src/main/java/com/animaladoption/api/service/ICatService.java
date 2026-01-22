package com.animaladoption.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.animaladoption.api.dto.cat.CatCreateDTO;
import com.animaladoption.api.dto.cat.CatDTO;
import com.animaladoption.api.dto.cat.CatFilterDTO;
import com.animaladoption.api.dto.cat.CatUpdateDTO;

public interface ICatService {
	Page<CatDTO> findAll(Pageable page, CatFilterDTO filter);

	CatDTO findByIdDTO(UUID id);

	void delete(UUID id);

	CatDTO add(CatCreateDTO dto);

	CatDTO update(UUID id, CatUpdateDTO dto);

	Page<CatDTO> findAllByAvaliableAndPublishIsTrue(Pageable page, CatFilterDTO filter);

	void isPublish(UUID id);

	void notPublish(UUID id, String motivo);
}
