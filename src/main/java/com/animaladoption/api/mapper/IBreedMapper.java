package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;

import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.model.Breed;
import com.shareddtos.mapper.IBaseMapper;

@Mapper(componentModel = "spring")
public interface IBreedMapper extends IBaseMapper<Breed, BreedDTO> {

}
