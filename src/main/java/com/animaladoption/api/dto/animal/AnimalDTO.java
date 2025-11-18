package com.animaladoption.api.dto.animal;

import java.io.Serial;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.animaladoption.api.dto.ContactDTO;
import com.animaladoption.api.dto.base.BaseDTO;
import com.animaladoption.api.dto.breed.BreedDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AnimalDTO extends BaseDTO {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private Integer age;
	private BreedDTO breed;
	private List<UUID> images;
    private List<ImageDTO> imagesComplet;
	private Set<ContactDTO> contacts;
}
