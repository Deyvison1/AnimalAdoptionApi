package com.animaladoption.api.service.impl;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.animaladoption.api.dto.dog.DogFilterDTO;
import com.animaladoption.api.repository.specification.DogSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.animaladoption.api.dto.ContactDTO;
import com.animaladoption.api.dto.animal.AnimalImageDTO;
import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.dto.dog.DogCreateDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.dto.dog.DogUpdateDTO;
import com.animaladoption.api.exception.NotFoundException;
import com.animaladoption.api.mapper.IBreedMapper;
import com.animaladoption.api.mapper.IContactMapper;
import com.animaladoption.api.mapper.IDogMapper;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.model.Breed;
import com.animaladoption.api.model.Contact;
import com.animaladoption.api.model.Dog;
import com.animaladoption.api.repository.IDogRepository;
import com.animaladoption.api.service.IBreedService;
import com.animaladoption.api.service.IDogService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements IDogService {
    private final IDogRepository repo;
    private final IDogMapper mapper;
    private final IBreedService breedService;
    private final IBreedMapper breedMapper;
    private final IContactMapper contactMapper;

    /**
     * Retorna uma página de cães.
     *
     * @param page informações de paginação
     * @param filter informações dos filtros
     * @return página contendo DogDTO
     */
    @Override
    @Transactional
    public Page<DogDTO> findAll(Pageable page, DogFilterDTO filter) {
        Specification<Dog> spec = DogSpecification.filterBy(filter);

        Pageable pageableRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Dog> dogs = repo.findAll(spec, pageableRequest);
        return dogs
                .map(mapper::toDto)
                .map(dto -> {
                    if (Objects.nonNull(dto.getImages())) {
                        dto.getImages().sort(Comparator.comparing(AnimalImageDTO::getActive).reversed());
                    }
                    return dto;
                });
    }

    /**
     * Cria um novo cão.
     *
     * @param dto dados para criação
     * @return DTO do cão criado
     */
    @Override
    @Transactional
    public DogDTO add(DogCreateDTO dto) {
        Dog entity = setRequestCreate(dto);
        return mapper.toDto(repo.save(entity));
    }

    /**
     * Atualiza um cão existente.
     *
     * @param id  identificador do cão
     * @param dto dados atualizados
     * @return DTO atualizado
     * @throws NotFoundException se o cão não for encontrado
     */
    @Override
    @Transactional
    public DogDTO update(UUID id, DogUpdateDTO dto) {
        Dog entity = findById(id);
        DogDTO dtoMapped = setRequestUpdate(dto);

        updateBasicFields(entity, dtoMapped);

        if (Objects.nonNull(dto.getContacts())) {
            setContactsToUpdate(entity.getContacts(), dto.getContacts(), entity);
        }

        return mapper.toDto(repo.save(entity));
    }

    /**
     * Remove um cão pelo ID.
     *
     * @param id identificador do cão
     * @throws NotFoundException se o cão não for encontrado
     */
    @Override
    @Transactional
    public void delete(UUID id) {
        Dog entity = findById(id);
        repo.delete(entity);
    }

    /**
     * Busca um cão pelo ID e retorna como DTO.
     *
     * @param id identificador do cão
     * @return DTO do cão encontrado
     * @throws NotFoundException se o cão não for encontrado
     */
    @Override
    @Transactional
    public DogDTO findByIdDTO(UUID id) {
        return mapper.toDto(findById(id));
    }

    /**
     * Mapeia dados do DTO de atualização para o DTO do mapper
     */
    private DogDTO setRequestUpdate(DogUpdateDTO dto) {
        DogDTO dtoMapped = mapper.updateToDto(dto);

        BreedDTO breedDTO = breedService.findByIdToDto(dto.getBreedId());
        dtoMapped.setBreed(BreedDTO.builder()
                .id(breedDTO.getId())
                .name(breedDTO.getName())
                .build());

        return dtoMapped;
    }

    /**
     * Atualiza ou cria contatos associados ao Animal
     */
    private void setContactsToUpdate(Set<Contact> entities, Set<ContactDTO> dtos, Animal animal) {

        Map<UUID, Contact> existingById = entities.stream()
                .filter(e -> Objects.nonNull(e.getId()))
                .collect(Collectors.toMap(Contact::getId, e -> e));

        Set<Contact> updatedEntities = dtos.stream()
                .map(dto -> {
                    Contact entity = Optional.ofNullable(dto.getId())
                            .map(existingById::get)
                            .orElseGet(Contact::new);

                    if (isChangeContact(entity, dto)) {
                        entity.setName(dto.getName());
                        entity.setValue(dto.getValue());
                    }

                    entity.setAnimal(animal);

                    return entity;
                })
                .collect(Collectors.toSet());

        entities.clear();
        entities.addAll(updatedEntities);
    }

    /**
     * Verifica se houve alteração no contato
     */
    private boolean isChangeContact(Contact entity, ContactDTO dto) {
        if (Objects.isNull(entity) || Objects.isNull(dto)) return true;
        return !Objects.equals(entity.getName(), dto.getName()) ||
               !Objects.equals(entity.getValue(), dto.getValue());
    }

    /**
     * Cria entidade Dog a partir de DTO de criação
     */
    private Dog setRequestCreate(DogCreateDTO dto) {
        Dog entity = mapper.createToEntity(dto);

        BreedDTO breedDTO = breedService.findByIdToDto(dto.getBreedId());
        entity.setBreed(Breed.builder()
                .id(breedDTO.getId())
                .name(breedDTO.getName())
                .build());

        Set<Contact> contacts = contactMapper.toEntitySet(dto.getContacts());
        if (Objects.nonNull(contacts)) {
            contacts.forEach(contact -> contact.setAnimal(entity));
        }
        entity.setContacts(contacts);

        return entity;
    }

    /**
     * Atualiza campos básicos de Dog
     */
    private void updateBasicFields(Dog entity, DogDTO dto) {
        if (Objects.nonNull(dto.getName()) && !dto.getName().equals(entity.getName())) {
            entity.setName(dto.getName());
        }

        if (Objects.nonNull(dto.getAge()) && !dto.getAge().equals(entity.getAge())) {
            entity.setAge(dto.getAge());
        }

        if (!entity.getBreed().getId().equals(dto.getBreed().getId())) {
            entity.setBreed(breedMapper.toEntity(dto.getBreed()));
        }

        entity.setAvailable(dto.getAvailable());
    }

    /**
     * Busca Dog pelo ID ou lança NotFoundException
     */
    private Dog findById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
    }
}
