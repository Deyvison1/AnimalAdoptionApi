package com.animaladoption.api.dto.animal;

import java.io.Serial;

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
public class AnimalTypeDTO extends BaseDTO {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
}
