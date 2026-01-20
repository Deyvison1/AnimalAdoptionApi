package com.animaladoption.api.converter;

import com.animaladoption.api.enums.StatusAnimal;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusAnimalConverter implements AttributeConverter<StatusAnimal, Long> {

	@Override
	public Long convertToDatabaseColumn(StatusAnimal status) {
		return StatusAnimal.toId(status);
	}

	@Override
	public StatusAnimal convertToEntityAttribute(Long dbData) {
		return StatusAnimal.fromId(dbData);
	}
}
