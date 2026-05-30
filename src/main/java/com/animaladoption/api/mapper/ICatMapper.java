package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.animaladoption.api.dto.cat.CatCreateDTO;
import com.animaladoption.api.dto.cat.CatDTO;
import com.animaladoption.api.dto.cat.CatUpdateDTO;
import com.animaladoption.api.model.Cat;
import com.shareddtos.mapper.IBaseMapper;

@Mapper(componentModel = "spring", uses = {
		IContactMapper.class }, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ICatMapper extends IBaseMapper<Cat, CatDTO> {

	@Mapping(target = "contacts", source = "contacts")
	Cat createToEntity(CatCreateDTO dto);

	@Mapping(target = "contacts", source = "contacts")
	CatDTO updateToDto(CatUpdateDTO dto);
}
