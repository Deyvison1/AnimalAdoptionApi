package com.animaladoption.api.repository.specification;

import com.animaladoption.api.dto.dog.DogFilterDTO;
import com.animaladoption.api.model.Dog;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DogSpecification {
    public static Specification<Dog> filterBy(DogFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (filter != null) {
                addPredicateIfPresent(predicates, hasName(filter.getName()), root, cb);
                addPredicateIfPresent(predicates, hasBreedName(filter.getBreed()), root, cb);
                addPredicateIfPresent(predicates, rangeAge(filter.getAge()), root, cb);
                addPredicateIfPresent(predicates, hasContactValue(filter.getValueContact()), root, cb);
                addPredicateIfPresent(predicates, hasContactName(filter.getNameContact()), root, cb);
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addPredicateIfPresent(List<Predicate> predicates, Specification<Dog> spec, Root<Dog> root,
                                              CriteriaBuilder cb) {
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, null, cb);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }
    }

    public static Specification<Dog> rangeAge(Integer age) {
        return (root, query, cb) -> {
            if (Objects.isNull(age)) {
                return null;
            }

            Integer minAge = age - 1;
            Integer maxAge = age + 1;

            return cb.between(root.get("age"), minAge, maxAge);
        };
    }

    private static Specification<Dog> hasContactValue(String contactValue) {
        return (root, query, cb) -> {
            if (contactValue == null || contactValue.isBlank()) {
                return null;
            }

            Join<Object, Object> contactJoin = root.join("contacts");

            return cb.like(
                    cb.lower(contactJoin.get("value")),
                    "%" + contactValue.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Dog> hasContactName(String contactName) {
        return ((root, query, cb) ->  {
            Join<Object, Object> contactJoin = root.join("contacts");
            if (Objects.isNull(contactName) || contactName.isBlank()) {
                return null;
            }
            return cb.like(
                    cb.lower(contactJoin.get("name")),
                    "%" + contactName.toLowerCase() + "%"
            );
        });
    }

    private static Specification<Dog> hasName(String name) {
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    private static Specification<Dog> hasBreedName(String breedName) {
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
