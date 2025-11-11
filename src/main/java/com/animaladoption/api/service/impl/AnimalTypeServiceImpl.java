package com.animaladoption.api.service.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.animaladoption.api.dto.animal.AnimalTypeDTO;
import com.animaladoption.api.exception.NotFoundException;
import com.animaladoption.api.mapper.IAnimalTypeMapper;
import com.animaladoption.api.model.AnimalType;
import com.animaladoption.api.repository.IAnimalTypeRepository;
import com.animaladoption.api.service.IAnimalTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalTypeServiceImpl implements IAnimalTypeService {

    private final IAnimalTypeRepository repo;
    private final IAnimalTypeMapper mapper;

    /**
     * Retorna uma página de tipos de animais.
     *
     * @param page informações de paginação
     * @return página contendo AnimalTypeDTO
     */
    @Override
    public Page<AnimalTypeDTO> findAll(Pageable page) {
        var result = repo.findAll(page);
        return result.map(mapper::toDto);
    }

    /**
     * Cria um novo tipo de animal.
     *
     * @param dto dados para criação
     * @return DTO do tipo de animal criado
     */
    @Override
    public AnimalTypeDTO add(AnimalTypeDTO dto) {
        AnimalType entity = mapper.toEntity(dto);
        return mapper.toDto(repo.save(entity));
    }

    /**
     * Atualiza um tipo de animal existente.
     *
     * @param id identificador do tipo de animal
     * @param dto dados atualizados
     * @return DTO atualizado
     * @throws NotFoundException se o tipo não for encontrado
     */
    @Override
    public AnimalTypeDTO update(UUID id, AnimalTypeDTO dto) {
        AnimalType entity = findById(id);
        isChange(entity, dto);
        return mapper.toDto(repo.save(entity));
    }

    /**
     * Remove um tipo de animal pelo ID.
     *
     * @param id identificador do tipo de animal
     * @throws NotFoundException se o tipo não for encontrado
     */
    @Override
    public void delete(UUID id) {
        AnimalType entity = findById(id);
        repo.delete(entity);
    }

    /**
     * Busca um tipo de animal pelo ID e retorna como DTO.
     *
     * @param id identificador do tipo
     * @return DTO do tipo encontrado
     * @throws NotFoundException se não existir
     */
    @Override
    public AnimalTypeDTO findByIdToDto(UUID id) {
        AnimalType entity = repo.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
        return mapper.toDto(entity);
    }

    private void isChange(AnimalType entity, AnimalTypeDTO dto) {
        if (Objects.nonNull(entity) && Objects.nonNull(dto)) {
            if (Objects.nonNull(entity.getName()) && Objects.nonNull(dto.getName())
                    && !entity.getName().equals(dto.getName())) {
                entity.setName(dto.getName());
            }
        }

    }

    private AnimalType findById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
    }


}
