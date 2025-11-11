package com.animaladoption.api.dto.animal;

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
public class AnimalImageDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	private String filename;
	private String contentType;
	private byte[] data;
	private Boolean active;
}
