package com.animaladoption.api.dto.cat;

import com.animaladoption.api.dto.animal.AnimalDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CatDTO extends AnimalDTO {
    @Serial
	private static final long serialVersionUID = 1L;

	private Boolean available;
}
