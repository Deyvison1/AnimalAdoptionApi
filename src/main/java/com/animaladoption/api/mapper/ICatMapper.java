package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.animaladoption.api.dto.cat.CatCreateDTO;
import com.animaladoption.api.dto.cat.CatDTO;
import com.animaladoption.api.dto.cat.CatUpdateDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.mapper.base.IBaseMapper;
import com.animaladoption.api.model.Cat;
import com.animaladoption.api.model.Dog;

@Mapper(componentModel = "spring", uses = { IContactMapper.class })
public interface ICatMapper extends IBaseMapper<Cat, CatDTO> {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "breed", ignore = true)
	@Mapping(target = "images", ignore = true)
	@Mapping(target = "motivo", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "dateUpdateStatus", ignore = true)
	@Mapping(target = "contacts", source = "contacts")
	Cat createToEntity(CatCreateDTO dto);

	@Mapping(target = "breed", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "images", ignore = true)
	@Mapping(target = "motivo", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "dateUpdateStatus", ignore = true)
	@Mapping(target = "imagesComplet", ignore = true)
	@Mapping(target = "contacts", source = "contacts")
	CatDTO updateToDto(CatUpdateDTO dto);
}
