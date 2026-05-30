package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.animaladoption.api.dto.dog.DogCreateDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.dto.dog.DogUpdateDTO;
import com.animaladoption.api.model.Dog;
import com.shareddtos.mapper.IBaseMapper;

@Mapper(componentModel = "spring", uses = {
		IContactMapper.class }, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface IDogMapper extends IBaseMapper<Dog, DogDTO> {

	@Mapping(target = "contacts", source = "contacts")
	Dog createToEntity(DogCreateDTO dto);

	@Mapping(target = "contacts", source = "contacts")
	DogDTO updateToDto(DogUpdateDTO dto);
}
