package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.animaladoption.api.dto.dog.DogCreateDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.dto.dog.DogUpdateDTO;
import com.animaladoption.api.mapper.base.IBaseMapper;
import com.animaladoption.api.model.Dog;

@Mapper(componentModel = "spring", uses = { IContactMapper.class })
public interface IDogMapper extends IBaseMapper<Dog, DogDTO> {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "breed", ignore = true)
	@Mapping(target = "images", ignore = true)
	@Mapping(target = "description", source = "description")
	@Mapping(target = "contacts", source = "contacts")
	Dog createToEntity(DogCreateDTO dto);

	@Mapping(target = "breed", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "images", ignore = true)
	@Mapping(target = "imagesComplet", ignore = true)
	@Mapping(target = "contacts", source = "contacts")
	DogDTO updateToDto(DogUpdateDTO dto);
}
