package com.animaladoption.api.dto;

import com.animaladoption.api.dto.animal.AnimalDTO;
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
public class ContactDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	private String name;
	private String value;
	private AnimalDTO animal;
}
