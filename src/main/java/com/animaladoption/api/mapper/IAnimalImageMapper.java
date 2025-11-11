package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.animaladoption.api.dto.animal.AnimalImageDTO;
import com.animaladoption.api.mapper.base.IBaseMapper;
import com.animaladoption.api.model.AnimalImage;

@Mapper(componentModel = "spring", uses = IAnimalMapper.class)
public interface IAnimalImageMapper extends IBaseMapper<AnimalImage, AnimalImageDTO> {
	@Override
	@Mapping(target = "animal", ignore = true)
	AnimalImage toEntity(AnimalImageDTO dto);
}
