package com.animaladoption.api.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.animaladoption.api.dto.animal.AnimalImageDTO;

public interface IAnimalImageService {
	void remove(UUID id);

	AnimalImageDTO uploadImageToDatabase(UUID animalId, MultipartFile file, boolean active) throws IOException;

	AnimalImageDTO getImageById(UUID id);

	void activeImage(UUID id);
}
