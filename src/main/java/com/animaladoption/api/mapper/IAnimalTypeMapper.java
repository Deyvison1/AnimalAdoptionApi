package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;

import com.animaladoption.api.dto.animal.AnimalTypeDTO;
import com.animaladoption.api.mapper.base.IBaseMapper;
import com.animaladoption.api.model.AnimalType;

@Mapper(componentModel = "spring")
public interface IAnimalTypeMapper extends IBaseMapper<AnimalType, AnimalTypeDTO> {

}
