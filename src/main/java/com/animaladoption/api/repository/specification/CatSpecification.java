package com.animaladoption.api.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import com.animaladoption.api.dto.cat.CatFilterDTO;
import com.animaladoption.api.enums.StatusAnimal;
import com.animaladoption.api.model.Cat;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CatSpecification {
	public static Specification<Cat> filterBy(CatFilterDTO filter) {
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

	private static void addPredicateIfPresent(List<Predicate> predicates, Specification<Cat> spec, Root<Cat> root,
			CriteriaBuilder cb) {
		if (spec != null) {
			Predicate predicate = spec.toPredicate(root, null, cb);
			if (predicate != null) {
				predicates.add(predicate);
			}
		}
	}

	public static Specification<Cat> rangeAge(Integer age) {
		return (root, query, cb) -> {
			if (Objects.isNull(age)) {
				return null;
			}

			Integer minAge = age - 1;
			Integer maxAge = age + 1;

			return cb.between(root.get("age"), minAge, maxAge);
		};
	}

	public static Specification<Cat> onlyAvailableAndPublished() {
		return (root, query, cb) -> cb.and(cb.isTrue(root.get("available")),
				root.get("status").in(StatusAnimal.PUBLISHED, StatusAnimal.REPUBLISHED));
	}

	private static Specification<Cat> hasContactValue(String contactValue) {
		return (root, query, cb) -> {
			if (Objects.isNull(contactValue) || contactValue.isBlank()) {
				return null;
			}

			Join<Object, Object> contactJoin = root.join("contacts");

			return cb.like(cb.lower(contactJoin.get("value")), "%" + contactValue.toLowerCase() + "%");
		};
	}

	private static Specification<Cat> hasContactName(String contactName) {
		return ((root, query, cb) -> {
			Join<Object, Object> contactJoin = root.join("contacts");
			if (Objects.isNull(contactName) || contactName.isBlank()) {
				return null;
			}
			return cb.like(cb.lower(contactJoin.get("name")), "%" + contactName.toLowerCase() + "%");
		});
	}

	private static Specification<Cat> hasName(String name) {
		return (root, query, cb) -> (name == null || name.isEmpty()) ? null
				: cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

	private static Specification<Cat> hasBreedName(String breedName) {
		return (root, query, cb) -> {
			if (breedName == null || breedName.isBlank()) {
				return null;
			}

			return cb.like(cb.lower(root.join("breed").get("name")), "%" + breedName.toLowerCase() + "%");
		};
	}

}
