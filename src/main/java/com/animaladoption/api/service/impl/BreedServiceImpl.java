package com.animaladoption.api.service.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.exception.NotFoundException;
import com.animaladoption.api.mapper.IAnimalTypeMapper;
import com.animaladoption.api.mapper.IBreedMapper;
import com.animaladoption.api.model.Breed;
import com.animaladoption.api.repository.IBreedRepository;
import com.animaladoption.api.service.IBreedService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements IBreedService {

	private final IBreedRepository repo;
	private final IBreedMapper mapper;
	private final IAnimalTypeMapper animalTypeMapper;

	@Override
	public Page<BreedDTO> findAll(Pageable page) {
		return repo.findAll(page).map(mapper::toDto);
	}

	@Override
	public BreedDTO add(BreedDTO dto) {
		Breed entity = mapper.toEntity(dto);
		return mapper.toDto(repo.save(entity));
	}

	@Override
	public BreedDTO update(UUID id, BreedDTO dto) {
		Breed entity = findById(id);
		isChange(entity, dto);
		return mapper.toDto(repo.save(entity));
	}

	@Override
	public void remove(UUID id) {
		Breed entity = findById(id);
		repo.delete(entity);
	}

	@Override
	public BreedDTO findByIdToDto(UUID id) {
		Breed entity = repo.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
		return mapper.toDto(entity);
	}

	private void isChange(Breed entity, BreedDTO dto) {
		if (Objects.nonNull(entity) && Objects.nonNull(dto)) {
			if (Objects.nonNull(entity.getName()) && Objects.nonNull(dto.getName())
					&& !entity.getName().equals(dto.getName())) {
				entity.setName(dto.getName());
			}

			if (Objects.nonNull(entity.getNationality()) && Objects.nonNull(dto.getNationality())
					&& !entity.getNationality().equals(dto.getNationality())) {
				entity.setNationality(dto.getNationality());
			}

			if (Objects.nonNull(entity.getType()) && Objects.nonNull(dto.getType())
					&& !entity.getType().getName().equals(dto.getType().getName())) {
				entity.setType(animalTypeMapper.toEntity(dto.getType()));
			}
		}
	}

	private Breed findById(UUID id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
	}
}
