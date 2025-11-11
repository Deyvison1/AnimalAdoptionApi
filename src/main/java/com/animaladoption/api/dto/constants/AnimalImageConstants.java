package com.animaladoption.api.dto.constants;

public final class AnimalImageConstants {
	private AnimalImageConstants() {
	}

	// Swagger
	public static final String TITLE = "Animal Image.";
	public static final String DESCRIPTION = "Gerencia as imagens do animal.";

	// Entity
	public static final String SCHEMA = "animal_adoption";

	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/animal-image";

	// Roles
	public static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";

	// Response descriptions
	public static final String FIND_BY_ID = "Busca dos do registro pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca dos registros com paginação realizada com sucesso.";
	public static final String CREATED = "Imagem do animal criado com sucesso.";
	public static final String UPDATED = "Imagem do animal atualizado com sucesso.";
	public static final String DELETED = "Imagem do animal excluído com sucesso.";
	

	public static final String UPLOAD = "Upload de Imagem realizado com sucesso.";
	public static final String DOWNLOAD = "Downoad de Imagem realizado com sucesso.";
	public static final String ACTIVE_IMAGE = "Imagem ativada com sucesso.";
}
