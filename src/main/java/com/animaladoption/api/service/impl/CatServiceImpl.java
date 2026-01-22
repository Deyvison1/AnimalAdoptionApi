package com.animaladoption.api.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.animaladoption.api.client.IImageClient;
import com.animaladoption.api.dto.ContactDTO;
import com.animaladoption.api.dto.animal.ImageDTO;
import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.dto.cat.CatCreateDTO;
import com.animaladoption.api.dto.cat.CatDTO;
import com.animaladoption.api.dto.cat.CatFilterDTO;
import com.animaladoption.api.dto.cat.CatUpdateDTO;
import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.exception.NotFoundException;
import com.animaladoption.api.exception.NotPublishException;
import com.animaladoption.api.mapper.IBreedMapper;
import com.animaladoption.api.mapper.ICatMapper;
import com.animaladoption.api.mapper.IContactMapper;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.model.Breed;
import com.animaladoption.api.model.Cat;
import com.animaladoption.api.model.Contact;
import com.animaladoption.api.repository.ICatRepository;
import com.animaladoption.api.repository.specification.CatSpecification;
import com.animaladoption.api.service.IBreedService;
import com.animaladoption.api.service.ICatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatServiceImpl implements ICatService {

	private final ICatRepository repo;
	private final ICatMapper mapper;
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
	public Page<CatDTO> findAll(Pageable page, CatFilterDTO filter) {
		return findDogs(page, filter, false);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CatDTO> findAllByAvaliableAndPublishIsTrue(Pageable page, CatFilterDTO filter) {
		return findDogs(page, filter, true);
	}

	private Page<CatDTO> findDogs(Pageable page, CatFilterDTO filter, boolean onlyActive) {

		Specification<Cat> spec = Specification.where(null);

		// 1. Se precisa filtrar apenas ativos
		if (onlyActive) {
			spec = spec.and(CatSpecification.onlyAvailableAndPublished());
		}

		// 2. Aplica os filtros enviados
		if (filter != null) {
			spec = spec.and(CatSpecification.filterBy(filter));
		}

		// 3. Paginação + ordenação
		Pageable pageableRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createdDate"));

		// 4. Busca
		Page<Cat> cats = repo.findAll(spec, pageableRequest);

		// 5. Conversão
		return cats.map(this::convertToDtoWithImages);
	}

	private CatDTO convertToDtoWithImages(Cat cat) {
		CatDTO dto = mapper.toDto(cat);

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
	public CatDTO add(CatCreateDTO dto) {
		Cat entity = prepareCreateEntity(dto);
		return mapper.toDto(repo.save(entity));
	}

	@Override
	@Transactional
	public CatDTO update(UUID id, CatUpdateDTO dto) {
		Cat entity = findById(id);
		CatDTO dtoMapped = mapUpdateToDto(dto);

		updateBasicFields(entity, dtoMapped);

		if (dto.getContacts() != null) {
			updateContacts(entity, dto.getContacts());
		}

		return mapper.toDto(repo.save(entity));
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		Cat entity = findById(id);
		isDeleteDogIsPublished(entity);
		repo.delete(entity);
	}

	private void isDeleteDogIsPublished(Cat entity) {
		if (entity.getStatus() == StatusAnimal.PUBLISHED) {
			throw new NotPublishException(409, "Não e possivel excluir um animal que já está publicado");
		}

		if (entity.getStatus() == StatusAnimal.REPUBLISHED) {
			throw new NotPublishException(409, "Não e possivel excluir um animal que está em republicação");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CatDTO findByIdDTO(UUID id) {
		return convertToDtoWithImages(findById(id));
	}

	private Cat prepareCreateEntity(CatCreateDTO dto) {
		Cat entity = mapper.createToEntity(dto);

		entity.setBreed(loadBreed(dto.getBreedId()));
		entity.setStatus(StatusAnimal.NOT_PUBLISHED);

		Set<Contact> contacts = contactMapper.toEntitySet(dto.getContacts());
		if (contacts != null) {
			contacts.forEach(c -> c.setAnimal(entity));
		}
		entity.setContacts(contacts);

		return entity;
	}

	private CatDTO mapUpdateToDto(CatUpdateDTO dto) {
		CatDTO mapped = mapper.updateToDto(dto);
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

	private void updateBasicFields(Cat entity, CatDTO dto) {
		Optional.ofNullable(dto.getName()).filter(n -> !n.equals(entity.getName())).ifPresent(entity::setName);

		Optional.ofNullable(dto.getAge()).filter(a -> !a.equals(entity.getAge())).ifPresent(entity::setAge);

		if (!entity.getBreed().getId().equals(dto.getBreed().getId())) {
			entity.setBreed(breedMapper.toEntity(dto.getBreed()));
		}

		entity.setAvailable(dto.getAvailable());
	}

	private void updateContacts(Cat entity, Set<ContactDTO> contactDTOs) {
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

	private Cat findById(UUID id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException("Cat não encontrado: " + id));
	}

	@Override
	@Transactional
	public void isPublish(UUID id) {
		Cat entity = findById(id);

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
		Cat entity = findById(id);
		if (Strings.isBlank(motivo)) {
			throw new NotPublishException(400, "Não e possivel despublicar algum animal sem justificar o motivo.");
		}

		entity.setStatus(StatusAnimal.DESPUBLICADO);
		entity.setMotivo(motivo);
		repo.save(entity);
	}
}
