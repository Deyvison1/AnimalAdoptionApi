package com.animaladoption.api.dto.constants;

public class AnimalTypeConstants {
	private AnimalTypeConstants() {
	}

	// Swagger
	public static final String TITLE = "Animal Type.";
	public static final String DESCRIPTION = "Gerencia os tipos de animais.";

	// Entity
	public static final String SCHEMA = "animal_adoption";

	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/animal-type";

	// Roles
	public static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";

	// Response descriptions
	public static final String FIND_BY_ID = "Busca dos do registro pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca dos registros com paginação realizada com sucesso.";
	public static final String CREATED = "Tipo de animal criado com sucesso.";
	public static final String UPDATED = "Tipo de animal atualizado com sucesso.";
	public static final String DELETED = "Tipo de animal excluído com sucesso.";
}
