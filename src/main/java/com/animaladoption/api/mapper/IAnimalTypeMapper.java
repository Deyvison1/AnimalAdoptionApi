package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;

import com.animaladoption.api.dto.animal.AnimalTypeDTO;
import com.animaladoption.api.model.AnimalType;
import com.shareddtos.mapper.IBaseMapper;

@Mapper(componentModel = "spring")
public interface IAnimalTypeMapper extends IBaseMapper<AnimalType, AnimalTypeDTO> {

}
