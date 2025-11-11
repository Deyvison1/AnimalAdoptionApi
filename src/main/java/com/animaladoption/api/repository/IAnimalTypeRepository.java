package com.animaladoption.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animaladoption.api.model.AnimalType;

@Repository
public interface IAnimalTypeRepository extends JpaRepository<AnimalType, UUID> {

}
