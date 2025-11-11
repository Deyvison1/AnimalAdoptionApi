package com.animaladoption.api.model;

import java.io.Serial;

import com.animaladoption.api.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(schema = "animal_adoption")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AnimalType extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;

}
