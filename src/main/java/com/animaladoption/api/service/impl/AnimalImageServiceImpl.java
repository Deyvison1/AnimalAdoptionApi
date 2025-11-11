package com.animaladoption.api.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.animaladoption.api.dto.animal.AnimalImageDTO;
import com.animaladoption.api.exception.DefaultActiveImageException;
import com.animaladoption.api.exception.NotFoundException;
import com.animaladoption.api.mapper.IAnimalImageMapper;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.model.AnimalImage;
import com.animaladoption.api.repository.IAnimalImageRepository;
import com.animaladoption.api.repository.IAnimalRepository;
import com.animaladoption.api.service.IAnimalImageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalImageServiceImpl implements IAnimalImageService {
	private final IAnimalImageRepository repository;
	private final IAnimalImageMapper mapper;
	private final IAnimalRepository animalRepository;

    /**
     * Remove uma imagem pelo ID, garantindo que não seja a única ativa.
     *
     * @param id UUID da imagem que será removida
     * @throws DefaultActiveImageException se a imagem removida for a única ativa
     */
	@Override
	@Transactional
	public void remove(UUID id) {
		AnimalImage entity = findById(id);
		validateActiveImages(entity);
		repository.deleteById(id);
	}

    /**
     * Define a imagem informada como ativa e desativa as demais do mesmo animal.
     *
     * @param id UUID da imagem que deve ser ativada
     */
	@Transactional
	public void activeImage(UUID id) {
		AnimalImage entity = findById(id);
		UUID animalId = entity.getAnimal().getId();

		List<AnimalImage> images = repository.findByAnimalId(animalId);

		images.forEach(img -> img.setActive(img.getId().equals(id)));
	}

    /**
     * Realiza o upload de uma imagem para o banco de dados, vinculando-a ao animal.
     *
     * @param animalId UUID do animal que receberá a imagem
     * @param file Arquivo da imagem enviada
     * @param active Se verdadeiro, define esta imagem como ativa
     * @return DTO da imagem salva
     * @throws IOException se ocorrer falha na leitura do arquivo
     * @throws DefaultActiveImageException se já existir outra imagem ativa
     */
	@Override
	@Transactional
	public AnimalImageDTO uploadImageToDatabase(UUID animalId, MultipartFile file, boolean active) throws IOException {
		Animal animal = animalRepository.findById(animalId)
				.orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

		validationDuplicateImage(animalId, file.getOriginalFilename());
		if (active) {
			boolean existsActive = repository.existsByAnimalIdAndActiveTrue(animalId);
			if (existsActive) {
				throw new DefaultActiveImageException("Já existe uma imagem ativa para este corte.");
			}
		}

		if (animal.getImages().isEmpty()) {
			active = true;
		}

		AnimalImage entity = AnimalImage.builder().animal(animal).filename(file.getOriginalFilename())
				.contentType(file.getContentType()).data(file.getBytes()).active(active).build();

		AnimalImage saved = repository.save(entity);

		return mapper.toDto(saved);
	}

    /**
     * Busca uma imagem pelo seu ID.
     *
     * @param id UUID da imagem
     * @return DTO da imagem encontrada
     * @throws NotFoundException se a imagem não for encontrada
     */
	@Override
	public AnimalImageDTO getImageById(UUID id) {
		AnimalImage entity = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Imagem não encontrada: " + id));

        return mapper.toDto(entity);
	}

	private void validationDuplicateImage(UUID animalId, String fileName) {
		boolean existesByName = repository.existsByAnimalIdAndFilename(animalId, fileName);
		if (existesByName) {
			throw new DefaultActiveImageException("Essa imagem ja existe.");
		}
	}

	private AnimalImage findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
	}

	private void validateActiveImages(AnimalImage entity) {
		List<AnimalImage> allImages = repository.findByAnimalId(entity.getAnimal().getId());

		if (allImages == null || allImages.isEmpty()) {
			return;
		}

		long activeCountExcludingCurrent = allImages.stream()
				.filter(img -> !img.getId().equals(entity.getId()) && Boolean.TRUE.equals(img.getActive())).count();

		if (activeCountExcludingCurrent == 0) {
			throw new DefaultActiveImageException("Não é possível remover a última imagem ativa.");
		}

		if (activeCountExcludingCurrent > 1) {
			throw new DefaultActiveImageException("Não pode haver mais de uma imagem ativa no corte.");
		}
	}
}
