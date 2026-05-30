package com.animaladoption.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.animaladoption.api.model.Cat;

@Repository
public interface ICatRepository extends JpaRepository<Cat, UUID>, JpaSpecificationExecutor<Cat> {
}
