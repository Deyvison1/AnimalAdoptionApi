package com.animaladoption.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.animaladoption.api.model.Dog;

@Repository
public interface IDogRepository extends JpaRepository<Dog, UUID>, JpaSpecificationExecutor<Dog> {
}
