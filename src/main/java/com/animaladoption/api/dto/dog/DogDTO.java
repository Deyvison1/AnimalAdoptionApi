package com.animaladoption.api.dto.dog;

import com.animaladoption.api.dto.animal.AnimalDTO;

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
public class DogDTO extends AnimalDTO {
	private static final long serialVersionUID = 1L;

	private Boolean available;
}
