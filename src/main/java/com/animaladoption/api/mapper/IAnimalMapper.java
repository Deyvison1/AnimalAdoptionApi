package com.animaladoption.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.SubclassMapping;

import com.animaladoption.api.dto.animal.AnimalDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.model.Dog;
import com.shareddtos.mapper.IBaseMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface IAnimalMapper extends IBaseMapper<Animal, AnimalDTO> {

	@Override
	@SubclassMapping(source = DogDTO.class, target = Dog.class)
	Animal toEntity(AnimalDTO dto);

	@Override
	@SubclassMapping(source = Dog.class, target = DogDTO.class)
	AnimalDTO toDto(Animal entity);

	@ObjectFactory
	default Animal createEntity(AnimalDTO dto) {
		if (dto instanceof DogDTO) {
			return new Dog();
		}
		throw new IllegalArgumentException("Tipo de AnimalDTO não suportado: " + dto.getClass());
	}

	@ObjectFactory
	default AnimalDTO createDto(Animal entity) {
		if (entity instanceof Dog) {
			return new DogDTO();
		}
		throw new IllegalArgumentException("Tipo de Animal não suportado: " + entity.getClass());
	}
}
