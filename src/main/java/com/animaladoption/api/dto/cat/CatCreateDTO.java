package com.animaladoption.api.dto.cat;

import java.util.Set;
import java.util.UUID;

import com.animaladoption.api.dto.ContactDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatCreateDTO {
	private String name;
	private int age;
	private UUID breedId;
	private String description;
	private Boolean available;
	private Set<ContactDTO> contacts;
}
