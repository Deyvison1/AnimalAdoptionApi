package com.animaladoption.api.dto.constants;

public class DogConstants {
	private DogConstants() {
	}

	// Swagger
	public static final String TITLE = "Dogs.";
	public static final String DESCRIPTION = "Gerencia os cachorros.";
	
	// Entity
	public static final String SCHEMA = "animal_adoption";

	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/dog";

	// Roles
	public static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";
	public static final String ADMIN_READ_AUTHORITY = "hasAnyAuthority('ADMIN', 'ADMIN_READ')";
	// Response descriptions
	public static final String FIND_BY_ID = "Busca dos do registro pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca dos registros com paginação realizada com sucesso.";
	public static final String CREATED = "Cachorro criado com sucesso.";
	public static final String UPDATED = "Cachorro atualizado com sucesso.";
	public static final String DELETED = "Cachorro excluído com sucesso.";
}
