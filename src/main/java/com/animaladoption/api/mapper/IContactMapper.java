package com.animaladoption.api.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.animaladoption.api.dto.ContactDTO;
import com.animaladoption.api.mapper.base.IBaseMapper;
import com.animaladoption.api.model.Contact;

@Mapper(componentModel = "spring", uses = { IAnimalMapper.class })
public interface IContactMapper extends IBaseMapper<Contact, ContactDTO> {

	@Override
	Contact toEntity(ContactDTO dto);

	@Override
	@Mapping(target = "animal", ignore = true)
	ContactDTO toDto(Contact entity);

	// Mapear Set<ContactDTO> para Set<Contact>
	default Set<Contact> toEntitySet(Set<ContactDTO> dtos) {
		if (dtos == null)
			return null;
		return dtos.stream().map(this::toEntity).collect(Collectors.toSet());
	}

	// Mapear Set<Contact> para Set<ContactDTO>
	default Set<ContactDTO> toDtoSet(Set<Contact> entities) {
		if (entities == null)
			return null;
		return entities.stream().map(this::toDto).collect(Collectors.toSet());
	}
}