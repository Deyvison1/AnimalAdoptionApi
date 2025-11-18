package com.animaladoption.api.model;

import java.io.Serial;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.animaladoption.api.model.base.BaseEntity;
import com.animaladoption.api.model.converter.UUIDListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "animal_adoption")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Animal extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private Integer age;
	@ManyToOne(optional = false)
	@JoinColumn(name = "breed_id")
	private Breed breed;
	@Convert(converter = UUIDListConverter.class)
	@Column(name = "images", columnDefinition = "TEXT")
	private List<UUID> images;
	@OneToMany(mappedBy = "animal", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Contact> contacts;
}
