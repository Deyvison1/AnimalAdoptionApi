package com.animaladoption.api.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.animaladoption.api.dto.animal.ImageDTO;

public interface IImageService {
	ImageDTO uploadImage(UUID animalId, MultipartFile file, boolean active) throws IOException;

	ImageDTO findById(UUID id);

	void activeImage(UUID id, List<UUID> idIsActive);

	void delete(UUID id, UUID animalId);
}
