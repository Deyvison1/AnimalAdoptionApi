package com.animaladoption.api.dto.constants;

public class PublicConstants {
	private PublicConstants() {
	}

	// Swagger
	public static final String TITLE = "Public.";
	public static final String DESCRIPTION = "Rotas publicas.";

	public static final String BASE_PATH = "/public";


	// Response descriptions
	public static final String FIND_BY_ID = "Busca dos do registro pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca dos registros com paginação realizada com sucesso.";
	public static final String CREATED = "Tipo de animal criado com sucesso.";
	public static final String UPDATED = "Tipo de animal atualizado com sucesso.";
	public static final String DELETED = "Tipo de animal excluído com sucesso.";
}
