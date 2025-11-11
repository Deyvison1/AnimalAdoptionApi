package com.animaladoption.api.dto.constants;

public class BreedConstants {
	private BreedConstants() {
	}

	// Swagger
	public static final String TITLE = "Breed.";
	public static final String DESCRIPTION = "Gerencia as raças.";

	// Entity
	public static final String SCHEMA = "animal_adoption";

	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/breed";

	// Roles
	public static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";

	// Response descriptions
	public static final String FIND_BY_ID = "Busca dos do registro pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca dos registros com paginação realizada com sucesso.";
	public static final String CREATED = "Raça criada com sucesso.";
	public static final String UPDATED = "Raça atualizado com sucesso.";
	public static final String DELETED = "Raça excluído com sucesso.";
}
