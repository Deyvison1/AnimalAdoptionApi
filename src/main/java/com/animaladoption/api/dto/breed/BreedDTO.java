package com.animaladoption.api.dto.breed;

import java.io.Serial;

import com.animaladoption.api.dto.animal.AnimalTypeDTO;
import com.animaladoption.api.dto.base.BaseDTO;
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
public class BreedDTO extends BaseDTO {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
	private String nationality;
	private AnimalTypeDTO type;
}
