package com.animaladoption.api.repository.specification;

import com.animaladoption.api.dto.dog.DogFilterDTO;
import com.animaladoption.api.model.Dog;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DogSpecification {
    public static Specification<Dog> filterBy(DogFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (filter != null) {
                addPredicateIfPresent(predicates, hasName(filter.getName()), root, cb);
                addPredicateIfPresent(predicates, hasBreedName(filter.getBreed()), root, cb);
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addPredicateIfPresent(List<Predicate> predicates, Specification<Dog> spec, Root<Dog> root,
                                              CriteriaBuilder cb) {
        if (spec != null) {
            Predicate predicate = (Predicate) spec.toPredicate(root, null, cb);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }
    }

    public static Specification<Dog> hasName(String name) {
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    public static Specification<Dog> hasBreedName(String breedName) {
        return (root, query, cb) -> {
            if (breedName == null || breedName.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.join("breed").get("name")),
                    "%" + breedName.toLowerCase() + "%"
            );
        };
    }

}
