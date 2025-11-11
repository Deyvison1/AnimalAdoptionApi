package com.animaladoption.api.dto.base;

import java.io.Serial;

import com.animaladoption.api.dto.breed.BreedDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class AnimalBaseDTO extends BaseDTO {

	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
	private String descripton;
	private Integer age;
	private BreedDTO breed;
}
