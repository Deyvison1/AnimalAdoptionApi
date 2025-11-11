package com.animaladoption.api.model;

import java.io.Serial;

import com.animaladoption.api.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "animal_adoption")
@Entity
public class Breed extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
	private String nationality;
	@ManyToOne(optional = false)
	@JoinColumn(name = "animal_type_id", nullable = false)
	private AnimalType type;
}
