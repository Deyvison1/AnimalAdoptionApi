package com.animaladoption.api.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.animaladoption.api.client.IImageClient;
import com.animaladoption.api.dto.animal.ImageDTO;
import com.animaladoption.api.repository.specification.DogSpecification;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.animaladoption.api.dto.ContactDTO;
import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.dto.dog.*;
import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.exception.NotFoundException;
import com.animaladoption.api.exception.NotPublishException;
import com.animaladoption.api.mapper.*;
import com.animaladoption.api.model.*;
import com.animaladoption.api.repository.IDogRepository;
import com.animaladoption.api.service.IBreedService;
import com.animaladoption.api.service.IDogService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogServiceImpl implements IDogService {

	private final IDogRepository repo;
	private final IDogMapper mapper;
	private final IBreedService breedService;
	private final IBreedMapper breedMapper;
	private final IContactMapper contactMapper;
	private final IImageClient imageClient;

	@Value("${image-api.url-images}")
	private String baseUrlImage;

	/*
	 * ------------------------------------------------------------- LISTAGEM DE
	 * CÃES -------------------------------------------------------------
	 */

	@Override
	@Transactional(readOnly = true)
	public Page<DogDTO> findAll(Pageable page, DogFilterDTO filter) {
		return findDogs(page, filter, false);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DogDTO> findAllByAvaliableAndPublishIsTrue(Pageable page, DogFilterDTO filter) {
		return findDogs(page, filter, true);
	}

	private Page<DogDTO> findDogs(Pageable page, DogFilterDTO filter, boolean onlyActive) {

		Specification<Dog> spec = Specification.where(null);

		// 1. Se precisa filtrar apenas ativos
		if (onlyActive) {
			spec = spec.and(DogSpecification.onlyAvailableAndPublished());
		}

		// 2. Aplica os filtros enviados
		if (filter != null) {
			spec = spec.and(DogSpecification.filterBy(filter));
		}

		// 3. Paginação + ordenação
		Pageable pageableRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createdDate"));

		// 4. Busca
		Page<Dog> dogs = repo.findAll(spec, pageableRequest);

		// 5. Conversão
		return dogs.map(this::convertToDtoWithImages);
	}

	private DogDTO convertToDtoWithImages(Dog dog) {
		DogDTO dto = mapper.toDto(dog);

		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			dto.setImagesComplet(dto.getImages().stream().map(this::loadImageDTO).collect(Collectors.toList()));
		}

		return dto;
	}

	private ImageDTO loadImageDTO(UUID id) {
		try {
			ImageDTO img = imageClient.getImage(id);

			if (Strings.isNotBlank(baseUrlImage)) {
				img.setUrl(baseUrlImage + img.getUrl());
			}
			return img;

		} catch (Exception e) {
			log.error("Erro ao carregar imagem {}: {}", id, e.getMessage());
			throw new NotFoundException("Imagem não encontrada: " + id);
		}
	}

	@Override
	@Transactional
	public DogDTO add(DogCreateDTO dto) {
		Dog entity = prepareCreateEntity(dto);
		return mapper.toDto(repo.save(entity));
	}

	@Override
	@Transactional
	public DogDTO update(UUID id, DogUpdateDTO dto) {
		Dog entity = findById(id);
		DogDTO dtoMapped = mapUpdateToDto(dto);

		updateBasicFields(entity, dtoMapped);

		if (dto.getContacts() != null) {
			updateContacts(entity, dto.getContacts());
		}

		return mapper.toDto(repo.save(entity));
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		Dog entity = findById(id);
		isDeleteDogIsPublished(entity);
		repo.delete(entity);
	}

	private void isDeleteDogIsPublished(Dog entity) {
		if (entity.getStatus() == StatusAnimal.PUBLISHED) {
			throw new NotPublishException(409, "Não e possivel excluir um animal que já está publicado");
		}
		
		if (entity.getStatus() == StatusAnimal.REPUBLISHED) {
			throw new NotPublishException(409, "Não e possivel excluir um animal que está em republicação");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public DogDTO findByIdDTO(UUID id) {
		return convertToDtoWithImages(findById(id));
	}


	private Dog prepareCreateEntity(DogCreateDTO dto) {
		Dog entity = mapper.createToEntity(dto);

		entity.setBreed(loadBreed(dto.getBreedId()));
		entity.setStatus(StatusAnimal.NOT_PUBLISHED);

		Set<Contact> contacts = contactMapper.toEntitySet(dto.getContacts());
		if (contacts != null) {
			contacts.forEach(c -> c.setAnimal(entity));
		}
		entity.setContacts(contacts);

		return entity;
	}

	private DogDTO mapUpdateToDto(DogUpdateDTO dto) {
		DogDTO mapped = mapper.updateToDto(dto);
		mapped.setBreed(loadBreedDto(dto.getBreedId()));
		return mapped;
	}

	private Breed loadBreed(UUID id) {
		BreedDTO dto = breedService.findByIdToDto(id);
		return Breed.builder().id(dto.getId()).name(dto.getName()).build();
	}

	private BreedDTO loadBreedDto(UUID id) {
		BreedDTO dto = breedService.findByIdToDto(id);
		return BreedDTO.builder().id(dto.getId()).name(dto.getName()).build();
	}

	private void updateBasicFields(Dog entity, DogDTO dto) {
		Optional.ofNullable(dto.getName()).filter(n -> !n.equals(entity.getName())).ifPresent(entity::setName);

		Optional.ofNullable(dto.getAge()).filter(a -> !a.equals(entity.getAge())).ifPresent(entity::setAge);

		if (!entity.getBreed().getId().equals(dto.getBreed().getId())) {
			entity.setBreed(breedMapper.toEntity(dto.getBreed()));
		}

		entity.setAvailable(dto.getAvailable());
	}

	private void updateContacts(Dog entity, Set<ContactDTO> contactDTOs) {
		Map<UUID, Contact> existing = entity.getContacts().stream().filter(c -> c.getId() != null)
				.collect(Collectors.toMap(Contact::getId, c -> c));

		Set<Contact> updated = contactDTOs.stream().map(dto -> updateOrCreateContact(dto, existing, entity))
				.collect(Collectors.toSet());

		entity.getContacts().clear();
		entity.getContacts().addAll(updated);
	}

	private Contact updateOrCreateContact(ContactDTO dto, Map<UUID, Contact> existing, Animal animal) {
		Contact entity = Optional.ofNullable(dto.getId()).map(existing::get).orElseGet(Contact::new);

		if (contactHasChanged(entity, dto)) {
			entity.setName(dto.getName());
			entity.setValue(dto.getValue());
		}

		entity.setAnimal(animal);
		return entity;
	}

	private boolean contactHasChanged(Contact entity, ContactDTO dto) {
		return !Objects.equals(entity.getName(), dto.getName()) || !Objects.equals(entity.getValue(), dto.getValue());
	}

	private Dog findById(UUID id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException("Dog não encontrado: " + id));
	}

	@Override
	@Transactional
	public void isPublish(UUID id) {
		Dog entity = findById(id);
		
		if (StatusAnimal.DESPUBLICADO == entity.getStatus()) {
			entity.setStatus(StatusAnimal.REPUBLISHED);
		} else if (StatusAnimal.NOT_PUBLISHED == entity.getStatus()) {
			entity.setStatus(StatusAnimal.PUBLISHED);
		}
		
		if (!entity.getAvailable()) {
			throw new NotPublishException(400, "Somente animais disponiveis podem ser publicados.");
		}

		entity.setDateUpdateStatus(LocalDateTime.now());
		repo.save(entity);
	}

	@Override
	@Transactional
	public void notPublish(UUID id, String motivo) {
		Dog entity = findById(id);
		if(Strings.isBlank(motivo)) {
			throw new NotPublishException(400, "Não e possivel despublicar algum animal sem justificar o motivo.");
		}

		entity.setStatus(StatusAnimal.DESPUBLICADO);
		entity.setMotivo(motivo);
		repo.save(entity);
	}
}
