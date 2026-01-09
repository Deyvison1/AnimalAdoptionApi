package com.animaladoption.api.model;

import java.io.Serial;

import com.animaladoption.api.dto.constants.DogConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DogConstants.SCHEMA)
public class Dog extends Animal {

	@Serial
	private static final long serialVersionUID = 1L;

	private Boolean available;

}
