package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;

import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.mapper.base.IBaseMapper;
import com.animaladoption.api.model.Breed;

@Mapper(componentModel = "spring")
public interface IBreedMapper extends IBaseMapper<Breed, BreedDTO> {

}
