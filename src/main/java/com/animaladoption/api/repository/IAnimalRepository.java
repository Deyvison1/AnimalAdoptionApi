package com.animaladoption.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.model.Animal;

@Repository
public interface IAnimalRepository extends JpaRepository<Animal, UUID> {
	@Query("""
		    SELECT a
		    FROM Animal a
		    WHERE a.status = :status
		      AND a.dateUpdateStatus IS NOT NULL
		""")
List<Animal> findByStatusRepublished(@Param("status") StatusAnimal status);
}
